package com.attornatus.desafio.controller.person;

import com.attornatus.desafio.business.basic.exception.NotFoundException;
import com.attornatus.desafio.business.person.PersonService;
import com.attornatus.desafio.dto.person.PersonDto;
import com.attornatus.desafio.dto.person.PersonUpdatingDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
class UpdatePersonControllerTest {

    @MockBean
    private PersonService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private final Faker faker = new Faker();

    public static final String PERSON_NOT_FOUND_MESSAGE = "Person with id = %d not found.";

    @Test
    void shouldReturn200WhenUpdateSuccesfully() throws Exception {
        long id = 1L;
        String name = faker.name().fullName();
        LocalDate birth = LocalDate.parse(faker.date().birthday("yyyy-MM-dd"));
        PersonUpdatingDto personDto = new PersonUpdatingDto(name, birth);

        Mockito.when(service.update(personDto, id)).thenReturn(new PersonDto(id, name, birth));

        mockMvc.perform(put("/person/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(personDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.birthDay").value(birth.toString()));
    }

    @Test
    void shouldReturn400WhenMandatoryPropertyAreMissing() throws Exception {
        LocalDate birth = LocalDate.parse(faker.date().birthday("yyyy-MM-dd"));
        PersonUpdatingDto personDto = new PersonUpdatingDto(null, birth);

        mockMvc.perform(put("/person/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(personDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenNotExistsPersonWithId() throws Exception {
        long id = 1L;
        String name = faker.name().fullName();
        LocalDate birth = LocalDate.parse(faker.date().birthday("yyyy-MM-dd"));
        PersonUpdatingDto personDto = new PersonUpdatingDto(name, birth);

        String errorMessage = PERSON_NOT_FOUND_MESSAGE.formatted(id);
        Mockito.when(service.update(personDto, id))
                .thenThrow(new NotFoundException(errorMessage));

        mockMvc.perform(put("/person/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(personDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }
}
