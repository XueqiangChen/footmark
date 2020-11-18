package com.xueqiang.footmark.service;

import com.xueqiang.footmark.service.impl.CustomerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {

	@Autowired
	private CustomerService customerService;

	@Test
	public void testUseJdbcTemplate() {
		customerService.useJdbcTemplate();
	}
}