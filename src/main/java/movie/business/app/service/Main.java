package movie.business.app.service;

import lombok.extern.slf4j.Slf4j;
import movie.business.app.enums.FinanceType;
import movie.business.app.enums.MovieGenre;
import movie.business.app.enums.MovieStatus;
import movie.business.app.manager.ContractManager;
import movie.business.app.manager.FinanceManager;
import movie.business.app.manager.MovieManager;
import movie.business.app.manager.PremiereManager;
import movie.business.app.model.Contract;
import movie.business.app.model.FinanceRecord;
import movie.business.app.model.Movie;
import movie.business.app.model.Premiere;
import movie.business.app.repository.FinanceRepository;
import movie.business.app.repository.PremiereRepository;
import movie.business.app.util.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

@Slf4j
public class Main {
    public static void main(String[] args) {
        MovieManager movieManager = new MovieManager();
        ContractManager contractManager = new ContractManager();
        PremiereManager premiereManager = new PremiereManager();
        FinanceManager financeManager = new FinanceManager();
        PremiereRepository premiereRepository = new PremiereRepository();

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
            System.out.println("12. Удалить финансовую запись");
            System.out.println("13. Продажа билетов");
            System.out.println("14. Возврат билетов");
            System.out.println("15. Показать финансовый отчет");
            System.out.println("16. Добавить отзыв");
            System.out.println("17. Показать отзывы");
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
                    String movieId = UUID.randomUUID().toString().substring(0, 5); // Генерация уникального ID фильма
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

                    Movie movie = new Movie(movieId, title, status, genre);
                    movieManager.addMovie(movie);
                    System.out.println("Фильм добавлен: " + title + " genre: " + genre);
                    movieManager.saveMovies();
                    break;

                case 2:
                    System.out.print("Введите ID фильма для удаления: ");
                    String movieToRemoveId = scanner.nextLine().trim();
                    movieManager.removeMovie(movieToRemoveId);
                    break;

                case 3:
                    System.out.println("Список фильмов: ");
                    movieManager.loadMovie();  // Загружаем данные из файла
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
                    if (premiereId == null || premiereId.trim().isEmpty()) {
                        System.out.println("Ошибка: ID не может быть пустым!");
                        break;
                    }
                    if (premiereManager.findPremiereById(premiereId) != null) {
                        System.out.println("Ошибка: Премьера с таким ID уже существует.");
                        break;
                    }

                    System.out.print("Введите название фильма для премьеры: ");
                    String premiereTitle = scanner.nextLine();

                    int ticketCount = 0;
                    while (ticketCount <= 0) {
                        System.out.print("Введите количество билетов: ");
                        try {
                            ticketCount = Integer.parseInt(scanner.nextLine());
                            if (ticketCount <= 0) {
                                System.out.println("Ошибка: Количество билетов должно быть положительным числом.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Ошибка: Введите корректное количество билетов.");
                        }
                    }

                    // Получаем место премьеры до даты — потому что нужна зона
                    System.out.print("Введите место премьеры: ");
                    String premierePlace = scanner.nextLine();

                    ZonedDateTime premiereDate = null;
                    while (premiereDate == null) {
                        System.out.print("Введите дату премьеры (в формате dd.MM.yyyy или dd.MM.yyyy HH:mm z(Например: 20.03.2025 15:00)): ");
                        String dateInput = scanner.nextLine().trim();

                        DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm z");
                        DateTimeFormatter noZoneFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                        DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

                        try {
                            // 1. Полный формат с временной зоной
                            premiereDate = ZonedDateTime.parse(dateInput, fullFormatter);
                        } catch (DateTimeParseException e1) {
                            try {
                                // 2. Формат без зоны
                                LocalDateTime localDateTime = LocalDateTime.parse(dateInput, noZoneFormatter);
                                ZoneId zone = DateUtils.getZoneIdByLocation(premierePlace);
                                premiereDate = ZonedDateTime.of(localDateTime, zone);
                                System.out.println("Временная зона автоматически установлена: " + zone);
                            } catch (DateTimeParseException e2) {
                                try {
                                    // 3. Только дата
                                    LocalDate localDate = LocalDate.parse(dateInput, dateOnlyFormatter);
                                    LocalTime defaultTime = LocalTime.of(12, 0);
                                    ZoneId zone = DateUtils.getZoneIdByLocation(premierePlace);
                                    premiereDate = ZonedDateTime.of(localDate, defaultTime, zone);
                                    System.out.println("Установлено время по умолчанию 12:00 и временная зона: " + zone);
                                } catch (DateTimeParseException e3) {
                                    System.out.println("Ошибка: Неверный формат даты. Попробуйте снова.");
                                }
                            }
                        }
                    }

                            double budget = 0;
                            while (budget <= 0) {
                                System.out.print("Введите бюджет премьеры: ");
                                try {
                                    budget = Double.parseDouble(scanner.nextLine());
                                    if (budget <= 0) {
                                        System.out.println("Ошибка: Бюджет должен быть больше 0.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Ошибка: Введите корректное значение бюджета.");
                                }
                            }

                    Premiere newPremiere = new Premiere(premiereId, premiereTitle, premiereDate, premierePlace, ticketCount, budget);
                    premiereManager.addPremiere(newPremiere);
                    // Вызываем сохранение премьер в файл через репозиторий:
                    premiereRepository.savePremieresToFile(premiereManager.getPremiereMap(),false);
                    break;

                case 8: // Добавление гостя на премьеру
                    System.out.print("Введите ID премьеры для добавления гостя: ");
                    String premiereIdForGuest = scanner.nextLine();
                    System.out.print("Введите имя гостя: ");
                    String guestName = scanner.nextLine();
                    String isGuestAge; // Объявляем переменную ЗАРАНЕЕ
                    do {
                        System.out.print("Возраст гостя больше 18 ( да / нет ): ");
                        isGuestAge = scanner.nextLine().trim().toLowerCase();

                        if (!isGuestAge.equals("да") && !isGuestAge.equals("нет")) {
                            System.out.println("Пожалуйста, введите 'да' или 'нет'.");
                        }
                    } while (!isGuestAge.equals("да") && !isGuestAge.equals("нет"));

                    if (isGuestAge.equals("да")) {
                        // Находим премьеру по ID
                        Premiere premiereForGuest = premiereManager.findPremiereById(premiereIdForGuest);

                        if (premiereForGuest != null) {
                            // Добавляем гостя в найденную премьеру
                            premiereForGuest.addGuest(guestName, true);
                            // Вызываем сохранение гостей в файл через репозиторий:
                            premiereRepository.saveGuestsToFile(premiereForGuest, false);
                        } else {
                            System.out.println("Премьера с таким ID не найдена.");
                        }
                    } else {
                        System.out.println("Гость не достиг 18 лет. Добавление невозможно.");
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

                            // Загружаем гостей из файла
                            premiereRepository.loadGuestsFromFile(premiere,false);

                            System.out.println("ID: " + entry.getKey() + ", Название: " + premiere.getMovieTitle() +
                                    ", Дата: " + premiere.getDate() + ", Место: " + premiere.getLocation());
                            List<String> guests = premiere.getGuestList();
                            if (guests.isEmpty()) {
                                System.out.println("  Гостей пока нет.");
                            } else {
                                System.out.println("  Гости:");
                                for (String guest : guests) {
                                    System.out.println("    - " + guest);
                                }
                            }
                            System.out.println(); // Пустая строка для разделения премьер
                        }
                        premiereRepository.savePremieresToFile(premiereMap, false);
                    }
                    break;

                case 11:
                    FinanceType type = null;
                    while (type == null) {
                        System.out.print("Введите тип записи (INCOME, CREDIT, SPONSORSHIP, EXPENSE, CAST, ADVERTISING, BUDGET, OTHER  ): ");
                        String typeInput = scanner.nextLine().trim().toUpperCase();
                        try {
                            type = FinanceType.valueOf(typeInput);
                        } catch (IllegalArgumentException exception) {
                            System.out.println("Ошибка: Неверный тип записи. Попробуйте еще раз.");
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

                    // Проверяем, связано ли это с премьерой
                    System.out.print("Введите ID премьеры (если не связано с премьерой, нажмите Enter): ");
                    String Id = scanner.nextLine().trim();
                    if (!Id.isEmpty()) {
                        Premiere linkedPremiere = premiereManager.findPremiereById(Id);
                        if (linkedPremiere != null) {
                            description = "Премьера: " + linkedPremiere.getMovieTitle() + ". " + description;
                        } else {
                            System.out.println("Внимание: Премьера с таким ID не найдена. Запись будет сохранена без связи с премьерой.");
                        }
                    }
                    // Для даты: если она не введена, можно использовать текущую дату
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

                    LocalDate date = null;
                    while (date == null) { // Цикл для перезапроса даты
                        System.out.print("Введите дату (в формате DD.MM.YYYY, нажмите Enter для текущей даты)): ");
                        String input = scanner.nextLine().trim();
                        if (input.isEmpty()) {
                            date = LocalDate.now();  // Используем текущую дату
                            System.out.println("Дата установлена как текущая: " + date.format(formatter));
                        } else {
                            try {
                                date = LocalDate.parse(input,formatter);
                            } catch (DateTimeParseException e) {
                                System.out.println("Ошибка: Неверный формат даты. Попробуйте еще раз.");
                            }
                        }
                    }

                    financeManager.addFinanceRecord(
                            new FinanceRecord(UUID.randomUUID().toString().substring(0, 5), type, amount, description, date)
                    );
                    System.out.println("Финансовая запись успешно добавлена.");
                    break;

                case 12:
                    System.out.print("Введите ID финансовой записи для удаления: ");
                    String recordToRemoveId = scanner.nextLine();
                    try {
                        financeManager.removeFinanceRecord(recordToRemoveId);
                        System.out.println("Финансовая запись успешно удалена.");
                    } catch (IllegalArgumentException exception) {
                        System.out.println("Ошибка: " + exception.getMessage());
                    }
                    break;

                case 13: // Продажа билетов на премьеру
                    System.out.print("Введите ID премьеры для продажи билетов: ");
                    String premiereIdForTickets = scanner.nextLine(); // Вводим ID премьеры
                    System.out.print("Введите количество билетов для продажи: ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Ошибка: Введите корректное количество билетов.");
                        scanner.next(); // Пропускаем некорректный ввод
                    }
                    int ticketsToSell = scanner.nextInt();
                    scanner.nextLine(); // Очистка буфера

                    // Находим премьеру по ID
                    Premiere premiere = premiereManager.findPremiereById(premiereIdForTickets);

                    if (premiere != null) {
                        double ticketPrice = premiere.getTicketPrice();
                        double totalIncome = ticketPrice * ticketsToSell;

                        // Пробуем продать билеты через метод sellTickets в Premiere
                        if (premiere.sellTickets(ticketsToSell)) {
                            System.out.println("Продано билетов: " + ticketsToSell +
                                    " по цене " + ticketPrice+ " на сумму: " + totalIncome);

                            // Запись о финансовой операции в формате CSV
                            FinanceRecord financeRecord = new FinanceRecord(
                                    UUID.randomUUID().toString().substring(0, 5),
                                    FinanceType.INCOME,
                                    totalIncome,
                                    "Продажа билетов на премьеру: " + premiere.getMovieTitle(),
                                    LocalDate.now()
                            );

                            // Запись в финансовый менеджер
                            financeManager.addFinanceRecord(financeRecord);

                            //  Сохраняем премьеру с обновлёнными билетами
                            premiereRepository.savePremieresToFile(premiereManager.getPremiereMap(), false);

                            // Экспортируем финансы в CSV после продажи билетов
                            financeManager.generateFinanceReport(false);
                        }else{
                            System.out.println("Недостаточно билетов для продажи.");

                        }
                    } else {
                        System.out.println("Премьера с таким ID не найдена.");
                    }
                    break;

                case 14: // Возврат билетов
                    System.out.print("Введите ID премьеры для возврата билетов: ");
                    String premiereIdForReturn = scanner.nextLine();
                    System.out.print("Введите количество билетов для возврата: ");

                    while (!scanner.hasNextInt()) {
                        System.out.println("Ошибка: Введите корректное количество билетов.");
                        scanner.next(); // Пропускаем некорректный ввод
                    }
                    int ticketsToReturn = scanner.nextInt();
                    scanner.nextLine();

                    // Находим премьеру по ID
                    Premiere premiereForReturn = premiereManager.findPremiereById(premiereIdForReturn);

                    if (premiereForReturn != null) {
                        double ticketPrice = premiereForReturn.getTicketPrice();
                        double totalRefund = ticketPrice * ticketsToReturn;

                        try {
                            // Возвращаем билеты для найденной премьеры
                            premiereForReturn.returnTickets(ticketsToReturn, premiereForReturn.getTicketSold());
                            System.out.println("Возвращено билетов: " + ticketsToReturn +
                                    " по цене " + ticketPrice + " на сумму: " + totalRefund);

                            // Запись о возврате в финансовый менеджер
                            financeManager.addFinanceRecord(new FinanceRecord(
                                    UUID.randomUUID().toString().substring(0, 5),
                                    FinanceType.EXPENSE,
                                    totalRefund,
                                    "Возврат билетов на премьеру: " + premiereForReturn.getMovieTitle(),
                                    LocalDate.now()
                            ));

                             //  Сохраняем обновлённую премьеру
                            premiereRepository.savePremieresToFile(premiereManager.getPremiereMap(), false);

                            // Экспортируем финансы в CSV после возврата билетов
                            financeManager.generateFinanceReport(true);
                        } catch (IllegalArgumentException e) {
                            // Если возникла ошибка (например, возвращаем больше билетов, чем было продано), выводим сообщение
                            System.out.println("Ошибка: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Премьера с таким ID не найдена.");
                    }
                    break;

                case 15:// Генерация отчета
                    if (financeManager.hasRecords()) {
                        financeManager.generateFinanceReport(true);

                        // Генерация PDF-отчета из тех же данных
                        List<FinanceRecord> records = financeManager.getAllFinanceRecords();
                        new FinanceRepository().generatePDFReport(records);
                    } else {
                        System.out.println("Отчет не может быть сгенерирован, так как нет записей для анализа.");
                    }
                    break;


                case 16: // Добавление отзыва
                    System.out.print("Введите ID фильма для отзыва: ");
                    String premiereIdForReview = scanner.nextLine();

                    Premiere premiereForReview = premiereManager.findPremiereById(premiereIdForReview);

                    if (premiereForReview != null) {
                        System.out.print("Введите ваш отзыв: ");
                        String review = scanner.nextLine();
                        // Добавляем отзыв в список и сохраняем в файл
                        premiereForReview.getReviews().add(review);

                        List<String> reviews = premiereForReview.getReviews();
                        // Сохраняем отзывы в файл

                        premiereRepository.saveReviewsToFile(premiereForReview, false);

                        System.out.println("Отзыв добавлен для премьеры " + premiereForReview.getMovieTitle());
                        if (reviews.isEmpty()) {
                            System.out.println("  Отзывов пока нет.");
                        } else {
                            for (String rev : reviews) {
                                System.out.println("  - " + rev);
                            }
                        }
                    } else {
                        System.out.println("Премьера с таким ID не найдена.");
                    }
                    break;

                case 17:
                    System.out.print("Введите ID премьеры для просмотра отзывов: ");
                    String premiereIdForReviews = scanner.nextLine();
                    // Ищем премьеру в менеджере
                    Premiere premiereToShowReviews = premiereManager.findPremiereById(premiereIdForReviews);

                    if (premiereToShowReviews != null) {

                        // Загружаем отзывы из файла
                        premiereRepository.loadReviewsFromFile(premiereToShowReviews, false);
                        List<String> reviews = premiereToShowReviews.getReviews();

                        String movieTitle = premiereToShowReviews.getMovieTitle();

                        if (reviews.isEmpty()) {
                            System.out.println("К премьере " + movieTitle + " пока нет отзывов.");
                        } else {
                            System.out.println("Отзывы о премьере " + movieTitle + ":");
                            for (String review : reviews) {
                                System.out.println("- " + review);
                            }
                        }
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

