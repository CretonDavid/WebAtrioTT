package com.steekse.webatrioerp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "jobs")
@Data
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String position;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "persons_id")
    private Person person;

    public boolean isCurrentJob() {
        return endDate == null || endDate.isAfter(LocalDate.now());
    }
}
