package ru.job4j.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *  PasswordEncoder - бин компонент используеться
 *  при создании новых пользователей и аутентификации.
 *  BCryptPasswordEncoder - инструмент шифрования паролей.
 *
 *  Для решения проблемы зацикливания выносом бин PasswordEncoder
 *  в отдельный класс, в Security инициализируем по конструктору.
 */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
