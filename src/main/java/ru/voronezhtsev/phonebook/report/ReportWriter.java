package ru.voronezhtsev.phonebook.report;

import liquibase.pro.packaged.I;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Класс для печати отчета с содержимым телефонной книги
 * @author Воронежцев Игорь
 */
public class ReportWriter {
    private final ReportBuilder reportBuilder;

    /**
     * @param reportBuilder Билдер отчета {@link ReportBuilder}
     */
    public ReportWriter(ReportBuilder reportBuilder) {
        this.reportBuilder = reportBuilder;
    }

    /**
     * Печать отчета в файл.
     * Сначала отчет создается потом выводится в файл.
     *
     * @param fileName Имя файла
     */
    public void write(String fileName) {
        try (Writer writer = new FileWriter(fileName)){
            Phonebook phonebook = reportBuilder.build();
            JAXBContext context = JAXBContext.newInstance(Phonebook.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(phonebook, writer);
        } catch (JAXBException | IOException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
