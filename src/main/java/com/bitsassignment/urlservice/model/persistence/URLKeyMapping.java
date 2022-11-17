package com.bitsassignment.urlservice.model.persistence;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("URLKeyMapping")
@Builder
@Data
public class URLKeyMapping {
    @Id
    private String encodedId;

    private String longUrl;
}
