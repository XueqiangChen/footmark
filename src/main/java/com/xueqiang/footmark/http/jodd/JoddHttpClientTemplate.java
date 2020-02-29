package com.xueqiang.footmark.http.jodd;

import com.xueqiang.footmark.utils.StaticVariable;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.util.StringPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class JoddHttpClientTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoddHttpClientTemplate.class);

    private String sendHttpGet(String url, List<String> headers) throws UnsupportedEncodingException {
        HttpResponse httpResponse = HttpRequest.get(url)
                .timeout(StaticVariable.DEFAULT_TIMEOUT).send();

        String responseBody = new String(httpResponse.bodyBytes(), StringPool.UTF_8);

        if (httpResponse.statusCode() != 200 || StringUtils.isEmpty(responseBody)) {
            LOGGER.warn("request to get url={} failed, statusCode={}, responseBody = {}", url,
                    httpResponse.statusCode(), responseBody);

            throw new RuntimeException("Failed to get url.");
        } else{
            LOGGER.info("Call VMS2 OK! GET url={}, statusCode={}, responseBody = {}", url,
                    httpResponse.statusCode(), responseBody);
        }

        return responseBody;
    }

    public static void main(String[] args) {
        List<String> list1 =    null;
        List<String> list2 = new ArrayList<>();


        list2.add("a");
        list2.add("b");
        list2.add("c");

        boolean rest = list2.containsAll(list1);
        System.out.println(rest);
    }
}
