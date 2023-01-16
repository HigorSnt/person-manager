package com.attornatus.desafio.business.address;

import com.attornatus.desafio.business.person.PersonRepository;
import com.attornatus.desafio.dto.address.AddressCreationDto;
import com.attornatus.desafio.dto.address.AddressDto;
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
class FindAllByPersonIdTest {

    @Autowired
    private AddressService service;

    @Autowired
    private PersonRepository personRepository;

    private final Faker faker = new Faker();
    private Person person;

    @BeforeEach
    @Transactional
    void setUp() {
        person = makePerson();

        for (int i = 0; i < 10; i++) {
            String streetName = faker.address().streetName();
            long number = Long.parseLong(faker.address().streetAddressNumber());
            String city = faker.address().city();
            String zipCode = faker.address().zipCode();
            AddressCreationDto addressCreationDto = new AddressCreationDto(streetName, zipCode, number, city);
            service.save(person.getId(), addressCreationDto);
        }
    }

    @Test
    @Transactional
    void shouldListAllAddressByPersonId() {
        List<AddressDto> allAddress = service.findAllByPersonId(person.getId());
        Assertions.assertEquals(10, allAddress.size());
    }

    @Test
    @Transactional
    void shouldReturnEmptyListWhenPersonHasNoAddress() {
        Person person = makePerson();
        Assertions.assertEquals(0, service.findAllByPersonId(person.getId()).size());
    }

    private Person makePerson() {
        String name = faker.name().fullName();
        LocalDate birth = LocalDate.parse(faker.date().birthday("yyyy-MM-dd"));
        Person person = new Person(name, birth);
        return personRepository.save(person);
    }
}
