package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.repository.PersonRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
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
     * Для этого мы можем воспользоваться рефлексией для вызова нужных
     * геттеров и сеттеров.
     * @param person
     * @return Optional<Person>
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Override
    public Optional<Person> updatePatch(Person person) throws InvocationTargetException, IllegalAccessException {
        var current = personRepository.findById(person.getId());
        if (current.isEmpty()) {
            throw new NullPointerException("Person id: " + person.getId() + " , not found");
        }
        var methods = current.get().getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method : methods) {
            String name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                Method getMethod = namePerMethod.get(name);
                Method setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    return Optional.empty();
                }

                Object newValue = getMethod.invoke(person);
                if (newValue != null) {
                    setMethod.invoke(current.get(), newValue);
                }
            }
        }
        personRepository.save(current.get());
        return current;
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

    @Override
    public Optional<Person> findPersonByLogin(String login) {
        return personRepository.findPersonByLogin(login);
    }

}
