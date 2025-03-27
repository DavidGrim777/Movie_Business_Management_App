package movie.business.app.repository;

import lombok.extern.slf4j.Slf4j;
import movie.business.app.model.Premiere;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@Slf4j
public class PremiereRepository {

    public void savePremieresToFile(Map<String, Premiere> premiereMap, boolean testModus) {
        if (!testModus) {
        String fileName = "premieres.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("ID, Название, Дата, Бюджет, Локация, Кол-во билетов, Продано билетов");
            writer.newLine();

            for (Premiere premiere : premiereMap.values()) {
                String premiereData = premiere.getId() + ", " +
                        premiere.getMovieTitle() + ", " +
                        premiere.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm z", Locale.ENGLISH)) + ", " +
                        premiere.getBudget() + ", " +
                        premiere.getLocation() + ", " +
                        premiere.getTicketCount()+ ", " +
                        premiere.getTicketSold();
                writer.write(premiereData);
                writer.newLine();
            }
            System.out.println("Все премьеры сохранены в файл." + fileName);
            log.info("Все премьеры сохранены в файл {}", fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении премьер: " + e.getMessage());
            log.warn("Ошибка при сохранении премьер: {}", e.getMessage());
        }
        } else {
            String fileName = "test_premieres.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write("ID, Название, Дата, Бюджет, Локация, Кол-во билетов, Продано билетов");
                writer.newLine();

                for (Premiere premiere : premiereMap.values()) {
                    String premiereData = premiere.getId() + ", " +
                            premiere.getMovieTitle() + ", " +
                            premiere.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm z", Locale.ENGLISH)) + ", " +
                            premiere.getBudget() + ", " +
                            premiere.getLocation() + ", " +
                            premiere.getTicketCount()+ ", " +
                            premiere.getTicketSold();
                    writer.write(premiereData);
                    writer.newLine();
                }
                System.out.println("Все премьеры сохранены в тестовый файл." + fileName);
                log.info("Все премьеры сохранены в тестовый файл {}", fileName);
            } catch (IOException e) {
                System.out.println("Ошибка при сохранении тестовых премьер: " + e.getMessage());
                log.warn("Ошибка при сохранении тестовых премьер: {}", e.getMessage());
            }
        }
    }

    // Метод для сохранения гостей в файл
    public void saveGuestsToFile( Premiere premiere, boolean testMode) {
        String id = premiere.getId();
        List<String> guestList = premiere.getGuestList();
        if (!testMode) {
            String fileName = id + "_guests.dat"; // Используем ID премьеры для имени файла
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
                oos.writeObject(guestList);
                System.out.println("Список гостей для премьеры " + id + " сохранен в файл: " + fileName);
            } catch (IOException e) {
                System.out.println("Ошибка при сохранении гостей: " + id + ": " + e.getMessage());
                log.warn("Ошибка при сохранении гостей в файл для премьеры {}: {}", id , e.getMessage());
            }
        } else {
            String fileName = id + "_testGuests.dat"; // Используем ID премьеры для имени файла
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
                oos.writeObject(guestList);
                System.out.println("Список гостей для премьеры " + id + " сохранен в файл: " + fileName);
            } catch (IOException e) {
                System.out.println("Ошибка при сохранении гостей: " + id + ": " + e.getMessage());
                log.warn("Ошибка при сохранении гостей из тестов в файл для премьеры {}: {}", id, e.getMessage());
            }
        }
    }

    // Метод для сохранения отзывов в текстовый файл
    public void saveReviewsToFile(Premiere premiere, boolean testProcess) {
        String id = premiere.getId();
        List<String> reviews = premiere.getReviews();
        if (!testProcess) {
            String fileName = id + "_reviews.txt"; // Используем ID премьеры для имени файла
            Path filePath = Paths.get(fileName);
            try {
                // Записываем все отзывы в файл (добавление строк)
                Files.write(filePath, reviews, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                System.out.println("Отзывы для премьеры " + id + " сохранены в файл: " + fileName);
            } catch (IOException e) {
                System.out.println("Ошибка при сохранении отзывов для премьеры " + id + ": " + e.getMessage());
                log.warn("Ошибка при сохранении отзывов в файл для премьеры {}: {}", id, e.getMessage());
            }
        } else {
            String fileName = id + "_testReviews.txt"; // Используем ID премьеры для имени файла
            Path filePath = Paths.get(fileName);
            try {
                // Записываем все отзывы в файл (добавление строк)
                Files.write(filePath, reviews, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                System.out.println("Отзывы для премьеры " + id + " сохранены в файл: " + fileName);
            } catch (IOException e) {
                System.out.println("Ошибка при сохранении отзывов для премьеры " + id + ": " + e.getMessage());
                log.warn("Ошибка при сохранении отзывов из тестов в файл для премьеры {}: {} ", id, e.getMessage());
            }
        }
    }

    public Map<String, Premiere> loadPremiereFromFile() {
        Map<String, Premiere> premiereMap = new HashMap<>();
        String fileName = "premieres.txt";

        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Файл премьер не найден. Начинаем с пустого списка.");
            return new HashMap<>();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean firstLine = true; // Добавляем флаг для заголовка

            while ((line = reader.readLine()) != null) {
                if (firstLine) { // Пропускаем заголовок
                    firstLine = false;
                    continue;
                }
                String[] data = line.split(", ");
                if (data.length >= 7) {  // Убедимся, что у нас есть все данные
                    Premiere premiere = createPremiere(data);  // Создаем объект Premiere
                    if (premiere != null) {
                        premiereMap.put(premiere.getId(), premiere);  // Добавляем премьеру в карту
                    }
                }
            }
            // Проверка на наличие данных
            if (premiereMap.isEmpty()) {
                log.info("Нет данных в файле.");
                System.out.println("Нет данных в файле.");

            } else {
                System.out.println("Данные успешно загружены из файла." + fileName);
            }
        } catch (IOException e) {
            log.info("Данные не найдены. Начинаем с пустого списка.");
            System.out.println("Данные не найдены. Начинаем с пустого списка.");
            premiereMap = new HashMap<>(); // <-- Создание пустой карты при ошибке
        }
        return premiereMap;
    }
    // Метод для создания объекта Premiere
    public Premiere createPremiere(String[] data) {
        try {
            String id = data[0];  // ID премьеры
            String movieTitle = data[1];  // Название фильма
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm z", Locale.ENGLISH);
            ZonedDateTime dateTime = ZonedDateTime.parse(data[2], formatter);  // Преобразуем строку в ZonedDateTime

            double budget = Double.parseDouble(data[3]);  // Бюджет
            String location = data[4];  // Местоположение
            int ticketCount = Integer.parseInt(data[5]);  // Количество билетов

            Premiere premiere = new Premiere(id, movieTitle, dateTime, location, ticketCount, budget);  // Создаем объект Premiere
            premiere.setBudget(budget);  // Устанавливаем бюджет
            return premiere;
        } catch (Exception e) {
            log.error("Ошибка при создании премьеры из данных: {}", String.join(", ", data), e);
            return null;  // Возвращаем null, если произошла ошибка
        }
    }

    // Метод для загрузки гостей из файла
    public void loadGuestsFromFile(Premiere premiere, boolean testMode) {
        String id = premiere.getId();
        if (!testMode) {
            String fileName = id + "_guests.dat"; // Используем ID премьеры для имени файла
            Path path = Paths.get(fileName);
            System.out.println("Месторасположение файла: " + path.toAbsolutePath());
            if (!Files.exists(path)) {
                log.warn("Файл не найден: {} Создаю новый список гостей.", fileName);
                premiere.setGuestList(new ArrayList<>()); // Создаем пустой список гостей
                return; // Прерываем выполнение метода, если файла нет
            }
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
                Object obj = ois.readObject();
                if (obj instanceof List<?>) { // Проверяем, что obj — это List
                    List<String> guests = new ArrayList<>();
                    for (Object item : (List<?>) obj) { // Приводим элементы списка к String
                        if (item instanceof String) {
                            guests.add((String) item);
                        } else {
                            log.warn("Найден неподходящий элемент в списке гостей: {} ", item);
                        }
                    }
                    premiere.setGuestList(guests);
                    System.out.println("Список гостей для премьеры " + id + " загружен из файла: " + fileName);
                } else {
                    log.warn("Файл не содержит корректный список гостей.");
                    premiere.setGuestList(new ArrayList<>()); // Создаем пустой список, если формат неверный
                }
            } catch (IOException | ClassNotFoundException e) {
                log.warn("Ошибка при загрузке гостей из файла для премьеры {}: {}", id, e.getMessage());
                System.out.println("Ошибка при загрузке гостей из файла для премьеры " + id + ": " + e.getMessage());
            }
        } else {
            String fileName = id + "_testGuests.dat"; // Используем ID премьеры для имени файла
            Path path = Paths.get(fileName);
            System.out.println("Месторасположение файла: " + path.toAbsolutePath());
            if (!Files.exists(path)) {
                log.warn("Файл тестовый не найден: {} Создаю новый список гостей.", fileName);
                premiere.setGuestList(new ArrayList<>()); // Создаем пустой список гостей
                return; // Прерываем выполнение метода, если файла нет
            }
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
                Object obj = ois.readObject();
                if (obj instanceof List<?>) { // Проверяем, что obj — это List
                    List<String> guests = new ArrayList<>();
                    for (Object item : (List<?>) obj) { // Приводим элементы списка к String
                        if (item instanceof String) {
                            guests.add((String) item);
                        } else {
                            log.warn("Найден неподходящий элемент в списке гостей для теста: {} ", item);
                        }
                    }
                    premiere.setGuestList(guests);
                    System.out.println("Список гостей для премьеры " + id + " загружен из тестового файла: " + fileName);
                } else {
                    log.warn("Файл не содержит корректный список гостей для теста.");
                    premiere.setGuestList(new ArrayList<>()); // Создаем пустой список, если формат неверный
                }
            } catch (IOException | ClassNotFoundException e) {
                log.warn("Ошибка при загрузке гостей из тестового файла для премьеры {}: {}", id, e.getMessage());
                System.out.println("Ошибка при загрузке гостей из тестового файла для премьеры " + id + ": " + e.getMessage());
            }
        }
    }

    // Метод для загрузки отзывов из текстового файла
    public void loadReviewsFromFile(Premiere premiere, boolean testMode) {
        String id = premiere.getId();
        if (!testMode) {
            String fileName = id + "_reviews.txt"; // Используем ID премьеры для имени файла
            Path filePath = Paths.get(fileName);
            try {
                List<String> reviews = Files.readAllLines(filePath); // Читаем все строки в список
                premiere.setReviews(new ArrayList<>(reviews));
                System.out.println("Отзывы для премьеры " + id + " загружены из файла: " + fileName);
            } catch (IOException e) {
                System.out.println("Ошибка при загрузке отзывов для премьеры " + id + ": " + e.getMessage());
                log.warn("Ошибка при загрузке отзывов из файла для премьеры {}: {}", id, e.getMessage());
            }
            if (!Files.exists(filePath)) {
                System.out.println("Файл с отзывами не найден для премьеры " + id);
            }
        } else {
            String fileName = id + "_testReviews.txt"; // Используем ID премьеры для имени файла
            Path filePath = Paths.get(fileName);
            try {
                List<String> reviews = Files.readAllLines(filePath); // Читаем все строки в список
                premiere.setReviews(new ArrayList<>(reviews));
                System.out.println("Отзывы для премьеры " + id + " загружены из файла: " + fileName);
            } catch (IOException e) {
                System.out.println("Ошибка при загрузке отзывов для премьеры " + id + ": " + e.getMessage());
                log.warn("Ошибка при загрузке отзывов из тестового файла для премьеры {}: {}", id, e.getMessage());
            }
        }
    }
}
