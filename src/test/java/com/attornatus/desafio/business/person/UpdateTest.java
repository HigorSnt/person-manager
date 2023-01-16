package com.attornatus.desafio.business.person;

import com.attornatus.desafio.business.basic.exception.NotFoundException;
import com.attornatus.desafio.dto.person.PersonUpdatingDto;
import com.attornatus.desafio.entity.person.Person;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@ActiveProfiles({"test"})
class UpdateTest {

    @Autowired
    private PersonService service;

    @Autowired
    private PersonRepository personRepository;

    private final Faker faker = new Faker();

    private Person person;

    @BeforeEach
    @Transactional
    void setUp() {
        String name = faker.name().fullName();
        LocalDate birth = LocalDate.parse(faker.date().birthday("yyyy-MM-dd"));
        Person person = new Person(name, birth);

        this.person = personRepository.save(person);
    }

    @Test
    @Transactional
    void shouldUpdateSuccessfully() {
        LocalDate newBirthDate = LocalDate.parse("1990-10-09");
        String newName = "João";

        Person personToUpdate = personRepository.findById(person.getId()).orElseThrow();
        Assertions.assertNotEquals(newName, personToUpdate.getName());
        Assertions.assertNotEquals(newBirthDate, personToUpdate.getBirthDay());

        PersonUpdatingDto personUpdatingDto = new PersonUpdatingDto(newName, newBirthDate);
        service.update(personUpdatingDto, person.getId());

        Person updatedPerson = personRepository.findById(person.getId()).orElseThrow();
        Assertions.assertEquals(newName, updatedPerson.getName());
        Assertions.assertEquals(newBirthDate, updatedPerson.getBirthDay());
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenTryToUpdateAUnregisteredPerson() {
        PersonUpdatingDto personUpdatingDto = new PersonUpdatingDto("João", LocalDate.parse("1990-10-09"));
        NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class,
                () -> service.update(personUpdatingDto, -1L));
        Assertions.assertEquals("Person with id = -1 not found.", notFoundException.getMessage());
    }
}
