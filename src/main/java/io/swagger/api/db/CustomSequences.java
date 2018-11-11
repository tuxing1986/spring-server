package io.swagger.api.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.CrudRepository;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "customSequences")
public class CustomSequences {
    @Id
    @Getter
    @Setter
    private String id;
    
    @Getter
    @Setter
    private int seq;
}