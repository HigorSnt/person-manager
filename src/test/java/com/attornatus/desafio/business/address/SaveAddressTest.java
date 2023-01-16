package com.attornatus.desafio.business.address;

import com.attornatus.desafio.business.basic.exception.NotFoundException;
import com.attornatus.desafio.business.person.PersonRepository;
import com.attornatus.desafio.dto.address.AddressCreationDto;
import com.attornatus.desafio.entity.address.Address;
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
class SaveAddressTest {

    @Autowired
    private AddressService service;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addressRepository;

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
    void shouldSaveSuccessfully() {
        String streetName = faker.address().streetName();
        long number = Long.parseLong(faker.address().streetAddressNumber());
        String city = faker.address().city();
        String zipCode = faker.address().zipCode();
        AddressCreationDto addressCreationDto = new AddressCreationDto(streetName, zipCode, number, city);

        Assertions.assertEquals(0, addressRepository.findAll().size());
        Assertions.assertEquals(0, person.getAddress().size());
        service.save(person.getId(), addressCreationDto);
        Assertions.assertEquals(1, addressRepository.findAll().size());
        Assertions.assertEquals(1, person.getAddress().size());

        Address address = addressRepository.findAll().stream().findFirst().get();
        Assertions.assertEquals(streetName, address.getStreet());
        Assertions.assertEquals(number, address.getNumber());
        Assertions.assertEquals(city, address.getCity());
        Assertions.assertEquals(zipCode, address.getZipCode());

        Address personAddress = addressRepository.findAll().stream().findFirst().get();
        Assertions.assertTrue(personAddress.getMainAddress());
        Assertions.assertTrue(person.getAddress().contains(personAddress));
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenPersonIdNotExists() {
        String streetName = faker.address().streetName();
        long number = Long.parseLong(faker.address().streetAddressNumber());
        String city = faker.address().city();
        String zipCode = faker.address().zipCode();
        AddressCreationDto addressCreationDto = new AddressCreationDto(streetName, zipCode, number, city);

        Assertions.assertThrows(NotFoundException.class, () -> service.save(-1L, addressCreationDto));
    }

    @Test
    @Transactional
    void shouldSaveSuccessfullyAnotherAddressToSamePerson() {
        shouldSaveSuccessfully();

        String streetName = faker.address().streetName();
        long number = Long.parseLong(faker.address().streetAddressNumber());
        String city = faker.address().city();
        String zipCode = faker.address().zipCode();
        AddressCreationDto addressCreationDto = new AddressCreationDto(streetName, zipCode, number, city);

        Assertions.assertEquals(1, addressRepository.findAll().size());
        Assertions.assertEquals(1, addressRepository.findAll().size());
        Long secondAddressId = service.save(person.getId(), addressCreationDto).id();
        Assertions.assertEquals(2, addressRepository.findAll().size());
        Assertions.assertEquals(2, addressRepository.findAll().size());

        Address secondAddress = addressRepository.findById(secondAddressId).get();
        Assertions.assertEquals(streetName, secondAddress.getStreet());
        Assertions.assertEquals(number, secondAddress.getNumber());
        Assertions.assertEquals(city, secondAddress.getCity());
        Assertions.assertEquals(zipCode, secondAddress.getZipCode());
        Assertions.assertFalse(secondAddress.getMainAddress());
        Assertions.assertTrue(person.getAddress().contains(secondAddress));
    }
}
