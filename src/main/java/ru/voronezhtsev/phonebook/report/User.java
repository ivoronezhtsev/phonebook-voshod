package ru.voronezhtsev.phonebook.report;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

/**
 * Название контакта для отображения в XML
 *
 * @author Воронежцев Игорь
 */
@XmlType(name = "user")
@XmlRootElement
public class User {
    private String firstName = "";
    private String lastName = "";

    public User() {
    }

    /**
     * @param firstName Имя контакта
     * @param lastName Фамилия контакта
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return firstName.equals(user.firstName) &&
                lastName.equals(user.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}
