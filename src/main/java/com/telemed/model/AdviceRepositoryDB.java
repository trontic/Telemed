package com.telemed.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdviceRepositoryDB extends CrudRepository<Advice, Integer> {
    List<Advice> findById(int id);
}
