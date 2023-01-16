package com.attornatus.desafio.controller.address;

import com.attornatus.desafio.business.address.AddressService;
import com.attornatus.desafio.dto.address.AddressCreationDto;
import com.attornatus.desafio.dto.address.AddressDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @Operation(summary = "Save an address to a person")
    @ApiResponse(
            responseCode = "201",
            description = "Saved address",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = AddressDto.class))})
    @PostMapping
    public ResponseEntity<AddressDto> save(@RequestParam("personId") Long personId,
                                           @Valid @RequestBody AddressCreationDto addressCreationDto) {
        return new ResponseEntity<>(service.save(personId, addressCreationDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all addresses of a person")
    @ApiResponse(
            responseCode = "200",
            description = "A person's address list",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AddressDto.class)))})
    @GetMapping("/{personId}")
    public ResponseEntity<List<AddressDto>> findAllByPersonId(@PathVariable("personId") Long personId) {
        return new ResponseEntity<>(service.findAllByPersonId(personId), HttpStatus.OK);
    }

    @Operation(summary = "Sets an address as primary")
    @ApiResponse(
            responseCode = "200",
            description = "The new main address",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = AddressDto.class))})
    @PatchMapping("/{personId}")
    public ResponseEntity<AddressDto> setAddressAsMain(@PathVariable("personId") Long personId,
                                                       @RequestParam("addressId") Long addressId) {
        return new ResponseEntity<>(service.setAddressAsMain(personId, addressId), HttpStatus.OK);
    }
}
