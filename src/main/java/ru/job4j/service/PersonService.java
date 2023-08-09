package ru.job4j.service;

import ru.job4j.domain.Person;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public interface PersonService {
    Iterable<Person> findAll();

    Optional<Person> findById(int id);

    Optional<Person> save(Person person);

    Optional<Person> updatePatch(Person person)
            throws InvocationTargetException, IllegalAccessException;

    Optional<Person> update(Person person);

    Optional<Person> deleteById(int personId);

    Optional<Person> findPersonByLogin(String login);

}
