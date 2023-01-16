package com.attornatus.desafio.dto.address;

import com.attornatus.desafio.entity.address.Address;
import jakarta.validation.constraints.NotNull;

public record AddressCreationDto(
        @NotNull String streetName,
        @NotNull String zipCode,
        @NotNull Long number,
        @NotNull String city) {

    public Address toEntity() {
        return new Address(
                streetName,
                zipCode,
                number,
                city);
    }

}
