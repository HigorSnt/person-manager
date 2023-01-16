package com.attornatus.desafio.business.person;


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
import java.util.List;

@SpringBootTest
@ActiveProfiles({"test"})
class FindAllTest {

    @Autowired
    private PersonService service;

    @Autowired
    private PersonRepository personRepository;

    private final Faker faker = new Faker();

    @BeforeEach
    @Transactional
    void setUp() {
        for (int i = 0; i < 10; i++) {
            String name = faker.name().fullName();
            LocalDate birth = LocalDate.parse(faker.date().birthday("yyyy-MM-dd"));
            Person person = new Person(name, birth);
            personRepository.save(person);
        }
    }

    @Test
    @Transactional
    void shouldListAll() {
        List<PersonDto> people = service.findAllAsDto();
        Assertions.assertEquals(10, people.size());
    }
}
