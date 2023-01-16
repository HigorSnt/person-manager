package com.attornatus.desafio.business.person;

import com.attornatus.desafio.dto.person.PersonCreationDto;
import com.attornatus.desafio.dto.person.PersonDto;
import com.attornatus.desafio.dto.person.PersonUpdatingDto;
import com.attornatus.desafio.entity.address.Address;
import com.attornatus.desafio.entity.person.Person;

import java.util.List;

public interface PersonService {

    PersonDto save(PersonCreationDto personCreationDto);

    PersonDto update(PersonUpdatingDto personUpdatingDto, Long id);

    Person findById(Long id);

    PersonDto findByIdAsDto(Long id);

    List<PersonDto> findAllAsDto();

    Person addAddress(Long personId, Address address);

}
