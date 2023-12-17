package com.telemed.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepositoryDB extends CrudRepository<Record, Integer> {
    Iterable<Record> findAllByUser(User currentUser);

    Record findRecordById(int id);

    List<Record> findByUser(User user);
}
