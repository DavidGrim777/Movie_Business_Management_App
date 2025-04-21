package movie.business.app.manager;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import movie.business.app.enums.MovieGenre;
import movie.business.app.enums.MovieStatus;
import movie.business.app.model.Movie;
import movie.business.app.repository.MovieRepository;

import java.io.*;
import java.util.*;

@Getter
@Setter
@Slf4j
public class MovieManager {
    private final MovieRepository movieRepository;
    private List<Movie> movies;
    private static final String FILE_NAME = "movie.txt";

    // Конструктор инициализирует список фильмов
    public MovieManager(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        this.movies = new ArrayList<>();
    }

    // Метод для добавления фильма в список
    public void addMovie(Movie movie) {
        if (movie == null) {
            System.out.println("Попытка добавить null-фильм.");
            log.warn("Попытка добавить null-фильм.");
            return;
        }
        movieRepository.seve(movie);
        System.out.println("film dobavlen: " + movie.getTitle());
        log.info("film dobavlen: {}", movie.getTitle());
    }

    // Метод для удаления фильма по ID
    public void removeMovie(String movieId) {
        if (movieId == null || movieId.trim().isEmpty()) {
            System.out.println("Попытка удалить фильм с пустым ID.");
            log.warn("Попытка удалить фильм с пустым ID.");
            return;
        }
        Optional<Movie> movieToRemove = movieRepository.findById(movieId);
        if (movieToRemove.isPresent()) {
            movieRepository.deleteById(movieId);
            System.out.println("Фильм удалён: " + movieToRemove.get().getTitle());
            log.info("Фильм удалён: {}", movieToRemove.get().getTitle());
        } else {
            System.out.println("Фильм с ID " + movieId + " не найден.");
            log.warn("Фильм с ID {} для удаления не найден.", movieId);
        }
    }


    // Метод для обновления данных фильма
    public void updateMovie(Movie updatedMovie) {
        if (updatedMovie == null) {
            System.out.println("Попытка обновить null-фильм.");
            log.warn("Попытка обновить null-фильм.");
            return;
        }
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId().equals(updatedMovie.getId())) {
                movies.set(i, updatedMovie);
                System.out.println("Фильм обновлён: " + updatedMovie.getTitle());
                log.info("Фильм обновлён: {}", updatedMovie.getTitle());
                return;
            }
        }
        System.out.println("Фильм с ID " + updatedMovie.getId() + " не найден.");
        log.warn("Фильм с ID {} для обновления не найден.", updatedMovie.getId());
    }

    // Метод выводит в консоль все фильмы из списка
    public void printAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            System.out.println("Список фильмов пуст.");
        } else {
            for (Movie movie : movies) {
                System.out.println("ID фильма: " + movie.getId());
                System.out.println("Название: " + movie.getTitle());
                System.out.println("Статус: " + movie.getStatus());
                System.out.println("Жанр: " + movie.getGenre());
                System.out.println("Продюсер: " + movie.getProducer());
                System.out.println("Актеры: " + movie.getActors());

                System.out.println("---------------------------------------");
            }
        }
    }

    public void saveMovies() {
        List<Movie> movies = movieRepository.findAll();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, false))) {// false - перезапись
            for (Movie movie : movies) {
                bufferedWriter.write(movie.getId() + ", " + movie.getTitle() + ", " + movie.getStatus() + ", " + movie.getGenre());
                bufferedWriter.newLine();
            }
            System.out.println("Фильмы сохранены в файл.");
            log.info("Фильмы сохранены в файл.");
        } catch (IOException exception) {
            System.out.println("Ошибка при загрузке списка фильмов: " + exception.getMessage());
            log.error("Ошибка при загрузке списка фильмов: {}", exception.getMessage());
        }
    }

    public void loadMovie() {
        movies.clear(); // Очищаем список перед загрузкой
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String movieLine;
            int lineNumber = 0;
            while ((movieLine = reader.readLine()) != null) {
                lineNumber++;
                String[] parts = movieLine.split(", ");
                if (parts.length >= 4) { // Если в строке минимум 4 элемента
                    try {
                        String id = parts[0].trim();
                        String title = parts[1].trim();
                        MovieStatus status = MovieStatus.valueOf(parts[2].trim());
                        MovieGenre genre = MovieGenre.valueOf(parts[3].trim()); // Разбираем жанр
                        Movie movie = new Movie(id, title, genre, status);
                        movieRepository.seve(movie);
                    } catch (IllegalArgumentException exception) {
                        log.warn("Ошибка в строке {}: Некорректный статус или жанр -> {}", lineNumber, movieLine);
                    }
                } else {
                    log.warn("Ошибка в строке {}: Недостаточно данных -> {}", lineNumber, movieLine);
                }
            }
            System.out.println("Фильмы успешно загружены из файла.");
            log.info("Фильмы успешно загружены из файла.");
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + FILE_NAME);
            log.error("Файл не найден: {}", FILE_NAME);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
            log.error("Ошибка при чтении файла: {}", e.getMessage());
        }
    }

    public void addProducer(Movie movie, String producerName) {
        if (producerName == null || producerName.trim().isEmpty()) {
            System.out.println("Имя продюсера не может быть пустым.");
            log.warn("Имя продюсера не может быть пустым.");
            return;
        }

        if (!movie.getProducer().contains(producerName)) {
            movie.getProducer().add(producerName);
            movieRepository.seve(movie);
            System.out.println("Продюсер добавлен: " + producerName);
            log.info("Продюсер добавлен: {}", producerName);
        } else {
            System.out.println("Продюсер уже существует.");
            log.warn("Попытка добавить уже существующего продюсера: {}", producerName);
        }
    }

    public void removeProducer(Movie movie, String producerName) {
        if (producerName == null || producerName.trim().isEmpty()) {
            System.out.println("Имя продюсера не может быть пустым.");
            log.warn("Имя продюсера не может быть пустым.");
            return;
        }

        if (movie.getProducer().remove(producerName)) {
            movieRepository.seve(movie);
            System.out.println("Продюсер удалён: " + producerName);
            log.info("Продюсер удалён: {}", producerName);
        } else {
            System.out.println("Продюсер не найден.");
            log.warn("Попытка удалить несуществующего продюсера: {}", producerName);
        }
    }

    public void addActor(Movie movie, String actorName) {
        if (actorName == null || actorName.trim().isEmpty()) {
            System.out.println("Имя актёра не может быть пустым.");
            log.warn("Имя актёра не может быть пустым.");
            return;
        }

        if (!movie.getActors().contains(actorName)) {
            movie.getActors().add(actorName);
            movieRepository.seve(movie);
            System.out.println("Актёр добавлен: " + actorName);
            log.info("Актёр добавлен: {}", actorName);
        } else {
            System.out.println("Актёр уже существует.");
            log.warn("Попытка добавить уже существующего актёра: {}", actorName);
        }
    }

    public void removeActor(Movie movie, String actorName) {
        if (actorName == null || actorName.trim().isEmpty()) {
            System.out.println("Имя актёра не может быть пустым.");
            log.warn("Имя актёра не может быть пустым.");
            return;
        }

        if (movie.getActors().remove(actorName)) {
            movieRepository.seve(movie);
            System.out.println("Актёр удалён: " + actorName);
            log.info("Актёр удалён: {}", actorName);
        } else {
            System.out.println("Актёр не найден.");
            log.warn("Попытка удалить несуществующего актёра: {}", actorName);
        }
    }

    public void updateStatus(Movie movie, MovieStatus newStatus) {
        if (newStatus == null) {
            System.out.println("Неверный статус. Обновление не выполнено.");
            log.warn("Неверный статус. Обновление не выполнено.");
            return;
        }

        if (movie.getStatus() == newStatus) {
            System.out.println("Фильм уже имеет статус: " + newStatus);
            log.info("Фильм уже имеет статус: {}", newStatus);
            return;
        }

        movie.setStatus(newStatus);
        movieRepository.seve(movie);
        System.out.println("Статус фильма обновлён на: " + newStatus);
        log.info("Статус фильма обновлён на: {}", newStatus);
    }

    public void createMovie(Scanner scanner) {
        String movieId = UUID.randomUUID().toString().substring(0, 5);
        String title;
        do {
            System.out.print("Введите название фильма: ");
            title = scanner.nextLine().trim();
            if (title.isEmpty()) {
                System.out.println("Ошибка: Название фильма не может быть пустым.");
            }
        } while (title.isEmpty());

        MovieStatus status = null;
        while (status == null) {
            System.out.print("Введите статус фильма (PLANNED, IN_PROGRESS, COMPLETED): ");
            String statusInput = scanner.nextLine().trim().toUpperCase();
            try {
                status = MovieStatus.valueOf(statusInput);
            } catch (IllegalArgumentException exception) {
                log.error("Ошибка: Некорректный статус фильма. Попробуйте снова.");
            }
        }

        MovieGenre genre = null;
        while (genre == null) {
            System.out.print("Введите жанр фильма (ACTION, DRAMA, COMEDY, HORROR, FANTASY, THRILLER, ROMANCE, DOCUMENTARY, ANIMATION, ADVENTURE, CRIME, MYSTERY, FAMILY, WAR, MUSICAL): ");
            String genreInput = scanner.nextLine().trim().toUpperCase();
            try {
                genre = MovieGenre.valueOf(genreInput);
            } catch (IllegalArgumentException exception) {
                log.error("Ошибка: введён некорректный жанр.");
            }
        }

        Movie movie = new Movie(movieId, title, genre, status);
        addMovie(movie);
        saveMovies();
        System.out.println("Фильм добавлен: " + title + " жанр: " + genre);
    }
}
