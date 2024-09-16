package com.steekse.webatrioerp.service;

import com.steekse.webatrioerp.dto.JobDto;
import com.steekse.webatrioerp.dto.PersonDto;

import java.time.LocalDate;
import java.util.List;

public interface IPersonService {
    void createPerson(PersonDto personDto);
    void addJobToPerson(Long personId, JobDto jobDto);
    List<PersonDto> getAllPersonsSorted();
    List<PersonDto> getPersonsByCompany(String companyName);
    List<JobDto> getJobsByPersonAndDateRange(Long personId, LocalDate startDate, LocalDate endDate);
}
