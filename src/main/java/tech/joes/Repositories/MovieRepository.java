package tech.joes.Repositories;

import org.springframework.data.repository.CrudRepository;
import tech.joes.Models.Movie;

/**
 * Created by joe on 05/04/2017.
 */
public interface MovieRepository extends CrudRepository<Movie,Long> {
}
