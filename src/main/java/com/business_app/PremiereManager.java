package com.business_app;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PremiereManager {

    private Map<String, Premiere> premiereMap = new HashMap<>();
    private static final String FILE_NAME = "premieres.txt";

    public PremiereManager() {
        loadFromFile();
    }

    public Map<String, Premiere> getPremiereMap() {
        return premiereMap;
    }

    // Метод для добавления новой премьеры в список
    public void addPremiere(Premiere premiere) {
        if (premiere == null) {
            log.warn("Попытка добавить null в список премьеры.");
            throw new IllegalArgumentException("Премьера не может быть null.");
        }
        if (premiere.getId() == null) {
            log.warn("Попытка добавить премьеру без ID.");
            throw new IllegalArgumentException("ID премьеры не может быть null.");
        }
        premiereMap.put(premiere.getId(), premiere);
        System.out.println ("Премьера добавлена: " + premiere.getMovieTitle());
        saveToFile();
    }

    // Метод для генерации отчета по всем премьерам
    public void generatePremiereReport() {
        if (premiereMap.isEmpty()) {
            log.warn("Попытка сгенерировать отчет для пустого списка премьеры.");
            System.out.println("Нет премьеры для генерации отчета.");
            return;
        }
        log.info("Начало генерации отчетов для всех премьер.");
        for (Premiere premiere : premiereMap.values()) {
            try {
                String report = premiere.generateReport();
                System.out.println(report); // Генерируем отчет для каждой премьеры
                System.out.println("Отчет о премьере сгенерирован для: " + premiere.getMovieTitle());
                log.info(report); // Логируем отчет о премьере
            } catch (Exception e) {
                log.warn("Ошибка при генерации отчета для премьеры " + premiere.getMovieTitle() + ": " + e.getMessage());
            }
        }
    }

    //Метод для поиска премьеры по ID.
    public Premiere findPremiereById(String id) {
        Premiere premiere = premiereMap.get(id);
        if (premiere == null) {
            log.warn("Премьера с ID " + id + " не найдена.");
            System.out.println("Премьера с ID " + id + " не найдена.");
        } else {
            System.out.println("Премьера найдена по ID: " + id);
        }
        return premiere;
    }

    //Метод для удаления премьеры по ID
    public void removePremiereById(String id) {
        Premiere premiere = premiereMap.remove(id);
        if (premiere == null) {
            log.warn("Не удалось удалить премьеру с ID " + id + ": Премьера не найдена.");
        System.out.println("Не удалось удалить премьеру с ID " + id + ": Премьера не найдена.");
        } else {
            System.out.println("Премьера с ID " + id + " удалена.");
            saveToFile();
        }
    }
    //Метод для получения информации о количестве премьер
    public int getPremiereCount() {
        return premiereMap.size();
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Premiere premiere : premiereMap.values()) {
                // Форматируем данные премьеры в строку
                String premiereData = premiere.getId() + ", " + premiere.getMovieTitle() + ", " +
                        premiere.getDate().toString() + ", " + premiere.getBudget() + ", " + premiere.getLocation() + ", " +
                        premiere.getTicketCount();
                writer.write(premiereData);  // Записываем строку в файл
                writer.newLine();  // Переход на новую строку
            }
            System.out.println("Данные успешно сохранены в файл " + FILE_NAME);

        } catch (IOException e) {
           log.warn("Ошибка при сохранении данных: "+ e.getMessage());
           System.out.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data.length >= 6) {  // Убедимся, что у нас есть все данные
                    String id = data[0];  // ID премьеры
                    String movieTitle = data[1];  // Название фильма
                    ZonedDateTime dateTime = ZonedDateTime.parse(data[2]);  // Преобразуем строку в ZonedDateTime
                    double budget = Double.parseDouble(data[3]);  // Бюджет
                    String location = data[4];  // Местоположение
                    int ticketCount = Integer.parseInt(data[5]);  // Количество билетов

                    Premiere premiere = new Premiere(id, movieTitle, dateTime, location, ticketCount);  // Создаем объект Premiere
                    premiere.setBudget(budget);  // Устанавливаем бюджет
                    premiereMap.put(id, premiere);  // Добавляем премьеру в карту
                }
            }
            // Проверка на наличие данных
            if (premiereMap.isEmpty()) {
                log.info("Нет данных в файле.");
            System.out.println("Нет данных в файле.");
        } else {
            System.out.println("Данные успешно загружены из файла.");
        }
        } catch (IOException e) {
            log.info("Данные не найдены. Начинаем с пустого списка.");
            System.out.println("Данные не найдены. Начинаем с пустого списка.");
            premiereMap = new HashMap<>(); // <-- Создание пустой карты при ошибке
        }
    }
}