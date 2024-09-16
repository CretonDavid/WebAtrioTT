package com.steekse.webatrioerp.service.impl;

import com.steekse.webatrioerp.dto.JobDto;
import com.steekse.webatrioerp.dto.PersonDto;
import com.steekse.webatrioerp.exception.AgeLimitExceededException;
import com.steekse.webatrioerp.exception.PersonNotFoundException;
import com.steekse.webatrioerp.mapper.JobMapper;
import com.steekse.webatrioerp.mapper.PersonMapper;
import com.steekse.webatrioerp.model.Job;
import com.steekse.webatrioerp.model.Person;
import com.steekse.webatrioerp.repository.JobRepository;
import com.steekse.webatrioerp.repository.PersonRepository;
import com.steekse.webatrioerp.service.IPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements IPersonService {

    private static final Logger logger = LoggerFactory.getLogger(IPersonService.class);
    private final PersonRepository personRepository;
    private final JobRepository jobRepository;

    public PersonServiceImpl(PersonRepository personRepository, JobRepository jobRepository) {
        this.personRepository = personRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    public void createPerson(PersonDto personDto) {
        Person person = PersonMapper.INSTANCE.toEntity(personDto);
        int age = LocalDate.now().getYear() - person.getBirthDate().getYear();
        if (age >= 150) {
            throw new AgeLimitExceededException("Person cannot be more than 150 years old");
        }
        personRepository.save(person);
        logger.info("New person has been created");
    }

    @Override
    public void addJobToPerson(Long personId, JobDto jobDto) {
        Optional<Person> personOpt = personRepository.findById(personId);
        if (personOpt.isPresent()) {
            Job job = JobMapper.INSTANCE.toEntity(jobDto);
            job.setPerson(personOpt.get());
            jobRepository.save(job);
        } else {
            throw new PersonNotFoundException("Person not found");
        }
        logger.info("New Job has been added to :" + personId);
    }

    @Override
    public List<PersonDto> getAllPersonsSorted() {
        return personRepository.findAllByOrderByLastNameAsc()
                .stream()
                .map(PersonMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonDto> getPersonsByCompany(String companyName) {
        return personRepository.findByJobsCompanyName(companyName)
                .stream()
                .map(PersonMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobDto> getJobsByPersonAndDateRange(Long personId, LocalDate startDate, LocalDate endDate) {
        Optional<Person> personOpt = personRepository.findById(personId);
        if (personOpt.isPresent()) {
            return jobRepository.findByPersonAndStartDateBetween(personOpt.get(), startDate, endDate)
                    .stream()
                    .map(JobMapper.INSTANCE::toDto)
                    .collect(Collectors.toList());
        } else {
            throw new PersonNotFoundException("Person not found");
        }
    }
}
