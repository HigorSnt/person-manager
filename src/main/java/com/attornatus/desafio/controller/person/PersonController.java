package com.attornatus.desafio.controller.person;

import com.attornatus.desafio.business.person.PersonService;
import com.attornatus.desafio.dto.person.PersonCreationDto;
import com.attornatus.desafio.dto.person.PersonDto;
import com.attornatus.desafio.dto.person.PersonUpdatingDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService service;

    @Autowired
    public PersonController(PersonService service) {
        this.service = service;
    }

    @Operation(summary = "Save a person")
    @ApiResponse(
            responseCode = "201",
            description = "Saved person",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = PersonDto.class))})
    @PostMapping
    public ResponseEntity<PersonDto> save(@Valid @RequestBody PersonCreationDto personCreationDto) {
        return new ResponseEntity<>(service.save(personCreationDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a person")
    @ApiResponse(
            responseCode = "200",
            description = "Updated person",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = PersonDto.class))})
    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> update(@Valid @RequestBody PersonUpdatingDto personUpdatingDto,
                                            @PathVariable("id") Long id) {
        return new ResponseEntity<>(service.update(personUpdatingDto, id), HttpStatus.OK);
    }

    @Operation(summary = "Get a person by id")
    @ApiResponse(
            responseCode = "200",
            description = "Founded person",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = PersonDto.class))})
    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.findByIdAsDto(id), HttpStatus.OK);
    }

    @Operation(summary = "List all saved people")
    @ApiResponse(
            responseCode = "200",
            description = "All saved people",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PersonDto.class)))})
    @GetMapping
    public ResponseEntity<List<PersonDto>> findAll() {
        return new ResponseEntity<>(service.findAllAsDto(), HttpStatus.OK);
    }
}
