package com.business_app;

import java.util.List;

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
        return producer;
    }

    public List<String> getActors() {
        return actors;
    }

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
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
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
        if (budget < 0) {
            System.out.println("Бюджет не может быть отрицательным.");
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

    public void addProducer(String producerName) {
        if (producerName == null || producerName.trim().isEmpty()) {
            System.out.println("Имя продюсера не может быть пустым.");
            return;
        }
        if (!producer.contains(producerName)) {
            producer.add(producerName);
            System.out.println("Режиссёр добавлен: " + producerName);
        } else {
            System.out.println("Этот режиссёр уже добавлен.");
        }
    }

    public void removeProducer(String producerName) {

        if (producerName == null || producerName.trim().isEmpty()) {
            System.out.println("Имя продюсера не может быть пустым.");
            return;
        }
        if (producer.remove(producerName)) {
            System.out.println("Режиссёр удалён: " + producerName);
        } else {
            System.out.println("Режиссёр не найден.");
        }
    }

    public void addActor(String actorName) {

        if (actorName == null || actorName.trim().isEmpty()) {
            System.out.println("Имя актёра не может быть пустым.");
            return;
        }
        if (!actors.contains(actorName)) {
            actors.add(actorName);
            System.out.println("Актёр добавлен: " + actorName);
        } else {
            System.out.println("Этот актёр уже добавлен.");
        }
    }

    public void removeActor(String actorName) {

        if (actorName == null || actorName.trim().isEmpty()) {
            System.out.println("Имя актёра не может быть пустым.");
            return;
        }
        if (actors.remove(actorName)) {
            System.out.println("Актёр удалён: " + actorName);
        } else {
            System.out.println("Актёр не найден.");
        }
    }

    public void updateStatus(MovieStatus newStatus) {
        if (newStatus == null) {
            System.out.println("Некорректный статус. Обновление не выполнено.");
            return;
        }
        MovieStatus status = MovieStatus.values()[0];

        switch (newStatus) {
            case PLANNED:
                System.out.println("Фильм теперь находится в производстве.");
                break;
            case IN_PROGRESS:
                System.out.println("Фильм теперь выпущен.");
                break;
            case COMPLETED:
                System.out.println("Фильм отменён.");
                break;
            default:
                System.out.println("Неизвестный статус.");
        }

        if (status == newStatus) {
            System.out.println("Фильм уже имеет статус: " + newStatus);
            return;
        }
        System.out.println("Статус фильма успешно обновлён на: " + newStatus);

    }
}
