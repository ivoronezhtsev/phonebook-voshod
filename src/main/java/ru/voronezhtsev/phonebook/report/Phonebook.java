package ru.voronezhtsev.phonebook.report;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlType(name = "phonebook")
@XmlRootElement()
@XmlAccessorType (XmlAccessType.FIELD)
public class Phonebook {
    @XmlElementWrapper(name = "records")
    @XmlElement(name = "record")
    private List<ReportRecord> records;
    public Phonebook() {
    }
    public Phonebook(List<ReportRecord> records) {
        this.records = records;
    }
}
