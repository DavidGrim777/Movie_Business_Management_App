package com.business_app;

import java.util.ArrayList;
import java.util.List;

public class FinanceManager {

    private List<FinanceRecord> financeRecords;

    public FinanceManager() {
        this.financeRecords = new ArrayList<>();
    }

    // Метод для добавления финансовой записи
    public void addFinanceRecord(FinanceRecord record) {
        financeRecords.add(record);
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
            if (record.getType() == FinanceType.EXPENSE) {
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
            if (record.getType() == FinanceType.INCOME) {
                totalIncome += record.getAmount();  // Добавляем сумму дохода
            }
        }
        return totalIncome;  // Возвращаем общую сумму доходов
    }

    // Генерация отчета о финансах (например, в CSV или PDF)
    public void generateFinanceReport() {
        // Пример генерации отчета: просто выводим все записи
        System.out.println("Finance Report:");
        for (FinanceRecord record : financeRecords) {
            System.out.println("ID: " + record.getId() + ", Type: " + record.getType() +
                    ", Amount: " + record.getAmount() + ", Description: " + record.getDescription() +
                    ", Date: " + record.getDate());
        }
    }
}