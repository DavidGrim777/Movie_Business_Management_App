package com.business_app;

import java.time.LocalDate;

public class FinanceRecord {
    public FinanceRecord(int recordId, com.business_app.FinanceType financeType, double amount, String description, LocalDate date) {
    }

    public enum FinanceType {INCOME, EXPENSE}

        private int id;
        private FinanceType type;
        private double amount;
        private String description;
        private LocalDate date;

    public FinanceRecord(int id, FinanceType type, double amount, String description, LocalDate date) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public FinanceType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }
}


