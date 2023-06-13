package ru.job4j.service;

import ru.job4j.domain.Person;

import java.util.Optional;

public interface PersonService {
    Iterable<Person> findAll();

    Optional<Person> findById(int id);

    Optional<Person> save(Person person);

    void update(Person person);

    void delete(Person person);

}
