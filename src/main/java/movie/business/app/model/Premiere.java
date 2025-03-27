package movie.business.app.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Slf4j
public class Premiere implements Serializable {
    @Serial//с Java 14, рекомендуется аннотировать @Serial
    private static final long serialVersionUID = 1L; // Версия класса для сериализации
    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm z";

    private String id;
    private String movieTitle;
    private ZonedDateTime date;
    private String location;
    private int ticketCount; // количество оставшихся билетов (вычисляется как initialTicketCount - ticketSold)
    private int initialTicketCount; // Исходное количество билетов
    private int ticketSold;// кол-во проданных билетов
    private double budget;
    private List<String> guestList;// Список гостей
    private List<String> reviews;
    private double ticketPrice; // Стоимость билета


    public Premiere(String id, String movieTitle, ZonedDateTime date, String location, int ticketCount,
                    int initialTicketCount, int ticketSold, double budget, List<String> guestList,
                    List<String> reviews, double ticketPrice) {
        setId(id); // Проверка на null, пустую строку и длину
        this.movieTitle = movieTitle;
        this.date = date;
        this.location = location;
        this.ticketCount = ticketCount;
        this.initialTicketCount = ticketCount;
        this.ticketSold = 0;
        this.budget = budget;
        this.guestList = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.ticketPrice = 10;

        // Вызов метода setDate для корректного парсинга даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String formattedDate = date.format(formatter);
        setDate(formattedDate);
    }

    // Перегруженный конструктор для создания премьеры с передачей только основных параметров
    public Premiere(String id, String movieTitle, ZonedDateTime date, String location, int ticketCount, double budget) {
        this(id, movieTitle, date, location, ticketCount, ticketCount, 0, budget, new ArrayList<>(), new ArrayList<>(), 10);
    }

    public void setDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Дата не может быть пустой или null.");
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            this.date = ZonedDateTime.parse(date,formatter); // Используем ZonedDateTime для парсинга
        } catch (Exception e) {
            log.warn("Некорректный формат даты. Требуется: {} ", DATE_FORMAT);
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
    public void addGuest(String guestName, boolean isGuestAge) {

        if (guestName == null || guestName.trim().isEmpty()) {
            log.warn("Ошибка при добавлении гостя: Имя гостя не может быть пустым.");
            return;
        }
        if (!isGuestAge) {
            log.warn("Ошибка при добавлении гостя: Гость {} должен быть старше 18 лет для посещения этой премьеры.", guestName);
            return;
        }
        guestList.add(guestName); // Добавляем гостя в список
        System.out.println ("Гость " + guestName + " добавлен в список.");
    }

    // Метод для проверки, можем ли мы продать указанное количество билетов
    public boolean canSellTickets(int count) {
        if (count <= 0) {
            log.warn("Ошибка: Количество билетов не может быть отрицательным.");
            return false;
        }
        int availableTickets = initialTicketCount - ticketSold;
        // Проверяем, не продано ли больше билетов, чем есть в наличии
        if (count <= availableTickets) {
            return true; // Если количество проданных билетов и новые билеты не превышают доступных, возвращаем true
        } else {
            log.warn("Ошибка: Недостаточно билетов для продажи {}. Доступно {} билетов.", count, availableTickets);
            return false; // Если билетов недостаточно, возвращаем false
        }
    }

    // Метод для продажи билетов
    public boolean sellTickets(int count) {
        // Проверяем, можем ли продать указанное количество билетов
        if (canSellTickets(count)) {
            // Если можем продать, то увеличиваем количество проданных билетов
            ticketSold = ticketSold + count;
            // Обновляем количество оставшихся билетов
            ticketCount = initialTicketCount - ticketSold;
            return true;  // Продажа успешна
        } else {
            // Если нет, выводим сообщение об ошибке
            log.warn("Ошибка при продаже билетов: Недостаточно билетов.");
            System.out.println("Ошибка: Недостаточно билетов для продажи.");
            return false;  // Продажа не удалась
        }
    }

    // Метод для возврата билетов
    public void returnTickets(int ticketsToReturn, int ticketsSold) {
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

    // Пока не используется, но может пригодиться в расширении логики
    public boolean isBudgetAvailable(double budget) {
        // Проверка, чтобы бюджет премьеры не был отрицательным
        if (budget <= 0) {
            log.warn("Ошибка: Бюджет премьеры не может быть отрицательным.");
            return false;
        }
        return true; // Если бюджет достаточен, возвращаем true
    }

    // Пока не используется, но может пригодиться если использовать для увеличения бюджета (например, спонсорство)
    public void addBudget(double budgetToAdd) {
        if (budgetToAdd <= 0) {
            throw new IllegalArgumentException("Бюджет не может быть отрицательным или нулевым.");
        }
            this.budget += budgetToAdd;  // Добавляем к текущему бюджету
        }

    // Метод для добавления отзыва
    public void addReview(String review) {
        if (review == null || review.trim().isEmpty()) {
            log.warn("Ошибка при добавлении отзыва: отзыв не может быть пустым.");
            System.out.println("Ошибка при добавлении отзыва: отзыв не может быть пустым.");
        } else {
            reviews.add(review); // Добавляем отзыв в список
            System.out.println ("Отзыв добавлен: " + review);
        }
    }
    // Генерация отчета о премьере
    public String generateReport() {
        double totalRevenue = ticketSold * ticketPrice; // Примерная стоимость билета $10
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = date.format(formatter);  // Форматируем дату
        return  "Отчет о премьере: " + movieTitle + "\n" +
                "Дата: " + formattedDate + "\n" +
                "Место проведения: " + location + "\n" +
                "Продано билетов: " + ticketSold + "\n" +
                "Общая прибыль: $" + totalRevenue + "\n" +
                "Список гостей: " + (guestList.isEmpty() ? "Нет гостей" : String.join(", ", guestList)) + "\n" +
                "Отзывы: " + String.join("; ", reviews) + "\n";
    }
}