package com.business_app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void testAddFinanceRecord(int id, FinanceType type, double amount, String description, String date) {
        FinanceRecord record = new FinanceRecord(id, type, amount, description, date);

        financeManager.addFinanceRecord(record);

        assertEquals(1, financeManager.getAllFinanceRecords().size());
        assertEquals(record, financeManager.getAllFinanceRecords().get(0));
    }

    //Параметризованный тест для некорректного ID
    @ParameterizedTest
    @CsvSource({
            "0, INCOME, 1000.0, Invalid ID, 2025-02-11",  // Неверный id
            "-1, EXPENSE, 200.0, Invalid ID, 2025-02-10"  // Неверный id
    })
    void testAddFinanceRecordWithInvalidId(int id, String type, double amount, String description, String date) {
        FinanceType financeType = FinanceType.valueOf(type);
        FinanceRecord record = new FinanceRecord(id, financeType, amount, description, date);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            financeManager.addFinanceRecord(record);
        });

        assertEquals("id должно быть больше 0.", exception.getMessage());
    }

    // Параметризованный тест для расчета общих расходов
    @ParameterizedTest
    @CsvSource({
            "200.0, Groceries, 2025-02-10",
            "150.0, Electricity, 2025-02-11",
            "150.0, Water, 2025-02-09"
    })
    void testCalculateTotalExpenses(double amount, String description, String date) {
        FinanceRecord expense = new FinanceRecord(1, FinanceType.EXPENSE, amount, description, date);
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
    void testCalculateTotalIncome(double amount, String description, String date) {
        FinanceRecord income = new FinanceRecord(1, FinanceType.INCOME, amount, description, date);
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
    void testAddFinanceRecordWithInvalidAmount(double amount, String description, String date) {
        FinanceRecord invalidRecord = new FinanceRecord(1, FinanceType.EXPENSE, amount, description, date);

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
    void testAddFinanceRecordWithEmptyDescription(int id, FinanceType type, double amount, String description, String date) {
        FinanceRecord record = new FinanceRecord(id, type, amount, description, date);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            financeManager.addFinanceRecord(record);
        });

        assertEquals("Описание не может быть пустым.", exception.getMessage());
    }

    // Параметризованный тест для добавления записи с некорректным форматом даты
    @ParameterizedTest
    @CsvSource({
            "1, INCOME, 1000.0, Salary, 2025-13-10",  // Некорректная дата
            "2, EXPENSE, 200.0, Groceries, 2025-02-45", // Некорректная дата
            "3, EXPENSE, 200.0, Groceries, 12025-02-02"  // Некорректная дата
    })
    void testAddFinanceRecordWithInvalidDate(int id, FinanceType type, double amount, String description, String date) {
        FinanceRecord record = new FinanceRecord(id, type, amount, description, date);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            financeManager.addFinanceRecord(record);
        });

        assertEquals("Некорректный формат даты.", exception.getMessage());
    }

    // Тестируем добавление финансовой записи если тип записи null
    @Test
    void testAddFinanceRecordWithFinanceTypeNull() {
        // Arrange: подготовка данных
        FinanceRecord invalidType = new FinanceRecord(1, null, 1000.0, "Salary", "2025-02-11");

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
        FinanceRecord invalidRecord = new FinanceRecord(1, null, 1000.0, "Invalid type", "2025-02-11");

        // Act & Assert: проверка выбрасывания исключения при добавлении записи с некорректным типом
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            financeManager.addFinanceRecord(invalidRecord);
        });

        assertEquals("Некорректный тип записи: null", exception.getMessage());
    }

    // Тестируем генерацию отчета
    @Test
    void testGenerateFinanceReport() {
        // Arrange: подготовка данных
        FinanceRecord income = new FinanceRecord(1, FinanceType.INCOME, 1000.0, "Salary", "2025-02-10");
        financeManager.addFinanceRecord(income);

        // Act: генерация отчета
        financeManager.generateFinanceReport();  // просто проверяем, что метод не вызывает ошибок
    }

    // Тестируем экспорт в CSV
    @Test
    void testExportToCSV() {
        // Arrange: подготовка данных
        FinanceRecord income = new FinanceRecord(1, FinanceType.INCOME, 1000.0, "Salary", "2025-02-10");
        financeManager.addFinanceRecord(income);

        // Act: экспорт в CSV
        //financeManager.exportToCSV();  // проверяем, что метод не вызывает ошибок
    }
}
