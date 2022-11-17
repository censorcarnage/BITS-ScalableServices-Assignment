package com.bitsassignment.urlservice.controller;

import com.bitsassignment.urlservice.model.request.URLGenerationRequest;
import com.bitsassignment.urlservice.model.response.URLGenerationResponse;
import com.bitsassignment.urlservice.service.URLGenerationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class URLController {

    @Autowired
    private URLGenerationService urlGenerationService;

    @PostMapping("/shorturl/generate")
    ResponseEntity<URLGenerationResponse> generateURL(@RequestBody URLGenerationRequest request) {
       return ResponseEntity.ok(urlGenerationService.generateShortUrl(request));
    }

    @GetMapping("/shorturl/{key}")
    ResponseEntity<URLGenerationResponse> getURL(@PathVariable String key) {
        return ResponseEntity.ok(urlGenerationService.getShortUrl(key));
    }
}
