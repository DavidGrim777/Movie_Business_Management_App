package com.business_app;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class PremiereTest {

    private Premiere premiere;

    @BeforeEach
    void setUp() {

        // Инициализация объекта Premiere перед каждым тестом
        premiere = new Premiere(1, "Titanic", "02.02.2025 10:00", "Cinema City",
                    100,40, 1000.0, 16);
    }

    @ParameterizedTest
    @CsvSource({
            "'500.0', true",  // Сумма 500.0, достаточно средств
            "'0.0', true",     // Сумма 0.0, бюджет всегда доступен (0 не может быть отрицательным)
            "'-500.0', false", // Сумма -500.0, некорректная сумма
            "'', false",       // Пустое значение, некорректный ввод
            "'abcd', false"    // Некорректное значение (нечисловое)
    })
    void testIsBudgetAvailable(String requiredAmount, boolean expected) {
        // Act
        boolean result = premiere.isBudgetAvailable(requiredAmount);

        // Assert
        assertEquals(expected, result, "Проверка на доступность бюджета " + requiredAmount);
    }

    @ParameterizedTest
    @CsvSource({
            "50, true",   // Доступно 50 билетов, можно продать
            "150, false", // Недостаточно билетов (максимум 100)
            "1, true",    // Можно продать 1 билет
            "0, false",   // Нельзя продать 0 билетов
            "-10, false", // Невозможно продать отрицательное количество билетов
    })
    void testCanSellTickets(int ticketsToSell, boolean expectedResult) {
        // Act
        boolean result = premiere.canSellTickets(ticketsToSell);

        // Assert
        assertEquals(expectedResult, result, "Проверка доступности продажи билетов для количества: " + ticketsToSell);
    }

    @ParameterizedTest
    @CsvSource({
            "50",  // Успешная продажа
            "1",   // Пример успешной продажи
    })
    void testSellTickets_WithValidAmount(int ticketsToSell) {
        // Act
        premiere.sellTickets(ticketsToSell);

        // Assert
        assertEquals(ticketsToSell, premiere.getTicketSold(),
                "Количество проданных билетов должно быть обновлено для " + ticketsToSell);
    }

    @ParameterizedTest
    @CsvSource({
            "0",     // Невозможно продать 0 билетов
            "-10",   // Невозможно продать отрицательное количество билетов
            "150",   // Превышено количество доступных билетов
    })
    void testSellTickets_WithInvalidAmount(int ticketsToSell) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            premiere.sellTickets(ticketsToSell);
        });

        // Assert
        assertEquals("Ошибка при продаже билетов: Недостаточно билетов.", exception.getMessage(),
                "Ошибка при продаже билетов для количества: " + ticketsToSell);
    }

    @Test
    void testReturnTickets_WithValidCount() {
        // Arrange
        premiere.sellTickets(50); // Продаем 50 билетов
        int ticketsToReturn = 60;

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        premiere.returnTickets(ticketsToReturn);
        });
        // Assert
        assertEquals("Невозможно вернуть больше билетов, чем было продано.", exception.getMessage(), "Ошибка при возврате билетов");
    }

    @Test
    void testReturnTickets_WithInvalidCount() {
        // Arrange
        premiere.sellTickets(50); // Продаем 50 билетов
        int ticketsToReturn = 60;

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        premiere.returnTickets(ticketsToReturn);
        });

        // Assert
        assertEquals("Невозможно вернуть больше билетов, чем было продано.", exception.getMessage(), "Ошибка при возврате билетов");
    }

    @ParameterizedTest
    @CsvSource({
            "'Alice', 20, true",   // Валидный гость
            "'Bob', 16, false",    // Недопустимый гость (меньше минимального возраста)
            "'', 25, false"        // Пустое имя
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

    @Test
    void testAddReview_WithValidReview() {
        // Arrange
        String review = "Amazing movie!";

        // Act
        premiere.addReview(review);

        // Assert
        assertTrue(premiere.getReviews().contains(review), "Отзыв должен быть добавлен в список отзывов");
    }

    @Test
    void testAddReview_WithEmptyReview() {
        // Arrange
        String review = "";

        // Act
        premiere.addReview(review);

        // Assert
        assertFalse(premiere.getReviews().contains(review), "Отзыв не должен быть добавлен, если он пустой");
    }
    @Test
    void testAddReview_WithNullReview() {
        // Arrange
        String review = null;

        // Act
        premiere.addReview(review);

        // Assert
        assertFalse(premiere.getReviews().contains(review), "Отзыв не должен быть добавлен, если он null");
    }

    @Test
    void testGenerateReport() {
        // Arrange
        premiere.sellTickets(50);
        premiere.addReview("Amazing movie!");

        // Act
        String report = premiere.generateReport();

        // Assert
        assertNotNull(report, "Отчет должен быть сгенерирован");
        assertTrue(report.contains("Отчет о премьере: Titanic"), "Отчет должен содержать название фильма");
        assertTrue(report.contains("50"), "Отчет должен содержать количество проданных билетов");
        assertTrue(report.contains("John Doe"), "Отчет должен содержать список гостей");
        assertTrue(report.contains("Amazing movie!"), "Отчет должен содержать отзывы");
    }
}
