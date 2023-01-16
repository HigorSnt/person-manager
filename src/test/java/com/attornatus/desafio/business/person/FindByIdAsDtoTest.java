package com.attornatus.desafio.business.person;

import com.attornatus.desafio.business.basic.exception.NotFoundException;
import com.attornatus.desafio.dto.person.PersonDto;
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
class FindByIdAsDtoTest {

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
    void shouldFindPersonByIdSuccessfully() {
        PersonDto personDto = service.findByIdAsDto(person.getId());

        Assertions.assertNotNull(personDto);
        Assertions.assertEquals(person.getName(), personDto.name());
        Assertions.assertEquals(person.getBirthDay(), personDto.birthDay());
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenIdNotExists() {
        NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class,
                () -> service.findByIdAsDto(-1L));

        Assertions.assertEquals("Person with id = -1 not found.", notFoundException.getMessage());
    }
}
