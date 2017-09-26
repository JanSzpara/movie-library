package tech.joes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.joes.models.Movie;
import tech.joes.repositories.MovieRepository;

import java.util.Collection;
import java.util.List;

@RestController
@EnableAutoConfiguration
public class MovieController {

    @Autowired
    private MovieRepository repository;

    @RequestMapping(method = RequestMethod.GET, value = "/movies")
    @ResponseBody
    public ResponseEntity<Collection<Movie>> getAllMovies() {

        Page<Movie> movies = (Page<Movie>) repository.findAll();

        return new ResponseEntity<>(movies.getContent(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/movies/{id}")
    @ResponseBody
    public ResponseEntity<Movie> getMovieWithId(@PathVariable String id) {

        Movie movie = repository.findOne(id);

        if (movie == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/movies/releaseYear/{year}")
    @ResponseBody
    public ResponseEntity<List<Movie>> getMoviesByReleaseYear(@PathVariable Integer year) {

        List<Movie> movies = repository.findMoviesByReleaseYear(year);

        if (movies.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(movies, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/movies")
    @ResponseBody
    public ResponseEntity addMovie(@RequestBody Movie input) {
        return new ResponseEntity<>(repository.save(input), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/movies/{id}")
    @ResponseBody
    public ResponseEntity updateMovie(@PathVariable String id, @RequestBody Movie input) {
        Movie movie = repository.findOne(id);

        if (movie == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        movie.setTitle(input.getTitle());
        movie.setBlurb(input.getBlurb());
        movie.setReleaseYear(input.getReleaseYear());
        movie.setRuntime(input.getRuntime());

        return new ResponseEntity<>(repository.save(movie), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/movies/{id}")
    @ResponseBody
    public ResponseEntity deleteMovie(@PathVariable String id) {
        Movie movie = repository.findOne(id);

        if (movie == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        repository.delete(movie);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/movies/runtime/between/{fromTime}/{toTime}")
    @ResponseBody
    public ResponseEntity<Collection<Movie>> getMoviesByRuntimeIsBetween(@PathVariable Integer fromTime, @PathVariable Integer toTime) {

        List<Movie> movies = repository.findMoviesByRuntimeIsBetween(fromTime, toTime);

        if (movies.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/movies/title/blurb/contains/{keyword}")
    @ResponseBody
    public ResponseEntity<Collection<Movie>> getMoviesByTitleContainingOrBlurbContaining(@PathVariable String keyword) {

        List<Movie> movies = repository.findMoviesByTitleContainingOrBlurbContaining(keyword, keyword);

        if (movies.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(movies, HttpStatus.OK);
    }
}