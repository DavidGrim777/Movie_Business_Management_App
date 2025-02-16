package com.business_app;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
class MovieManager {
    private List<Movie> movies;

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
        System.out.println("Фильм добавлен: " + movie.getTitle());
        log.info("Фильм добавлен: {}", movie.getTitle());
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
                return;
            }
        }
        System.out.println("Фильм с ID " + updatedMovie.getId() + " не найден.");
        log.warn("Фильм с ID {} не найден.", updatedMovie.getId());
    }

    // Метод выводит в консоль все фильмы из списка
    public void printAllMovies() {
        if (movies.isEmpty()) {
            System.out.println("Список фильмов пуст.");
        } else {
            for (Movie movie : movies) {
                System.out.println(movie);
            }
        }
    }
}