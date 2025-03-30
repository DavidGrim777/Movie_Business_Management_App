package movie.business.app.enums;

public enum FinanceType {
    INCOME ("Доход"),             // Доходы от проката
    CREDIT("Кредит"),             // Кредиты банка
    SPONSORSHIP("Спонсорство"),   // Спонсорские средства
    EXPENSE("Расход"),            // Расходы на съёмки
    CAST("Актёрский состав"),     // Расходы на гонорары актёров и персонала
    ADVERTISING("Реклама"),       // Расходы на рекламу
    BUDGET("Бюджет"),             // Основной бюджет (например, для премьеры)
    OTHER ("Другое");             // Прочее
    private final String category;

    FinanceType(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}