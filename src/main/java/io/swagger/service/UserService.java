package io.swagger.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import io.swagger.model.User;
import io.swagger.api.db.*;
import static org.springframework.data.domain.ExampleMatcher.matching;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Transactional
public class UserService {
    private UserRepository repo;
    
    @Transactional(readOnly = true)
    public List<User> search(UserSearchCriteria criteria, Pageable pageable) {
      Example<User> example = createSearchExample(criteria);
      return repo.findAll(example, pageable).getContent();
    }
    
    protected Example<User> createSearchExample(UserSearchCriteria criteria) {
      User user = new User();
      
      return Example.of(user, matching().withMatcher("name", contains()));
    }
}
