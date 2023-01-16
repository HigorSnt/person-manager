package com.attornatus.desafio.controller.person;

import com.attornatus.desafio.business.person.PersonService;
import com.attornatus.desafio.dto.person.PersonCreationDto;
import com.attornatus.desafio.dto.person.PersonDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
class SavePersonControllerTest {

    @MockBean
    private PersonService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private final Faker faker = new Faker();

    @Test
    void shouldReturn201WhenSaveSuccessfully() throws Exception {
        String name = faker.name().fullName();
        LocalDate birth = LocalDate.parse(faker.date().birthday("yyyy-MM-dd"));
        PersonCreationDto personDto = new PersonCreationDto(name, birth);

        Mockito.when(service.save(personDto)).thenReturn(new PersonDto(1L, name, birth));

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(personDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.birthDay").value(birth.toString()));
    }

    @Test
    void shouldReturn400WhenMandatoryPropertyAreMissing() throws Exception {
        LocalDate birth = LocalDate.parse(faker.date().birthday("yyyy-MM-dd"));
        PersonCreationDto personDto = new PersonCreationDto(null, birth);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(personDto)))
                .andExpect(status().isBadRequest());
    }
}
