package com.bitsassignment.urlservice.repository.redis;

import com.bitsassignment.urlservice.model.persistence.URLKeyMapping;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheRepository extends CrudRepository<URLKeyMapping, String> {
}
