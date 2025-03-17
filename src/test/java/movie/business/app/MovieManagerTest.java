package movie.business.app;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieManagerTest {

    private MovieManager movieManager;
    private static final String FILE_NAME = "movie.txt";

    @BeforeEach
    void setUp() {
        // Перед каждым тестом создаём новый экземпляр MovieManager
        movieManager = new MovieManager();
        // Удаляем файл, если он уже существует
        deleteTestFile();
        // Создаём пустой файл для тестов
        createTestFile();
    }

    @AfterEach
    void AfterTestDeleteTestFile() {
        // После каждого теста удаляем тестовый файл
        deleteTestFile();
    }

    @Test
    void shouldAddMovie() {
        // Тест: добавление фильма в список
        Movie movie = new Movie("1", "Inception", MovieStatus.PLANNED);
        movieManager.addMovie(movie);
        List<Movie> movies = movieManager.getMovies();

        // Проверяем, что фильм добавился
        assertEquals(1, movies.size(), "Ожидался один фильм в списке");
        assertEquals("Inception", movies.get(0).getTitle());
        assertEquals(MovieStatus.PLANNED, movies.get(0).getStatus());
    }

    @Test
    void shouldNotAddNullMovie() {
        // Тест: нельзя добавить null-фильм
        movieManager.addMovie(null);
        List<Movie> movies = movieManager.getMovies();

        // Список должен остаться пустым
        assertTrue(movies.isEmpty(), "Ожидался пустой список фильмов");
    }

    @Test
    void shouldRemoveMovie() {
        // Тест: удаление фильма по ID
        Movie movie = new Movie("1", "Matrix", MovieStatus.IN_PROGRESS);
        movieManager.addMovie(movie);

        movieManager.removeMovie("1");
        List<Movie> movies = movieManager.getMovies();

        // Фильм должен быть удалён
        assertTrue(movies.isEmpty(), "Фильм должен быть удалён");
    }

    @Test
    void shouldNotRemoveNonExistentMovie() {
        // Тест: нельзя удалить несуществующий фильм
        movieManager.removeMovie("999");
        List<Movie> movies = movieManager.getMovies();

        // Список должен остаться пустым
        assertTrue(movies.isEmpty(), "Список должен остаться пустым");
    }

    @Test
    void shouldNotRemoveMovieWithEmptyId() {
        // Тест: нельзя удалить фильм, если ID пустой
        Movie movie = new Movie("1", "Matrix", MovieStatus.IN_PROGRESS);
        movieManager.addMovie(movie);

        movieManager.removeMovie(" ");
        List<Movie> movies = movieManager.getMovies();

        // Фильм не должен удаляться при пустом ID
        assertEquals(1, movies.size(), "Фильм не должен удаляться при пустом ID");
    }

    @Test
    void shouldUpdateMovie() {
        // Тест: обновление фильма
        Movie movie = new Movie("1", "Titanic", MovieStatus.PLANNED);
        movieManager.addMovie(movie);

        Movie updatedMovie = new Movie("1", "Titanic (Updated)", MovieStatus.IN_PROGRESS);
        movieManager.updateMovie(updatedMovie);

        List<Movie> movies = movieManager.getMovies();
        assertEquals(1, movies.size());
        assertEquals("Titanic (Updated)", movies.get(0).getTitle());
        assertEquals(MovieStatus.IN_PROGRESS, movies.get(0).getStatus());
    }

    @Test
    void shouldNotUpdateNonExistentMovie() {
        // Тест: нельзя обновить несуществующий фильм
        Movie updatedMovie = new Movie("999", "Non-existent", MovieStatus.COMPLETED);
        movieManager.updateMovie(updatedMovie);

        List<Movie> movies = movieManager.getMovies();
        assertTrue(movies.isEmpty(), "Фильм не должен обновляться, если его нет");
    }

    @Test
    void shouldLoadMoviesFromFile() {
        // Тест: загрузка фильмов из файла
        writeToFile("1, Interstellar, PLANNED\n2, Joker, IN_PROGRESS");

        movieManager.loadMovie();
        List<Movie> movies = movieManager.getMovies();

        // Проверяем, что фильмы корректно загрузились
        assertEquals(2, movies.size());
        assertEquals("Interstellar", movies.get(0).getTitle());
        assertEquals(MovieStatus.PLANNED, movies.get(0).getStatus());

        assertEquals("Joker", movies.get(1).getTitle());
        assertEquals(MovieStatus.IN_PROGRESS, movies.get(1).getStatus());
    }

    @Test
    void shouldHandleInvalidMovieDataGracefully() {
        // Тест: обработка некорректных данных в файле
        writeToFile("1, Good Movie, PLANNED\nINVALID DATA\n3, Bad Movie, UNKNOWN");

        movieManager.loadMovie();
        List<Movie> movies = movieManager.getMovies();

        // Только один корректный фильм должен загрузиться
        assertEquals(1, movies.size(), "Только один корректный фильм должен загрузиться");
        assertEquals("Good Movie", movies.get(0).getTitle());
    }

    @Test
    void shouldHandleEmptyFileGracefully() {
        // Тест: загрузка из пустого файла
        movieManager.loadMovie();
        List<Movie> movies = movieManager.getMovies();

        // Ожидаем, что список фильмов будет пустым
        assertTrue(movies.isEmpty(), "Ожидался пустой список фильмов");
    }

    private void deleteTestFile() {
        // Удаляем тестовый файл, если он существует
        try {
            Files.deleteIfExists(Paths.get(FILE_NAME));
        } catch (IOException e) {
            fail("Ошибка при удалении файла: " + e.getMessage());
        }
    }

    private void createTestFile() {
        // Создаём пустой тестовый файл
        try {
            Files.createFile(Paths.get(FILE_NAME));
        } catch (IOException e) {
            fail("Ошибка при создании файла: " + e.getMessage());
        }
    }

    private void writeToFile(String content) {
        // Записываем текст в тестовый файл
        try {
            Files.write(Paths.get(FILE_NAME), content.getBytes());
        } catch (IOException e) {
            fail("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    private List<String> readFileLines() {
        // Читаем строки из тестового файла
        try {
            return Files.readAllLines(Paths.get(FILE_NAME));
        } catch (IOException e) {
            fail("Ошибка при чтении файла: " + e.getMessage());
            return List.of();
        }
    }
}
