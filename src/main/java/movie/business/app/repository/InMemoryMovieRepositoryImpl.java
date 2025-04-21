package movie.business.app.repository;

import movie.business.app.model.Movie;

import java.util.*;

public class InMemoryMovieRepositoryImpl implements MovieRepository {

    private final Map<String, Movie> movieMap = new HashMap<>();

    @Override
    public void seve(Movie movie) {
        movieMap.put(movie.getId(), movie);
    }

    @Override
    public Optional<Movie> findById(String id) {
        return Optional.ofNullable(movieMap.get(id));
    }

    @Override
    public List<Movie> findAll() {
        return new ArrayList<>(movieMap.values());
    }

    @Override
    public void update(Movie movie) {
        movieMap.put(movie.getId(), movie);
    }

    @Override
    public void deleteById(String id) {

    }
}
