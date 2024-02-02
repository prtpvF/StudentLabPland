package com.poland.student.StudentLab.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Person")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    @NotEmpty
    private String name;

    @Column(name = "surname", nullable = false)
    @NotEmpty
    private String surname;

    @Column(name = "username", nullable = false, unique = true)
    @NotEmpty
    private String username;

    @Column(name = "age", nullable = false)
    @Min(17)
    private int age;

    @Column(name = "contact_information", nullable = false)
    @NotEmpty
    private String contactInformation;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "password", nullable = false)
    @NotEmpty
    private String password;

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

}
