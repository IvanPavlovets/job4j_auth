package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Person;
import ru.job4j.domain.PersonRecord;
import ru.job4j.handlers.Operation;
import ru.job4j.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private static final Logger LOG = LoggerFactory.getLogger(PersonController.class.getSimpleName());
    private final PersonService persons;
    private final ObjectMapper objectMapper;

    @PostMapping("/sign-up")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Person> signUp(@Valid @RequestBody Person person) {
        if (person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("Login and password mustn't be empty");
        }
        if (person.getPassword().length() < 3 || person.getPassword().isEmpty() || person.getPassword().isBlank()) {
            throw new IllegalArgumentException(
                    "Invalid password. Password length must be more than 3 characters.");
        }
        return getResponseEntity(this.persons.save(person));
    }

    @GetMapping("/all")
    public Iterable<Person> findAll() {
        return this.persons.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        return getResponseEntityById(this.persons.findById(id), id);
    }


    @PutMapping("/")
    public ResponseEntity<Person> updatePatch(@RequestBody PersonRecord personRecord) {
        if (personRecord.password() == null) {
            throw new NullPointerException("Password mustn't be empty");
        }
        if (personRecord.password().length() < 3 || personRecord.password().length() > 8
                || personRecord.password().isBlank()) {
            throw new IllegalArgumentException(
                    "Invalid password. Password length must be more than 3 characters and less 8.");
        }
        return getResponseEntityById(this.persons.updatePatch(personRecord), personRecord.id());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Person> delete(@PathVariable int id) {
        return getResponseEntityById(this.persons.deleteById(id), id);
    }

    /**
     * Получения отклика и ResponseStatusException для методо поиска по id
     * @param person полученый person
     * @param id person id
     * @return ResponseEntity<Person> or exception
     */
    private ResponseEntity<Person> getResponseEntityById(Optional<Person> person, int id) {
        return new ResponseEntity<>(
                person.orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Person id: " + id + ", is not found. Please, check id."
                        )
                ),
                new MultiValueMapAdapter<>(Map.of("Job4jCustomHeader", List.of("job4j"))),
                HttpStatus.OK
        );
    }

    /**
     * Получения отклика и ResponseStatusException
     * @param person
     * @return ResponseEntity<Person> or exception
     */
    private ResponseEntity<Person> getResponseEntity(Optional<Person> person) {
        return new ResponseEntity<>(
                person.orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "The person has not be saved, the login is already taken."
                        )
                ),
                new MultiValueMapAdapter<>(Map.of("Job4jCustomHeader", List.of("job4j"))),
                HttpStatus.CREATED
        );
    }

    /**
     * @ExceptionHandler
     * Данная аннотация позволяет отслеживать и обрабатывать исключения на уровне класса.
     * Если использовать ее например в контроллере, то исключения только данного
     * контроллера будут обрабатываться.
     * @param e
     * @param request
     * @param response
     * @throws IOException
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {{
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
        LOG.error(e.getLocalizedMessage());
    }

}
