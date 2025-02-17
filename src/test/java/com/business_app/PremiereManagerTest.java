package com.business_app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PremiereManagerTest {
    private PremiereManager premiereManager;
    private Premiere premiere;

    @BeforeEach
    void setUp() {
        premiereManager = new PremiereManager();
        premiere = new Premiere(1, "Titanic", "02.02.2025 10:00", "Cinema City", 100, 40, 1000.0, 16);
    }

    @Test
    void testAddPremiere() {
        // Arrange: Подготовка данных
        Premiere premiereToAdd = new Premiere(2, "Avatar", "05.05.2025 12:00", "IMAX", 200, 50, 1500.0, 18);

        // Act: Добавление премьеры
        premiereManager.addPremiere(premiereToAdd);

        // Assert: Проверка, что премьера была добавлена
        assertEquals(1, premiereManager.getPremiereCount(), "Количество премьер должно быть 1.");
    }

    @Test
    void testFindPremiereById() {
        // Arrange: Подготовка данных
        premiereManager.addPremiere(premiere);

        // Act: Поиск премьеры по ID
        Premiere foundPremiere = premiereManager.findPremiereById(1);

        // Assert: Проверка, что премьера найдена
        assertNotNull(foundPremiere, "Премьера с ID 1 должна быть найдена.");
        assertEquals("Titanic", foundPremiere.getMovieTitle(), "Название фильма должно быть Titanic.");
    }

    @Test
    void testFindPremiereByInvalidId() {
        // Arrange: Премьера ещё не добавлена
        premiereManager.addPremiere(premiere);

        // Act: Поиск премьеры с несуществующим ID
        Premiere foundPremiere = premiereManager.findPremiereById(999);

        // Assert: Проверка, что премьера не найдена
        assertNull(foundPremiere, "Премьера с ID 999 не должна быть найдена.");
    }

    @Test
    void testRemovePremiereById() {
        // Arrange: Добавляем премьеры
        premiereManager.addPremiere(premiere);

        // Act: Удаление премьеры по ID
        premiereManager.removePremiereById(1);

        // Assert: Проверка, что премьера была удалена
        assertEquals(0, premiereManager.getPremiereCount(), "Количество премьер должно быть 0.");
    }

    @Test
    void testRemovePremiereByInvalidId() {
        // Arrange: Премьера ещё не добавлена
        premiereManager.addPremiere(premiere);

        // Act: Пытаемся удалить премьеру с несуществующим ID
        premiereManager.removePremiereById(999);

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
        Premiere premiere1 = new Premiere(1, "Titanic", "02.02.2025 10:00", "Cinema City", 100, 40, 1000.0, 16);
        Premiere premiere2 = new Premiere(2, "Avatar", "05.05.2025 12:00", "IMAX", 200, 50, 1500.0, 18);
        premiereManager.addPremiere(premiere1);
        premiereManager.addPremiere(premiere2);

        // Act: Генерация отчета
        premiereManager.generatePremiereReport();

        // Assert: Проверяем, что отчеты сгенерированы для каждой премьеры
        // Здесь можно использовать подходы с mockito для проверки вывода в консоль
    }
}
