package com.steekse.webatrioerp;

import com.steekse.webatrioerp.dto.JobDto;
import com.steekse.webatrioerp.dto.PersonDto;
import com.steekse.webatrioerp.exception.AgeLimitExceededException;
import com.steekse.webatrioerp.exception.PersonNotFoundException;
import com.steekse.webatrioerp.mapper.PersonMapper;
import com.steekse.webatrioerp.model.Job;
import com.steekse.webatrioerp.model.Person;
import com.steekse.webatrioerp.repository.PersonRepository;
import com.steekse.webatrioerp.repository.JobRepository;
import com.steekse.webatrioerp.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;
    @Mock
    private JobRepository jobRepository;
    @InjectMocks
    private PersonServiceImpl personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test pour la méthode createPerson
    @Test
    void testSavePerson_ValidAge() {
        // Given
        PersonDto personDto = new PersonDto();
        personDto.setFirstName("John");
        personDto.setLastName("Doe");
        personDto.setBirthDate(LocalDate.now().minusYears(30)); // 30 years old

        Person person = PersonMapper.INSTANCE.toEntity(personDto);

        Mockito.when(personRepository.save(ArgumentMatchers.any(Person.class))).thenReturn(person);

        // When
        personService.createPerson(personDto);

        // Then
        Mockito.verify(personRepository, Mockito.times(1)).save(ArgumentMatchers.any(Person.class));
    }

    @Test
    void testSavePerson_AgeExceedsLimit() {
        // Given
        PersonDto personDto = new PersonDto();
        personDto.setFirstName("John");
        personDto.setLastName("Doe");
        personDto.setBirthDate(LocalDate.now().minusYears(151)); // 151 years old

        // When & Then
        Assertions.assertThrows(AgeLimitExceededException.class, () -> personService.createPerson(personDto));
        Mockito.verify(personRepository, Mockito.times(0)).save(ArgumentMatchers.any(Person.class)); // Should not save
    }

    // Test pour la méthode addJobToPerson
    @Test
    void testAddJobToPerson_Success() {
        // Given
        Person person = new Person();
        person.setId(1L);
        JobDto jobDto = new JobDto();
        jobDto.setCompanyName("TechCorp");
        jobDto.setPosition("Developer");
        jobDto.setStartDate(LocalDate.now().minusYears(1));

        Mockito.when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        Mockito.when(jobRepository.save(ArgumentMatchers.any(Job.class))).thenReturn(new Job());

        // When
        personService.addJobToPerson(1L, jobDto);

        // Then
        Mockito.verify(jobRepository, Mockito.times(1)).save(ArgumentMatchers.any(Job.class));
    }

    @Test
    void testAddJobToPerson_PersonNotFound() {
        // Given
        JobDto jobDto = new JobDto();
        jobDto.setCompanyName("TechCorp");

        Mockito.when(personRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(PersonNotFoundException.class, () -> personService.addJobToPerson(1L, jobDto));
        Mockito.verify(jobRepository, Mockito.times(0)).save(ArgumentMatchers.any(Job.class)); // Ne doit pas appeler save
    }

    // Test pour la méthode getAllPersonsSorted
    @Test
    void testGetAllPersonsSorted() {
        // Given
        Person person1 = new Person();
        person1.setLastName("Doe");

        Person person2 = new Person();
        person2.setLastName("Smith");

        Mockito.when(personRepository.findAllByOrderByLastNameAsc()).thenReturn(List.of(person1, person2));

        // When
        List<PersonDto> result = personService.getAllPersonsSorted();

        // Then
        Assertions.assertEquals(2, result.size());
        Mockito.verify(personRepository, Mockito.times(1)).findAllByOrderByLastNameAsc();
    }

    // Test pour la méthode getPersonsByCompany
    @Test
    void testGetPersonsByCompany() {
        // Given
        Person person1 = new Person();
        person1.setLastName("Doe");

        Mockito.when(personRepository.findByJobsCompanyName("TechCorp")).thenReturn(List.of(person1));

        // When
        List<PersonDto> result = personService.getPersonsByCompany("TechCorp");

        // Then
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Doe", result.get(0).getLastName());
        Mockito.verify(personRepository, Mockito.times(1)).findByJobsCompanyName("TechCorp");
    }

    // Test pour la méthode getJobsByPersonAndDateRange
    @Test
    void testGetJobsByPersonAndDateRange_Success() {
        // Given
        Person person = new Person();
        person.setId(1L);

        Job job = new Job();
        job.setCompanyName("TechCorp");

        Mockito.when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        Mockito.when(jobRepository.findByPersonAndStartDateBetween(person, LocalDate.now().minusYears(2), LocalDate.now()))
                .thenReturn(List.of(job));

        // When
        List<JobDto> result = personService.getJobsByPersonAndDateRange(1L, LocalDate.now().minusYears(2), LocalDate.now());

        // Then
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("TechCorp", result.get(0).getCompanyName());
        Mockito.verify(jobRepository, Mockito.times(1)).findByPersonAndStartDateBetween(person, LocalDate.now().minusYears(2), LocalDate.now());
    }

    @Test
    void testGetJobsByPersonAndDateRange_PersonNotFound() {
        // Given
        Mockito.when(personRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(PersonNotFoundException.class, () ->
                personService.getJobsByPersonAndDateRange(1L, LocalDate.now().minusYears(2), LocalDate.now()));
        Mockito.verify(jobRepository, Mockito.times(0)).findByPersonAndStartDateBetween(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
    }
}
