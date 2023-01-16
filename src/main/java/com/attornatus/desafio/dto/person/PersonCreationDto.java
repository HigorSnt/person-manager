package com.attornatus.desafio.dto.person;

import com.attornatus.desafio.entity.person.Person;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record PersonCreationDto(
        @NotNull
        String name,

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate birthDay) {

    public Person toEntity() {
        return new Person(name, birthDay);
    }

}
