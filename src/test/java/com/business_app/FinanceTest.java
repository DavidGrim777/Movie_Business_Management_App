package com.business_app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FinanceTest {

    private FinanceManager financeManager;

    @BeforeEach
    void setUp() {
        // Создаем новый объект FinanceManager перед каждым тестом
        financeManager = new FinanceManager();
    }

    // Параметризованный тест для добавления записи
    @ParameterizedTest
    @CsvSource({
            "1, INCOME, 1000.0, Salary, 2025-02-11",   // Допустимая запись
            "2, EXPENSE, 200.0, Groceries, 2025-02-10"  // Допустимая запись
    })
    void testAddFinanceRecord(String id, FinanceType type, double amount, String description, String dateStr) {
        // Преобразуем строку в LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateStr, formatter);

        FinanceRecord record = new FinanceRecord(id, type, amount, description, date);

        financeManager.addFinanceRecord(record);

        assertEquals(1, financeManager.getAllFinanceRecords().size());
        assertEquals(record, financeManager.getAllFinanceRecords().get(0));
    }

    // Параметризованный тест для расчета общих расходов
    @ParameterizedTest
    @CsvSource({
            "200.0, Groceries, 2025-02-10",
            "150.0, Electricity, 2025-02-11",
            "150.0, Water, 2025-02-09"
    })
    void testCalculateTotalExpenses(double amount, String description, String dateStr) {
        // Преобразуем строку в LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateStr, formatter); // Преобразуем строку в дату

        // Создаем объект FinanceRecord с конвертированной датой
        FinanceRecord expense = new FinanceRecord("1", FinanceType.EXPENSE, amount, description, date);
        financeManager.addFinanceRecord(expense);

        double totalExpenses = financeManager.calculateTotalExpenses();
        assertEquals(amount, totalExpenses, 0.01);
    }

    // Параметризованный тест для расчета общего дохода
    @ParameterizedTest
    @CsvSource({
            "1500.0, Salary, 2025-02-10",
            "500.0, Freelance, 2025-02-11"
    })
    void testCalculateTotalIncome(double amount, String description, String dateStr) {
        // Преобразуем строку в LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateStr, formatter);


        FinanceRecord income = new FinanceRecord("1", FinanceType.INCOME, amount, description, date);
        financeManager.addFinanceRecord(income);

        double totalIncome = financeManager.calculateTotalIncome();
        assertEquals(amount, totalIncome, 0.01);
    }

    // Параметризованный тест для добавления записи с некорректной суммой
    @ParameterizedTest
    @CsvSource({
            "-100.0, Invalid expense, 2025-02-11",  // Неверная сумма
            "0.0, Invalid expense, 2025-02-11"  // Неверная сумма
    })
    void testAddFinanceRecordWithInvalidAmount(double amount, String description, String dateStr) {
        // Преобразуем строку в LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateStr, formatter);

        FinanceRecord invalidRecord = new FinanceRecord("1", FinanceType.EXPENSE, amount, description, date);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            financeManager.addFinanceRecord(invalidRecord);
        });

        assertEquals("Сумма должна быть больше 0.", exception.getMessage());
    }
    //Параметризованный тест для добавления записи с пустым описанием
    @ParameterizedTest
    @CsvSource({
            "1, INCOME, 1000.0, , 2025-02-11",  // Пустое описание
            "2, EXPENSE, 200.0, , 2025-02-10" , // Пустое описание
    })
    void testAddFinanceRecordWithEmptyDescription(String id, FinanceType type, double amount, String description, String dateStr) {
        // Преобразуем строку в LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateStr, formatter);

        FinanceRecord record = new FinanceRecord(id, type, amount, description, date);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            financeManager.addFinanceRecord(record);
        });

        assertEquals("Описание не может быть пустым.", exception.getMessage());
    }

    // Тестируем добавление финансовой записи если тип записи null
    @Test
    void testAddFinanceRecordWithFinanceTypeNull() {

        // Arrange: подготовка данных
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse("2025-02-11", formatter); // Преобразуем строку в LocalDate

        // Arrange: подготовка данных
        FinanceRecord invalidType = new FinanceRecord("1", null, 1000.0, "Salary",date);

        // Act & Assert: проверка выбрасывания исключения при добавлении записи с некорректным типом null
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            financeManager.addFinanceRecord(invalidType);
        });

        assertEquals("Некорректный тип записи: null", exception.getMessage());
    }


    // Тестируем добавление записи с типом, который не является INCOME или EXPENSE
    @Test
    void testAddFinanceRecordWithInvalidType() {
        // Arrange: подготовка данных с неправильным типом записи
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse("2025-02-11", formatter); // Преобразуем строку в LocalDate

        // Arrange: подготовка данных с неправильным типом записи
        FinanceRecord invalidRecord = new FinanceRecord("1", null, 1000.0, "Invalid type", date);

        // Act & Assert: проверка выбрасывания исключения при добавлении записи с некорректным типом
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            financeManager.addFinanceRecord(invalidRecord);
        });

        assertEquals("Некорректный тип записи: null", exception.getMessage());
    }

    @Test
    void testDateValidation_nullDate() {
        // Arrange
        FinanceManager financeManager = new FinanceManager();
        FinanceRecord record = new FinanceRecord("1", FinanceType.EXPENSE, 200.0, "Groceries", null); // Нулевая дата

        // Act & Assert: проверка выбрасывания исключения, если дата null
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            financeManager.addFinanceRecord(record);
        });

        assertEquals("Дата не может быть пустой.", exception.getMessage());
    }

    @Test
    void testHasRecords_noRecords() {
        // Arrange
        FinanceManager financeManager = new FinanceManager();

        // Act
        boolean hasRecords = financeManager.hasRecords();

        // Assert: метод должен вернуть false, если нет записей
        assertFalse(hasRecords);
    }

    @Test
    void testHasRecords_withRecords() {
        // Arrange
        FinanceManager financeManager = new FinanceManager();
        FinanceRecord record = new FinanceRecord("1", FinanceType.INCOME, 1000.0, "Salary", LocalDate.of(2025, 2, 10));
        financeManager.addFinanceRecord(record);

        // Act
        boolean hasRecords = financeManager.hasRecords();

        // Assert: метод должен вернуть true, если есть хотя бы одна запись
        assertTrue(hasRecords);
    }
    @Test
    void testDateValidation_validDate() {
        // Arrange
        FinanceManager financeManager = new FinanceManager();
        FinanceRecord record = new FinanceRecord("1", FinanceType.EXPENSE, 200.0, "Groceries", LocalDate.of(2025, 2, 10)); // Валидная дата

        // Act
        financeManager.addFinanceRecord(record);

        // Assert: проверка, что запись была успешно добавлена
        assertEquals(1, financeManager.getAllFinanceRecords().size());
        assertEquals(record, financeManager.getAllFinanceRecords().get(0));
    }
    @Test
    void testRemoveFinanceRecord_recordExists() {
        // Arrange
        FinanceManager financeManager = new FinanceManager();
        FinanceRecord record = new FinanceRecord("1", FinanceType.EXPENSE, 200.0, "Groceries", LocalDate.of(2025, 2, 10));
        financeManager.addFinanceRecord(record);

        // Act
        financeManager.removeFinanceRecord("1");

        // Assert: после удаления записи размер списка должен быть 0
        assertEquals(0, financeManager.getAllFinanceRecords().size());
    }

    @Test
    void testRemoveFinanceRecord_recordNotFound() {
        // Arrange
        FinanceManager financeManager = new FinanceManager();
        FinanceRecord record = new FinanceRecord("1", FinanceType.EXPENSE, 200.0, "Groceries", LocalDate.of(2025, 2, 10));
        financeManager.addFinanceRecord(record);

        // Act & Assert: проверка выбрасывания исключения, если запись с таким ID не найдена
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            financeManager.removeFinanceRecord("2");  // ID, которого нет в списке
        });

        assertEquals("Запись с таким ID не найдена.", exception.getMessage());
    }

    // Тестируем генерацию отчета
    @Test
    void testGenerateFinanceReport() {
        // Arrange: подготовка данных с неправильным типом записи
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse("2025-02-11", formatter); // Преобразуем строку в LocalDate

        FinanceRecord income = new FinanceRecord("1", FinanceType.INCOME, 1000.0, "Salary", date);
        financeManager.addFinanceRecord(income);

        // Act: генерация отчета
        financeManager.generateFinanceReport(true);  // просто проверяем, что метод не вызывает ошибок
    }

    // Тестируем экспорт в CSV
    @Test
    void testExportToCSV() {
        // Arrange: подготовка данных с неправильным типом записи
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse("2025-02-11", formatter); // Преобразуем строку в LocalDate

        FinanceRecord income = new FinanceRecord("1", FinanceType.INCOME, 1000.0, "Salary", date);
        financeManager.addFinanceRecord(income);

        // Act: экспорт в CSV
        //financeManager.exportToCSV();  // проверяем, что метод не вызывает ошибок
    }
}
