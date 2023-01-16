package com.attornatus.desafio.controller.address;

import com.attornatus.desafio.business.address.AddressService;
import com.attornatus.desafio.dto.address.AddressCreationDto;
import com.attornatus.desafio.dto.address.AddressDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
class SaveAddressControllerTest {

    @MockBean
    private AddressService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private final Faker faker = new Faker();

    @Test
    void shouldReturn201WhenSaveSuccessfully() throws Exception {
        String streetName = faker.address().streetName();
        long number = Long.parseLong(faker.address().streetAddressNumber());
        String city = faker.address().city();
        String zipCode = faker.address().zipCode();
        AddressCreationDto addressCreationDto = new AddressCreationDto(streetName, zipCode, number, city);

        long id = 1L;
        AddressDto addressDto = new AddressDto(id, streetName, zipCode, number, city, true);
        Mockito.when(service.save(id, addressCreationDto))
                .thenReturn(addressDto);

        mockMvc.perform(post("/address")
                        .param("personId", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(addressCreationDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(addressDto.id()))
                .andExpect(jsonPath("$.street").value(addressDto.street()))
                .andExpect(jsonPath("$.zipCode").value(addressDto.zipCode()))
                .andExpect(jsonPath("$.number").value(addressDto.number()))
                .andExpect(jsonPath("$.city").value(addressDto.city()))
                .andExpect(jsonPath("$.mainAddress").value(addressDto.mainAddress()));
    }
}
