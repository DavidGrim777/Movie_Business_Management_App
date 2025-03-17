package movie.business.app.manager;

import movie.business.app.model.Premiere;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FinanceManagerTest {
    private FinanceManager financeManager;

    @BeforeEach
    void setUp() {
        // Инициализация объектов
        financeManager = new FinanceManager();
    }

    @Test
    void testAddPremiereBudget_invalidBudget() {
        // Создаем объект Premiere с полным набором параметров
        ZonedDateTime releaseDate = ZonedDateTime.now();  // Пример текущей даты для релиза
        String director = "Director Name";  // Пример имени режиссера
        String genre = "Action";  // Пример жанра
        int budget = 0;  // Начальный бюджет премьеры, который должен вызвать ошибку

        // Создаем экземпляр Premiere
        Premiere premiere = new Premiere("Test Movie", director, releaseDate, genre, budget);

        // Проверяем, что бюджет правильный перед добавлением
        assertEquals(budget, premiere.getBudget());

        // Выполняем попытку добавления бюджета, ожидая, что ошибка будет залогирована
        financeManager.addPremiereBudget(premiere, -500.0);  // Попытка добавления отрицательного бюджета

        // После вызова метода проверяем, что бюджет не изменился
        assertEquals(budget, premiere.getBudget(), "Бюджет не должен измениться при неправильных данных.");
    }

    @Test
    void testAddPremiereBudget() {
        // Создаем объект Premiere с полным набором параметров
        ZonedDateTime releaseDate = ZonedDateTime.now(); // Пример текущей даты для релиза
        String director = "Director Name";  // Пример имени режиссера
        String genre = "Action";  // Пример жанра
        int budget = 0;  // Начальный бюджет премьеры

        Premiere premiere = new Premiere("Test Movie", director, releaseDate, genre, budget);

        // Теперь можно использовать этот объект в тесте
        financeManager.addPremiereBudget(premiere, 500.0);  // Пример добавления бюджета

        // Проверяем, что бюджет увеличился на 500.0
        assertEquals(500.0, premiere.getBudget());
    }
}