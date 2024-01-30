package com.poland.student.StudentLab.Repo;

import com.poland.student.StudentLab.Model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepo extends JpaRepository<Image, Integer> {
}
