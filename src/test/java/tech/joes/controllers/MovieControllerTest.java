package tech.joes.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.joes.Application;
import tech.joes.generators.MovieFakeDataGenerator;
import tech.joes.models.Movie;
import tech.joes.repositories.MovieRepository;
import tech.joes.serilaizers.MovieTestSerializer;
import tech.joes.services.MovieService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class MovieControllerTest {

    private final static int DUMMY_DATA_SIZE = 5;
    @Mock
    private MovieRepository mockMovieRepository;
    @Mock
    private MovieService mockMovieService;

    @InjectMocks
    private MovieController movieController;

    private MockMvc mockMvc;

    private Gson gson;

    private final static List<Movie> dummyData = MovieFakeDataGenerator.getDummyData(DUMMY_DATA_SIZE);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(movieController)
                .build();

        gson = new GsonBuilder()
                .registerTypeAdapter(Movie.class, new MovieTestSerializer())
                .create();
    }

    @Test
    public void shouldReturnAllAvaliableWhenGetAllMovies() throws Exception {
        //given
        List<Movie> data = dummyData;
        //when
        when(mockMovieService.getAllMovies()).thenReturn(data);
        //then
        mockMvc.perform(get("/movies/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(dummyData.size())));
    }

    @Test
    public void shouldReturnCorrectMovieWhenGetSingleMovie() throws Exception {
        //given
        Integer indexToTest = 2;
        Optional<Movie> expectedResult = Optional.of(dummyData.get(indexToTest - 1));
        //when
        when(mockMovieService.getMovieWithId(indexToTest.toString())).thenReturn(expectedResult);
        //then
        mockMvc.perform(get("/movies/" + indexToTest))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id", is(expectedResult.get().getId())))
                .andExpect(jsonPath("$.title", is(expectedResult.get().getTitle())))
                .andExpect(jsonPath("$.blurb", is(expectedResult.get().getBlurb())))
                .andExpect(jsonPath("$.releaseYear", is(expectedResult.get().getReleaseYear())))
                .andExpect(jsonPath("$.runtime", is(expectedResult.get().getRuntime())));
    }

    @Test
    public void shouldReturnCorrectMoviesWhenGetMoviesByReleaseYear() throws Exception {
        //given
        int year = 1965;
        List<Movie> expectedResult = dummyData;
        Movie first = expectedResult.get(0);
        Movie second = expectedResult.get(1);
        //when
        when(mockMovieService.getMoviesByReleaseYear(year)).thenReturn(expectedResult);
        //then
        mockMvc.perform(get("/movies/releaseYear/" + year))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(expectedResult.size())))
                .andExpect(jsonPath("$[0].id", is(first.getId())))
                .andExpect(jsonPath("$[0].title", is(first.getTitle())))
                .andExpect(jsonPath("$[0].blurb", is(first.getBlurb())))
                .andExpect(jsonPath("$[0].releaseYear", is(first.getReleaseYear())))
                .andExpect(jsonPath("$[0].runtime", is(first.getRuntime())))
                .andExpect(jsonPath("$[1].id", is(second.getId())))
                .andExpect(jsonPath("$[1].title", is(second.getTitle())))
                .andExpect(jsonPath("$[1].blurb", is(second.getBlurb())))
                .andExpect(jsonPath("$[1].releaseYear", is(second.getReleaseYear())))
                .andExpect(jsonPath("$[1].runtime", is(second.getRuntime())));
    }

    @Test
    public void shouldCreateNewMovieWhenCreate() throws Exception {
        //given
        Movie newMovie = dummyData.get(0);
        final String jsonData = gson.toJson(newMovie);
        //when
        when(mockMovieRepository.save(newMovie)).thenReturn(newMovie);
        //then
        mockMvc.perform(post("/movies/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldUpdateExistingMovieWhenUpdate() throws Exception {
        //given
        Integer indexToTest = 2;
        Movie updatedMovie = dummyData.get(indexToTest - 1);
        String jsonData = gson.toJson(updatedMovie);
        //when
        when(mockMovieService.updateMovie(anyString(), any(Movie.class))).thenReturn(Optional.of(updatedMovie));
        //then
        mockMvc.perform(put("/movies/" + indexToTest)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteMovieWhenDelete() throws Exception {
        //given
        Integer indexToTest = 2;
        //when
        when(mockMovieService.deleteMovie(indexToTest.toString())).thenReturn(true);
        //then
        mockMvc.perform(delete("/movies/" + indexToTest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnCorrectMoviesWhenGetByRuntimeInBetween() throws Exception {
        //given
        int fromTime = 1;
        int toTime = 4;
        List<Movie> expectedResult = dummyData;
        Movie first = expectedResult.get(0);
        Movie second = expectedResult.get(1);
        //when
        when(mockMovieService.getMoviesByRuntimeIsBetween(fromTime, toTime)).thenReturn(expectedResult);
        //then
        mockMvc.perform(get("/movies/runtime/between/" + fromTime + "/" + toTime))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(expectedResult.size())))
                .andExpect(jsonPath("$[0].id", is(first.getId())))
                .andExpect(jsonPath("$[0].title", is(first.getTitle())))
                .andExpect(jsonPath("$[0].blurb", is(first.getBlurb())))
                .andExpect(jsonPath("$[0].releaseYear", is(first.getReleaseYear())))
                .andExpect(jsonPath("$[0].runtime", is(first.getRuntime())))
                .andExpect(jsonPath("$[1].id", is(second.getId())))
                .andExpect(jsonPath("$[1].title", is(second.getTitle())))
                .andExpect(jsonPath("$[1].blurb", is(second.getBlurb())))
                .andExpect(jsonPath("$[1].releaseYear", is(second.getReleaseYear())))
                .andExpect(jsonPath("$[1].runtime", is(second.getRuntime())));
    }

    @Test
    public void shouldReturnCorrectMoviesWhenGetMoviesByTitleOrBlurbContainingKeyword() throws Exception {
        //given
        String keyword = "keyword";
        List<Movie> expectedResult = dummyData;
        Movie first = expectedResult.get(0);
        Movie second = expectedResult.get(1);
        //when
        when(mockMovieService.getMoviesByTitleContainingOrBlurbContaining(keyword)).thenReturn(expectedResult);
        //then
        mockMvc.perform(get("/movies/title/blurb/contains/" + keyword))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(expectedResult.size())))
                .andExpect(jsonPath("$[0].id", is(first.getId())))
                .andExpect(jsonPath("$[0].title", is(first.getTitle())))
                .andExpect(jsonPath("$[0].blurb", is(first.getBlurb())))
                .andExpect(jsonPath("$[0].releaseYear", is(first.getReleaseYear())))
                .andExpect(jsonPath("$[0].runtime", is(first.getRuntime())))
                .andExpect(jsonPath("$[1].id", is(second.getId())))
                .andExpect(jsonPath("$[1].title", is(second.getTitle())))
                .andExpect(jsonPath("$[1].blurb", is(second.getBlurb())))
                .andExpect(jsonPath("$[1].releaseYear", is(second.getReleaseYear())))
                .andExpect(jsonPath("$[1].runtime", is(second.getRuntime())));
    }
}
