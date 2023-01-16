package com.attornatus.desafio.controller.address;

import com.attornatus.desafio.business.address.AddressService;
import com.attornatus.desafio.business.basic.exception.NotFoundException;
import com.attornatus.desafio.dto.address.AddressDto;
import com.attornatus.desafio.dto.person.PersonDto;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
class SetAddressAsMainControllerTest {

    @MockBean
    private AddressService service;

    @Autowired
    private MockMvc mockMvc;

    private final Faker faker = new Faker();

    @Test
    void shouldReturn200WhenChangeMainAddressSuccessfully() throws Exception {
        AddressDto addressDto = makeAddressDto();
        PersonDto personDto = makePerson();

        Mockito.when(service.setAddressAsMain(1L, 2L))
                .thenReturn(addressDto);

        mockMvc.perform(patch("/address/{personId}", 1L)
                        .param("addressId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(addressDto.id()))
                .andExpect(jsonPath("$.street").value(addressDto.street()))
                .andExpect(jsonPath("$.zipCode").value(addressDto.zipCode()))
                .andExpect(jsonPath("$.number").value(addressDto.number()))
                .andExpect(jsonPath("$.city").value(addressDto.city()))
                .andExpect(jsonPath("$.mainAddress").value(addressDto.mainAddress()));
    }

    @Test
    void shouldReturn404WhenAddressNotFound() throws Exception {
        String errorMessage = "Address with id = 2 not found.";
        Mockito.when(service.setAddressAsMain(1L, 2L))
                .thenThrow(new NotFoundException(errorMessage));

        mockMvc.perform(patch("/address/{personId}", 1L)
                        .param("addressId", "2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    private AddressDto makeAddressDto() {
        String streetName = faker.address().streetName();
        long number = Long.parseLong(faker.address().streetAddressNumber());
        String city = faker.address().city();
        String zipCode = faker.address().zipCode();

        return new AddressDto(2L, streetName, zipCode, number, city, true);
    }

    private PersonDto makePerson() {
        String name = faker.name().fullName();
        LocalDate birth = LocalDate.parse(faker.date().birthday("yyyy-MM-dd"));
        return new PersonDto(1L, name, birth);
    }
}
