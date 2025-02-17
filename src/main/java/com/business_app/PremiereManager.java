package com.business_app;

import java.util.ArrayList;
import java.util.List;

public class PremiereManager {

    private List<Premiere> premieres;

    public PremiereManager() {
        this.premieres = new ArrayList<>();
    }

    // Метод для добавления новой премьеры в список
    public void addPremiere(Premiere premiere) {
        premieres.add(premiere);
    }

    // Метод для генерации отчета по всем премьерам
    public void generatePremiereReport() {
        for (Premiere premiere : premieres) {
            System.out.println(premiere.generateReport()); // Генерируем отчет для каждой премьеры
        }
    }
}