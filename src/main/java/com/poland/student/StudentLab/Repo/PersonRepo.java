package com.poland.student.StudentLab.Repo;

import com.poland.student.StudentLab.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepo extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String username);

    boolean existsByUsername(String username);
}
