package com.attornatus.desafio.business.address;

import com.attornatus.desafio.business.basic.exception.NotFoundException;
import com.attornatus.desafio.business.person.PersonService;
import com.attornatus.desafio.dto.address.AddressCreationDto;
import com.attornatus.desafio.dto.address.AddressDto;
import com.attornatus.desafio.entity.address.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
class AddressServiceImpl implements AddressService {

    private static final String PERSON_NOT_FOUND_MESSAGE = "Person with id = %d not found.";
    private static final String ADDRESS_NOT_FOUND_MESSAGE = "Address with id = %d not found.";

    private final AddressRepository repository;

    private final PersonService personService;

    @Autowired
    public AddressServiceImpl(AddressRepository repository, PersonService personService) {
        this.repository = repository;
        this.personService = personService;
    }

    @Override
    @Transactional
    public AddressDto save(Long personId, AddressCreationDto addressCreationDto) {
        Address address = addressCreationDto.toEntity();

        if (!repository.existsMainAddress(personId)) {
            address.setMainAddress(true);
        }

        address = repository.save(address);
        personService.addAddress(personId, address);
        return new AddressDto(address);
    }

    @Override
    public List<AddressDto> findAllByPersonId(Long personId) {
        return repository.findAllByPersonId(personId)
                .stream()
                .map(AddressDto::new)
                .toList();
    }

    @Override
    @Transactional
    public AddressDto setAddressAsMain(Long personId, Long addressId) {
        Address oldMainAddress = repository.findMainAddressByPersonId(personId);

        if (oldMainAddress == null) {
            throw new NotFoundException(PERSON_NOT_FOUND_MESSAGE.formatted(personId));
        }

        oldMainAddress.setMainAddress(false);

        Address newMainAddress = repository.findByPersonIdAndAddressId(personId, addressId);

        if (newMainAddress == null) {
            throw new NotFoundException(ADDRESS_NOT_FOUND_MESSAGE.formatted(personId));
        }

        newMainAddress.setMainAddress(true);
        return new AddressDto(newMainAddress);
    }
}
