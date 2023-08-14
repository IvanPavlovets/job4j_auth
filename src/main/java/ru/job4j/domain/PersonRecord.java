package ru.job4j.domain;

/**
 * Заменяет класс шаблона DTO, конструкцией record.
 * обьект для хранения и передачи неизменяемых значений.
 * @param id
 * @param password
 */
public record PersonRecord(int id, String password) {

}
