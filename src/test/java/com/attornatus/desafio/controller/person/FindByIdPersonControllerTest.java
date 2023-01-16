package com.attornatus.desafio.controller.person;

import com.attornatus.desafio.business.basic.exception.NotFoundException;
import com.attornatus.desafio.business.person.PersonService;
import com.attornatus.desafio.dto.person.PersonDto;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
class FindByIdPersonControllerTest {

    @MockBean
    private PersonService service;

    @Autowired
    private MockMvc mockMvc;

    private final Faker faker = new Faker();

    public static final String PERSON_NOT_FOUND_MESSAGE = "Person with id = %d not found.";

    @Test
    void shouldReturn200WhenFindPerson() throws Exception {
        long id = 1L;
        String name = faker.name().fullName();
        LocalDate birth = LocalDate.parse(faker.date().birthday("yyyy-MM-dd"));

        Mockito.when(service.findByIdAsDto(id)).thenReturn(new PersonDto(id, name, birth));

        mockMvc.perform(get("/person/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.birthDay").value(birth.toString()));
    }

    @Test
    void shouldReturn404WhenNotExistsPersonWithId() throws Exception {
        long id = 10L;

        String errorMessage = PERSON_NOT_FOUND_MESSAGE.formatted(id);
        Mockito.when(service.findByIdAsDto(id))
                .thenThrow(new NotFoundException(errorMessage));

        mockMvc.perform(get("/person/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }
}
