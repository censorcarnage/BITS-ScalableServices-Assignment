package com.bitsassignment.urlservice.service;

import com.bitsassignment.urlservice.config.SharedRangeConfig;
import com.bitsassignment.urlservice.model.persistence.URLKeyMapping;
import com.bitsassignment.urlservice.model.persistence.URLKeyStorage;
import com.bitsassignment.urlservice.model.request.URLGenerationRequest;
import com.bitsassignment.urlservice.model.response.URLGenerationResponse;
import com.bitsassignment.urlservice.repository.mongo.URLKeyStorageRepository;
import com.bitsassignment.urlservice.repository.redis.CacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class URLGenerationService {

    @Autowired
    private SharedRangeConfig sharedRangeConfig;

    @Autowired
    private CacheRepository cacheRepository;

    @Autowired
    private URLKeyStorageRepository urlKeyStorageRepository;

    public URLGenerationResponse generateShortUrl(URLGenerationRequest request) {
        long uniqueId = sharedRangeConfig.getCurrentRange();
        String encodedKey = BaseEncoderService.base62Encode(uniqueId);
        log.info("{} {}",uniqueId,encodedKey);
        encodedKey = completeLengthTillSevenIfNeeded(encodedKey);
        addToStorage(request.getLongUrl(),encodedKey);
        addToCache(request.getLongUrl(),encodedKey);
        return createShortUrlResponse(request,encodedKey);
    }

    public URLGenerationResponse getShortUrl(String key) {
        URLGenerationResponse urlGenerationResponse;
        URLKeyMapping urlKeyMapping = findShortURLMappingInCache(key);
        if(!Objects.isNull(urlKeyMapping)) {
            urlGenerationResponse = URLGenerationResponse.builder().encodedString(key).longUrl(urlKeyMapping.getLongUrl()).build();
        }
        else {
            URLKeyStorage urlKeyStorage = findInMappingStorage(key);
            if(!Objects.isNull(urlKeyStorage))
                urlGenerationResponse = URLGenerationResponse.builder().encodedString(key).longUrl(urlKeyStorage.getLongUrl()).build();
            else
                urlGenerationResponse = URLGenerationResponse.builder().encodedString(null).longUrl(null).errorMessage("Original URL not found. Please generate a new one.").build();
        }
        return urlGenerationResponse;
    }

    private URLKeyStorage findInMappingStorage(String key) {
        URLKeyStorage urlKeyStorage = null;
        Optional<URLKeyStorage> urlStorageResponse = urlKeyStorageRepository.findById(key);
        if(urlStorageResponse.isPresent())
            urlKeyStorage = urlStorageResponse.get();
        return urlKeyStorage;
    }

    private URLKeyMapping findShortURLMappingInCache(String key) {
        URLKeyMapping urlKeyMapping = null;
        Optional<URLKeyMapping> response = cacheRepository.findById(key);
        if(response.isPresent())
            urlKeyMapping = response.get();
        return urlKeyMapping;
    }

    private URLGenerationResponse createShortUrlResponse(URLGenerationRequest request, String encodedKey) {
        return URLGenerationResponse.builder().longUrl(request.getLongUrl()).encodedString(encodedKey).build();
    }

    private String completeLengthTillSevenIfNeeded(String encodedKey) {
        StringBuilder sb = new StringBuilder(encodedKey);
        while(sb.length() < 7) {
            sb.append("c");
        }
        return sb.toString();
    }

    @Async
    void addToCache(String longUrl,String encodedKey) {
        URLKeyMapping urlKeyMapping = URLKeyMapping.builder().encodedId(encodedKey).longUrl(longUrl).build();
        cacheRepository.save(urlKeyMapping);
    }

    @Async
    void addToStorage(String longUrl, String encodedKey) {
        URLKeyStorage urlKeyStorage = URLKeyStorage.builder().encodedKeyId(encodedKey).longUrl(longUrl).createdTimestamp(LocalDateTime.now()).build();
        urlKeyStorageRepository.save(urlKeyStorage);
    }
}
