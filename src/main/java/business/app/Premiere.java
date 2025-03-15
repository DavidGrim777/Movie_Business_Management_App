package business.app;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
@Getter
@Setter
@ToString
public class Premiere implements Serializable {

    private static final Logger logger = Logger.getLogger(Premiere.class.getName());
    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm z";

    private String id;
    private String movieTitle;
    private ZonedDateTime date;
    private String location;
    private int ticketCount; // кол-во доступных билетов
    private int initialTicketCount; // Исходное количество билетов
    private int ticketSold;// кол-во проданных билетов
    private double budget;
    private List<String> guestList;// Список гостей
    private List<String> reviews;
    private double ticketPrice; // Стоимость билета
    private int minAgeForAdmission; // Минимальный возраст для посещения премьеры


    public Premiere(String id, String movieTitle, ZonedDateTime date, String location, int ticketCount) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.date = date;
        this.location = location;
        this.ticketCount = ticketCount;
        this.initialTicketCount = ticketCount; // Начальное количество билетов
        this.ticketSold = 0;
        this.budget = 0;
        this.guestList = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.ticketPrice = 10;
        this.minAgeForAdmission = 18;

        // Вызов метода setDate для корректного парсинга даты
        // Используем форматирование ZonedDateTime в строку
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String formattedDate = date.format(formatter);
        setDate(formattedDate);

        loadGuestsFromFile();
    }
    public void setDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Дата не может быть пустой или null.");
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            this.date = ZonedDateTime.parse(date,formatter); // Используем ZonedDateTime для парсинга
        } catch (Exception e) {
            logger.warning("Некорректный формат даты. Требуется: " + DATE_FORMAT);
            throw new IllegalArgumentException("Ошибка: Некорректный формат даты. Требуется: " + DATE_FORMAT);
        }
    }
    // Метод для установки количества билетов
    public void setTicketCount(int ticketCount) {
        if (ticketCount < 0) {
            throw new IllegalArgumentException("Количество билетов не может быть отрицательным.");
        }
        this.ticketCount = ticketCount;

    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID не может быть пустым или null.");
        }
        if (id.length() > 30) {
            throw new IllegalArgumentException("ID не может быть длиннее 30 символов.");
        }
        this.id = id;
    }

    public String getLocation() {
        if (location == null || location.isEmpty()) {
            return "Местоположение не указано";
        }
        return location;
    }

    // Метод для добавления гостей с проверкой возраста и минимального возраста премьеры
    public void addGuest(String guestName, int guestAge) {

        if (guestName == null || guestName.trim().isEmpty()) {
            logger.warning("Ошибка при добавлении гостя: Имя гостя не может быть пустым.");
            return;
        }
        if (guestAge < minAgeForAdmission) {
            logger.warning("Ошибка при добавлении гостя: Гость " + guestName + " должен быть старше " + minAgeForAdmission + " лет для посещения этой премьеры.");
            return;
        }
        guestList.add(guestName); // Добавляем гостя в список
        System.out.println ("Гость " + guestName + " добавлен в список.");

        saveGuestsToFile(); // Сохраняем гостей в файл
    }

    // Метод для проверки, можем ли мы продать указанное количество билетов
    public boolean canSellTickets(int count) {
        if (count <= 0) {
            logger.warning("Ошибка: Количество билетов не может быть отрицательным.");
            return false;
        }
        // Проверяем, не продано ли больше билетов, чем есть в наличии
        if (ticketSold + count <= ticketCount) {
            return true; // Если количество проданных билетов и новые билеты не превышают доступных, возвращаем true
        } else {
            logger.warning("Ошибка: Недостаточно билетов для продажи " + count + " билетов.");
            return false; // Если билетов недостаточно, возвращаем false
        }
    }

    /// Метод для продажи билетов
    public boolean sellTickets(int count) {
        // Проверяем, можем ли продать указанное количество билетов
        if (canSellTickets(count)) {
            // Если можем продать, то увеличиваем количество проданных билетов
            ticketSold = ticketSold + count;
            return true;  // Продажа успешна
        } else {
            // Если нет, выводим сообщение об ошибке
            logger.warning("Ошибка при продаже билетов: Недостаточно билетов.");
            System.out.println("Ошибка: Недостаточно билетов для продажи.");
            return false;  // Продажа не удалась
        }
    }

    /// Метод для возврата билетов
    public void returnTickets(int ticketsToReturn, int ticketsSold, boolean isRefundable) {
        if (ticketsToReturn <= 0) {
            throw new IllegalArgumentException("Ошибка при возврате билетов: количество билетов должно быть положительным.");
        }
        if (ticketsToReturn > ticketsSold) {
            throw new IllegalArgumentException("Ошибка при возврате билетов: Невозможно вернуть больше билетов, чем было продано.");
        }

        // Уменьшаем количество проданных билетов
        ticketSold -= ticketsToReturn;

        // Возвращаем билеты в доступные для продажи, но не превышаем начальное количество билетов
        ticketCount = initialTicketCount - ticketSold;


        System.out.println("Проданные билеты: " + ticketsSold);
        System.out.println("Возвращено " + ticketsToReturn + " билетов.");
        System.out.println("Оставшиеся билеты для продажи: " + ticketCount);
    }

    // Метод для проверки бюджета
    public boolean isBudgetAvailable(double budget) {
        // Проверка, чтобы бюджет премьеры не был отрицательным
        if (budget <= 0) {
            logger.warning("Ошибка: Бюджет премьеры не может быть отрицательным.");
            return false;
        } else {
            return true; // Если бюджет достаточен, возвращаем true
        }
    }

    // Метод для добавления бюджета
    public void addBudget(double budgetToAdd) {
        if (budgetToAdd > 0) {
            this.budget += budgetToAdd;  // Добавляем к текущему бюджету
        } else {
            throw new IllegalArgumentException("Бюджет не может быть отрицательным или нулевым.");
        }
    }

    // Метод для добавления отзыва
    public void addReview(String review) {
        if (review == null || review.trim().isEmpty()) {
            logger.warning("Ошибка при добавлении отзыва: отзыв не может быть пустым.");
            System.out.println("Ошибка при добавлении отзыва: отзыв не может быть пустым.");
        } else {
            reviews.add(review); // Добавляем отзыв в список
            System.out.println ("Отзыв добавлен: " + review);
            saveReviewsToFile(); // Сохраняем отзывы в текстовый файл
        }
    }

    // Метод для сохранения отзывов в текстовый файл
    public void saveReviewsToFile() {
        String fileName = id + "_reviews.txt"; // Используем ID премьеры для имени файла
        Path filePath = Paths.get(fileName);
        try {
            // Записываем все отзывы в файл (добавление строк)
            Files.write(filePath, reviews, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("Отзывы для премьеры " + id + " сохранены в файл: " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении отзывов для премьеры " + id + ": " + e.getMessage());
            logger.warning("Ошибка при сохранении отзывов в файл для премьеры " + id + ": " + e.getMessage());
        }
    }

    // Метод для загрузки отзывов из текстового файла
    public void loadReviewsFromFile() {
        String fileName = id + "_reviews.txt"; // Используем ID премьеры для имени файла
        Path filePath = Paths.get(fileName);
        if (!Files.exists(filePath)) {
            System.out.println("Файл с отзывами не найден для премьеры " + id);
            return;
        }

        try {
            reviews = Files.readAllLines(filePath); // Читаем все строки в список
            System.out.println("Отзывы для премьеры " + id + " загружены из файла: " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке отзывов для премьеры " + id + ": " + e.getMessage());
            logger.warning("Ошибка при загрузке отзывов из файла для премьеры " + id + ": " + e.getMessage());
        }
    }

    // Метод для очистки списка гостей
    public void clearGuests() {
        guestList.clear();
        System.out.println("Список гостей для премьеры " + movieTitle + " очищен.");
    }

    // Метод для сохранения гостей в файл
    private void saveGuestsToFile() {
        String fileName = id + "_guests.dat"; // Используем ID премьеры для имени файла
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
            oos.writeObject(guestList);
            System.out.println("Список гостей для премьеры " + id + " сохранен в файл: " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении гостей: " + id + ": " + e.getMessage());
            logger.warning("Ошибка при сохранении гостей в файл для премьеры " + id + ": " + e.getMessage());
        }
    }

    // Метод для загрузки гостей из файла
    public void loadGuestsFromFile() {
        String fileName = id + "_guests.dat"; // Используем ID премьеры для имени файла
        Path path = Paths.get(fileName);
        System.out.println("Месторасположение файла: " + path.toAbsolutePath());
        if (!Files.exists(path)) {
            logger.warning("Файл не найден: " + fileName);
            return; // Прерываем выполнение метода, если файла нет
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            guestList = (List<String>) ois.readObject();
            System.out.println ("Список гостей для премьеры " + id + " загружен из файла: " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            logger.warning("Ошибка при загрузке гостей из файла для премьеры " + id + ": " + e.getMessage());
            System.out.println("Ошибка при загрузке гостей из файла для премьеры " + id + ": " + e.getMessage());;
        }
    }

    // Генерация отчета о премьере
    public String generateReport() {
        double totalRevenue = ticketSold * ticketPrice; // Примерная стоимость билета $10
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = date.format(formatter);  // Форматируем дату
        String report = "Отчет о премьере: " + movieTitle + "\n" +
                "Дата: " + formattedDate + "\n" +
                "Место проведения: " + location + "\n" +
                "Продано билетов: " + ticketSold + "\n" +
                "Общая прибыль: $" + totalRevenue + "\n" +
                "Список гостей: " + (guestList.isEmpty() ? "Нет гостей" : String.join(", ", guestList)) + "\n" +
                "Отзывы: " + String.join("; ", reviews) + "\n";
        return report; // Возвращаем строку отчета
    }
}