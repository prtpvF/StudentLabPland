package com.poland.student.StudentLab.Repo;

import com.poland.student.StudentLab.Model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepo extends JpaRepository<Room, Integer> {
}
