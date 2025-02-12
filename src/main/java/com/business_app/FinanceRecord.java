package com.business_app;

public class FinanceRecord {

    private final int id;
    private FinanceType type; // тип записи доход/расход
    private double amount; // сумма
    private String description; // описание
    private String date;

    public FinanceRecord(int id, FinanceType type, double amount, String description, String date) {
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

    public String getDate() {
        return date;
    }

    public void generateReport() {
        String report = "Отчет о финансовой записи:\n";
        report += "ID: " + id + "\n";
        report += "Тип: " + type + "\n";
        report += "Сумма: " + String.format("%.2f", amount) + "\n";
        report += "Описание: " + description + "\n";
        report += "Дата: " + date + "\n";
        System.out.println(report);
    }

    @Override
    public String toString() {
        return "FinanceRecord{id=" + id + ", тип=" + type + ", сумма=" + amount + ", описание='" + description + "', дата='" + date + "'}";
    }
}