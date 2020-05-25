package ru.voronezhtsev.phonebook;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.voronezhtsev.phonebook.report.ReportBuilder;
import ru.voronezhtsev.phonebook.report.ReportWriter;

import static org.jooq.impl.DSL.max;
import static ru.voronezhtsev.phonebook.flyway.db.h2.Tables.PHONES;
import static ru.voronezhtsev.phonebook.flyway.db.h2.Tables.USERS;

/**
 * Сервис для работы телефонной книгой в БД.
 *
 * @author Воронежцев Игорь
 */
@Service
public class DatabaseService {

    private static final int START_ID = 0;
    private static final String JDBC_URL = "jdbc:h2:~/phonebook";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    private int phoneId;
    private int userId;

    public DatabaseService() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException(exception);
        }
        recalculateIds();
    }

    private void recalculateIds() {
        phoneId = getPhonesId();
        userId = getUsersId();
    }

    private int getPhonesId() {
        try(DSLContext context = DSL.using(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            Result<Record1<Integer>> result = context.select(max(PHONES.ID)).from(PHONES).fetch();
            for (Record1<Integer> r : result) {
                return r.value1() == null ? START_ID : r.value1();
            }
            return START_ID;
        }
    }

    private int getUsersId() {
        try(DSLContext context = DSL.using(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            Result<Record1<Integer>> result = context.select(max(USERS.ID)).from(USERS).fetch();
            for (Record1<Integer> r : result) {
                return r.value1() == null ? START_ID : r.value1();
            }
            return START_ID;
        }
    }

    /**
     * @return Список контактов в БД
     */
    public String list() {
        try(DSLContext context = DSL.using(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            Result<Record> records = context.select().from(USERS).join(PHONES).on(USERS.ID.eq(PHONES.USER_ID)).fetch();
            StringBuilder result = new StringBuilder();
            for (Record r : records) {
                String firstName = r.get(USERS.FIRST_NAME);
                String lastName = r.get(USERS.LAST_NAME);
                String phone = r.get(PHONES.PHONE);
                int phoneId = r.get(PHONES.ID);
                result.append(String.format("%d %s %s %s\n", phoneId, firstName, lastName, phone));
            }
            return result.toString();
        }
    }

    /**
     * Добавляет в базу данных Контакт с номером телефона
     * @param firstName Имя контакта
     * @param lastName Фамилия контакта
     * @param phone Номер телефона
     */
    public void add(@NonNull String firstName, @NonNull String lastName, @NonNull String phone) {
        try(DSLContext context = DSL.using(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            userId++;
            context.insertInto(USERS, USERS.ID, USERS.FIRST_NAME, USERS.LAST_NAME)
                    .values(userId, firstName, lastName).execute();
            phoneId++;
            context.insertInto(PHONES, PHONES.ID, PHONES.PHONE, PHONES.USER_ID).values(phoneId, phone, userId).execute();
        }
    }

    /**
     * Удаляет запись с номером телефона из базы данных. Если у пользователя не остается ниодного телефона он также
     * удаляется
     * @param phoneId Идентификатор записи с номером телефона
     */
    public void delete(int phoneId) {
        try(DSLContext context = DSL.using(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            context.delete(PHONES).where(PHONES.ID.eq(phoneId)).execute();
            // В результате удаления телефона, пользователь в таблице USERS может оказаться без телефона,
            // находим такого и удаляем
            Result<Record1<Integer>> phonesId = context.select(PHONES.ID).from(USERS).leftJoin(PHONES).on(USERS.ID.eq(PHONES.USER_ID)).fetch();
            for (Record1<Integer> rec : phonesId) {
                if (rec.value1() != null) {
                    context.delete(USERS).where(USERS.ID.ne(rec.value1())).execute();
                    break;
                }
            }
        }
        recalculateIds();
    }

    /**
     * Экспортирует базу данных в файл
     * @param fileName Файл для экспорта базы данных
     */
    public void export(String fileName) {
        try(DSLContext context = DSL.using(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            ReportWriter reportWriter = new ReportWriter(new ReportBuilder(context));
            reportWriter.write(fileName);
        }
    }
}
