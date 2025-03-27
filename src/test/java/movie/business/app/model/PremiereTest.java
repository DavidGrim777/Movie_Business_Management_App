package movie.business.app.model;

import lombok.extern.slf4j.Slf4j;
import movie.business.app.repository.PremiereRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class PremiereTest {

    private Premiere premiere;
    private final String testId = "1";  // Тестовый ID премьеры

    @BeforeEach
    void setUp() {
        premiere = new Premiere(testId, "Titanic",
                ZonedDateTime.of(2025, 2, 2, 10, 0, 0, 0, ZoneId.of("UTC")),
                "Cinema City", 100, 5000000);
        premiere.setReviews(new ArrayList<>());
    }
    @AfterEach
    void tearDown() {
        // Удаляем тестовые файлы после каждого теста
        deleteTestFile(testId + "_testReviews.txt");
        deleteTestFile(testId + "_testGuests.dat");
        deleteTestFile(testId + "test_premieres.txt");
    }

    private void deleteTestFile(String fileName) {
        Path filePath = Paths.get(fileName);
        try {
            Files.deleteIfExists(filePath);
            System.out.println("Файл " + fileName + " успешно удалён.");
            log.info("Файл {} успешно удалён.", fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при удалении файла: " + fileName + " - " + e.getMessage());
            log.warn("Ошибка при удалении файла {}: {}", fileName, e.getMessage());
        }
    }

    @Test
    void testSaveAndLoadReviews() throws IOException {
        PremiereRepository repository = new PremiereRepository();
        Path filePath = Path.of(testId + "_testReviews.txt");
        Files.deleteIfExists(filePath);

        List<String> reviews = Arrays.asList("Отличный фильм!", "Очень понравилось.");
        premiere.getReviews().addAll(reviews);

        // Сохраняем отзывы в тестовый файл
        repository.saveReviewsToFile(premiere, true);

        assertTrue(Files.exists(filePath), "Файл с отзывами не был создан!");

        premiere.getReviews().clear();
        repository.loadReviewsFromFile(premiere, true);
        assertEquals(reviews, premiere.getReviews(), "Загруженные отзывы не совпадают с исходными!");
    }

    @Test
    void testSaveAndLoadGuests(){
        // Добавляем тестовых гостей
        List<String> guests = Arrays.asList("Иван Иванов", "Мария Петрова");
        premiere.getGuestList().addAll(guests);

        // Сохраняем гостей в файл (важно!)
        PremiereRepository repository = new PremiereRepository();
        repository.saveGuestsToFile(premiere, true);

        // Проверяем, создался ли файл
        Path filePath = Path.of(System.getProperty("user.dir"), testId + "_testGuests.dat");
        System.out.println("File path: " + filePath.toAbsolutePath()); // Печатаем полный путь
        assertTrue(Files.exists(filePath), "Файл с гостями не был создан!");


        // Проверяем, что гости загружены корректно
        assertEquals(guests, premiere.getGuestList(), "Загруженные гости не совпадают с исходными!");
    }

    @Test
    void testSetIdValid() {
        // Arrange: Корректный ID
        String validId = "12345";

        // Act: Установка ID
        assertDoesNotThrow(() -> premiere.setId(validId), "Корректный ID не должен вызывать исключение");

        // Assert: Проверяем, что ID установлен правильно
        assertEquals(validId, premiere.getId());
    }

    @Test
    void testSetIdNull() {
        // Arrange: Null ID
        String invalidId = null;

        // Act & Assert: Ожидаем исключение
        Exception exception = assertThrows(IllegalArgumentException.class, () -> premiere.setId(invalidId));
        assertEquals("ID не может быть пустым или null.", exception.getMessage());
    }

    @Test
    void testSetIdEmpty() {
        // Arrange: Пустой ID
        String invalidId = "  ";

        // Act & Assert: Ожидаем исключение
        Exception exception = assertThrows(IllegalArgumentException.class, () -> premiere.setId(invalidId));
        assertEquals("ID не может быть пустым или null.", exception.getMessage());
    }

    @Test
    void testSetIdTooLong() {
        // Arrange: Слишком длинный ID (больше 30 символов)
        String invalidId = "1234567890123456789012345678901"; // 31 символ

        // Act & Assert: Ожидаем исключение
        Exception exception = assertThrows(IllegalArgumentException.class, () -> premiere.setId(invalidId));
        assertEquals("ID не может быть длиннее 30 символов.", exception.getMessage());
    }

    @Test
    void testGetLocation() {
        // Тест 1: Корректное местоположение
        Premiere premiereWithLocation = new Premiere("1", "Titanic", ZonedDateTime.now(), "Cinema City", 100,5000000);
        assertEquals("Cinema City", premiereWithLocation.getLocation(), "Метод getLocation() должен возвращать указанное местоположение");

        // Тест 2: Пустое местоположение
        Premiere premiereWithEmptyLocation = new Premiere("2", "Avatar", ZonedDateTime.now(), "", 150, 1000000);
        assertEquals("Местоположение не указано", premiereWithEmptyLocation.getLocation(), "Метод getLocation() должен корректно обрабатывать пустое местоположение");

        // Тест 3: null местоположение
        Premiere premiereWithNullLocation = new Premiere("3", "Inception", ZonedDateTime.now(), null, 200,2000000);
        assertEquals("Местоположение не указано", premiereWithNullLocation.getLocation(), "Метод getLocation() должен корректно обрабатывать null местоположение");
    }

    private static final String VALID_DATE = "02.02.2025 10:00 UTC"; // Пример корректной даты
    private static final String INVALID_DATE = "10-03-2025 12:00"; // Некорректный формат

    // Тест для проверки корректности парсинга времени (даты)
    @Test
    void testValidDate() {
        assertNotNull(premiere.getDate(), "Дата не должна быть null");
    }

    @Test
    void testSetDateValid() {
        assertDoesNotThrow(() -> premiere.setDate(VALID_DATE), "Корректная дата не должна вызывать исключение");
    }

    @Test
    void testSetDateNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> premiere.setDate(null));
        assertEquals("Дата не может быть пустой или null.", exception.getMessage());
    }

    @Test
    void testSetDateEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> premiere.setDate("  "));
        assertEquals("Дата не может быть пустой или null.", exception.getMessage());
    }

    @Test
    void testSetDateInvalidFormat() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> premiere.setDate(INVALID_DATE));
        assertTrue(exception.getMessage().contains("Ошибка: Некорректный формат даты."));
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
    @MethodSource("guestDataProvider")
    void testAddGuest(String guestName, boolean guestAge, boolean expectedResult) {
        // Act: Добавление гостя
        premiere.addGuest(guestName, guestAge);

        // Assert: Проверка, был ли добавлен гость в список
        if (expectedResult) {
            assertTrue(premiere.getGuestList().contains(guestName), "Гость " + guestName + " должен быть добавлен в список.");
        } else {
            assertFalse(premiere.getGuestList().contains(guestName), "Гость " + guestName + " не должен быть добавлен в список.");
        }
    }

    // Метод-поставщик данных для @MethodSource
    static Stream<Arguments> guestDataProvider() {
        return Stream.of(
                Arguments.of("Alice", true, true),       // Валидный гость
                Arguments.of("Bob", false, false),        // Недопустимый гость (меньше минимального возраста)
                Arguments.of("", true, false),           // Пустое имя
                Arguments.of(null, true, false)         // Реальный null

        );
    }

    @Test
    void testSetTicketCount() {
        // Arrange: подготовка данных
        Premiere premiere = new Premiere("1", "Titanic", ZonedDateTime.now(), "Cinema City", 100, 5000000);

        // Act: установка нового значения количества билетов
        premiere.setTicketCount(150);

        // Assert: проверка, что количество билетов обновлено корректно
        assertEquals(150, premiere.getTicketCount(), "Количество билетов должно быть 150");

        // Act: попытка установить отрицательное количество билетов
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            premiere.setTicketCount(-1));


        // Assert: проверка, что выбрасывается исключение с ожидаемым сообщением
        assertEquals("Количество билетов не может быть отрицательным.", exception.getMessage());
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
        // Устанавливаем проданные билеты вручную для теста
        premiere.sellTickets(50); // Продаем 50 билетов
        // Пример с возвратом больше билетов, чем продано:
        try {
            premiere.returnTickets(30, 10); // Пример с количеством больше, чем продано
            fail("Ожидалась ошибка: Невозможно вернуть больше билетов, чем было продано");
        } catch (IllegalArgumentException e) {
            assertEquals("Ошибка при возврате билетов: Невозможно вернуть больше билетов, чем было продано.", e.getMessage());
        }

        // Пример с возвратом отрицательного количества билетов:
        try {
            premiere.returnTickets(-5, 10); // Пример с отрицательным количеством
            fail("Ожидалась ошибка: количество билетов должно быть положительным");
        } catch (IllegalArgumentException e) {
            assertEquals("Ошибка при возврате билетов: количество билетов должно быть положительным.", e.getMessage());
        }
    }

    @Test
    void testSetTicketSold() {
        premiere.setTicketSold(50);

        // Проверка, что количество проданных билетов установлено корректно
        assertEquals(50, premiere.getTicketSold(), "Количество проданных билетов должно быть 50");
    }

    // Тестирование негативного сценария, когда продажа билетов превышает доступное количество
    @Test
    void testSellMoreTicketsThanAvailable() {
        premiere.setTicketCount(100);  // Устанавливаем количество билетов в 100
        premiere.setTicketSold(50);    // Уже продано 50

        // Проверка на то, что нельзя продать больше билетов, чем доступно
        assertFalse(premiere.sellTickets(60), "Невозможно продать больше билетов, чем есть в наличии");
        assertEquals(50, premiere.getTicketSold(), "Количество проданных билетов не должно измениться");
    }

    // Параметризированный тест для добавления отзывов
    @ParameterizedTest
    @CsvSource({
            "'Amazing movie!', true",  // Валидный отзыв
            "'', false",               // Пустой отзыв
            "'null', false"            // null отзыв
    })
    void testAddReview(String review, boolean expectedResult) {
        // Если строка review равна "null", интерпретируем её как настоящий null
        if ("null".equals(review)) {
            review = null;
        }

        // Act
        premiere.addReview(review);

        // Assert
        if (expectedResult) {
            // Проверяем, что отзыв добавлен в список
            assertTrue(premiere.getReviews().contains(review), "Отзыв должен быть добавлен в список отзывов");
        } else {
            // Проверяем, что отзыв не добавлен в список
            assertFalse(premiere.getReviews().contains(review), "Отзыв не должен быть добавлен");
        }
    }

    // Параметризированный тест для проверки состояния списка отзывов
    @ParameterizedTest
    @CsvSource({
            "null, true",   // Пустой список отзывов (нет добавленных отзывов)
            "'Amazing movie!', false",  // Список с одним отзывом
            "'Amazing movie!, Not bad, but could be better.', false"  // Список с несколькими отзывами
    })
    void testGetReviews(String reviewInput, boolean isEmpty) {
        // Добавляем отзыв, если он не равен "null"
        if (!"null".equals(reviewInput)) {
            premiere.addReview(reviewInput);
        }

        // Act & Assert
        if (isEmpty) {
            assertTrue(premiere.getReviews().isEmpty(), "Список отзывов должен быть пустым");
        } else {
            assertFalse(premiere.getReviews().isEmpty(), "Список отзывов не должен быть пустым");
        }
    }

    // Параметризированный тест на возврат билетов
    @ParameterizedTest
    @CsvSource({
            "20, 50, 30, 70, true",  // Корректный возврат 20 билетов
            "0, 50, 50, 50, false",   // Невозможный возврат 0 билетов
            "-1, 50, 50, 50, false",  // Невозможный возврат отрицательного числа билетов
            "60, 50, 50, 50, false"   // Попытка вернуть больше, чем было продано
    })
    void testReturnTickets(int ticketsToReturn, int initialSold, int expectedSold, int expectedAvailable, boolean shouldThrowException) {
        // Устанавливаем начальные значения
        premiere.setTicketSold(initialSold);

        if (shouldThrowException) {
            // Проверяем успешный возврат билетов
            premiere.returnTickets(ticketsToReturn, premiere.getTicketSold());

            // Проверяем, что количество проданных билетов уменьшилось на количество возвращенных
            assertEquals(expectedSold, premiere.getTicketSold(), "Количество проданных билетов должно уменьшиться");

            // Проверяем, что количество доступных для продажи билетов увеличилось
            assertEquals(expectedAvailable, premiere.getTicketCount(), "Количество доступных билетов должно увеличиться");
        } else {
            // Проверяем, что выбрасывается исключение для неправильных значений
            assertThrows(IllegalArgumentException.class, () ->
                premiere.returnTickets(ticketsToReturn, premiere.getTicketSold()), "Ошибка при возврате билетов");
        }
    }

    @ParameterizedTest
    @CsvSource({
            "500.0, true, true",    // Положительный бюджет, доступен и добавляется
            "0.0, false, false",    // Нулевой бюджет, не доступен, выбрасывается исключение
            "-500.0, false, false"  // Отрицательный бюджет, не доступен, выбрасывается исключение
    })
    void testBudgetOperations(double budget, boolean isAvailable, boolean isAddedSuccessfully) {
        // Arrange
        premiere.setBudget(100.0);  // Начальный бюджет

        // Act & Assert для проверки доступности бюджета
        assertEquals(isAvailable, premiere.isBudgetAvailable(budget),
                "Результат доступности бюджета должен быть: " + isAvailable);

        // Проверяем метод добавления бюджета только если budget > 0
        if (isAddedSuccessfully) {
            // Act для добавления бюджета
            premiere.addBudget(budget);
            // Assert для проверки нового бюджета
            assertEquals(100.0 + budget, premiere.getBudget(), "Бюджет должен быть увеличен на: " + budget);
        } else {
            // Act & Assert для нулевого или отрицательного бюджета (должно быть исключение)
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                premiere.addBudget(budget));

            assertEquals("Бюджет не может быть отрицательным или нулевым.", exception.getMessage());
        }
    }
}