package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.domain.PersonDTO;
import ru.job4j.repository.PersonRepository;

import java.util.Optional;

/**
 * Слой бизнес обработки модели Person
 */
@Service
@AllArgsConstructor
public class PersonSpringDataService implements PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder encoder;

    @Override
    public Iterable<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    /**
     * в методе шифруем password,
     * установлиный пользователем.
     * @param person
     * @return Optional<Person>
     */
    @Override
    public Optional<Person> save(Person person) {
        Optional<Person> rsl = Optional.empty();
        try {
            person.setPassword(encoder.encode(person.getPassword()));
            rsl = Optional.of(personRepository.save(person));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsl;
    }


    /**
     * метод PATCH, который предназначен для частичного обновления данных.
     * подставляем найденому person, переданый password из DTO.
     * @param personDTO
     * @return Optional<Person>
     */
    @Override
    public Optional<Person> updatePatch(PersonDTO personDTO) {
        var findedPerson = personRepository.findById(personDTO.id());
        if (findedPerson.isPresent()) {
            Person person = findedPerson.get();
            person.setPassword(personDTO.password());
            return this.save(person);
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

    @Override
    public Optional<Person> findPersonByLogin(String login) {
        return personRepository.findPersonByLogin(login);
    }

}
