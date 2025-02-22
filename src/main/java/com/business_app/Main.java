package com.business_app;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

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
            System.out.println("8. Удалить премьеру");
            System.out.println("9. Показать все премьеры");
            System.out.println("10. Добавить финансовую запись");
            System.out.println("11. Удалить финансовую запись");
            System.out.println("12. Показать финансовый отчет");
            System.out.println("13. Выйти");

            System.out.print("Выберите действие: ");
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException exception) {
                System.out.println("Ошибка: Введите корректное число.");
                scanner.nextLine();
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Введите ID фильма: ");
                        String movieId = scanner.nextLine();
                        System.out.print("Введите название фильма: ");
                        String title = scanner.nextLine();
                        MovieStatus status = null;
                        while (status == null) {
                            System.out.print("Введите статус фильма (PLANNED, IN_PROGRESS, COMPLETED): ");
                            String statusInput = scanner.nextLine().trim().toUpperCase();
                            try {
                                status = MovieStatus.valueOf(statusInput);
                            } catch (IllegalArgumentException exception) {
                                System.out.println("Ошибка: Некорректный статус фильма. Попробуйте снова.");
                            }
                        }

                        Movie movie = new Movie(movieId, title, status);
                        movieManager.addMovie(movie);
                        System.out.println("Фильм добавлен: " + title);

                        System.out.print("Хотите добавить премьеру для этого фильма? (да/нет): ");
                        String premiereResponse = scanner.nextLine().trim().toLowerCase();

                        if (premiereResponse.equals("да")) {
                            System.out.print("Введите ID премьеры: ");
                            String premiereId = scanner.nextLine();
                            System.out.print("Введите дату премьеры (yyyy-MM-dd): ");
                            try {
                                LocalDate premiereDate = LocalDate.parse(scanner.nextLine());
                                System.out.print("Введите место премьеры: ");
                                String premierePlace = scanner.nextLine();
                                premiereManager.addPremiere(new Premiere(premiereId, premiereDate, premierePlace));
                            } catch (DateTimeParseException exception) {
                                System.out.println("Ошибка: Неверный формат даты.");
                            }
                        } else if (!premiereResponse.equals("нет")) {
                            System.out.println("Ошибка: Введите 'да' или 'нет'.");
                        }

                        System.out.print("Хотите добавить контракт для этого фильма? (да/нет): ");
                        String contractResponse = scanner.nextLine().trim().toLowerCase();

                        if (contractResponse.equals("да")) {
                            System.out.print("Введите ID контракта: ");
                            String contractId = scanner.nextLine();
                            System.out.print("Введите имя: ");
                            String personName = scanner.nextLine();
                            System.out.print("Введите роль: ");
                            String role = scanner.nextLine();
                            System.out.print("Введите дату начала (yyyy-MM-dd): ");
                            try {
                                LocalDate startDate = LocalDate.parse(scanner.nextLine());
                                System.out.print("Введите дату окончания (yyyy-MM-dd): ");
                                LocalDate endDate = LocalDate.parse(scanner.nextLine());
                                System.out.print("Введите гонорар: ");
                                double salary = Double.parseDouble(scanner.nextLine());
                                contractManager.addContract(new Contract(contractId, personName, role, startDate, endDate, salary));
                            } catch (DateTimeParseException exception) {
                                System.out.println("Ошибка: Неверный формат даты.");
                            } catch (NumberFormatException exception) {
                                System.out.println("Ошибка: Гонорар должен быть числом.");
                            }
                        } else if (!contractResponse.equals("нет")) {
                            System.out.println("Ошибка: Введите 'да' или 'нет'.");
                        }

                        System.out.print("Хотите добавить финансовую запись для этого фильма? (да/нет): ");
                        String financeResponse = scanner.nextLine().trim().toLowerCase();

                        if (financeResponse.equals("да")) {
                            System.out.print("Введите ID записи: ");
                            String recordId = scanner.nextLine();

                            System.out.print("Введите тип записи (INCOME, EXPENSE): ");
                            String typeInput = scanner.nextLine().trim().toUpperCase();

                            try {
                                FinanceType type = FinanceType.valueOf(typeInput);
                                System.out.print("Введите сумму: ");
                                double amount = Double.parseDouble(scanner.nextLine().trim());

                                if (amount < 0) {
                                    System.out.println("Ошибка: Сумма не может быть отрицательной.");
                                    break;
                                }

                                System.out.print("Введите описание: ");
                                String description = scanner.nextLine().trim();
                                if (description.isEmpty()) {
                                    description = "Без описания";
                                }

                                financeManager.addFinanceRecord(new FinanceRecord(recordId, amount, type, description));
                            } catch (IllegalArgumentException exception) {
                                System.out.println("Ошибка: Неверный тип записи. Используйте INCOME или EXPENSE.");
                            }
                        } else if (!financeResponse.equals("нет")) {
                            System.out.println("Ошибка: Введите 'да' или 'нет'.");
                        }

                        break;

                    case 2:
                        System.out.print("Введите ID фильма для удаления: ");
                        String movieToRemoveId = scanner.nextLine();
                        movieManager.removeMovie(movieToRemoveId);
                        break;

                    case 3:
                        movieManager.printAllMovies();
                        break;

                    case 4:
                        System.out.print("Введите ID контракта: ");
                        String contractId = scanner.nextLine();
                        System.out.print("Введите имя: ");
                        String personName = scanner.nextLine();
                        System.out.print("Введите роль: ");
                        String role = scanner.nextLine();
                        System.out.print("Введите дату начала (yyyy-MM-dd): ");
                        LocalDate startDate = LocalDate.parse(scanner.nextLine());
                        System.out.print("Введите дату окончания (yyyy-MM-dd): ");
                        LocalDate endDate = LocalDate.parse(scanner.nextLine());
                        System.out.print("Введите гонорар: ");
                        double salary = scanner.nextDouble();
                        scanner.nextLine();
                        contractManager.addContract(new Contract(contractId, personName, role, startDate, endDate, salary));
                        break;

                    case 5:
                        System.out.print("Введите ID контракта для удаления: ");
                        String contractIdToRemove = scanner.nextLine();
                        contractManager.removeContract(contractIdToRemove);
                        break;

                    case 6:
                        contractManager.printAllContracts();
                        break;

                    case 7:
                        System.out.print("Введите ID премьеры: ");
                        String premiereId = scanner.nextLine();
                        System.out.print("Введите дату премьеры (yyyy-MM-dd): ");
                        LocalDate premiereDate = LocalDate.parse(scanner.nextLine());
                        System.out.print("Введите место премьеры: ");
                        String premierePlace = scanner.nextLine();
                        premiereManager.addPremiere(new Premiere(premiereId, premiereDate, premierePlace));
                        break;

                    case 8:
                        System.out.print("Введите ID премьеры для удаления: ");
                        String premiereIdToRemove = scanner.nextLine();
                       // premiereManager.removePremiere(premiereIdToRemove);  // TODO
                        break;

                    case 9:
                        premiereManager.printAllPremieres();
                        break;

                    case 10:
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

                        financeManager.addFinanceRecord(new FinanceRecord(recordId, amount, type, description));
                        System.out.println("Финансовая запись успешно добавлена.");
                        break;

                    case 11:
                        System.out.print("Введите ID финансовой записи для удаления: ");
                        String recordToRemoveId = scanner.nextLine();
                        financeManager.removeFinanceRecord(recordToRemoveId);
                        break;

                    case 12:
                        financeManager.generateFinanceReport();
                        break;

                    case 13:
                        System.out.println("Выход из приложения...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Ошибка: Выберите корректный пункт меню.");
                }
            } catch (Exception exception) {
                System.out.println("Ошибка: " + exception.getMessage());
            }
        }
    }
}
