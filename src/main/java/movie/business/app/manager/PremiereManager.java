package movie.business.app.manager;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import movie.business.app.model.Premiere;
import movie.business.app.repository.PremiereRepository;

import java.util.Map;

@Slf4j
@Getter
public class PremiereManager {
    // Карта, которая хранит премьеры, где ключ — это ID премьеры, а значение — сам объект Premiere
    private final Map<String, Premiere> premiereMap;
    private final PremiereRepository repository = new PremiereRepository();
    private final boolean testMode;

    // Конструктор класса, который загружает данные о премьерах из файла при создании объекта
    public PremiereManager(boolean testMode) {
        this.testMode = testMode;
        this.premiereMap = repository.loadPremiereFromFile();
    }

    // Конструктор по умолчанию (обычный режим)
    public PremiereManager() {
        this(false);
    }

    // Метод для добавления новой премьеры в список
    public void addPremiere(Premiere premiere) {
        if (premiere == null || premiere.getId() == null) {
            log.error("ID и премьера не может быть null.");
            throw new IllegalArgumentException("ID и премьера не может быть null.");
        }
        premiereMap.put(premiere.getId(), premiere);
        System.out.println("Премьера добавлена: " + premiere.getMovieTitle());
        savePremieresToFile();
    }

    public void savePremieresToFile() {
        repository.savePremieresToFile(premiereMap, testMode);
    }

    //Метод для поиска премьеры по ID.
    public Premiere findPremiereById(String id) {
        Premiere premiere = premiereMap.get(id);
        if (premiere == null) {
            System.out.println("Премьеры с ID " + id + " еще нет.");
        } else {
            System.out.println("Премьера найдена по ID: " + id);
        }
        return premiere;
    }

    //Метод для удаления премьеры по ID
    public void removePremiereById(String id) {
        Premiere premiere = premiereMap.remove(id);
        if (premiere == null) {
            log.warn("Не удалось удалить премьеру с ID {}: Премьера не найдена.", id);
            System.out.println("Не удалось удалить премьеру с ID " + id + ": Премьера не найдена.");
        } else {
            System.out.println("Премьера с ID " + id + " удалена.");
            savePremieresToFile();
        }
    }

    // Метод для генерации отчета по всем премьерам
    public void generatePremiereReport() {
        if (premiereMap.isEmpty()) {
            log.warn("Попытка сгенерировать отчет для пустого списка премьеры.");
            System.out.println("Нет премьеры для генерации отчета.");
            return;
        }
        log.info("Начало генерации отчетов для всех премьер.");
        for (Premiere premiere : premiereMap.values()) {
            try {
                String report = premiere.generateReport();
                System.out.println(report); // Генерируем отчет для каждой премьеры
                System.out.println("Отчет о премьере сгенерирован для: " + premiere.getMovieTitle());
                log.info(report); // Логируем отчет о премьере
            } catch (Exception e) {
                log.warn("Ошибка при генерации отчета для премьеры {}: {}", premiere.getMovieTitle(), e.getMessage());
            }
        }
    }

    //Метод для получения информации о количестве премьер
    public int getPremiereCount() {
        return premiereMap.size();
    }
}