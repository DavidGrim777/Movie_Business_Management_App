package com.business_app;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Movie {
    private String id;
    private String title;
    private String genre;
    private String startDate;
    private String endDate;
    private MovieStatus status;
    private double budget;
    private List<String> producer;
    private List<String> actors;

    // // Конструктор инициализирует все поля фильма

    public Movie(String id, String title, String genre, String startDate, String endDate,
                 MovieStatus status, double budget, List<String> producer, List<String> actors) {

        this.id = id;
        this.title = title;
        this.genre = genre;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.budget = budget;
        this.producer = producer;
        this.actors = actors;
    }
    // Геттеры для получения значений полей

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public MovieStatus getStatus() {
        return status;
    }

    public double getBudget() {
        return budget;
    }

    public List<String> getProducer() {
        if (producer == null) {
            return new ArrayList<>();
        }
        return producer;
    }

    public List<String> getActors() {
        if (actors == null) {
            return new ArrayList<>();
        }
        return actors;
    }
    // Сеттеры с проверками входных данных

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID не может быть пустым.");
            return;
        }
        this.id = id;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Название не может быть пустым.");
            return;
        }
        this.title = title;
    }

    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            System.out.println("Жанр не может быть пустым.");
            return;
        }
        this.genre = genre;
    }

    public void setStartDate(String startDate) {
        if (startDate == null || startDate.trim().isEmpty()) {
            System.out.println("Дата начала не может быть пустой.");
            log.warn("Дата начала не может быть пустой.");
        }
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        if (endDate == null || endDate.trim().isEmpty()) {
            System.out.println("Дата окончания не может быть пустой.");
            log.warn("Дата окончания не может быть пустой.");
        }
        this.endDate = endDate;
    }

    public void setStatus(MovieStatus status) {
        if (status == null) {
            System.out.println("Статус не может быть null.");
            return;
        }
        this.status = status;
    }

    public void setBudget(double budget) {
        if (budget <= 0) {
            System.out.println("Бюджет не может быть отрицательным или меньше 0.");
            return;
        }
        this.budget = budget;
    }

    public void setProducer(List<String> producer) {
        if (producer == null) {
            System.out.println("Список продюсеров не может быть null.");
            return;
        }
        this.producer = producer;
    }

    public void setActors(List<String> actors) {
        if (actors == null) {
            System.out.println("Список актёров не может быть null.");
            return;
        }
        this.actors = actors;
    }
    // Методы для работы с продюсерами

    public void addProducer(String producerName) {
        if (producerName == null || producerName.trim().isEmpty()) {
            System.out.println("Имя продюсера не может быть пустым.");
            log.warn("Имя продюсера не может быть пустым.");
            return;
        }
        if (!producer.contains(producerName)) {
            producer.add(producerName);
            System.out.println("Продюсера добавлен: " + producerName);
            log.info("Продюсера добавлен: {}", producerName);
        } else {
            System.out.println("Этот продюсера уже добавлен.");
            log.warn("Этот продюсера уже добавлен.");
        }
    }

    public void removeProducer(String producerName) {

        if (producerName == null || producerName.trim().isEmpty()) {
            System.out.println("Имя продюсера не может быть пустым.");
            log.warn("Имя продюсера не может быть пустым.");
            return;
        }
        if (producer.remove(producerName)) {
            System.out.println("Продюсера удалён: " + producerName);
            log.info("Продюсера удалён: {}", producerName);
        } else {
            System.out.println("Продюсера не найден.");
            log.info("Продюсера не найден.");
        }
    }
    // Методы для работы с актёрами

    public void addActor(String actorName) {

        if (actorName == null || actorName.trim().isEmpty()) {
            System.out.println("Имя актёра не может быть пустым.");
            log.warn("Имя актёра не может быть пустым.");
            return;
        }
        if (!actors.contains(actorName)) {
            actors.add(actorName);
            System.out.println("Актёр добавлен: " + actorName);
            log.info("Актёр добавлен: {}", actorName);
        } else {
            System.out.println("Этот актёр уже добавлен.");
            log.info("Этот актёр уже добавлен.");
        }
    }

    public void removeActor(String actorName) {

        if (actorName == null || actorName.trim().isEmpty()) {
            System.out.println("Имя актёра не может быть пустым.");
            log.warn("Имя актёра не может быть пустым.");
            return;
        }
        if (actors.remove(actorName)) {
            System.out.println("Актёр удалён: " + actorName);
            log.info("Актёр удалён: {}",actorName);
        } else {
            System.out.println("Актёр не найден.");
            log.info("Актёр не найден.");
        }
    }

    public void updateStatus(MovieStatus newStatus) {
        if (newStatus == null) {
            System.out.println("Некорректный статус. Обновление не выполнено.");
            log.warn("Некорректный статус. Обновление не выполнено.");
            return;
        }
        // Метод для обновления статуса фильма

        switch (newStatus) {
            case PLANNED:
                System.out.println("Фильм теперь находится в производстве.");
                log.info("Фильм теперь находится в производстве.");
                break;
            case IN_PROGRESS:
                System.out.println("Фильм теперь выпущен.");
                log.info("Фильм теперь выпущен.");
                break;
            case COMPLETED:
                System.out.println("Фильм отменён.");
                log.info("Фильм отменён.");
                break;
            default:
                System.out.println("Неизвестный статус.");
                log.warn("Неизвестный статус.");
        }

        if (status == newStatus) {
            System.out.println("Фильм уже имеет статус: " + newStatus);
            log.info("Фильм уже имеет статус: {}", newStatus);
            return;
        }
        System.out.println("Статус фильма успешно обновлён на: " + newStatus);
        log.info("Статус фильма успешно обновлён на: {}", newStatus);

    }
}
