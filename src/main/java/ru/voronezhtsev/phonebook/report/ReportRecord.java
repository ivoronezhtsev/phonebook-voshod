package ru.voronezhtsev.phonebook.report;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Запись телефонной книги(контакт). Состоит из названия контакта и списка телефонов.
 *
 * @author Воронежцев Игорь
 */
@XmlType(name = "record")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportRecord {
    private User user;
    @XmlElementWrapper(name = "phones")
    @XmlElement(name = "phone")
    private List<String> phones;

    public ReportRecord() {
    }

    /**
     * @param user Название контакта {@link User}
     * @param phones Список телефонов контакта
     */
    public ReportRecord(User user, List<String> phones) {
        this.user = user;
        this.phones = phones;
    }
}
