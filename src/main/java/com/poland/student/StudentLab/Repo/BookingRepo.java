package com.poland.student.StudentLab.Repo;

import com.poland.student.StudentLab.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer> {

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Booking b WHERE b.room.id = :room_id AND b.date = :date")
    boolean existsByRoomAndDate(@Param("room_id") int roomId, @Param("date") Date date);

    @Query("SELECT b FROM Booking b WHERE b.room.id = :room_id AND b.date = :date")
    Booking findByRoomAndDate(@Param("room_id") int roomId, @Param("date") Date date);
}
