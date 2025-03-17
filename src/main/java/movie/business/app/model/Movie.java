package movie.business.app.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import movie.business.app.enums.MovieGenre;
import movie.business.app.enums.MovieStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@ToString
@Slf4j
public class Movie {

    private String id;                        // Уникальный идентификатор фильма
    private String title;                     // Название фильма
    private MovieGenre genre;                 // Жанр фильма
    private LocalDate startDate;              // Дата начала показа фильма
    private LocalDate endDate;                // Дата окончания показа фильма
    private MovieStatus status;               // Статус фильма (PLANNED, IN_PROGRESS, COMPLETED)
    private double budget;                    // Бюджет фильма
    private List<String> producer;            // Список продюсеров фильма
    private List<String> actors;              // Список актёров фильма

    // Конструктор с минимальным набором данных для создания фильма
    public Movie(String movieId, String title, MovieStatus movieStatus) {
        this.id = movieId;
        this.title = title;
        this.status = movieStatus;
    }
    // Конструктор, инициализирующий все поля фильма
    public Movie(String id, String title, MovieGenre genre, LocalDate startDate, LocalDate endDate,
                 MovieStatus status, double budget, List<String> producer, List<String> actors) {

        this.id = id;
        this.title = title;
        this.genre = genre;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.budget = budget;
        this.producer = (producer != null) ? producer : new ArrayList<>();
        this.actors = (actors != null) ? actors : new ArrayList<>();
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

    public void setGenre(MovieGenre genre) {
        if (genre == null) {
            System.out.println("Жанр не может быть пустым.");
            return;
        }
        this.genre = genre;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            System.out.println("Дата начала не может быть пустой.");
            log.warn("Дата начала не может быть пустой.");
            return;
        }
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate == null) {
            System.out.println("Дата окончания не может быть пустой.");
            log.warn("Дата окончания не может быть пустой.");
            return;
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
        if (producer == null){
            producer = new ArrayList<>();
        }
        if (!producer.contains(producerName)) {
            producer.add(producerName);
            System.out.println("Продюсер добавлен: " + producerName);
            log.info("Продюсер добавлен: {}", producerName);
        } else {
            System.out.println("Этот продюсер уже добавлен.");
            log.warn("Этот продюсер уже добавлен.");
        }
    }

    public void removeProducer(String producerName) {

        if (producerName == null || producerName.trim().isEmpty() || producer == null || !producer.remove(producerName)) {
            System.out.println("Имя продюсера не может быть пустым.");
            log.warn("Имя продюсера не может быть пустым.");
            return;
        }
        if (producer.remove(producerName)) {
            System.out.println("Продюсер удалён: " + producerName);
            log.info("Продюсер удалён: {}", producerName);
        } else {
            System.out.println("Продюсер не найден.");
            log.info("Продюсер не найден.");
        }
    }

    // Методы для работы с актёрами
    public void addActor(String actorName) {

        if (actorName == null || actorName.trim().isEmpty()) {
            System.out.println("Имя актёра не может быть пустым.");
            log.warn("Имя актёра не может быть пустым.");
            return;
        }
        if (actors == null){
            actors = new ArrayList<>();
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

        if (actorName == null || actorName.trim().isEmpty() || actors == null || !actors.remove(actorName)) {
            System.out.println("Имя актёра не может быть пустым.");
            log.warn("Имя актёра не может быть пустым.");
            return;
        }
        if (actors.remove(actorName)) {
            System.out.println("Актёр удалён: " + actorName);
            log.info("Актёр удалён: {}", actorName);
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

        if (status == newStatus) {
            System.out.println("Фильм уже имеет статус: " + newStatus);
            log.info("Фильм уже имеет статус: " + newStatus);
            return;
        }
        this.status = newStatus;
        System.out.println("Статус фильма успешно обновлён на: " + newStatus);
        log.info("Статус фильма успешно обновлён на: " + newStatus);
    }
}
