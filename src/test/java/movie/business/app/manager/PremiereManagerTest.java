package movie.business.app.manager;

import movie.business.app.model.Premiere;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PremiereManagerTest {

    private PremiereManager premiereManager;
    private Premiere premiere;

    @BeforeEach
    void setUp() {
        premiereManager = new PremiereManager(true);
        premiereManager.getPremiereMap().clear();
        // Используем правильный формат для даты с учетом часового пояса
        ZonedDateTime date = ZonedDateTime.of(2025, 2, 2, 10, 0, 0, 0, java.time.ZoneId.of("UTC+03:00"));
        premiere = new Premiere("1", "Titanic", date, "IMAX", 150,2000000);
    }
    @AfterEach
    void tearDown() {
        String testFileName = "test_premieres.txt";
        deleteTestFile(testFileName);  //  Удаляем тестовый файл после тестов
    }

    //  Добавляем метод удаления файла
    private void deleteTestFile(String fileName) {
        Path filePath = Paths.get(System.getProperty("user.dir"), fileName);
        try {
            Files.deleteIfExists(filePath);
            System.out.println("Файл " + fileName + " успешно удалён.");
        } catch (IOException e) {
            System.out.println("Ошибка при удалении файла: " + fileName + " - " + e.getMessage());
        }
    }


    @Test
    void testAddPremiere() {
        // Act
        premiereManager.addPremiere(premiere);

        // Assert: Проверка, что премьера была добавлена
        assertEquals(1, premiereManager.getPremiereMap().size(), "Количество премьер должно быть 1.");
        assertTrue(premiereManager.getPremiereMap().containsKey("1"), "Премьера с ID 1 должна быть добавлена.");
    }

    @Test
    void testAddPremiereWithNullPremiere() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> premiereManager.addPremiere(null)
        );
        assertEquals("ID и премьера не может быть null.", exception.getMessage());
    }

    @Test
    void testAddPremiereWithNullId() {
        // Проверка выброса исключения при создании премьеры с null ID
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                // лямбда-выражения () -> вызов метода без параметров
                () -> new Premiere(null, "Avatar", ZonedDateTime.now(), "IMAX", 200, 2000000)
        );
        assertEquals("ID не может быть пустым или null.", exception.getMessage());
    }

    @Test
    void testFindPremiereById() {
        // Arrange: Подготовка данных
        premiereManager.addPremiere(premiere);

        // Act: Поиск премьеры по ID
        Premiere foundPremiere = premiereManager.findPremiereById("1");

        // Assert: Проверка, что премьера найдена
        assertNotNull(foundPremiere, "Премьера с ID 1 должна быть найдена.");
        assertEquals("Titanic", foundPremiere.getMovieTitle(), "Название фильма должно быть Titanic.");
    }

    @Test
    void testFindPremiereByInvalidId() {
        // Arrange: Премьера ещё не добавлена
        premiereManager.addPremiere(premiere);

        // Act: Поиск премьеры с несуществующим ID
        Premiere foundPremiere = premiereManager.findPremiereById("999");

        // Assert: Проверка, что премьера не найдена
        assertNull(foundPremiere, "Премьера с ID 999 не должна быть найдена.");
    }

    @Test
    void testRemovePremiereById() {
        // Arrange: Добавляем премьеры
        premiereManager.addPremiere(premiere);

        // Act: Удаление премьеры по ID
        premiereManager.removePremiereById("1");

        // Assert: Проверка, что премьера была удалена
        assertEquals(0, premiereManager.getPremiereCount(), "Количество премьер должно быть 0.");
    }

    @Test
    void testRemovePremiereByInvalidId() {
        // Arrange: Премьера ещё не добавлена
        premiereManager.addPremiere(premiere);

        // Act: Пытаемся удалить премьеру с несуществующим ID
        premiereManager.removePremiereById("999");

        // Assert: Проверка, что количество премьер не изменилось
        assertEquals(1, premiereManager.getPremiereCount(), "Количество премьер должно быть 1.");
    }

    @Test
    void testGeneratePremiereReportWithNoPremieres() {

        // Act: Генерация отчета без добавленных премьер
        // Перехватываем консольный вывод, чтобы проверить сообщение
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));
        premiereManager.generatePremiereReport();

        // Проверяем, что выводится сообщение "Нет премьеры для генерации отчета."
        assertTrue(outContent.toString().contains("Нет премьеры для генерации отчета."),
                "Ожидалось сообщение о пустом списке премьер.");

        System.setOut(System.out); // Восстанавливаем стандартный вывод
    }

    @Test
    void testGeneratePremiereReportWithPremieres() {
        // Arrange: Добавляем несколько премьер
        ZonedDateTime date1 = ZonedDateTime.of(2025, 11, 10, 14, 30, 0, 0, java.time.ZoneId.of("UTC+03:00"));
        Premiere premiere1 = new Premiere("1", "Titanic", date1, "Cinema City", 100, 2000000);

        ZonedDateTime date2 = ZonedDateTime.of(2025, 5, 5, 12, 0, 0, 0, java.time.ZoneId.of("UTC+03:00"));
        Premiere premiere2 = new Premiere("2", "Avatar", date2, "IMAX", 200, 2000000);

        premiereManager.addPremiere(premiere1);
        premiereManager.addPremiere(premiere2);

        // Перехватываем консольный вывод, чтобы проверить содержимое отчёта
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        premiereManager.generatePremiereReport();

        String output = outContent.toString();
        assertTrue(output.contains("Titanic"), "Отчет должен содержать название Titanic.");
        assertTrue(output.contains("Avatar"), "Отчет должен содержать название Avatar.");

        System.setOut(System.out); // Восстанавливаем стандартный вывод
    }
}
