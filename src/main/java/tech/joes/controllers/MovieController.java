package tech.joes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.joes.models.Movie;
import tech.joes.services.MovieService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@EnableAutoConfiguration
public class MovieController {

    @Autowired
    private MovieService movieService;

    @RequestMapping(method = RequestMethod.GET, value = "/movies")
    @ResponseBody
    public ResponseEntity<Collection<Movie>> getAllMovies() {

        Collection<Movie> movies = movieService.getAllMovies();

        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/movies/{id}")
    @ResponseBody
    public ResponseEntity<Movie> getMovieWithId(@PathVariable String id) {

        Optional<Movie> movie = movieService.getMovieWithId(id);

        return getMovieResponseEntity(movie);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/movies/releaseYear/{year}")
    @ResponseBody
    public ResponseEntity<Collection<Movie>> getMoviesByReleaseYear(@PathVariable Integer year) {

        List<Movie> movies = movieService.getMoviesByReleaseYear(year);

        return getMovieListResponseEntity(movies);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/movies")
    @ResponseBody
    public ResponseEntity addMovie(@RequestBody Movie input) {
        return new ResponseEntity<>(movieService.addMovie(input), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/movies/{id}")
    @ResponseBody
    public ResponseEntity updateMovie(@PathVariable String id, @RequestBody Movie input) {
        Optional<Movie> movie = movieService.updateMovie(id, input);
        return getMovieResponseEntity(movie);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/movies/{id}")
    @ResponseBody
    public ResponseEntity deleteMovie(@PathVariable String id) {

        boolean deleted = movieService.deleteMovie(id);

        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/movies/runtime/between/{fromTime}/{toTime}")
    @ResponseBody
    public ResponseEntity<Collection<Movie>> getMoviesByRuntimeIsBetween(@PathVariable Integer fromTime, @PathVariable Integer toTime) {

        Collection<Movie> movies = movieService.getMoviesByRuntimeIsBetween(fromTime, toTime);

        return getMovieListResponseEntity(movies);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/movies/title/blurb/contains/{keyword}")
    @ResponseBody
    public ResponseEntity<Collection<Movie>> getMoviesByTitleContainingOrBlurbContaining(@PathVariable String keyword) {

        Collection<Movie> movies = movieService.getMoviesByTitleContainingOrBlurbContaining(keyword);

        return getMovieListResponseEntity(movies);
    }

    private ResponseEntity<Collection<Movie>> getMovieListResponseEntity(Collection<Movie> movies) {
        if (movies.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    private ResponseEntity<Movie> getMovieResponseEntity(Optional<Movie> movie) {
        if (!movie.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(movie.get(), HttpStatus.OK);
    }
}