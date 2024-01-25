package com.telemed.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import java.util.List;

@Repository
public interface RecordRepositoryDB extends CrudRepository<Record, Integer> {
    Iterable<Record> findAllByUser(User currentUser, Sort sort);

    Record findRecordById(int id);

    List<Record> findByUser(User user);
}
