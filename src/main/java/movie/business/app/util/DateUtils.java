package movie.business.app.util;

import java.time.ZoneId;

public class DateUtils {
    public static ZoneId getZoneIdByLocation(String location) {
        return switch (location.trim().toLowerCase()) {
            case "лондон", "london" -> ZoneId.of("Europe/London");
            case "нью-йорк", "new york" -> ZoneId.of("America/New_York");
            case "токио", "tokyo" -> ZoneId.of("Asia/Tokyo");
            case "берлин", "berlin" -> ZoneId.of("Europe/Berlin");
            case "москва", "moscow" -> ZoneId.of("Europe/Moscow");
            case "киев", "kiev", "kyiv" -> ZoneId.of("Europe/Kyiv");
            default -> {
                System.out.println("Город не распознан, используется системная временная зона.");
                yield ZoneId.systemDefault();
            }
        };
    }
}
