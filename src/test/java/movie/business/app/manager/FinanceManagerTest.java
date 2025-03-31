package movie.business.app.manager;

import movie.business.app.enums.FinanceType;
import movie.business.app.model.FinanceRecord;
import movie.business.app.model.Premiere;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        financeManager = new FinanceManager(true);
        financeManager.clearData(false);
    }
    @AfterEach
    void tearDown() {
        if (financeManager.isTestMode()) {
            deleteTestFile("test_finance_records.csv");
            deleteTestFile("test_finance_report.pdf");
        }
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

    @Test
    void testRemoveFinanceRecord_recordNotFound() {
        // Arrange
        FinanceRecord record = new FinanceRecord("1", FinanceType.EXPENSE, 200.0, "Groceries", LocalDate.of(2025, 2, 10));
        financeManager.addFinanceRecord(record);

        // Act & Assert: проверка выбрасывания исключения, если запись с таким ID не найдена
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            financeManager.removeFinanceRecord("2");  // ID, которого нет в списке
        });

        assertEquals("Запись с таким ID не найдена.", exception.getMessage());
    }

    @Test
    void testAddFinanceRecordWithEmptyId() {
        LocalDate date = LocalDate.of(2025, 2, 10);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                financeManager.addFinanceRecord(new FinanceRecord("", FinanceType.INCOME, 500.0, "Test", date))
        );
        assertEquals("ID не может быть пустым.", exception.getMessage());
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
        //TODO financeManager.exportToCSV();  // проверяем, что метод не вызывает ошибок
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
        FinanceManager financeManager = new FinanceManager(true);
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
        financeManager.clearData(true);

        // Assert
        assertTrue(financeManager.getAllFinanceRecords().isEmpty(), "Список записей должен быть пустым.");
    }

    @Test
    void testLoadFinanceRecordsFromFile() {
        // Arrange
        FinanceRecord record = new FinanceRecord("1", FinanceType.INCOME, 1000.0, "Продажа билетов", LocalDate.now());
        financeManager.addFinanceRecord(record);

        financeManager.saveFinanceRecordsToFile(); // сохраняем в test_finance_records.csv

        financeManager.clearRecordsInMemory(); //  очищает память

        assertTrue(financeManager.getAllFinanceRecords().isEmpty(), "Перед загрузкой список должен быть пуст.");

        // Act
        financeManager.loadFinanceRecordsFromFile();

        // Assert
        assertFalse(financeManager.getAllFinanceRecords().isEmpty(), "Должны быть загружены записи.");
        assertEquals("1", financeManager.getAllFinanceRecords().get(0).getId());
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
        System.out.println("TEST MODE: " + financeManager.isTestMode()); // Отладка

        FinanceRecord income = new FinanceRecord("1", FinanceType.INCOME, 1000.0, "Salary", LocalDate.now());
        financeManager.addFinanceRecord(income);

        String expectedFilePath = "test_finance_records.csv";

        financeManager.generateFinanceReport(false);

        File file = new File(expectedFilePath);
        assertTrue(file.exists(), "Файл отчета должен существовать.");
        assertTrue(file.length() > 0, "Файл отчета не должен быть пустым.");
    }

    @Test
    void testFinanceRecordConstructor_invalidAmount_throwsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new FinanceRecord("1", FinanceType.EXPENSE, -100.0, "Ошибка", LocalDate.now())
        );
        assertEquals("Сумма должна быть больше 0.", exception.getMessage());
    }

    @Test
    void testGenerateFinanceReport_noRecords() {
        financeManager.clearData(true);

        // Перехватываем вывод
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        financeManager.generateFinanceReport(true);

        String output = outContent.toString();
        assertTrue(output.contains("нет записей"), "Ожидается сообщение об отсутствии данных");

        System.setOut(System.out);
    }
    @Test
    void testAddValidFinanceRecord() {
        FinanceRecord record = new FinanceRecord("1", FinanceType.EXPENSE, 200.0, "Groceries", LocalDate.of(2025, 2, 10));
        financeManager.addFinanceRecord(record);

        assertEquals(1, financeManager.getAllFinanceRecords().size());
        assertEquals(record, financeManager.getAllFinanceRecords().get(0));
    }

    @Test
    void testRemoveExistingRecord() {
        FinanceRecord record = new FinanceRecord("1", FinanceType.INCOME, 1000.0, "Salary", LocalDate.now());
        financeManager.addFinanceRecord(record);

        financeManager.removeFinanceRecord("1");

        assertEquals(0, financeManager.getAllFinanceRecords().size());
    }

    @Test
    void testRemoveNonExistentRecord() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                financeManager.removeFinanceRecord("notExist")
        );

        assertEquals("Запись с таким ID не найдена.", exception.getMessage());
    }
    @Test
    void testAddPremiereBudget_valid() {
        Premiere premiere = new Premiere("1", "Titanic", ZonedDateTime.now(), "Cinema", 100, 1000000);
        double budgetToAdd = 50000;

        financeManager.addPremiereBudget(premiere, budgetToAdd);

        assertEquals(1, financeManager.getAllFinanceRecords().size());
        FinanceRecord record = financeManager.getAllFinanceRecords().get(0);
        assertEquals("Бюджет для премьеры: Titanic", record.getDescription());
        assertEquals(budgetToAdd, record.getAmount(), 0.01);
    }
    @Test
    void testAddPremiereBudget_invalidAmount() {
        Premiere premiere = new Premiere("2", "Avatar", ZonedDateTime.now(), "Cinema", 100, 1000000);

        financeManager.addPremiereBudget(premiere, 0);

        assertEquals(0, financeManager.getAllFinanceRecords().size(), "Запись не должна быть добавлена при нулевом бюджете");
    }
}