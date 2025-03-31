package movie.business.app.manager;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import movie.business.app.enums.FinanceType;
import movie.business.app.model.FinanceRecord;
import movie.business.app.model.Premiere;
import movie.business.app.repository.FinanceRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Getter
public class FinanceManager {

    private final List<FinanceRecord> financeRecords;
    private final FinanceRepository repository = new FinanceRepository();
    private static final String FILE_NAME = "finance_records.csv";
    private final boolean testMode;


    public FinanceManager(boolean testMode) {
        this.financeRecords = new ArrayList<>();
        this.testMode = testMode;
        loadFinanceRecordsFromFile();  // Загружаем данные из файла при создании объекта
    }

    // Конструктор по умолчанию (обычный режим)
    public FinanceManager() {
        this(false);
    }

    // Метод для очистки данных (если нужно сбросить все записи используется в тестах)
    public void clearData(boolean deleteFile) {
        financeRecords.clear();
        if (deleteFile) {
            String fileName;
            if (testMode) {
                fileName = "test_finance_records.csv";
            } else {
                fileName = "finance_records.csv";
            }
            try {
                Files.deleteIfExists(Paths.get(fileName));
                log.info("Файл {} успешно удалён.", fileName);
            } catch (IOException e) {
                log.error("Ошибка при удалении файла{}: {}", fileName, e.getMessage());
            }
        }
        saveFinanceRecordsToFile();
    }

    // Метод для добавления финансовой записи
    public void addFinanceRecord(FinanceRecord record) {
        Objects.requireNonNull(record, "Финансовая запись не может быть null");
        // Проверка на сумму
        if (record.getAmount() <= 0) {
            log.warn("Ошибка: сумма должна быть больше 0.");
            throw new IllegalArgumentException("Сумма должна быть больше 0.");
        }
        // Проверка на тип записи
        if (record.getType() == null ) {
            log.warn("Ошибка: тип записи не может быть null.");
            throw new IllegalArgumentException("Ошибка: тип записи не может быть null. " + record.getType());
        }
        // Проверка на корректность даты
        if (record.getDate() == null) {
            log.warn("Ошибка: дата не может быть пустой.");
            throw new IllegalArgumentException("Дата не может быть пустой.");
        }

        // Проверка на описание
        if (record.getDescription() == null || record.getDescription().isEmpty()) {
            log.warn("Ошибка: описание не может быть пустым.");
            throw new IllegalArgumentException("Описание не может быть пустым.");
        }

        financeRecords.add(record);
        log.info("Финансовая запись добавлена: {} ", record);
        saveFinanceRecordsToFile();
    }

    public void saveFinanceRecordsToFile() {
        repository.saveRecords(financeRecords, testMode);
    }

    public void loadFinanceRecordsFromFile() {
        financeRecords.clear();
        financeRecords.addAll(repository.loadRecords(testMode));
    }

    // Метод для проверки наличия записей
    public boolean hasRecords() {
        if (financeRecords.isEmpty()) {
            financeRecords.addAll(repository.loadRecords(testMode)); //  Загружаем из файла при вызове
        }
        return !financeRecords.isEmpty();  // Возвращаем true, если список не пустой
    }

    public void removeFinanceRecord(String recordId) {
        // Ищем запись с указанным ID
        FinanceRecord recordToRemove = null;
        for (FinanceRecord record : financeRecords) {
            if (record.getId().equals(recordId)) {
                recordToRemove = record;
                break;
            }
        }
        // Если запись найдена, удаляем её
        if (recordToRemove != null) {
            financeRecords.remove(recordToRemove);
            log.info("Финансовая запись с ID {} удалена. ", recordId);
            saveFinanceRecordsToFile();
        } else {
            log.warn("Ошибка: Запись с ID {} не найдена.", recordId);
            throw new IllegalArgumentException("Запись с таким ID не найдена.");
        }
    }

    // Метод для получения всех записей
    public List<FinanceRecord> getAllFinanceRecords() {
        return new ArrayList<>(financeRecords);// Возвращаем копию списка, чтобы сохранить инкапсуляцию
    }

    // Метод для вычисления общих расходов
    public double calculateTotalExpenses() {
        EnumSet<FinanceType> expenseTypes = EnumSet.of(
                FinanceType.EXPENSE,
                FinanceType.ADVERTISING,
                FinanceType.CAST,
                FinanceType.OTHER
        );

        return financeRecords.stream()
                .filter(record -> expenseTypes.contains(record.getType()))
                .mapToDouble(FinanceRecord::getAmount)
                .sum();
    }

    // Метод для вычисления общего дохода
    public double calculateTotalIncome() {
        EnumSet<FinanceType> incomeTypes = EnumSet.of(
                FinanceType.INCOME,
                FinanceType.SPONSORSHIP,
                FinanceType.CREDIT
        );

        return financeRecords.stream()
                .filter(record -> incomeTypes.contains(record.getType()))
                .mapToDouble(FinanceRecord::getAmount)
                .sum();
    }

    // Метод для получения суммы продаж билетов
    public double getTicketSales() {
        return financeRecords.stream()
                .filter(record -> record.getType() == FinanceType.INCOME && record.getDescription().contains("Продажа билетов"))
                .mapToDouble(FinanceRecord::getAmount)
                .sum();
    }

    // Метод для получения суммы возвратов билетов
    public double getTicketRefunds() {
        return financeRecords.stream()
                .filter(record -> record.getType() == FinanceType.EXPENSE && record.getDescription().contains("Возврат билетов"))
                .mapToDouble(FinanceRecord::getAmount)
                .sum();
    }

    // Пока не используется. Резервный метод для добавления бюджета для премьеры
    public void addPremiereBudget(Premiere premiere, double budgetToAdd) {
        if (budgetToAdd <= 0) {
            log.warn("Ошибка: бюджет должен быть больше 0.");
            System.out.println("Ошибка: бюджет должен быть больше 0.");
            return;
        }

        premiere.addBudget(budgetToAdd); // Увеличиваем бюджет премьеры

        // Создаем запись о доходе
        FinanceRecord record = new FinanceRecord(
                UUID.randomUUID().toString().substring(0, 5), // Генерируем случайный ID
                FinanceType.EXPENSE, // Тип - расход
                budgetToAdd, // Сумма добавленного бюджета
                "Бюджет для премьеры: " + premiere.getMovieTitle(), // Описание
                LocalDate.now() // Дата
        );

        financeRecords.add(record); // Добавляем запись в список финансовых операций
        System.out.println("Финансовая запись добавлена: " + record);
    }


    // Генерация финансового отчета в формате CSV
    public void generateFinanceReport(boolean printToConsole) {
        if (!hasRecords()) {
            System.out.println("Отчет не может быть сгенерирован, так как нет записей для анализа.");
            return;
        }

        List<FinanceRecord> records = repository.loadRecords(false); // Загружаем из файла для отчета

        double income = calculateTotalIncome();
        double expense = calculateTotalExpenses();
        double result = income - expense;

        double ticketSales = getTicketSales();
        double ticketRefunds = getTicketRefunds();

        int ticketsSold = 0;
        int ticketsRefunded = 0;
        double averageTicketPrice = 10.0;

        for (FinanceRecord record : records) {
            if (record.getType() == FinanceType.INCOME && record.getDescription().contains("Продажа билетов")) {
                ticketsSold += (int)(record.getAmount() / averageTicketPrice);
            }
            if (record.getType() == FinanceType.EXPENSE && record.getDescription().contains("Возврат билетов")) {
                ticketsRefunded += (int)(record.getAmount() / averageTicketPrice);
            }
        }

        saveFinanceRecordsToFile();
        repository.generatePDFReport(records);

        if (printToConsole) {
            System.out.println("=====Финансовый отчет=====");
            for (FinanceRecord record : records) {
                System.out.println(record);
        }

            System.out.println("================================");
            System.out.println("Общий доход: " + income);
            System.out.println("Общие расходы: " + expense);
            System.out.println("Итоговая прибыль: " + result);
            System.out.println("Продано билетов на сумму: "  + ticketsSold + " на сумму: " + ticketSales);
            System.out.println("Возвращено билетов на сумму: " + ticketsRefunded + " на сумму: " + ticketRefunds);
            System.out.println("================================");
        }

        System.out.println("Финансовый отчет успешно сохранен в " + FILE_NAME);
    }

    // Метод только для тестов — очищает список, но не файл
    public void clearRecordsInMemory() {
        financeRecords.clear();
    }
}
