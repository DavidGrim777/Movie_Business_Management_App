package com.business_app;

import java.time.LocalDate;
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
            System.out.println("3. Добавить контракт");
            System.out.println("4. Удалить контракт");
            System.out.println("5. Добавить премьеру");
            System.out.println("6. Показать все премьеры");
            System.out.println("7. Добавить финансовую запись");
            System.out.println("8. Показать финансовый отчет");
            System.out.println("9. Выйти");

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
                        System.out.print("Введите статус фильма (PLANNED, IN_PROGRESS, COMPLETED): ");
                        String status = scanner.nextLine().toUpperCase();

                        Movie movie = new Movie(movieId, title, MovieStatus.valueOf(status));
                        movieManager.addMovie(movie);
                        break;

                    case 2:
                        System.out.print("Введите ID фильма для удаления: ");
                        String movieToRemoveId = scanner.nextLine();
                        movieManager.removeMovie(movieToRemoveId);
                        break;

                    case 3:
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

                        Contract contract = new Contract(contractId, personName, role, startDate, endDate, salary);
                        contractManager.addContract(contract);
                        break;

                    case 4:
                        System.out.print("Введите ID контракта для удаления: ");
                        String contractToRemoveId = scanner.nextLine();
                        contractManager.removeContract(contractToRemoveId);
                        break;

                    case 5:
                        System.out.print("Введите ID премьеры: ");
                        String premiereId = scanner.nextLine();
                        System.out.print("Введите дату премьеры (yyyy-MM-dd): ");
                        LocalDate premiereDate = LocalDate.parse(scanner.nextLine());
                        System.out.print("Введите место премьеры: ");
                        String premierePlace = scanner.nextLine();

                        Premiere premiere = new Premiere(premiereId, premiereDate, premierePlace);
                        premiereManager.addPremiere(premiere);
                        break;

                    case 6:
                        premiereManager.generatePremiereReport();
                        break;

                    case 7:
                        System.out.print("Введите ID записи: ");
                        int recordId = scanner.nextInt();
                        System.out.print("Введите тип записи (INCOME, EXPENSE): ");
                        String type = scanner.next().toUpperCase();
                        System.out.print("Введите сумму: ");
                        double amount = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Введите описание: ");
                        String description = scanner.nextLine();
                        System.out.print("Введите дату (yyyy-MM-dd): ");
                        LocalDate date = LocalDate.parse(scanner.nextLine());

                        FinanceRecord record = new FinanceRecord(recordId, FinanceType.valueOf(type), amount, description, date);
                        financeManager.addFinanceRecord(record);
                        break;

                    case 8:
                        financeManager.generateFinanceReport();
                        break;

                    case 9:
                        System.out.println("Выход из приложения...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                        break;
                }
            } catch (IllegalArgumentException exception) {
                System.out.println("Ошибка: Некорректный ввод данных. " + exception.getMessage());
            } catch (Exception exception) {
                System.out.println("Произошла ошибка: " + exception.getMessage());
            }
        }
    }
}