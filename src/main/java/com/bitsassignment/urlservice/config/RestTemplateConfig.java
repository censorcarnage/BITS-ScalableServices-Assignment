package com.bitsassignment.urlservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.nio.Buffer;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@Slf4j
public class RestTemplateConfig {

    @Value("${config.http.read.timeout}")
    private int readTimeout;

    @Value("${config.http.connection.timeout}")
    private int connectionTimeout;

    @Bean
    @Primary
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder.setConnectTimeout(Duration.of(connectionTimeout, ChronoUnit.SECONDS)).setReadTimeout(Duration.of(readTimeout, ChronoUnit.SECONDS)).build();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(requestFactory);
        restTemplate.setRequestFactory(bufferingClientHttpRequestFactory);
        return restTemplate;
    }
}
