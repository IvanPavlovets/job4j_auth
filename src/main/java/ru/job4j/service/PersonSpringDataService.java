package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.repository.PersonRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonSpringDataService implements PersonService {
    private final PersonRepository personRepository;

    @Override
    public Iterable<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    @Override
    public Optional<Person> save(Person person) {
        Optional<Person> rsl = Optional.empty();
        try {
            rsl = Optional.of(personRepository.save(person));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    public Optional<Person> update(Person person) {
        var findedPerson = personRepository.findById(person.getId());
        if (findedPerson.isPresent()) {
            personRepository.save(person);
        }
        return findedPerson;
    }

    @Override
    public Optional<Person> deleteById(int personId) {
        var findedPerson = personRepository.findById(personId);
        if (findedPerson.isPresent()) {
            personRepository.deleteById(personId);
        }
        return findedPerson;
    }

}
