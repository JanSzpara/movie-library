package tech.joes.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import tech.joes.models.Movie;
import tech.joes.repositories.MovieRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;

    public Collection<Movie> getAllMovies() {
        return ((Page<Movie>) repository.findAll()).getContent();
    }

    public Optional<Movie> getMovieWithId(String id) {
        return Optional.of(repository.findOne(id));
    }

    public List<Movie> getMoviesByReleaseYear(Integer year) {
        return repository.findMoviesByReleaseYear(year);
    }

    public Movie addMovie(Movie input) {
        return repository.save(input);
    }

    public Optional<Movie> updateMovie(String id, Movie input) {
        Movie movie = repository.findOne(id);

        if (movie == null) {
            return Optional.empty();
        } else {
            movie.setTitle(input.getTitle());
            movie.setBlurb(input.getBlurb());
            movie.setReleaseYear(input.getReleaseYear());
            movie.setRuntime(input.getRuntime());
            movie = repository.save(movie);
            return Optional.of(movie);
        }
    }

    public boolean deleteMovie(String id) {
        Movie movie = repository.findOne(id);

        if (movie == null) {
            return false;
        } else {
            repository.delete(movie);
            return true;
        }
    }

    public Collection<Movie> getMoviesByRuntimeIsBetween(Integer fromTime, Integer toTime) {
        return repository.findMoviesByRuntimeIsBetween(fromTime, toTime);
    }

    public Collection<Movie> getMoviesByTitleContainingOrBlurbContaining(String keyword) {
        return repository.findMoviesByTitleContainingOrBlurbContaining(keyword, keyword);
    }
}
