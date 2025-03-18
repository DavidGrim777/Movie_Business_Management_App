package movie.business.app.manager;

import movie.business.app.model.Premiere;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
        premiereManager = new PremiereManager();
        premiereManager.setTestMode(true);  // Включаем тестовый режим
        premiereManager.clearData();  // Очищаем данные перед каждым тестом

        // Используем правильный формат для даты с учетом часового пояса
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm z");
        String dateString = "02.02.2025 10:00 +03:00";  // Формат с часовым поясом
        ZonedDateTime date = ZonedDateTime.parse(dateString, formatter);
        premiere = new Premiere("1", "Titanic", date, "IMAX", 150);
    }

    @Test
    void testAddPremiere() {
        // Act
        premiereManager.addPremiere(premiere);

        // Assert: Проверка, что премьера была добавлена
        assertEquals(1, premiereManager.getPremiereMap().size(), "Количество премьер должно быть 1.");
        assertTrue(premiereManager.getPremiereMap().containsKey("1"), "Премьера с ID 2 должна быть добавлена.");
    }

    @Test
    void testAddPremiereWithNullPremiere() {
        // Act & Assert: Проверка, что добавление null премьеры вызывает исключение
        Exception exception = assertThrows(IllegalArgumentException.class, () -> premiereManager.addPremiere(null));
        assertEquals("Премьера не может быть null.", exception.getMessage());
    }

    @Test
    void testAddPremiereWithNullId() {
        // Arrange: Премьера без ID
        Premiere premiereWithNullId = new Premiere(null, "Avatar", ZonedDateTime.now(), "IMAX", 200);

        // Act & Assert: Проверка, что добавление премьеры без ID вызывает исключение
        Exception exception = assertThrows(IllegalArgumentException.class, () -> premiereManager.addPremiere(premiereWithNullId));
        assertEquals("ID премьеры не может быть null.", exception.getMessage());
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
        premiereManager.generatePremiereReport();

        // Assert: Печать "Нет премьеры для генерации отчета."
        // Проверяем, что сообщение выводится в консоль
        // В реальных тестах можно использовать подходы с mockito для проверки консольных выводов
    }

    @Test
    void testGeneratePremiereReportWithPremieres() {
        // Arrange: Добавляем несколько премьер
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm z");
        String dateString1 = "10.11.2025 14:30 +03:00";  // Формат с часовым поясом
        ZonedDateTime date1 = ZonedDateTime.parse(dateString1, formatter);

        // Arrange: Добавляем несколько премьер
        Premiere premiere1 = new Premiere("1", "Titanic", date1, "Cinema City", 100);
        String dateString2 = "05.05.2025 12:00 +03:00";  // Формат с часовым поясом
        ZonedDateTime date2 = ZonedDateTime.parse(dateString2, formatter);
        Premiere premiere2 = new Premiere("2", "Avatar", date2, "IMAX", 200);
        premiereManager.addPremiere(premiere1);
        premiereManager.addPremiere(premiere2);

        // Act: Генерация отчета
        premiereManager.generatePremiereReport();

        // Assert: Проверяем, что отчеты сгенерированы для каждой премьеры
        // Здесь можно использовать подходы с mockito для проверки вывода в консоль
    }
}
