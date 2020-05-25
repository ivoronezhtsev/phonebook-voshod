package ru.voronezhtsev.phonebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа в приложение Телефонная книга.
 *
 * @author Воронежцев Игорь
 */
@SpringBootApplication
public class PhonebookApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhonebookApplication.class, args);
    }
}
