package movie.business.app.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import movie.business.app.enums.MovieGenre;
import movie.business.app.enums.MovieStatus;

import java.time.LocalDate;
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

    public Movie(String id, String title, MovieGenre genre, MovieStatus status) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.status = status;
    }

    public Movie(String id, String title, MovieGenre genre, LocalDate startDate, LocalDate endDate, MovieStatus status, double budget, List<String> producer, List<String> actors) {
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

}
