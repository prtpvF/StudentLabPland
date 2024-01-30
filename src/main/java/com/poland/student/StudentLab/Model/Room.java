package com.poland.student.StudentLab.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Room")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "type_of_bed")
    private String typeOfBed;
    @Column(name = "is_parking_include")
    private boolean isParkingInclude;
    @Column(name = "is_wifi_include")
    private boolean isWiFiInclude;
    @Column(name = "is_bathroom_separated")
    private boolean isBathroomSeparated;
    @Column(name = "is_kitchen_separated")
    private boolean isKitchenSeparated;
    @Column(name = "nutrition")
    private String nutrition;
    @Column(name = "is_washing_machine_include")
    private boolean isWashingMachineInclude;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "room")
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<Booking> bookings;
    private Long previewImageId;

    public void addImage(Image image){
        image.setRoom(this);
        images.add(image);
    }
}
