package com.vip.vipcloud.apiserver.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.vip.vipcloud.apiserver.crab.api.client.model.EnvVarQueryResult;
import com.vip.vipcloud.apiserver.crab.api.client.model.IsNodeSyncResult;
import com.vip.vipcloud.apiserver.pojo.deploy.config.CustomConfig;
import com.vip.vipcloud.apiserver.util.logger.LogEvent;
import com.vip.vipcloud.apiserver.util.logger.LogUtil;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.ThreadWaitSleeper;
import org.springframework.retry.backoff.UniformRandomBackOffPolicy;
import org.springframework.retry.policy.CompositeRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class StaticObjectInit {

	private static final Logger logger = LoggerFactory.getLogger(StaticObjectInit.class);

	public static final ObjectMapper YAML_OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

	public static final ObjectMapper JSON_OBJECT_MAPPER = new ObjectMapper();
	public static final ObjectReader JSON_OBJECT_READER = JSON_OBJECT_MAPPER.readerFor(Map.class);
	public static final ObjectWriter JSON_OBJECT_WRITER = JSON_OBJECT_MAPPER.writer();

	public static CloseableHttpClient HTTP_CLIENT;

	public static RestTemplate CMDB_API_TEMPLATE;
	public static RestTemplate CLUSTER_VERSION_TEMPLATE;
	public static RestTemplate CFGCENTER_API_TEMPLATE;
	public static RestTemplate CMDB_REST_TEMPLATE;
	public static RestTemplate CRAB_REST_TEMPLATE;
	public static RestTemplate CUTTLE_REST_TEMPLATE;
	public static RestTemplate EAGLE_REST_TEMPLATE;
	public static RestTemplate PIGEON_REST_TEMPLATE;
	public static RestTemplate CIDER_REST_TEMPLATE;
	public static RestTemplate HIGHTOWER_REST_TEMPLATE;
	public static RestTemplate MERCURY_REST_TEMPLATE;
	public static RestTemplate CALLBACK_REST_TEMPLATE;
	public static RestTemplate SALUS_REST_TEMPLATE;
	public static RestTemplate GRU_REST_TEMPLATE;
	public static RestTemplate SELF_TRANSFER_REST_TEMPLATE;
	public static RestTemplate HELM_PROXY_REST_TEMPLATE;
	public static RestTemplate CHART_REST_TEMPLATE;

	public static RetryTemplate RETRY_TEMPLATE = new RetryTemplate();
	public static TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");
	public static FastDateFormat ISO_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss'Z'", UTC_TIMEZONE);

	public static final Pattern PATTERN_FOR_DNS1123_LABEL = Pattern.compile("[a-z0-9]([-a-z0-9]*[a-z0-9])?");

	/**
	 * gru api中某些get查询方法需要传递body，但RestTemplate不支持get传递body，所以需要扩展HttpComponentsClientHttpRequestFactory，支持get方法传递body
	 */
	private static final class HttpComponentsClientHttpRequestWithBodyFactory extends HttpComponentsClientHttpRequestFactory {
		private HttpComponentsClientHttpRequestWithBodyFactory(HttpClient httpClient) {
			super(httpClient);
		}

		@Override
		protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
			if (httpMethod == HttpMethod.GET) {
				// HttpRequestBase不支持传递body，但HttpEntityEnclosingRequestBase可以支持传递body
				return new HttpGetRequestWithEntity(uri);
			}
			return super.createHttpUriRequest(httpMethod, uri);
		}
	}

	private static final class HttpGetRequestWithEntity extends HttpEntityEnclosingRequestBase {
		public HttpGetRequestWithEntity(final URI uri) {
			super.setURI(uri);
		}

		@Override
		public String getMethod() {
			return HttpMethod.GET.name();
		}
	}

	static {
		SSLConnectionSocketFactory trustAllSocketFactory = null;
		try {
			trustAllSocketFactory = new SSLConnectionSocketFactory(
					SSLContexts.custom().loadTrustMaterial((X509Certificate[] chain, String authType) -> true).build(),
					NoopHostnameVerifier.INSTANCE);
		} catch (Exception e) {
			LogUtil.error(logger, LogEvent.Template.DEFAULT, "Init SSLConnectionSocketFactory failed, use default one",
					e);
		}
		int timeout = Integer.parseInt(System.getProperty("retry.template.timeout", "2000"));
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).build();
		HTTP_CLIENT = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setMaxConnPerRoute(100)
				.setMaxConnTotal(500).setConnectionTimeToLive(5, TimeUnit.SECONDS)
				.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
				.setConnectionReuseStrategy(DefaultClientConnectionReuseStrategy.INSTANCE)
				.setSSLSocketFactory(trustAllSocketFactory).build();
		CMDB_API_TEMPLATE = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HTTP_CLIENT));
		CLUSTER_VERSION_TEMPLATE = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HTTP_CLIENT));
		CMDB_REST_TEMPLATE = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HTTP_CLIENT));
		CRAB_REST_TEMPLATE = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HTTP_CLIENT));
		CUTTLE_REST_TEMPLATE = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HTTP_CLIENT));
		EAGLE_REST_TEMPLATE = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HTTP_CLIENT));
		PIGEON_REST_TEMPLATE = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HTTP_CLIENT));
		CIDER_REST_TEMPLATE = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HTTP_CLIENT));

		// cfgcenter(service-center)的修改路由接口会因为应用的服务太多而用时较长，加大read timeout
		HttpComponentsClientHttpRequestFactory cfgCenterRequestFactory = new HttpComponentsClientHttpRequestFactory(HTTP_CLIENT);
		cfgCenterRequestFactory.setReadTimeout(5 * 1000); //5秒
		CFGCENTER_API_TEMPLATE = new RestTemplate(cfgCenterRequestFactory);

		//mercury open api返回时间较长，加大read timeout
		HttpComponentsClientHttpRequestFactory mercuryRequestFactory = new HttpComponentsClientHttpRequestFactory(HTTP_CLIENT);
		mercuryRequestFactory.setReadTimeout(10 * 1000); //10秒
		MERCURY_REST_TEMPLATE = new RestTemplate(mercuryRequestFactory);

		CALLBACK_REST_TEMPLATE = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HTTP_CLIENT));
		SALUS_REST_TEMPLATE = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HTTP_CLIENT));
		GRU_REST_TEMPLATE = new RestTemplate(new HttpComponentsClientHttpRequestWithBodyFactory(HTTP_CLIENT));
		HIGHTOWER_REST_TEMPLATE = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HTTP_CLIENT));

		// 自助快速发布依赖的外部API
		SELF_TRANSFER_REST_TEMPLATE = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HTTP_CLIENT));

		HELM_PROXY_REST_TEMPLATE = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HTTP_CLIENT));
		CHART_REST_TEMPLATE = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HTTP_CLIENT));

		//假如想要限制retry的总时间，一旦超过总时间，即使次数没达到，也停止retry，可以加上TimeoutRetryPolicy
		//这里只限制次数，最多5次
		SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
		simpleRetryPolicy.setMaxAttempts(5);

		CompositeRetryPolicy retryPolicy = new CompositeRetryPolicy();
		retryPolicy.setOptimistic(false);
		retryPolicy.setPolicies(new RetryPolicy[]{simpleRetryPolicy});
		RETRY_TEMPLATE.setRetryPolicy(retryPolicy);

		UniformRandomBackOffPolicy backOffPolicy = new UniformRandomBackOffPolicy();
		backOffPolicy.setSleeper(new ThreadWaitSleeper());
		backOffPolicy.setMinBackOffPeriod(100L);
		backOffPolicy.setMaxBackOffPeriod(400L);
		RETRY_TEMPLATE.setBackOffPolicy(backOffPolicy);

		SimpleModule module = new SimpleModule();
		module.addDeserializer(CustomConfig.class, new CustomConfig.CustomConfigDeserializer());
		module.addDeserializer(EnvVarQueryResult.class, new EnvVarQueryResult.EnvVarQueryResultDeserializer());
		module.addDeserializer(IsNodeSyncResult.class, new IsNodeSyncResult.IsNodeSyncResultDeserializer());
		JSON_OBJECT_MAPPER.registerModule(module);
		JSON_OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
	}
}