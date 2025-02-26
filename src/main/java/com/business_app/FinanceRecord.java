package com.business_app;

import java.time.LocalDate;

public class FinanceRecord {

    private String id;
    private FinanceType type; // тип записи доход/расход
    private double amount; // сумма
    private String description; // описание
    private LocalDate date;

    public FinanceRecord(String id, FinanceType type, double amount, String description, LocalDate date) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
    public String getId() {
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

    @Override
    public String toString() {
        return "FinanceRecord{id=" + id + ", тип=" + type + ", сумма=" + amount + ", описание='"
                + description + "', дата='" + date + "'}";
    }
}
