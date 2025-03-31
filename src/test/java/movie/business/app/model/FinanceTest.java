package movie.business.app.model;

import movie.business.app.enums.FinanceType;
import movie.business.app.manager.FinanceManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FinanceTest {

    private FinanceManager financeManager;

    @BeforeEach
    void setUp() {
        financeManager = new FinanceManager(true);
        financeManager.clearData(true);
    }
    @AfterEach
    void tearDown() {
        deleteTestFile("test_finance_records.csv");
        deleteTestFile("test_finance_report.pdf");
    }

    //  Добавляем метод удаления файла
    private void deleteTestFile(String fileName) {
        Path filePath = Paths.get(System.getProperty("user.dir"), fileName);
        try {
            Files.deleteIfExists(filePath);
            System.out.println("Файл " + fileName + " успешно удалён.");
        } catch (IOException e) {
            System.out.println("Ошибка при удалении файла: " + fileName + " - " + e.getMessage());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "1, INCOME, 1000.0, Salary, 2025-02-11",
            "2, EXPENSE, 200.0, Groceries, 2025-02-10"
    })
    void testAddFinanceRecord(String id, FinanceType type, double amount, String description, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        FinanceRecord record = new FinanceRecord(id, type, amount, description, date);

        financeManager.addFinanceRecord(record);

        assertEquals(1, financeManager.getAllFinanceRecords().size());
        assertEquals(record, financeManager.getAllFinanceRecords().get(0));
    }

    @ParameterizedTest
    @CsvSource({
            "-100.0, Invalid expense, 2025-02-11",
            "0.0, Invalid expense, 2025-02-11"
    })
    void testAddFinanceRecordWithInvalidAmount(double amount, String description, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new FinanceRecord("1", FinanceType.EXPENSE, amount, description, date)
        );

        assertEquals("Сумма должна быть больше 0.", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "1, INCOME, 1000.0, , 2025-02-11",
            "2, EXPENSE, 200.0, , 2025-02-10"
    })
    void testAddFinanceRecordWithEmptyDescription(String id, FinanceType type, double amount, String description, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new FinanceRecord(id, type, amount, description, date)
        );

        assertEquals("Описание не может быть пустым.", exception.getMessage());
    }

    @Test
    void testAddFinanceRecordWithNullType() {
        LocalDate date = LocalDate.of(2025, 2, 11);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new FinanceRecord("1", null, 1000.0, "Salary", date)
        );

        assertEquals("Тип не может быть null.", exception.getMessage());
    }

    @Test
    void testAddFinanceRecordWithNullDate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new FinanceRecord("1", FinanceType.INCOME, 1000.0, "Salary", null)
        );

        assertEquals("Дата не может быть пустой.", exception.getMessage());
    }

    @Test
    void testAddFinanceRecordWithEmptyId() {
        LocalDate date = LocalDate.of(2025, 2, 10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new FinanceRecord("", FinanceType.INCOME, 500.0, "Test", date)
        );

        assertEquals("ID не может быть пустым.", exception.getMessage());
    }
}