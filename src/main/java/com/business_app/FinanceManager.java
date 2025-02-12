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

    public double calculateTotalExpenses() {
        double totalExpenses = 0.0;
        for (FinanceRecord record : financeRecords) {
            if (record.getType() == FinanceRecord.FinanceType.EXPENSE) {
                totalExpenses = totalExpenses + record.getAmount();
            }
        }
        return totalExpenses;
    }

    public double calculateTotalIncome() {
        double totalIncome = 0.0;
        for (FinanceRecord record : financeRecords) {
            if (record.getType() == FinanceRecord.FinanceType.INCOME) {
                totalIncome = totalIncome + record.getAmount();
            }
        }
        return totalIncome;
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