package tech.joes.Controllers; /**
 * Created by joe on 05/04/2017.
 */

import org.springframework.boot.autoconfigure.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import tech.joes.Models.Movie;

import java.util.ArrayList;
import java.util.List;

@Controller
@EnableAutoConfiguration
public class MovieController {

    @RequestMapping("/movies/")
    @ResponseBody
    public ResponseEntity<List<Movie>> getAllMovies() {
        Movie movie = new Movie();
        movie.setTitle("Test Title");

        ArrayList<Movie> movies = new ArrayList<>();

        movies.add(movie);

        return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);
    }

}