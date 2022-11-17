package com.bitsassignment.urlservice.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Document("ShortURLStorage")
public class URLKeyStorage {
    @Id
    private String encodedKeyId;

    private String longUrl;

    private LocalDateTime createdTimestamp;
}
