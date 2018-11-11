package io.swagger.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

import io.swagger.model.Category;
import io.swagger.model.Pet;
import io.swagger.model.Tag;
import io.swagger.model.User;
import io.swagger.api.db.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.data.domain.ExampleMatcher.matching;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
@Transactional
@Service
public class UserService {
    private UserRepository repo;
    
    @Autowired
    MongoTemplate template;
    
    public List<Pet> searchPets() {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            Tag tag = new Tag();
            tag.setId((long) i);
            tag.setName("tag" + i);
            tags.add(tag);
            template.save(tag);
        }
        Category cate = new Category();
        cate.setId(22L);
        cate.setName("category");
        Pet pet = new Pet();
        pet.setId(10L);
        pet.setTags(tags);
        pet.setCategory(cate);
        
        pet.setName("pet mine");
        
        template.save(cate);
        template.save(pet);
        
        pet = new Pet();
        pet.setId(1L);
        pet.setName("name");
        
        cate = new Category();
        cate.setId(1L);
        cate.setName("cate");
        pet.setCategory(cate);
        
        List<Tag> ts = new ArrayList<Tag>();
        ts.addAll(tags);
        ts.remove(4);
        pet.setTags(ts);
        template.save(pet);
        
        
        //return template.findOne(new Query(where("category.name").is("category")), Pet.class);
        return template.find(new Query(where("tags.id").gt(3L)), Pet.class);
    }
    @Transactional(readOnly = true)
    public List<User> search(UserSearchCriteria criteria, Pageable pageable) {
      Example<User> example = createSearchExample(criteria);
      return repo.findAll(example, pageable).getContent();
    }
    
    protected Example<User> createSearchExample(UserSearchCriteria criteria) {
      User user = new User();
      Date date = new Date();
     
      return Example.of(user, matching().withMatcher("name", contains()));
    }
}
