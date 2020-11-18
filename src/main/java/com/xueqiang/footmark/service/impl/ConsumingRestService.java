package com.xueqiang.footmark.service.impl;

import com.xueqiang.footmark.model.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumingRestService {

	private static final Logger log = LoggerFactory.getLogger(ConsumingRestService.class);

	private static final String QUOTE_URL = "https://gturnquist-quoters.cfapps.io/api/random";

	@Autowired
	private RestTemplate restTemplate;

	public Quote consumingRest() {
		Quote quote = restTemplate.getForObject(QUOTE_URL, Quote.class);

		if (quote != null) {
			log.info(quote.toString());
		}

		return quote;
	}
}
