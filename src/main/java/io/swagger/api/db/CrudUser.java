package io.swagger.api.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import io.swagger.model.User;

@NoRepositoryBean
public interface CrudUser extends CrudRepository<User, Long> {
}
