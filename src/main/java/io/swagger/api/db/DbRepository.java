package io.swagger.api.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DbRepository<T> extends MongoRepository<T, Long> {
}
