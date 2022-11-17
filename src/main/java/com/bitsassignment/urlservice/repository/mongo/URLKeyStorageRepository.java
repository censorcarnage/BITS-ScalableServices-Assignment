package com.bitsassignment.urlservice.repository.mongo;

import com.bitsassignment.urlservice.model.persistence.URLKeyStorage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface URLKeyStorageRepository extends MongoRepository<URLKeyStorage,String> {
}
