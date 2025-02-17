package com.business_app;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class PremiereTest {

    private Premiere premiere;

    @BeforeEach
    void setUp() {

        // Инициализация объекта Premiere перед каждым тестом
        premiere = new Premiere(1, "Titanic", "02.02.2025 10:00", "Cinema City",
                100, 40, 1000.0, 16);
        premiere.setReviews(new ArrayList<>());  // Инициализация списка отзывов
    }

    // Тест для проверки id
    @Test
    void testId() {
        int expectedId = 1;
        // Проверка, что id соответствует ожидаемому
        assertEquals(expectedId, premiere.getId(), "Id должен быть равен " + expectedId);
    }

    // Тест для проверки корректности парсинга времени (даты)
    @Test
    void testValidDate() {
        String validDate = "02.02.2025 10:00";
        Premiere testPremiere = new Premiere(2, "Movie Title", validDate, "Cinema", 100, 2000.0, 15.0, 18);

        // Проверка, что дата установлена корректно
        assertNotNull(testPremiere.getDate(), "Дата не должна быть null");
    }

    // Тест для некорректного формата даты
    @Test
    void testInvalidDateFormat() {
        String invalidDate = "2025-02-02 10:00"; // Неверный формат
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Premiere(1, "Movie Title", invalidDate, "Cinema", 100, 2000.0, 15.0, 18);
        });

        // Проверка, что выбрасывается исключение с правильным сообщением
        assertEquals("Ошибка: Некорректный формат даты. Требуется: dd.MM.yyyy HH:mm", exception.getMessage());
    }

    // Тест для пустой даты
    @Test
    void testEmptyDate() {
        // Ожидаем, что будет выброшено исключение IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            new Premiere(1, "Titanic", "", "Cinema City", 100, 40, 1000.0, 16);
        }, "Дата не может быть пустой или null.");
    }

    @Test
    void testGetMovieTitleNotEmpty() {
        // Получаем значение movieTitle
        String movieTitle = premiere.getMovieTitle();

        // Проверяем, что movieTitle не является пустым или пустой строкой
        assertNotNull(movieTitle, "Название фильма не должно быть null");
        assertFalse(movieTitle.trim().isEmpty(), "Название фильма не должно быть пустым");
    }

    @ParameterizedTest
    @CsvSource({
            "'Alice', 20, true",   // Валидный гость
            "'Bob', 16, false",    // Недопустимый гость (меньше минимального возраста)
            "'', 25, false"       // Пустое имя
    })
    void testAddGuest(String guestName, int guestAge, boolean expectedResult) {
        // Arrange
        Premiere premiere = new Premiere(1, "Movie Title", LocalDateTime.now(), "Cinema",
                100, 0, 5000.0, Arrays.asList("John Doe", "Jane Smith"), Arrays.asList("Good", "Excellent"), 50.0, 18);

        // Act: Добавление гостя
        premiere.addGuest(guestName, guestAge);

        // Assert: Проверка состояния списка гостей
        if (expectedResult) {
            assertTrue(premiere.getGuestList().contains(guestName), "Гость должен быть добавлен в список.");
        } else {
            assertFalse(premiere.getGuestList().contains(guestName), "Гость не должен быть добавлен в список.");
        }
    }

    @ParameterizedTest
    @CsvSource({
            "500.0, true",   // Положительный бюджет
            "0.0, false",     // Нулевой бюджет
            "-500.0, false"  // Отрицательный бюджет
    })
    void testIsBudgetAvailable(double budget, boolean expectedResult) {
        // Act
        boolean result = premiere.isBudgetAvailable(budget);

        // Assert
        assertEquals(expectedResult, result, "Результат должен соответствовать ожиданиям для бюджета: " + budget);
    }

    @ParameterizedTest
    @CsvSource({
            "50, true",    // 50 билетов можно продать
            "0, false",    // 0 билетов нельзя продать
            "-10, false",  // Отрицательное количество билетов
            "150, false"   // Превышено количество доступных билетов
    })
    void testSellTickets(int ticketsToSell, boolean expectedResult) {
        // Arrange
        premiere.setTicketCount(100);  // Устанавливаем количество билетов в 100
        premiere.setTicketSold(50);   // Начальная продажа билетов — 50

        // Act
        boolean result = premiere.sellTickets(ticketsToSell);

        // Assert: Проверка, что результат соответствует ожидаемому
        assertEquals(expectedResult, result, "Статус продажи билетов неверен!");

        // Assert
        if (expectedResult) {
            assertEquals(50 + ticketsToSell, premiere.getTicketSold(), "Количество проданных билетов должно быть обновлено.");
        } else {
            assertEquals(50, premiere.getTicketSold(), "Количество проданных билетов не должно измениться.");
        }
    }

    @Test
    void testReturnTickets() {

        // Пример с возвратом больше билетов, чем продано:
        try {
            premiere.returnTickets(30, 10, true); // Пример с количеством больше, чем продано
            fail("Ожидалась ошибка: Невозможно вернуть больше билетов, чем было продано");
        } catch (IllegalArgumentException e) {
            assertEquals("Ошибка при возврате билетов: Невозможно вернуть больше билетов, чем было продано.", e.getMessage());
        }

        // Пример с возвратом отрицательного количества билетов:
        try {
            premiere.returnTickets(-5, 10, true); // Пример с отрицательным количеством
            fail("Ожидалась ошибка: количество билетов должно быть положительным");
        } catch (IllegalArgumentException e) {
            assertEquals("Ошибка при возврате билетов: количество билетов должно быть положительным.", e.getMessage());
        }
    }

    @ParameterizedTest
    @CsvSource({

            "'Amazing movie!', true",  // Валидный отзыв
            "'', false",               // Пустой отзыв
            "'null', false"            // null отзыв
    })
    void testAddReview(String review, boolean expectedResult) {
        // Если строка review равна "null", мы интерпретируем её как настоящий null
        if ("null".equals(review)) {
            review = null;
        }

        // Act
        premiere.addReview(review);

        // Assert
        if (expectedResult) {
            assertTrue(premiere.getReviews().contains(review), "Отзыв должен быть добавлен в список отзывов");
        } else {
            assertFalse(premiere.getReviews().contains(review), "Отзыв не должен быть добавлен");
        }
    }

}