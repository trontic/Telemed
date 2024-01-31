package com.telemed.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositoryDB extends CrudRepository<User, Integer> {

    User findByEmailAndPassword(String email, String password);

    List<User> findByType(int type);

    User findUserById(int id);

    List<User> findByLname(String lname);
    Page<User> findAll(Pageable pageable);
}
