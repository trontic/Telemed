package com.telemed.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TherapyRepositoryDB extends CrudRepository<Therapy, Integer> {

    List<Therapy> findByRecord_Id(int id);

    List<Therapy> findByRecord(Record record);

    Therapy findFirstByRecordAndIregularIsTrue(Record record);
}
