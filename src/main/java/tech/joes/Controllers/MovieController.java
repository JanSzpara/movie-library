package tech.joes.Controllers; /**
 * Created by joe on 05/04/2017.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.joes.Models.Movie;
import tech.joes.Repositories.MovieRepository;

import java.util.Collection;

@Controller
@EnableAutoConfiguration
public class MovieController {

    @Autowired
    private MovieRepository repository;

    @RequestMapping(method = RequestMethod.GET, value = "/movies/")
    @ResponseBody
    public ResponseEntity<Collection<Movie>> getAllMovies() {

        Collection<Movie> movies = (Collection<Movie>) repository.findAll();

        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/movies/{id}")
    @ResponseBody
    public ResponseEntity<Movie> getMovieWithId(@PathVariable Long id) {

        Movie movie = repository.findOne(id);

        return new ResponseEntity<>(movie, HttpStatus.OK);
    }




}