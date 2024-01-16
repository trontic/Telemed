package com.telemed.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TherapyRepositoryDB extends CrudRepository<Therapy, Integer> {

}
