package com.steekse.webatrioerp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "persons")
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Job> jobs = new ArrayList<>();

    public int getAge() {
        return LocalDate.now().getYear() - birthDate.getYear();
    }
}
