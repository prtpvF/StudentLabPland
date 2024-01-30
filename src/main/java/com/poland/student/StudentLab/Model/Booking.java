package com.poland.student.StudentLab.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne
    private Person person;
    @ManyToOne
    private Room room;

    private LocalDateTime dateOfBooking;
}
