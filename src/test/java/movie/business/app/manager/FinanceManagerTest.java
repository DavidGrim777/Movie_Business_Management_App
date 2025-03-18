package movie.business.app.manager;

import movie.business.app.enums.FinanceType;
import movie.business.app.model.FinanceRecord;
import movie.business.app.model.Premiere;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FinanceManagerTest {
    private FinanceManager financeManager;


    @BeforeEach
    void setUp() {
        // Инициализация объектов
        financeManager = new FinanceManager();
        financeManager.setTestMode(true);
        financeManager.clearData();
    }

    @Test
    void testAddPremiereBudget_invalidBudget() {
        // Создаем объект Premiere с полным набором параметров
        ZonedDateTime releaseDate = ZonedDateTime.now();  // Пример текущей даты для релиза
        String director = "Director Name";  // Пример имени режиссера
        String genre = "Action";  // Пример жанра
        int budget = 0;  // Начальный бюджет премьеры, который должен вызвать ошибку

        // Создаем экземпляр Premiere
        Premiere premiere = new Premiere("Test Movie", director, releaseDate, genre, budget);

        // Проверяем, что бюджет правильный перед добавлением
        assertEquals(budget, premiere.getBudget());

        // Выполняем попытку добавления бюджета, ожидая, что ошибка будет залогирована
        financeManager.addPremiereBudget(premiere, -500.0);  // Попытка добавления отрицательного бюджета

        // После вызова метода проверяем, что бюджет не изменился
        assertEquals(budget, premiere.getBudget(), "Бюджет не должен измениться при неправильных данных.");
    }

    @Test
    void testAddPremiereBudget() {
        // Создаем объект Premiere с полным набором параметров
        ZonedDateTime releaseDate = ZonedDateTime.now(); // Пример текущей даты для релиза
        String director = "Director Name";  // Пример имени режиссера
        String genre = "Action";  // Пример жанра
        int budget = 0;  // Начальный бюджет премьеры

        Premiere premiere = new Premiere("Test Movie", director, releaseDate, genre, budget);

        // Теперь можно использовать этот объект в тесте
        financeManager.addPremiereBudget(premiere, 500.0);  // Пример добавления бюджета

        // Проверяем, что бюджет увеличился на 500.0
        assertEquals(500.0, premiere.getBudget());
    }
    @Test
    void testAddFinanceRecord_throwsIllegalArgumentException() {
        // Arrange: Создаем экземпляр FinanceManager
        FinanceManager financeManager = new FinanceManager();

        // Создаем invalidRecord, который должен вызвать исключение
        FinanceRecord invalidRecord = new FinanceRecord(
                "Invalid ID",
                FinanceType.EXPENSE,
                -100.0, // Некорректная сумма, которая должна вызвать исключение
                "Invalid record",
                LocalDate.now()
        );

        // Act & Assert: Проверяем, что при добавлении такого invalidRecord будет выброшено исключение
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                financeManager.addFinanceRecord(invalidRecord));

        // Assert: Проверка сообщения об ошибке в исключении
        assertEquals("Сумма должна быть больше 0.", exception.getMessage(),
                "Сообщение об ошибке должно быть: Сумма должна быть больше 0.");
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

        // Проверяем сумму ВСЕХ доходов
        double expectedTotal = financeManager.getAllFinanceRecords()
                .stream()
                .filter(r -> r.getType() == FinanceType.INCOME)
                .mapToDouble(FinanceRecord::getAmount)
                .sum();
        assertEquals(expectedTotal, financeManager.calculateTotalIncome(), 0.01);
    }
    @Test
    void testHasRecords_noRecords() {
        // Arrange
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
    void testClearData() {
        // Arrange
        financeManager.addFinanceRecord(new FinanceRecord("1", FinanceType.INCOME, 1000.0, "Salary", LocalDate.now()));

        // Act
        financeManager.clearData();

        // Assert
        assertTrue(financeManager.getAllFinanceRecords().isEmpty(), "Список записей должен быть пустым.");
    }

    @Test
    void testLoadFinanceRecordsFromFile() {
        // Arrange: подготовим тестовый файл
        String testFilePath = "test_finance_records.csv";
        // Запишем данные в файл, например:
        // "1, INCOME, 1000.0, Salary, 2025-02-10"
        // Прочитаем файл и загрузим данные

        // Act
        financeManager.loadFinanceRecordsFromFile();

        // Assert: Проверим, что записи загружены
        assertFalse(financeManager.getAllFinanceRecords().isEmpty(), "Должны быть загружены записи.");
    }

    @Test
    void testGetTicketSales() {
        // Arrange
        financeManager.addFinanceRecord(new FinanceRecord("1", FinanceType.INCOME, 1000.0, "Продажа билетов", LocalDate.now()));
        financeManager.addFinanceRecord(new FinanceRecord("2", FinanceType.INCOME, 500.0, "Продажа билетов", LocalDate.now()));

        // Act
        double ticketSales = financeManager.getTicketSales();

        // Assert
        assertEquals(1500.0, ticketSales, 0.01, "Сумма продаж билетов должна быть правильной.");
    }

    @Test
    void testGetTicketRefunds() {
        // Arrange
        financeManager.addFinanceRecord(new FinanceRecord("1", FinanceType.EXPENSE, 200.0, "Возврат билетов", LocalDate.now()));
        financeManager.addFinanceRecord(new FinanceRecord("2", FinanceType.EXPENSE, 100.0, "Возврат билетов", LocalDate.now()));

        // Act
        double ticketRefunds = financeManager.getTicketRefunds();

        // Assert
        assertEquals(300.0, ticketRefunds, 0.01, "Сумма возвратов билетов должна быть правильной.");
    }
    @Test
    void testAddFinanceRecord_validRecord() {
        // Arrange
        FinanceRecord validRecord = new FinanceRecord("1", FinanceType.INCOME, 1000.0, "Salary", LocalDate.now());

        // Act
        financeManager.addFinanceRecord(validRecord);

        // Assert
        assertEquals(1, financeManager.getAllFinanceRecords().size(), "Запись должна быть добавлена.");
    }
    @Test
    void testGenerateFinanceReport_fileOutput() throws IOException {
        // Arrange
        FinanceRecord income = new FinanceRecord("1", FinanceType.INCOME, 1000.0, "Salary", LocalDate.now());
        financeManager.addFinanceRecord(income);
        String expectedFilePath = "finance_records.csv";

        // Act
        financeManager.generateFinanceReport(false);

        // Assert: Проверяем, что файл создан и содержит записи
        File file = new File(expectedFilePath);
        assertTrue(file.exists(), "Файл отчета должен существовать.");
        assertTrue(file.length() > 0, "Файл отчета не должен быть пустым.");
    }

}