package com.attornatus.desafio.business.person;

import com.attornatus.desafio.business.basic.exception.NotFoundException;
import com.attornatus.desafio.dto.person.PersonCreationDto;
import com.attornatus.desafio.dto.person.PersonDto;
import com.attornatus.desafio.dto.person.PersonUpdatingDto;
import com.attornatus.desafio.entity.address.Address;
import com.attornatus.desafio.entity.person.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
class PersonServiceImpl implements PersonService {

    private static final String PERSON_NOT_FOUND_MESSAGE = "Person with id = %d not found.";

    private final PersonRepository repository;

    @Autowired
    public PersonServiceImpl(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public PersonDto save(PersonCreationDto personCreationDto) {
        Person person = repository.save(personCreationDto.toEntity());
        return new PersonDto(person);
    }

    @Override
    @Transactional
    public PersonDto update(PersonUpdatingDto personUpdatingDto, Long id) {
        Person person = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(PERSON_NOT_FOUND_MESSAGE.formatted(id)));

        person.setName(personUpdatingDto.name());
        person.setBirthDay(personUpdatingDto.birthDay());
        return new PersonDto(person);
    }

    @Override
    public Person findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(PERSON_NOT_FOUND_MESSAGE.formatted(id)));
    }

    @Override
    public PersonDto findByIdAsDto(Long id) {
        Person person = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(PERSON_NOT_FOUND_MESSAGE.formatted(id)));
        return new PersonDto(person);
    }

    @Override
    public List<PersonDto> findAllAsDto() {
        List<Person> people = repository.findAll();
        return people.stream().map(PersonDto::new).toList();
    }

    @Override
    @Transactional
    public Person addAddress(Long personId, Address address) {
        Person person = findById(personId);
        person.addAddress(address);
        return person;
    }
}
