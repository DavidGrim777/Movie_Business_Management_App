package com.business_app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PremiereManager {

    private static final Logger logger = Logger.getLogger(PremiereManager.class.getName());
    private Map<Integer, Premiere> premiereMap = new HashMap<>();

    public PremiereManager() {
        this.premiereMap = new HashMap<>();
    }

    // Метод для добавления новой премьеры в список
    public void addPremiere(Premiere premiere) {
        if (premiere == null) {
            logger.warning("Попытка добавить null в список премьеры.");
            throw new IllegalArgumentException("Премьера не может быть null.");
        }
        premiereMap.put(premiere.getId(), premiere);
        logger.info("Премьера добавлена: " + premiere.getMovieTitle());
    }

    // Метод для генерации отчета по всем премьерам
    public void generatePremiereReport() {
        if (premiereMap.isEmpty()) {
            logger.warning("Попытка сгенерировать отчет для пустого списка премьеры.");
            System.out.println("Нет премьеры для генерации отчета.");
            return;
        }
        logger.info("Начало генерации отчетов для всех премьер.");
        for (Premiere premiere : premiereMap.values()) {
            try {
                String report = premiere.generateReport();
                System.out.println(report); // Генерируем отчет для каждой премьеры
                logger.info("Отчет о премьере сгенерирован для: " + premiere.getMovieTitle());
                logger.info(report); // Логируем отчет о премьере
            } catch (Exception e) {
                logger.warning("Ошибка при генерации отчета для премьеры " + premiere.getMovieTitle() + ": " + e.getMessage());
            }
        }
    }

    //Метод для получения списка всех премьер
    public List<Premiere> getPremieres() {
        return new ArrayList<>();
    }

    //Метод для поиска премьеры по ID.
    public Premiere findPremiereById(int id) {
        Premiere premiere = premiereMap.get(id);
        if (premiere == null) {
            logger.warning("Премьера с ID " + id + " не найдена.");
        } else {
            logger.info("Премьера найдена по ID: " + id);
        }
        return premiere;
    }

    //Метод для удаления премьеры по ID
    public void removePremiereById(int id) {
        Premiere premiere = premiereMap.remove(id);
        if (premiere == null) {
            logger.warning("Не удалось удалить премьеру с ID " + id + ": Премьера не найдена.");
        } else {
            logger.info("Премьера с ID " + id + " удалена.");
        }
    }

    //Метод для получения информации о количестве премьер
    public int getPremiereCount() {
        return premiereMap.size();
    }
}