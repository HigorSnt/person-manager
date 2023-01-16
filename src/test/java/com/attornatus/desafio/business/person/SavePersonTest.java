package com.attornatus.desafio.business.person;

import com.attornatus.desafio.dto.person.PersonCreationDto;
import com.attornatus.desafio.entity.person.Person;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@ActiveProfiles({"test"})
class SavePersonTest {

    @Autowired
    private PersonService service;

    @Autowired
    private PersonRepository personRepository;

    private final Faker faker = new Faker();

    @Test
    @Transactional
    void shouldSaveSuccessfully() {
        Assertions.assertEquals(0, personRepository.findAll().size());

        String name = faker.name().fullName();
        LocalDate birth = LocalDate.parse(faker.date().birthday("yyyy-MM-dd"));
        PersonCreationDto personDto = new PersonCreationDto(name, birth);
        service.save(personDto);

        Assertions.assertEquals(1, personRepository.findAll().size());
        Person person = personRepository.findAll().stream().findFirst().orElseThrow();
        Assertions.assertEquals(name, person.getName());
        Assertions.assertEquals(birth, person.getBirthDay());
    }
}
