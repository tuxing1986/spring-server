package io.swagger.api.db;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import io.swagger.model.Pet;
import io.swagger.model.User;

public interface UserRepository extends DbRepository<User> {
    
}
