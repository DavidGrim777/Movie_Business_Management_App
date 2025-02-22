package com.business_app;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class FinanceManager {

    private List<FinanceRecord> financeRecords;

    public FinanceManager() {
        this.financeRecords = new ArrayList<>();
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
        log.info("Финансовая запись добавлена: " + record);
    }
    // Метод для проверки наличия записей
    public boolean hasRecords() {
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
            log.info("Финансовая запись с ID " + recordId + " удалена.");
        } else {
            log.warn("Ошибка: Запись с ID " + recordId + " не найдена.");
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

    /// Генерация финансового отчета (например, в формате CSV или PDF)
    public void generateFinanceReport() {
        // Пример генерации отчета: просто выводим все записи
        System.out.println("Финансовый отчет:");
        for (FinanceRecord record : financeRecords) {
            System.out.println("ID: " + record.getId() + ", Тип: " + record.getType() +
                    ", Сумма: " + record.getAmount() + ", Описание: " + record.getDescription() +
                    ", Дата: " + record.getDate());
        }
    }

    // Экспорт в CSV
    public void exportToCSV() {
        System.out.println("Экспорт в CSV...");
        System.out.println("ID, Тип, Сумма, Описание, Дата");
        for (FinanceRecord record : financeRecords) {
            System.out.println(record.getId() + ", " + record.getType() + ", " + record.getAmount() + ", " + record.getDescription() + ", " + record.getDate());
        }
    }
}