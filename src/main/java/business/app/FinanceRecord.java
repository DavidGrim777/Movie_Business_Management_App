package business.app;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class FinanceRecord implements Serializable {

    private String id;
    private FinanceType type; // тип записи доход/расход
    private double amount; // сумма
    private String description; // описание
    private LocalDate date;

    public FinanceRecord(String id, FinanceType type, double amount, String description, LocalDate date) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }
}
