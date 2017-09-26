package tech.joes.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import tech.joes.models.Movie;

import java.util.List;

public interface MovieRepository extends ElasticsearchRepository<Movie, String> {
    List<Movie> findMoviesByReleaseYear(Integer year);
    List<Movie> findMoviesByRuntimeIsBetween(Integer fromTime, Integer toTime);
}
