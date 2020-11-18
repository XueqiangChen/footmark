package com.xueqiang.footmark.controller;

import com.xueqiang.footmark.model.Quote;
import com.xueqiang.footmark.service.impl.ConsumingRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumRestController {

	@Autowired
	private ConsumingRestService consumingRestService;

	@GetMapping("/guote")
	public Quote getGuote() {
		return consumingRestService.consumingRest();
	}
}
