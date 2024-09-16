package com.steekse.webatrioerp.controller;

import com.steekse.webatrioerp.dto.PersonDto;
import com.steekse.webatrioerp.dto.JobDto;
import com.steekse.webatrioerp.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private IPersonService personService;

    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody PersonDto personDto) {
        personService.createPerson(personDto);
        return ResponseEntity.ok("Person successfully created");
    }

    @PostMapping("/{personId}/jobs")
    public ResponseEntity<String> addJobToPerson(@PathVariable Long personId, @RequestBody JobDto jobDto) {
        personService.addJobToPerson(personId, jobDto);
        return ResponseEntity.ok("Job successfully added to : " + personId);
    }

    @GetMapping("/all")
    public List<PersonDto> getAllPersons() {
        return personService.getAllPersonsSorted();
    }

    @GetMapping("/company/{companyName}")
    public List<PersonDto> getPersonsByCompany(@PathVariable String companyName) {
        return personService.getPersonsByCompany(companyName);
    }

    @GetMapping("/{personId}/jobs")
    public List<JobDto> getJobsByPersonAndDateRange(@PathVariable Long personId,
                                                    @RequestParam LocalDate startDate,
                                                    @RequestParam LocalDate endDate) {
        return personService.getJobsByPersonAndDateRange(personId, startDate, endDate);
    }
}
