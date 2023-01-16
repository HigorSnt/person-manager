package com.attornatus.desafio.dto.person;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record PersonUpdatingDto(@NotNull
                                String name,

                                @NotNull
                                @DateTimeFormat(pattern = "yyyy-MM-dd")
                                LocalDate birthDay) {
}
