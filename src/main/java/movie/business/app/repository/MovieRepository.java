package movie.business.app.repository;

import movie.business.app.model.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {
    void seve(Movie movie);
    Optional<Movie> findById(String id);
    List<Movie> findAll();
    void update(Movie movie);
    void deleteById(String id);
}
