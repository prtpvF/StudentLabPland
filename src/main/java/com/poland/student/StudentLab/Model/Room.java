package com.poland.student.StudentLab.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Room")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "name can't be empty")
    private String name;

    @Column(name = "type_of_bed")
    @NotEmpty(message = "can't be empty")
    private String typeOfBed;
    @Column(name = "parking")
    private boolean parking;
    @Column(name = "wifie")
    private boolean wifi;
    @Column(name = "bathroom")
    private boolean bathroom;
    @Column(name = "kitchen")
    private boolean kitchen;
    @Column(name = "nutrition")
    @NotEmpty(message = "can't be empty")
    private String nutrition;
    @Column(name = "is_washing_machine_include")
    private boolean washingMachine;

    @Column(name = "price")
    @NotNull(message = "price can't be 0")
    private double price;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "room")
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Booking> bookings;
    private Long previewImageId;

    public void addImage(Image image) {
        image.setRoom(this);
        images.add(image);
    }

}