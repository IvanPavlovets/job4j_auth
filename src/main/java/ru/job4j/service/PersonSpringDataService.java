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
        return Optional.of(personRepository.save(person));
    }

    @Override
    public void update(Person person) {
        var findedPerson = personRepository.findById(person.getId());
        if (findedPerson.isEmpty()) {
            throw new NullPointerException("Person id: " + person.getId() + " , not found");
        }
        personRepository.save(person);
    }

    @Override
    public void delete(Person person) {
        var findedPerson = personRepository.findById(person.getId());
        if (findedPerson.isEmpty()) {
            throw new NullPointerException("Person id: " + person.getId() + " , not found");
        }
        personRepository.delete(person);
    }

}
