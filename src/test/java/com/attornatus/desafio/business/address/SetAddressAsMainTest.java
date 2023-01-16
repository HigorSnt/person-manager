package com.attornatus.desafio.business.address;

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
import java.util.List;

@SpringBootTest
@ActiveProfiles({"test"})
class SetAddressAsMainTest {

    @Autowired
    private AddressService service;

    @Autowired
    private PersonRepository personRepository;

    private final Faker faker = new Faker();
    private Person person;
    private Long addressId;

    @BeforeEach
    @Transactional
    void setUp() {
        person = makePerson();

        for (int i = 0; i < 2; i++) {
            String streetName = faker.address().streetName();
            long number = Long.parseLong(faker.address().streetAddressNumber());
            String city = faker.address().city();
            String zipCode = faker.address().zipCode();
            AddressCreationDto addressCreationDto = new AddressCreationDto(streetName, zipCode, number, city);
            addressId = service.save(person.getId(), addressCreationDto).id();
        }
    }

    @Test
    @Transactional
    void shouldChangeMainAddressSuccessfully() {
        Assertions.assertEquals(2, person.getAddress().size());
        Address oldMainAddress = person.getAddress().stream().filter(Address::getMainAddress).findFirst().get();
        Assertions.assertNotEquals(addressId, oldMainAddress.getId());

        service.setAddressAsMain(person.getId(), addressId);
        List<Address> personMainAddress = person.getAddress().stream().filter(Address::getMainAddress).toList();
        Assertions.assertEquals(1, personMainAddress.size());
        Assertions.assertEquals(addressId, personMainAddress.get(0).getId());
    }

    private Person makePerson() {
        String name = faker.name().fullName();
        LocalDate birth = LocalDate.parse(faker.date().birthday("yyyy-MM-dd"));
        Person person = new Person(name, birth);
        this.person = personRepository.save(person);
        return person;
    }
}
