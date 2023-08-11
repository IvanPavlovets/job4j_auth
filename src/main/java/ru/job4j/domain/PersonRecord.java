package ru.job4j.domain;

/**
 * Заменяет класс шаблона DTO, конструкцией record.
 * @param id
 * @param password
 */
public record PersonRecord(int id, String password) {

}
