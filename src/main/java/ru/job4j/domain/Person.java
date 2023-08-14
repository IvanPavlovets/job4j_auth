package ru.job4j.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.job4j.handlers.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class, Operation.OnDelete.class
    })
    private int id;
    @NotNull(message = "Login must be non null", groups = {
            Operation.OnCreate.class
    })
    @Size(min = 3, max = 8, message = "Login must be more than 3 and less 8")
    private String login;
    @NotNull(message = "Password must be non null")
    @Size(min = 3, max = 8, message = "Password must be more than 3 and less 8")
    private String password;
}
