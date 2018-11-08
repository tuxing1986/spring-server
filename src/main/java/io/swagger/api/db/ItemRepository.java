package io.swagger.api.db;

import org.springframework.transaction.annotation.Transactional;

import io.swagger.model.Pet;

//@Transactional(readOnly = true)
@Transactional
public interface ItemRepository extends DbRepository<Pet> {
}
