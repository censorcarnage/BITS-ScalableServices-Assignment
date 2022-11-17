package com.bitsassignment.urlservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories("com.bitsassignment.urlservice.repository.redis")
@Slf4j
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String springRedisHost;

    @Value("${spring.redis.port}")
    private Integer springRedisPort;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        log.info("Starting Redis Config");
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(springRedisHost);
        redisConfig.setPort(springRedisPort);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
}
