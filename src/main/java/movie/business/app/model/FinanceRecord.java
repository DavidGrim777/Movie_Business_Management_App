package movie.business.app.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import movie.business.app.enums.FinanceType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class FinanceRecord implements Serializable {
    @Serial//с Java 14, рекомендуется аннотировать его @Serial
    private static final long serialVersionUID = 1L; // Версия класса для сериализации
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private String id;
    private FinanceType type; // тип записи доход/расход
    private double amount; // сумма
    private String description; // описание
    private LocalDate date;

    public FinanceRecord(String id, FinanceType type, double amount, String description, LocalDate date) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID не может быть пустым.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Тип не может быть null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть больше 0.");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Описание не может быть пустым.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Дата не может быть пустой.");
        }
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }
}