package tech.joes.Repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import tech.joes.Models.Movie;

import java.util.List;

public interface MovieRepository extends ElasticsearchRepository<Movie, String> {
    List<Movie> findMoviesByReleaseYear(Integer year);
}
