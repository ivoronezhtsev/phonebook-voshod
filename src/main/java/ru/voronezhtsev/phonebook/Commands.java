package ru.voronezhtsev.phonebook;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

/**
 * Функции доступные с телефоной книгой
 *
 * @author Воронежцев Игорь
 */
@ShellComponent
public class Commands {

    private final DatabaseService databaseService;

    /**
     * @param databaseService Сервис для работы с БД
     */
    public Commands(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Выводит список контактов на консоль
     * @return Список контактов телефонной книги
     */
    @ShellMethod("Show all phone records: phone_id | first_name | last_name")
    public String list() {
        return databaseService.list();
    }

    /**
     * Добавляет контакт в телефонную книгу
     * @param firstName Имя контакта
     * @param lastName Фамилия контакта
     * @param phone Телефон
     */
    @ShellMethod("Add phone-record, arguments: [first_name] [last_name] [phone]")
    public void add(String firstName, String lastName, String phone) {
        databaseService.add(firstName, lastName, phone);
        System.out.println("\nUser added");
    }

    /**
     * Удаляет телефон у контакта если у контакта не остается телефонов удаляет контакт.
     * @param phoneId Идентификатор записи с телефоном у контакта
     *                (выводится в первом столбце комманды {@link #list()}
     */
    @ShellMethod("Delete phone-record, arguments: [phone_id]")
    public void delete(int phoneId) {
        databaseService.delete(phoneId);
    }

    /**
     * Экспорт телефонной книги в файл. Доступен только XML-формат.
     * @param fileName Имя файла
     */
    @ShellMethod("Export phonebook to xml-file, arguments: [file_name]")
    public void export(String fileName) {
        databaseService.export(fileName);
    }
}
