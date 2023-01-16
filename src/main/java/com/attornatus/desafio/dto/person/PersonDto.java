package com.attornatus.desafio.dto.person;

import com.attornatus.desafio.entity.person.Person;

import java.time.LocalDate;

public record PersonDto(Long id, String name, LocalDate birthDay) {

    public PersonDto(Person person) {
        this(
                person.getId(),
                person.getName(),
                person.getBirthDay()
        );
    }

}
