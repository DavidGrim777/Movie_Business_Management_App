package movie.business.app.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import movie.business.app.enums.ContractStatus;

import java.time.LocalDate;

@Getter
@Setter
@Slf4j
public class Contract {

    private String id;                    // Уникальный идентификатор контракта
    private String personName;            // Имя человека, с которым заключен контракт
    private String role;                  // Роль человека в проекте
    private LocalDate startDate;          // Дата начала контракта
    private LocalDate endDate;            // Дата окончания контракта
    private double salary;                // Гонорар
    private ContractStatus status;

    // Конструктор класса (создает объект с начальными значениями)

    public Contract(String id, String personName, String role, LocalDate startDate, LocalDate endDate, double salary, ContractStatus status) {
        this.id = id;
        this.personName = personName;
        this.role = role;
        this.startDate = startDate;
        this.endDate = endDate;
        this.salary = salary;
        this.status = status;
    }
}
