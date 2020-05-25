package ru.voronezhtsev.phonebook.report;

import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;



import org.springframework.lang.NonNull;
import ru.voronezhtsev.phonebook.flyway.db.h2.tables.records.UsersRecord;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.voronezhtsev.phonebook.flyway.db.h2.Tables.*;


/**
 * Класс создает отчет со всеми записями телефонной книги для вывода в формате XML
 *
 * @author Воронежцев Игорь
 */
public class ReportBuilder {

    @NonNull
    private final DSLContext context;

    public ReportBuilder(@NonNull DSLContext context) {
        this.context = context;
    }

    public Phonebook build() {
        Map<UsersRecord, Result<Record3<String, String, String>>> result = context.select(USERS.FIRST_NAME, USERS.LAST_NAME, PHONES.PHONE).from(USERS).join(PHONES).on(USERS.ID.eq(PHONES.USER_ID)).fetchGroups(USERS);
        List<ReportRecord> reportRecords = new ArrayList<>();
        for(UsersRecord usersRecord: result.keySet()) {
            List<String> phones = new ArrayList<>();
            User user = new User(usersRecord.getFirstName(), usersRecord.getLastName());
            for(Record3<String, String, String> rec: result.get(usersRecord)) {
                phones.add(rec.value3());
            }
            reportRecords.add(new ReportRecord(user, phones));
        }
        return new Phonebook(reportRecords);
    }
}
