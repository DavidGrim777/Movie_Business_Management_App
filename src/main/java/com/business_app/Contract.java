package com.business_app;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class Contract {

    private String id;                    // Уникальный идентификатор контракта
    private String personName;            // Имя человека, с которым заключен контракт
    private String role;                  // Роль человека в проекте
    private LocalDate startDate;          // Дата начала контракта
    private LocalDate endDate;            // Дата окончания контракта
    private double salary;                // Гонорар

    // Конструктор класса (создает объект с начальными значениями)
    public Contract(String id, String personName, String role, LocalDate startDate, LocalDate endDate, double salary) {
        this.id = id;
        this.personName = personName;
        this.role = role;
        this.startDate = startDate;
        this.endDate = endDate;
        this.salary = salary;
    }

    // Геттеры (методы для получения значений полей)
    public String getId() {
        return id;
    }

    public String getPersonName() {
        return personName;
    }

    public String getRole() {
        return role;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getSalary() {
        return salary;
    }

    // Сеттеры (методы для изменения значений полей с проверкой)
    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID не может быть пустым.");
            log.warn("ID не может быть пустым.");
            return;
        }
        this.id = id;
    }

    public void setPersonName(String personName) {
        if (personName == null || personName.trim().isEmpty()) {
            System.out.println("Имя не может быть пустым.");
            log.warn("Имя не может быть пустым.");
        }
        this.personName = personName;
    }

    public void setRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            System.out.println("Роль не может быть пустой.");
            log.warn("Роль не может быть пустой.");
        }
        this.role = role;
    }

    public void setStartDate(String startDate) {
        if (startDate == null || startDate.trim().isEmpty()) {
            System.out.println("Дата начала не может быть пустой.");
            log.warn("Дата начала не может быть пустой.");
        }
        this.startDate = LocalDate.parse(startDate);
    }

    public void setEndDate(String endDate) {
        if (endDate == null || endDate.trim().isEmpty()) {
            System.out.println("Дата окончания не может быть пустой.");
            log.warn("Дата окончания не может быть пустой.");
        }
        this.endDate = LocalDate.parse(endDate);
    }

    public void setSalary(double salary) {
        if (salary < 0) {
            System.out.println("Гонорар не может быть отрицательным.");
            log.warn("Гонорар не может быть отрицательным.");
        }
        this.salary = salary;
    }

    // Метод проверки, активен ли контракт (если дата окончания ещё не наступила)
    public boolean isActive() {
        return endDate != null && !endDate.isAfter(LocalDate.now());
    }

    // Переопределение метода toString() для вывода информации о контракте
    @Override
    public String toString() {
        return "Contract{" +
                "id='" + id + '\'' +
                ", personName='" + personName + '\'' +
                ", role='" + role + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", salary=" + salary +
                '}';
    }
}
