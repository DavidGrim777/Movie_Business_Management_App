package movie.business.app;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class Main {
    public static void main(String[] args) {
        MovieManager movieManager = new MovieManager();
        ContractManager contractManager = new ContractManager();
        PremiereManager premiereManager = new PremiereManager();
        FinanceManager financeManager = new FinanceManager();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Добавить фильм");
            System.out.println("2. Удалить фильм");
            System.out.println("3. Показать список фильмов");
            System.out.println("4. Добавить контракт");
            System.out.println("5. Удалить контракт");
            System.out.println("6. Показать список контрактов");
            System.out.println("7. Добавить премьеру");
            System.out.println("8. Добавить гостя на премьеру");
            System.out.println("9. Удалить премьеру");
            System.out.println("10. Показать все премьеры с гостями");
            System.out.println("11. Добавить финансовую запись");
            System.out.println("12. Добавить бюджет для премьеры");
            System.out.println("13. Удалить финансовую запись");
            System.out.println("14. Продажа билетов");
            System.out.println("15. Возврат билетов");
            System.out.println("16. Показать финансовый отчет");
            System.out.println("17. Добавить отзыв");
            System.out.println("18. Выйти");

            System.out.print("Выберите действие: ");
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException exception) {
                log.error("Ошибка: Введите корректное число.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    String movieId;
                    do {
                        System.out.print("Введите ID фильма: ");
                        movieId = scanner.nextLine().trim();
                        if (movieId.isEmpty()) {
                            System.out.println("Ошибка: ID фильма не может быть пустым.");
                        }
                    } while (movieId.isEmpty());

                    String title;
                    do {
                        System.out.print("Введите название фильма: ");
                        title = scanner.nextLine().trim();
                        if (title.isEmpty()) {
                            System.out.println("Ошибка: Название фильма не может быть пустым.");
                        }
                    } while (title.isEmpty());

                    MovieStatus status = null;
                    while (status == null) {
                        System.out.print("Введите статус фильма (PLANNED, IN_PROGRESS, COMPLETED): ");
                        String statusInput = scanner.nextLine().trim().toUpperCase();
                        try {
                            status = MovieStatus.valueOf(statusInput);
                        } catch (IllegalArgumentException exception) {
                            log.error("Ошибка: Некорректный статус фильма. Попробуйте снова.");
                        }
                    }
                    MovieGenre genre = null;
                    while (genre == null) {
                        System.out.print("Введите жанр фильма (ACTION, DRAMA, COMEDY, HORROR, FANTASY, THRILLER, ROMANCE, " +
                                "DOCUMENTARY, ANIMATION, ADVENTURE, CRIME, MYSTERY, FAMILY, WAR, MUSICAL): ");
                        String genreInput = scanner.nextLine().trim().toUpperCase();
                        try {
                            genre = MovieGenre.valueOf(genreInput);
                        } catch (IllegalArgumentException exception) {
                            log.error("Ошибка: введён некорректный жанр.");
                        }
                    }

                    Movie movie = new Movie(movieId, title, status);
                    movieManager.addMovie(movie);
                    System.out.println("Фильм добавлен: " + title + " genre: " + genre);

                    String movieData = movieId + ", " + title + ", " + status + ", " + genre;
                    movieManager.saveMovies(movieData);
                    break;


                case 2:
                    System.out.print("Введите ID фильма для удаления: ");
                    String movieToRemoveId = scanner.nextLine().trim();
                    movieManager.removeMovie(movieToRemoveId);
                    break;

                case 3:
                    System.out.println("Список фильмов: ");
                    movieManager.printAllMovies();
                    break;

                case 4:
                    System.out.print("Введите ID контракта: ");
                    String contractId = scanner.nextLine().trim();
                    System.out.print("Введите имя: ");
                    String personName = scanner.nextLine().trim();
                    System.out.print("Введите роль: ");
                    String role = scanner.nextLine().trim();
                    System.out.print("Введите дату начала (yyyy-MM-dd): ");
                    LocalDate startDate = LocalDate.parse(scanner.nextLine().trim());
                    System.out.print("Введите дату окончания (yyyy-MM-dd): ");
                    LocalDate endDate = LocalDate.parse(scanner.nextLine().trim());
                    System.out.print("Введите гонорар: ");
                    double salary = scanner.nextDouble();
                    scanner.nextLine();
                    contractManager.addContract(new Contract(contractId, personName, role, startDate, endDate, salary));
                    break;

                case 5:
                    System.out.print("Введите ID контракта для удаления: ");
                    String contractIdToRemove = scanner.nextLine().trim();
                    contractManager.removeContract(contractIdToRemove);
                    break;

                case 6:
                    contractManager.printAllContracts();
                    break;

                case 7:
                    System.out.print("Введите ID премьеры: ");
                    String premiereId = scanner.nextLine();
                    System.out.print("Введите название фильма для премьеры: ");
                    String premiereTitle = scanner.nextLine();
                    System.out.print("Введите количество билетов: ");
                    int ticketCount = scanner.nextInt();
                    scanner.nextLine();  // Очистка буфера после nextInt()
                    try {
                        System.out.print("Введите дату премьеры (dd.MM.yyyy HH:mm z) (например: 10.11.2025 14:30 GMT): ");
                        String dateInput = scanner.nextLine();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm z");
                        ZonedDateTime premiereDate = ZonedDateTime.parse(dateInput, formatter); // Парсим дату с учётом зоны
                        System.out.print("Введите место премьеры: ");
                        String premierePlace = scanner.nextLine();
                        premiereManager.addPremiere(new Premiere(premiereId, premiereTitle, premiereDate, premierePlace, ticketCount));
                    } catch (Exception exception) {
                        System.out.println("Ошибка: Неверный формат даты.");
                    }
                    break;

                case 8: // Добавление гостя на премьеру
                    System.out.print("Введите ID премьеры для добавления гостя: ");
                    String premiereIdForGuest = scanner.nextLine();
                    System.out.print("Введите имя гостя: ");
                    String guestName = scanner.nextLine();
                    System.out.print("Введите возраст гостя: ");
                    int guestAge = scanner.nextInt();
                    // Находим премьеру по ID
                    Premiere premiereForGuest = premiereManager.findPremiereById(premiereIdForGuest);

                    if (premiereForGuest != null) {
                        // Добавляем гостя в найденную премьеру
                        premiereForGuest.addGuest(guestName, guestAge);
                    } else {
                        System.out.println("Премьера с таким ID не найдена.");
                    }
                    break;

                case 9:
                    System.out.print("Введите ID премьеры для удаления: ");
                    String premiereIdToRemove = scanner.nextLine();
                    premiereManager.removePremiereById(premiereIdToRemove);
                    break;

                case 10:
                    Map<String, Premiere> premiereMap = premiereManager.getPremiereMap();
                    if (premiereMap.isEmpty()) {
                        System.out.println("Нет доступных премьер.");
                    } else {
                        System.out.println("Список премьер:");
                        for (Map.Entry<String, Premiere> entry : premiereMap.entrySet()) {
                            Premiere premiere = entry.getValue();  // Получаем объект премьеры
                            System.out.println("ID: " + entry.getKey() + ", Название: " + premiere.getMovieTitle() +
                                    ", Дата: " + premiere.getDate() + ", Место: " + premiere.getLocation());
                        }
                    }
                    break;

                case 11:
                    System.out.print("Введите ID записи: ");
                    String recordId = scanner.nextLine().trim();

                    FinanceType type = null;
                    while (type == null) {
                        System.out.print("Введите тип записи (INCOME, EXPENSE): ");
                        String typeInput = scanner.nextLine().trim().toUpperCase();
                        try {
                            type = FinanceType.valueOf(typeInput);
                        } catch (IllegalArgumentException exception) {
                            System.out.println("Ошибка: Неверный тип записи. Используйте INCOME или EXPENSE.");
                        }
                    }

                    double amount = -1;
                    while (amount < 0) {
                        System.out.print("Введите сумму: ");
                        String amountInput = scanner.nextLine().trim();
                        try {
                            amount = Double.parseDouble(amountInput);
                            if (amount < 0) {
                                System.out.println("Ошибка: Сумма не может быть отрицательной.");
                            }
                        } catch (NumberFormatException exception) {
                            System.out.println("Ошибка: Введите корректное число.");
                        }
                    }

                    System.out.print("Введите описание: ");
                    String description = scanner.nextLine().trim();
                    if (description.isEmpty()) {
                        description = "Без описания";
                    }
                    // Для даты: если она не введена, можно использовать текущую дату
                    LocalDate date = null;
                    System.out.print("Введите дату (в формате YYYY-MM-DD): ");
                    String dateInput = scanner.nextLine().trim();
                    if (!dateInput.isEmpty()) {
                        try {
                            date = LocalDate.parse(dateInput);
                        } catch (DateTimeParseException e) {
                            System.out.println("Ошибка: Неверный формат даты.");
                        }
                    }
                    if (date != null) {
                        financeManager.addFinanceRecord(new FinanceRecord(recordId, type, amount, description, date));
                        System.out.println("Финансовая запись успешно добавлена.");
                    }
                    break;

                case 12: // Добавление бюджета
                    System.out.print("Введите ID премьеры для добавления бюджета: ");
                    String premiereIdForBudget = scanner.nextLine();  // Вводим ID премьеры
                    Premiere premiereToCheck = premiereManager.findPremiereById(premiereIdForBudget);  // Ищем премьеру

                    if (premiereToCheck != null) {
                        System.out.print("Введите сумму бюджета для добавления: ");
                        double budgetToAdd = scanner.nextDouble();

                        // Проверка бюджета
                        if (premiereToCheck.isBudgetAvailable(budgetToAdd)) {  // Используем метод для проверки бюджета
                            premiereToCheck.addBudget(budgetToAdd);  // Добавляем бюджет
                            System.out.println("Бюджет для премьеры " + premiereToCheck.getMovieTitle() + ": " + premiereToCheck.getBudget() + " добавлен.");

                            // Экспортируем финансы в CSV после добавления бюджета
                            financeManager.generateFinanceReport(true);
                        } else {
                            System.out.println("Ошибка: бюджет для премьеры не может быть отрицательным или нулевым.");
                        }
                    } else {
                        System.out.println("Премьера с таким ID не найдена.");
                    }
                    break;

                case 13:
                    System.out.print("Введите ID финансовой записи для удаления: ");
                    String recordToRemoveId = scanner.nextLine();
                    try {
                        financeManager.removeFinanceRecord(recordToRemoveId);
                        System.out.println("Финансовая запись успешно удалена.");
                    } catch (IllegalArgumentException exception) {
                        System.out.println("Ошибка: " + exception.getMessage());
                    }
                    break;

                case 14: // Продажа билетов на премьеру
                    System.out.print("Введите ID премьеры для продажи билетов: ");
                    String premiereIdForTickets = scanner.nextLine(); // Вводим ID премьеры
                    System.out.print("Введите количество билетов для продажи: ");
                    int ticketsToSell = scanner.nextInt();

                    // Находим премьеру по ID
                    Premiere premiere = premiereManager.findPremiereById(premiereIdForTickets);

                    if (premiere != null) {
                        // Пробуем продать билеты через метод sellTickets в Premiere
                        if (premiere.sellTickets(ticketsToSell)) {
                            System.out.println("Билеты успешно проданы.");
                            // Экспортируем финансы в CSV после продажи билетов
                            financeManager.generateFinanceReport(false);
                        } else {
                            System.out.println("Ошибка при продаже билетов. Недостаточно билетов.");
                        }
                    } else {
                        System.out.println("Премьера с таким ID не найдена.");
                    }
                    break;

                case 15: // Возврат билетов
                    System.out.print("Введите ID премьеры для возврата билетов: ");
                    String premiereIdForReturn = scanner.nextLine();
                    System.out.print("Введите количество билетов для возврата: ");
                    int ticketsToReturn = scanner.nextInt();

                    // Находим премьеру по ID
                    Premiere premiereForReturn = premiereManager.findPremiereById(premiereIdForReturn);

                    if (premiereForReturn != null) {
                        try {
                            // Возвращаем билеты для найденной премьеры
                            premiereForReturn.returnTickets(ticketsToReturn, premiereForReturn.getTicketSold(), true);
                            // Экспортируем финансы в CSV после возврата билетов
                            financeManager.generateFinanceReport(true);
                        } catch (IllegalArgumentException e) {
                            // Если возникла ошибка (например, возвращаем больше билетов, чем было продано), выводим сообщение
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Премьера с таким ID не найдена.");
                    }
                    break;

                case 16:// Генерация отчета
                    if (financeManager.hasRecords()) {
                        financeManager.generateFinanceReport(true);
                    } else {
                        System.out.println("Отчет не может быть сгенерирован, так как нет записей для анализа.");
                    }
                    break;


                case 17: // Добавление отзыва
                    System.out.print("Введите ID фильма для отзыва: ");
                    String premiereIdForReview = scanner.nextLine();
                    System.out.print("Введите ваш отзыв: ");
                    String reviewText = scanner.nextLine();
                    // Ищем премьеру для отзыва
                    Premiere premiereForReview = premiereManager.findPremiereById(premiereIdForReview);

                    if (premiereForReview != null) {
                        // Добавляем отзыв для найденной премьеры
                        premiereForReview.addReview(reviewText);
                        System.out.println("Отзыв добавлен для премьеры " + premiereForReview.getMovieTitle());
                    } else {
                        System.out.println("Премьера с таким ID не найдена.");
                    }
                    break;

                case 18:
                    System.out.println("Выход из приложения...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Ошибка: Выберите корректный пункт меню.");
            }
        }
    }
}



