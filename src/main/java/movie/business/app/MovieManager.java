package movie.business.app;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
class MovieManager {
    private List<Movie> movies;
    private static final String FILE_NAME = "movie.txt";

    // Конструктор инициализирует список фильмов
    public MovieManager() {
        this.movies = new ArrayList<>();
    }

    // Метод для добавления фильма в список
    public void addMovie(Movie movie) {
        if (movie == null) {
            System.out.println("Попытка добавить null-фильм.");
            log.warn("Попытка добавить null-фильм.");
            return;
        }
        movies.add(movie);
        saveMovies();
    }

    // Метод для удаления фильма по ID
    public void removeMovie(String movieId) {
        if (movieId == null || movieId.trim().isEmpty()) {
            System.out.println("Попытка удалить фильм с пустым ID.");
            log.warn("Попытка удалить фильм с пустым ID.");
            return;
        }
        Movie movieToRemove = null;
        for (Movie movie : movies) {
            if (movie.getId().equals(movieId)) {
                movieToRemove = movie;
                break;
            }
        }
        if (movieToRemove != null) {
            movies.remove(movieToRemove);
            System.out.println("Фильм удалён: " + movieToRemove.getTitle());
            log.info("Фильм удалён: {}", movieToRemove.getTitle());
            saveMovies();
        } else {
            System.out.println("Фильм с ID " + movieId + " не найден.");
            log.warn("Фильм с ID {} не найден.", movieId);

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
                saveMovies();
                return;
            }
        }
        System.out.println("Фильм с ID " + updatedMovie.getId() + " не найден.");
        log.warn("Фильм с ID {} не найден.", updatedMovie.getId());
    }

    // Метод выводит в консоль все фильмы из списка
    public void printAllMovies() {
        loadMovie();
        if (movies.isEmpty()) {
            System.out.println("Список фильмов пуст.");
        } else {
            for (Movie movie : movies) {
                System.out.println(movie);
            }
        }
    }

    public void saveMovies() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
            for (Movie movie : movies){
            bufferedWriter.write(movie.toString());
            bufferedWriter.newLine();
        }
            System.out.println("Фильмы сохранены в файл.");
        } catch (IOException exception) {
            System.out.println("Ошибка при загрузке списка фильмов: " + exception.getMessage());
            log.error("Ошибка при загрузке списка фильмов: {}", exception.getMessage());
        }
    }

    public void loadMovie() {
        movies.clear();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))) {
            String movieLine;
            while ((movieLine = bufferedReader.readLine()) != null) {
                String[] parts = movieLine.split(", ");
                if (parts.length < 3 || parts[2] == null || parts[2].trim().isEmpty() || parts[2].contains("null")) {
                    log.warn("Некорректные данные о статусе фильма: {}", (Object) parts);
                    continue;
                }
                try {
                    String id = parts[0].trim();
                    String title = parts[1].trim();
                    MovieStatus status = MovieStatus.valueOf(parts[2].trim().toUpperCase());
                    movies.add(new Movie(id, title, status));
                }catch (IllegalArgumentException exception){
                    log.warn("Ошибка парсинга статуса фильма: {}", parts[2], exception);
                }
            }
            log.info("Фильмы загружены из файла.");
        } catch (FileNotFoundException exception) {
            log.error("Файл не найден: {}", exception.getMessage());
        } catch (IOException exception) {
            log.error("Ошибка ввода-вывода при загрузке фильмов: {}", exception.getMessage());
        }
    }

    public List<Movie> getMovies() {
        if (movies == null)
            movies = new ArrayList<>();
        return new ArrayList<>(movies);
    }
}