package com.bitsassignment.urlservice.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.bitsassignment.urlservice.repository.mongo")
public class MongoConfig {

    @Value("${mongo.connection.string}")
    private String mongoConnectionString;

    @Value("${mongodb.database.name}")
    private String mongoDbName;

    @Bean
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString(mongoConnectionString);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongo(), mongoDbName);
    }
}
