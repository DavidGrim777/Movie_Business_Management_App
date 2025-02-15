package com.business_app;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Premiere {

    private static final Logger logger = Logger.getLogger(Premiere.class.getName());
    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);


    private int id;
    private String movieTitle;
    private LocalDateTime date;
    private String location;
    private int ticketCount;
    private int ticketSold;
    private double budget;
    private List<String> guestList;// Список гостей
    private List<String> reviews;
    private double ticketPrice; // Стоимость билета
    private int minAgeForAdmission; // Минимальный возраст для посещения премьеры


    public Premiere(int id, String movieTitle, LocalDateTime date, String location,
                    int ticketCount, int ticketSold, double budget, List<String> guestList,
                    List<String> reviews, double ticketPrice, int minAgeForAdmission) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.date = date;
        this.location = location;
        this.ticketCount = ticketCount;
        this.ticketSold = 0;
        this.budget = budget;
        this.guestList = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.ticketPrice = ticketPrice;// Инициализируем стоимость билета
        this.minAgeForAdmission = minAgeForAdmission;// Устанавливаем минимальный возраст для посещения
    }

    public Premiere(int id, String movieTitle, String date, String location, int ticketCount,
                    double budget, double ticketPrice, int minAgeForAdmission) {

        if (movieTitle == null || movieTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Название фильма не может быть пустым");
        }
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Дата не может быть пустой");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Место проведения не может быть пустым");
        }
        if (ticketCount <= 0) {
            throw new IllegalArgumentException("Количество билетов должно быть больше 0");
        }
        if (budget <= 0) {
            throw new IllegalArgumentException("Бюджет должен быть больше 0");
        }

        // Пробуем установить дату
        setDate(date);
    }
    public void setDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Дата не может быть пустой или null.");
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            this.date = LocalDateTime.parse(date, formatter); // Используем LocalDateTime для даты
        } catch (DateTimeParseException e) {
            logger.warning("Некорректный формат даты. Требуется: " + DATE_FORMAT);
            throw new IllegalArgumentException("Ошибка: Некорректный формат даты. Требуется: " + DATE_FORMAT);
        }
    }

    // Метод для установки даты с проверкой формата

    public int getId() {
        return id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getLocation() {
        return location;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public int getTicketSold() {
        return ticketSold;
    }

    public double getBudget() {
        return budget;
    }

    public List<String> getGuestList() {
        return guestList;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public int getMinAgeForAdmission() {
        return minAgeForAdmission;
    }

    // Метод для добавления гостей с проверкой возраста и минимального возраста премьеры
    public void addGuest(String guestName, int guestAge) {
        if (guestName == null || guestName.trim().isEmpty()) {
            logger.warning("Ошибка при добавлении гостя: Имя гостя не может быть пустым.");
            return;
        }
        if (guestAge < minAgeForAdmission) {
            logger.warning("Ошибка при добавлении гостя: Гость " + guestName + "должен быть старше " + minAgeForAdmission + " лет для посещения этой премьеры.");
            return;
        }
        guestList.add(guestName); // Добавляем гостя в список
        logger.info("Гость " + guestName + " добавлен в список.");
    }

    // Метод для проверки доступности бюджета
    public boolean isBudgetAvailable(double cost) {
        if (cost < 0) {
            logger.warning("Ошибка: Стоимость не может быть отрицательной.");
            return false;
        }
        // Сравниваем, если бюджет больше или равен нужной сумме
        if (budget >= cost) {
            return true; // Если достаточно, возвращаем true
        } else {
            logger.warning("Ошибка: Недостаточно бюджета для суммы: " + cost);
            return false; // Если нет, возвращаем false
        }
    }

    // Метод для проверки, можем ли мы продать указанное количество билетов
    public boolean canSellTickets(int count) {
        if (count < 0) {
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

    // Метод для продажи билетов
    public void sellTickets(int count) {
        if (canSellTickets(count)) {
            ticketSold += count;
        } else {
            logger.warning("Ошибка при продаже билетов: Недостаточно билетов.");
            throw new IllegalArgumentException("Ошибка при продаже билетов: Недостаточно билетов.");
        }
    }

    /// Метод для возврата билетов
    public void returnTickets(int count) {
        if (count < 0) {
            logger.warning("Ошибка: Количество возвращаемых билетов не может быть отрицательным.");
            throw new IllegalArgumentException("Количество возвращаемых билетов не может быть отрицательным.");
        }
        if (ticketSold - count >= 0) {
            ticketSold -= count;
            logger.info("Возвращено " + count + " билетов. Общее количество проданных билетов: " + ticketSold);
        } else {
            logger.warning("Ошибка при возврате билетов: Невозможно вернуть больше билетов, чем было продано.");
            throw new IllegalArgumentException("Невозможно вернуть больше билетов, чем было продано.");
        }
    }

    // Метод для добавления отзыва
    public void addReview(String review) {
        if (review == null || review.trim().isEmpty()) {
            logger.warning("Ошибка при добавлении отзыва: отзыв не может быть пустым.");
        } else {
            reviews.add(review); // Добавляем отзыв в список
            logger.info("Отзыв добавлен: " + review);
        }
    }

    // Генерация отчета о премьере
    public String generateReport() {
        double totalRevenue = ticketSold * ticketPrice; // Примерная стоимость билета $10
        String report = "Отчет о премьере: " + movieTitle + "\n" +
                "Дата: " + date + "\n" +
                "Место проведения: " + location + "\n" +
                "Продано билетов: " + ticketSold + "\n" +
                "Общая прибыль: $" + totalRevenue + "\n" +
                "Список гостей: " + String.join(", ", guestList) + "\n" +
                "Отзывы: " + String.join("; ", reviews) + "\n";
        return report; // Возвращаем строку отчета
    }

    // Эмуляция отправки отчета по электронной почте
    public void sendReportByEmail() {
        String report = generateReport();
        logger.info("Отправка отчета по электронной почте: \n" + report);
    }
}
