package com.xueqiang.footmark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FootmarkApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootmarkApplication.class, args);
	}

}
