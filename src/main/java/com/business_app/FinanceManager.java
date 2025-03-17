package com.business_app;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Getter
public class FinanceManager {

    private final List<FinanceRecord> financeRecords;
    private static final String FILE_NAME = "finance_records.csv";
    private boolean isTestMode;  // Флаг для режима тестирования

    public FinanceManager() {
        this.financeRecords = new ArrayList<>();
        this.isTestMode = false;  // По умолчанию не в тестовом режиме
        loadFinanceRecordsFromFile();  // Загружаем данные из файла при создании объекта
    }
    // Установить флаг, чтобы знать, когда очистить данные в тестах
    public void setTestMode(boolean isTestMode) {
        this.isTestMode = isTestMode;
    }
    // Метод для очистки данных (только если мы в тестовом режиме)
    public void clearData() {
        if (isTestMode) {
            financeRecords.clear();
        }
    }

    // Метод для добавления финансовой записи
    public void addFinanceRecord(FinanceRecord record) {
        // Проверка на сумму
        if (record.getAmount() <= 0) {
            log.warn("Ошибка: сумма должна быть больше 0.");
            throw new IllegalArgumentException("Сумма должна быть больше 0.");
        }
        // Проверка на тип записи
        if (record.getType() == null || (!record.getType().equals(FinanceType.INCOME)
                && !record.getType().equals(FinanceType.EXPENSE))) {
            log.warn("Ошибка: некорректный тип записи.");
            throw new IllegalArgumentException("Некорректный тип записи: " + record.getType());
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
    }

    // Метод для проверки наличия записей
    public boolean hasRecords() {
        System.out.println("Количество записей: " + financeRecords.size());
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
        double totalExpenses = 0.0;
        // Проходим по всем записям
        for (FinanceRecord record : financeRecords) {
            // Если тип записи - расход
            if (record.getType().equals(FinanceType.EXPENSE)) {
                totalExpenses += record.getAmount();  // Добавляем сумму расхода
            }
        }
        return totalExpenses;  // Возвращаем общую сумму расходов
    }

    // Метод для вычисления общего дохода
    public double calculateTotalIncome() {
        double totalIncome = 0.0;
        // Проходим по всем записям
        for (FinanceRecord record : financeRecords) {
            // Если тип записи - доход
            if (record.getType().equals(FinanceType.INCOME)) {
                totalIncome += record.getAmount();  // Добавляем сумму дохода
            }
        }
        return totalIncome;  // Возвращаем общую сумму доходов
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

    // Метод для добавления бюджета для премьеры
    public void addPremiereBudget(Premiere premiere, double budgetToAdd) {
        if (budgetToAdd <= 0){
            log.warn("Ошибка: бюджет должен быть больше 0.");
            System.out.println("Ошибка: бюджет должен быть больше 0.");
            return;
        }

        premiere.addBudget(budgetToAdd); // Увеличиваем бюджет премьеры

        // Создаем запись о доходе
        FinanceRecord record = new FinanceRecord(
                UUID.randomUUID().toString().substring(0, 5), // Генерируем случайный ID
                FinanceType.INCOME, // Тип - доход
                budgetToAdd, // Сумма добавленного бюджета
                "Бюджет для премьеры: " + premiere.getMovieTitle(), // Описание
                LocalDate.now() // Дата
        );

        financeRecords.add(record); // Добавляем запись в список финансовых операций
        System.out.println("Финансовая запись добавлена: " + record);
    }


    // Генерация финансового отчета в формате CSV
    public void generateFinanceReport(boolean printToConsole) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            // Записываем заголовок CSV
            writer.write("ID, Тип, Сумма, Описание, Дата");
            writer.newLine();  // Переход на новую строку

            if (printToConsole) {
            // Выводим на экран и записываем в файл
            System.out.println("Финансовый отчет:");}
            // Добавляем информацию о продаже билетов, возвратах и бюджете премьеры
            double ticketSales = getTicketSales();
            double ticketRefunds = getTicketRefunds();

            // Прежде чем начать добавлять записи из financeRecords, добавим информацию о продажах билетов и возвратах
            String ticketSalesRecord = "Продажа билетов, " + ticketSales + ", Доход от продажи билетов, " + LocalDate.now();
            String ticketRefundsRecord = "Возврат билетов, " + ticketRefunds + ", Расходы на возврат билетов, " + LocalDate.now();


            if (printToConsole) {
                System.out.println(ticketSalesRecord);  // Выводим строку по продаже билетов
                System.out.println(ticketRefundsRecord);  // Выводим строку по возврату билетов
            }
            // Записываем их в файл
            writer.write(ticketSalesRecord);
            writer.newLine();
            writer.write(ticketRefundsRecord);
            writer.newLine();

            // Теперь добавляем обычные записи из financeRecords
            for (FinanceRecord record : financeRecords) {
                String recordLine = record.getId() + ", " + record.getType() + ", " + record.getAmount() +
                        ", " + record.getDescription() + ", " + record.getDate();
                if (printToConsole) {
                    System.out.println(recordLine);  // Выводим строку
                }
                writer.write(recordLine);  // Запись в файл
                writer.newLine();  // Переход на новую строку
            }
            System.out.println("Данные успешно экспортированы в файл " + FILE_NAME);
            log.info("Данные успешно экспортированы в файл {}", FILE_NAME);
        } catch (IOException e) {
            log.error("Ошибка при записи в файл: {}", e.getMessage());
        }
    }

    // Метод для загрузки финансовых записей из файла
    public void loadFinanceRecordsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            // Пропустить заголовок
            reader.readLine();
            // Читаем строки и добавляем записи в список
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data.length == 5) {
                    String id = data[0];
                    String typeString = data[1];
                    double amount = Double.parseDouble(data[2]);
                    String description = data[3];
                    LocalDate date = LocalDate.parse(data[4]);

                    FinanceType type;
                    try {
                        // Если в файле встречается "Бюджет", заменяем его на INCOME
                        if (typeString.equalsIgnoreCase("Бюджет")) {
                            type = FinanceType.INCOME;
                        } else {
                            type = FinanceType.valueOf(typeString);
                        }
                    } catch (IllegalArgumentException e) {
                        log.warn("Неизвестный тип записи.{} Запись будет пропущена", typeString);
                        continue; // Пропустить некорректную запись
                    }


                    // Создаем и добавляем новую финансовую запись
                    FinanceRecord record = new FinanceRecord(id, type, amount, description, date);
                    financeRecords.add(record);
                }
            }
            log.info("Финансовые записи успешно загружены из файла: " + FILE_NAME);
            System.out.println("Размер загруженных записей: " + financeRecords.size()); // Выводим размер коллекции
        } catch (IOException e) {
            log.error("Ошибка при загрузке финансовых записей из файла {}", e.getMessage());
            System.out.println("Ошибка при загрузке финансовых записей из файла: " + e.getMessage());
        }
    }
}
