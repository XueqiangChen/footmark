package com.xueqiang.footmark;

import com.xueqiang.footmark.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(StorageProperties.class)
public class FootmarkApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootmarkApplication.class, args);
	}
}
