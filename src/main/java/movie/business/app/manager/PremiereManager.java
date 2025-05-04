package movie.business.app.manager;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import movie.business.app.model.Premiere;
import movie.business.app.repository.PremiereRepository;
import movie.business.app.util.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Slf4j
@Getter
public class PremiereManager {

    private final PremiereRepository repository;
    private final boolean testMode;

    public PremiereManager(PremiereRepository repository, boolean testMode) {
        this.repository = repository;
        this.testMode = testMode;
    }

    public void createPremiere(Scanner scanner) {
        System.out.print("Введите ID премьеры: ");
        String premiereId = scanner.nextLine();
        if (premiereId == null || premiereId.trim().isEmpty()) {
            System.out.println("Ошибка: ID не может быть пустым!");
            return;
        }
        if (repository.findById(premiereId).isPresent()) {
            System.out.println("Ошибка: Премьера с таким ID уже существует.");
            return;
        }

        System.out.print("Введите название фильма для премьеры: ");
        String title = scanner.nextLine();

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

        System.out.print("Введите место премьеры: ");
        String location = scanner.nextLine();

        ZonedDateTime date = null;
        while (date == null) {
            System.out.print("Введите дату премьеры (dd.MM.yyyy или dd.MM.yyyy HH:mm z): ");
            String input = scanner.nextLine().trim();

            DateTimeFormatter full = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm z");
            DateTimeFormatter partial = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            DateTimeFormatter simple = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            try {
                date = ZonedDateTime.parse(input, full);
            } catch (DateTimeParseException e1) {
                try {
                    LocalDateTime ldt = LocalDateTime.parse(input, partial);
                    date = ZonedDateTime.of(ldt, DateUtils.getZoneIdByLocation(location));
                } catch (DateTimeParseException e2) {
                    try {
                        LocalDate ld = LocalDate.parse(input, simple);
                        date = ZonedDateTime.of(ld, LocalTime.of(12, 0), DateUtils.getZoneIdByLocation(location));
                    } catch (DateTimeParseException ignored) {
                        System.out.println("Ошибка: Неверный формат даты.");
                    }
                }
            }
        }

        double budget = 0;
        while (budget <= 0) {
            System.out.print("Введите бюджет: ");
            try {
                budget = Double.parseDouble(scanner.nextLine());
                if (budget <= 0) {
                    System.out.println("Ошибка: Бюджет должен быть положительным.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введите корректный бюджет.");
            }
        }

        Premiere premiere = new Premiere(premiereId, title, date, location, ticketCount, budget);
        repository.save(premiere, testMode);
        System.out.println("Премьера добавлена.");
    }

    // Метод для добавления новой премьеры в список
    public void addPremiere(Premiere premiere) {
        if (premiere == null || premiere.getId() == null) {
            log.error("ID и премьера не может быть null.");
            throw new IllegalArgumentException("ID и премьера не может быть null.");
        }
        repository.save(premiere, testMode);
        System.out.println("Премьера добавлена: " + premiere.getMovieTitle());
        savePremieresToFile();
    }

    public void savePremieresToFile() {
        repository.saveAll(repository.findAll(), testMode);
        System.out.println("Все премьеры сохранены в файл.");
        log.info("Все премьеры сохранены через метод saveAll().");
    }


    public void addGuestToPremiere(Scanner scanner) {
        System.out.print("Введите ID премьеры: ");
        String id = scanner.nextLine();
        Optional<Premiere> optional = repository.findById(id);

        if (optional.isEmpty()) {
            System.out.println("Премьера с таким ID не найдена.");
            return;
        }
        Premiere premiere = optional.get();

        System.out.print("Введите имя гостя: ");
        String guest = scanner.nextLine();

        System.out.print("Возраст больше 18? (да/нет): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        if (answer.equals("да")) {
            premiere.addGuest(guest, true);
            repository.saveGuestsToFile(premiere, testMode);
        } else {
            System.out.println("Гость не достиг 18 лет. Добавление невозможно.");
        }
    }

    public void addReviewToPremiere(Scanner scanner) {
        System.out.print("Введите ID премьеры: ");
        String id = scanner.nextLine();
        Optional<Premiere> optional = repository.findById(id);

        if (optional.isEmpty()) {
            System.out.println("Премьера не найдена.");
            return;
        }

        Premiere premiere = optional.get();
        System.out.print("Введите отзыв: ");
        String review = scanner.nextLine();
        premiere.addReview(review);
        repository.saveReviewsToFile(premiere, testMode);
    }

    public void printAllPremieresWithGuests() {
        List<Premiere> premieres = repository.findAll();
        if (premieres.isEmpty()) {
            System.out.println("Нет премьер.");
            return;
        }

        for (Premiere p : premieres) {
            repository.loadGuestsFromFile(p, testMode);
            System.out.println("ID: " + p.getId() + ", Фильм: " + p.getMovieTitle() + ", Дата: " + p.getDate() + ", Место: " + p.getLocation());
            if (p.getGuestList().isEmpty()) {
                System.out.println("  Гостей нет.");
            } else {
                p.getGuestList().forEach(g -> System.out.println("  - " + g));
            }
            System.out.println();
        }
    }

    public void printReviewsForPremiere(Scanner scanner) {
        System.out.print("Введите ID премьеры: ");
        String id = scanner.nextLine();
        Optional<Premiere> optional = repository.findById(id);

        if (optional.isEmpty()) {
            System.out.println("Премьера не найдена.");
            return;
        }

        Premiere p = optional.get();
        repository.loadReviewsFromFile(p, testMode);
        List<String> reviews = p.getReviews();

        if (reviews.isEmpty()) {
            System.out.println("Отзывы отсутствуют.");
        } else {
            System.out.println("Отзывы к фильму " + p.getMovieTitle() + ":");
            reviews.forEach(r -> System.out.println("- " + r));
        }
    }

    //Метод для поиска премьеры по ID.
    public Optional<Premiere> findPremiereById(String id) {
        Optional<Premiere> found = repository.findById(id);
        if (found.isPresent()) {
            System.out.println("Премьера найдена: " + found.get());
        } else {
            System.out.println("Премьера с ID " + id + " не найдена.");
        }
        return found;
    }


    //Метод для удаления премьеры по ID
    public void removePremiereById(String id) {
        Optional<Premiere> found = repository.findById(id);
        if (found.isEmpty()) {
            log.warn("Не удалось удалить премьеру с ID {}: Премьера не найдена.", id);
            System.out.println("Не удалось удалить премьеру с ID " + id + ": Премьера не найдена.");
            return;
        } repository.deleteById(id, testMode);
            System.out.println("Премьера с ID " + id + " удалена.");
            savePremieresToFile();
        }

    // Метод для генерации отчета по всем премьерам
    public void generatePremiereReport() {
        List<Premiere> all = repository.findAll();
        if (all.isEmpty()) {
            log.warn("Попытка сгенерировать отчет для пустого списка премьер.");
            System.out.println("Нет премьеры для генерации отчета.");
            return;
        }
        log.info("Начало генерации отчетов для всех премьер.");
        for (Premiere premiere : all) {
            try {
                String report = premiere.generateReport();
                System.out.println(report);
                log.info("Отчет о премьере: {}", premiere.getMovieTitle()); // Генерируем отчет для каждой премьеры
            } catch (Exception e) {
                log.warn("Ошибка при генерации отчета для премьеры: {}", e.getMessage());
            }
        }
    }

    //Метод для получения информации о количестве премьер
    public int getPremiereCount() {
        return repository.findAll().size();
    }

    public void clearPremieres() {
        repository.deleteAll(testMode);
    }
}
