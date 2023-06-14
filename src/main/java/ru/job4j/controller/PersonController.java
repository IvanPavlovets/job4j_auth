package ru.job4j.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.service.PersonSpringDataService;

import java.util.Optional;

/**
 * контроллер описывает CRUD операции
 * и построен по схеме Rest архитектуры:
 * GET/person/ список всех пользователей.
 * GET/person/{id} - пользователь с id.
 * POST/person/ - создает пользователя.
 * PUT/person/ - обновляет пользователя.
 * DELETE/person/ - удаляет.
 */
@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {
    private final PersonSpringDataService persons;

    @GetMapping("/")
    public Iterable<Person> findAll() {
        return this.persons.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        return getResponseEntity(this.persons.findById(id), HttpStatus.OK, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return getResponseEntity(this.persons.save(person), HttpStatus.CREATED, HttpStatus.CONFLICT);
    }

    @PutMapping("/")
    public ResponseEntity<Person> update(@RequestBody Person person) {
        return getResponseEntity(this.persons.save(person), HttpStatus.OK, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Person> delete(@PathVariable int id) {
        return getResponseEntity(this.persons.deleteById(id), HttpStatus.OK, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Person> getResponseEntity(Optional<Person> person, HttpStatus ok, HttpStatus notFound) {
        return new ResponseEntity<Person>(
                person.orElse(new Person()),
                person.isPresent() ? ok : notFound
        );
    }

}
