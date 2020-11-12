package com.xueqiang.footmark.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class FootmarkProperties {

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootstrapServers;

}
