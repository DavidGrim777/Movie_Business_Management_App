package movie.business.app.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Getter
@Slf4j
public class Contract {

    // Геттеры (методы для получения значений полей)
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

    // Сеттеры (методы для изменения значений полей с проверкой)
    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID не может быть пустым.");
            log.error("ID не может быть пустым.");
            return;
        }
        this.id = id;
    }

    public void setPersonName(String personName) {
        if (personName == null || personName.trim().isEmpty()) {
            System.out.println("Имя не может быть пустым.");
            log.error("Имя не может быть пустым.");
            return;
        }
        this.personName = personName;
    }

    public void setRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            log.error("Роль не может быть пустой.");
            System.out.println("Роль не может быть пустой.");
            return;
        }
        this.role = role;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            log.error("Дата начала не может быть пустой.");
            throw new IllegalArgumentException("Дата начала не может быть пустой.");
        }
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate == null) {
            log.error("Дата окончания не может быть пустой.");
            throw new IllegalArgumentException("Дата окончания не может быть пустой.");
        }
        this.endDate = endDate;
    }

    public void setSalary(double salary) {
        if (salary < 0) {
            log.error("Гонорар не может быть отрицательным.");
            throw new IllegalArgumentException("Гонорар не может быть отрицательным.");
        }
        this.salary = salary;
    }

    // Метод проверки, активен ли контракт (если дата окончания ещё не наступила)
    public boolean isActive() {
        return endDate != null && endDate.isAfter(LocalDate.now());
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
