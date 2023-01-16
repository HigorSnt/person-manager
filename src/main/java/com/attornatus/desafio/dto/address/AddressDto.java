package com.attornatus.desafio.dto.address;

import com.attornatus.desafio.entity.address.Address;

public record AddressDto(Long id, String street, String zipCode, Long number, String city, Boolean mainAddress) {

    public AddressDto(Address address) {
        this(
                address.getId(),
                address.getStreet(),
                address.getZipCode(),
                address.getNumber(),
                address.getCity(),
                address.getMainAddress()
        );
    }
}
