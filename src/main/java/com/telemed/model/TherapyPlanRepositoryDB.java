package com.telemed.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TherapyPlanRepositoryDB extends CrudRepository<TherapyPlan, Integer> {

    Iterable<TherapyPlan> findAllByUser(User user);

    List<TherapyPlan> findByUserAndDayPart(User user, String dayPart);


}
