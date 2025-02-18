package com.business_app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Premiere {

    private static final Logger logger = Logger.getLogger(Premiere.class.getName());
    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm";

    private int id;
    private String movieTitle;
    private LocalDateTime date;
    private String location;
    private int ticketCount; // кол-во доступных билетов
    private int ticketSold;// кол-во проданных билетов
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
        this.ticketSold = ticketSold;
        this.budget = budget;
        this.guestList = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.ticketPrice = ticketPrice;
        this.minAgeForAdmission = minAgeForAdmission;
    }

    public Premiere(int id, String movieTitle, String date, String location, int ticketCount,
                    double budget, double ticketPrice, int minAgeForAdmission) {
        this.id = id;  // Инициализируем id
        this.movieTitle = movieTitle;
        this.location = location;
        this.ticketCount = ticketCount;
        this.budget = budget;
        this.ticketPrice = ticketPrice;
        this.minAgeForAdmission = minAgeForAdmission;
        // Вызов метода setDate для корректного парсинга даты
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

    public LocalDateTime getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public int getTicketSold() {
        return ticketSold;
    }

    public void setTicketSold(int ticketSold) {
        this.ticketSold = ticketSold;
    }

    public List<String> getGuestList() {
        return guestList;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
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

    // Метод для проверки бюджета
    public boolean isBudgetAvailable(double budget) {
        // Проверка, чтобы бюджет премьеры не был отрицательным
        if (budget <= 0) {
            logger.warning("Ошибка: Бюджет премьеры не может быть отрицательным.");
            return false;
        }
        else   {
            return true; // Если бюджет достаточен, возвращаем true
        }
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

    /*// Эмуляция отправки отчета по электронной почте
    public void sendReportByEmail() {
        String report = generateReport();
        logger.info("Отправка отчета по электронной почте: \n" + report);
    }*/
}