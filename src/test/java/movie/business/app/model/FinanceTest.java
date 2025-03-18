package movie.business.app.model;

import movie.business.app.enums.FinanceType;
import movie.business.app.manager.FinanceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FinanceTest {

    private FinanceManager financeManager;

    @BeforeEach
    void setUp() {
        // Создаем новый объект FinanceManager перед каждым тестом
        financeManager = new FinanceManager();
        financeManager.setTestMode(true);  // Включаем тестовый режим
        financeManager.clearData();  // Очистить данные перед каждым тестом
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

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            financeManager.addFinanceRecord(invalidRecord));

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

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            financeManager.addFinanceRecord(record));


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
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            financeManager.addFinanceRecord(invalidType));


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
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            financeManager.addFinanceRecord(invalidRecord));

        assertEquals("Некорректный тип записи: null", exception.getMessage());
    }

    @Test
    void testDateValidation_nullDate() {
        // Arrange
        FinanceManager financeManager = new FinanceManager();
        FinanceRecord record = new FinanceRecord("1", FinanceType.EXPENSE, 200.0, "Groceries", null); // Нулевая дата

        // Act & Assert: проверка выбрасывания исключения, если дата null
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            financeManager.addFinanceRecord(record));

        assertEquals("Дата не может быть пустой.", exception.getMessage());
    }

    @Test
    void testDateValidation_validDate() {
        // Arrange
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
        FinanceRecord record = new FinanceRecord("1", FinanceType.EXPENSE, 200.0, "Groceries", LocalDate.of(2025, 2, 10));
        financeManager.addFinanceRecord(record);

        // Act
        financeManager.removeFinanceRecord("1");

        // Assert: после удаления записи размер списка должен быть 0
        assertEquals(0, financeManager.getAllFinanceRecords().size());
    }
}
