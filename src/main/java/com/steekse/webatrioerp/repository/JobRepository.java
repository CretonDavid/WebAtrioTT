package com.steekse.webatrioerp.repository;

import com.steekse.webatrioerp.model.Job;
import com.steekse.webatrioerp.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByPersonAndStartDateBetween(Person person, LocalDate startDate, LocalDate endDate);
}
