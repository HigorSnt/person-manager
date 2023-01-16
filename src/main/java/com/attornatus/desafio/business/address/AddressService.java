package com.attornatus.desafio.business.address;

import com.attornatus.desafio.dto.address.AddressCreationDto;
import com.attornatus.desafio.dto.address.AddressDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressService {

    AddressDto save(Long personId, AddressCreationDto addressCreationDto);

    List<AddressDto> findAllByPersonId(@Param("personId") Long personId);

    AddressDto setAddressAsMain(Long personId, Long addressId);

}
