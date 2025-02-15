package com.business_app;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class PremiereTest {

    private Premiere premiere;

    @BeforeEach
    void setUp() {

        // Инициализация объекта Premiere перед каждым тестом
        premiere = new Premiere(1, "Titanic", "2025-05-01", "Cinema City", 100, 1000.0, 30);
    }

    @Test
    void testIsBudgetAvailable_WithSufficientBudget() {
        // Arrange (подготовка): устанавливаем сумму, на которую проверяется бюджет
        double requiredAmount = 500.0;

        // Act (действие): проверка, доступен ли бюджет
        boolean result = premiere.isBudgetAvailable(requiredAmount);

        // Assert (проверка): бюджет должен быть доступен
        assertTrue(result, "Бюджет должен быть доступен");
    }

    @Test
    void testIsBudgetAvailable_WithInsufficientBudget() {
        // Arrange
        double requiredAmount = 1500.0;

        // Act
        boolean result = premiere.isBudgetAvailable(requiredAmount);

        // Assert
        assertFalse(result, "Бюджет должен быть недоступен");
    }

    @Test
    void testCanSellTickets_WithAvailableTickets() {
        // Arrange
        int ticketsToSell = 50;

        // Act
        boolean result = premiere.canSellTickets(ticketsToSell);

        // Assert
        assertTrue(result, "Билеты должны быть доступны для продажи");
    }

    @Test
    void testCanSellTickets_WithNotEnoughTickets() {
        // Arrange
        int ticketsToSell = 150;

        // Act
        boolean result = premiere.canSellTickets(ticketsToSell);

        // Assert
        assertFalse(result, "Билеты не должны быть доступны для продажи");
    }

    @Test
    void testSellTickets_WithAvailableTickets() {
        // Arrange
        int ticketsToSell = 50;

        // Act
        premiere.sellTickets(ticketsToSell);

        // Assert
        assertEquals(50, premiere.getTicketSold(), "Количество проданных билетов должно быть обновлено");
    }

    @Test
    void testSellTickets_WithNotEnoughTickets() {
        // Arrange
        int ticketsToSell = 150;

        // Act
        premiere.sellTickets(ticketsToSell);

        // Assert
        assertEquals(0, premiere.getTicketSold(), "Количество проданных билетов не должно изменяться из-за нехватки билетов");
    }

    @Test
    void testReturnTickets_WithValidCount() {
        // Arrange
        premiere.sellTickets(50); // Продаем 50 билетов
        int ticketsToReturn = 20;

        // Act
        premiere.returnTickets(ticketsToReturn);

        // Assert
        assertEquals(30, premiere.getTicketSold(), "Количество проданных билетов должно быть обновлено после возврата");
    }

    @Test
    void testReturnTickets_WithInvalidCount() {
        // Arrange
        premiere.sellTickets(50); // Продаем 50 билетов
        int ticketsToReturn = 60;

        // Act
        premiere.returnTickets(ticketsToReturn);

        // Assert
        assertEquals(50, premiere.getTicketSold(), "Количество проданных билетов не должно изменяться, если количество возврата превышает проданные");
    }

    @Test
    void testAddGuest_WithValidGuest() {
        // Arrange
        String guestName = "John Doe";

        // Act
        premiere.addGuest(guestName);

        // Assert
        assertTrue(premiere.getGuestList().contains(guestName), "Гость должен быть добавлен в список гостей");
    }

    @Test
    void testAddGuest_WithEmptyGuest() {
        // Arrange
        String guestName = "";

        // Act
        premiere.addGuest(guestName);

        // Assert
        assertFalse(premiere.getGuestList().contains(guestName), "Гость не должен быть добавлен, если имя пустое");
    }

    @Test
    void testAddGuest_WithNullGuest() {
        // Arrange
        String guestName = null;

        // Act
        premiere.addGuest(guestName);

        // Assert
        assertFalse(premiere.getGuestList().contains(guestName), "Гость не должен быть добавлен, если имя null");
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
        premiere.addGuest("John Doe");
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
