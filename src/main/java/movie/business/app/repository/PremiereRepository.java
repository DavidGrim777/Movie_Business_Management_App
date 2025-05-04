package movie.business.app.repository;

import movie.business.app.model.Premiere;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PremiereRepository {
    void save(Premiere premiere, boolean testMode);
    void deleteById(String id, boolean testMode);
    Optional<Premiere> findById(String id);
    List<Premiere> findAll();
    void saveAll(List<Premiere> premieres, boolean testMode);
    void savePremieresToFile(Map<String, Premiere> premiereMap, boolean testMode);

    void saveGuestsToFile(Premiere premiere, boolean testMode);

    void saveReviewsToFile(Premiere premiere, boolean testMode);

    Map<String, Premiere> loadPremiereFromFile();

    void loadGuestsFromFile(Premiere premiere, boolean testMode);

    void loadReviewsFromFile(Premiere premiere, boolean testMode);

    void deleteAll(boolean testMode);
}
