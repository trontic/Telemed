package com.telemed.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositoryDB extends PagingAndSortingRepository<User, Integer> {

    User findByEmailAndPassword(String email, String password);

    User findUserById(Integer id);

    List<User> findByLname(String lname);

    Page<User> findAll(Pageable pageable);

    Page<User> findByType(int type, Pageable pageable);

    // Additional method from CustomUserRepository
    List<User> findByFnameContainingIgnoreCaseOrLnameContainingIgnoreCase(String fname, String lname);

    void save(User newUser);

    void delete(User user);


    Optional<Object> findById(int userId);
}