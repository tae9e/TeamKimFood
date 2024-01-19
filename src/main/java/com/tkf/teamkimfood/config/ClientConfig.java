package com.tkf.teamkimfood.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration

public class ClientConfig {
    //외부 시스템과의 통신
    @Bean
    public RestTemplate restTemplate(){

        return new RestTemplate();
    }
}
