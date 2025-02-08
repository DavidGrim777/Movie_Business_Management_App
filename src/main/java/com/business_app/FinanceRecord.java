package com.business_app;

import com.github.javafaker.Finance;

public class FinanceRecord {

    public void generateReport() {
        //TODO
    }

    public enum Type {INCOME, EXPENSE}

        private int id;
        private FinanceType type;
        private double amount;
        private String description;
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

    }