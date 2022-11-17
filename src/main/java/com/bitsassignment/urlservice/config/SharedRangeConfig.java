package com.bitsassignment.urlservice.config;

import com.bitsassignment.urlservice.model.response.RangeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
@Slf4j
public class SharedRangeConfig {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${config.node.id}")
    private String nodeId;

    @Value("${config.range.url}")
    private String rangeUrl;

    private AtomicLong start;

    private AtomicLong end;

    @PostConstruct
    private void getSharedRange() {
        log.info("Getting the range after service startup.");
        rangeUrl = rangeUrl.concat(nodeId);
        ResponseEntity<RangeResponse> rangeResponse = restTemplate.exchange(rangeUrl, HttpMethod.GET,null, RangeResponse.class);
        if(Objects.equals(rangeResponse.getStatusCode(), HttpStatus.OK)) {
            start = new AtomicLong(rangeResponse.getBody().getStartValue());
            end =  new AtomicLong(rangeResponse.getBody().getEndValue());
            log.info("{}",start.intValue());
            log.info("{}",end.intValue());
        } else log.info("Unable to fetch range during startup.");
    }

    public synchronized long getCurrentRange() {
        long pointer;
        if(start.longValue() <= end.longValue()) {
            pointer = start.longValue();
            start.getAndIncrement();
        } else {
            getSharedRange();
            pointer = start.longValue();
            start.getAndIncrement();
        }
        return pointer;
    }
}
