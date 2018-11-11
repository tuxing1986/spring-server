package io.swagger.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
//import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import com.mongodb.MongoClient;

@Configuration
public class MongoDbConfiguration {

  public @Bean MongoClient mongoClient() {
      return new MongoClient("192.168.99.100");
  }

  public @Bean MongoTemplate mongoTemplate() {
      return new MongoTemplate(mongoClient(), "test");
  }
  
  @Bean
  public MongoDbFactory mongoDbFactory(){
      return new SimpleMongoDbFactory(mongoClient(), "test");
  }
  /*
  @Bean
  public MongoTransactionManager mongoTransactionManager() {
      return new MongoTransactionManager(mongoDbFactory());
  }*/
  
  /*@Bean
  public MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {  
      return new MongoTransactionManager(dbFactory);
  }*/
}