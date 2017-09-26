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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
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
    public void getAllMoviesEndpointReturnsAllAvailable() throws Exception {
        //given
        Page<Movie> pageData = new PageImpl<>(dummyData);
        //when
        when(mockMovieRepository.findAll()).thenReturn(pageData);
        //then
        mockMvc.perform(get("/movies/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(dummyData.size())));
    }

    @Test
    public void getSingleMovieEndpointReturnsCorrectMovie() throws Exception {
        //given
        Integer indexToTest = 2;
        Movie expectedResult = dummyData.get(indexToTest - 1);
        //when
        when(mockMovieRepository.findOne(indexToTest.toString())).thenReturn(expectedResult);
        //then
        mockMvc.perform(get("/movies/" + indexToTest))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id", is(expectedResult.getId())))
                .andExpect(jsonPath("$.title", is(expectedResult.getTitle())))
                .andExpect(jsonPath("$.blurb", is(expectedResult.getBlurb())))
                .andExpect(jsonPath("$.releaseYear", is(expectedResult.getReleaseYear())))
                .andExpect(jsonPath("$.runtime", is(expectedResult.getRuntime())));
    }

    @Test
    public void getMoviesByReleaseYearEndpointReturnsCorrectMovies() throws Exception {
        //given
        int year = 1965; // As we cannot guarantee the random years we will get - use one that is outside random range

        Movie first = dummyData.get(2);
        Movie second = dummyData.get(3);
        Movie third = dummyData.get(3);

        first.setReleaseYear(year); // Set two of the dummies to have the desired year
        second.setReleaseYear(year);
        third.setReleaseYear(year + 1);

        List<Movie> expectedResult = Arrays.asList(first, second, third);
        //when
        when(mockMovieRepository.findMoviesByReleaseYear(year)).thenReturn(expectedResult);
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
    public void createNewMovieEndpointsCreates() throws Exception {
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
    public void updateExistingMovieEndpointsUpdates() throws Exception {
        //given
        Integer indexToTest = 2;
        Movie updatedMovie = dummyData.get(indexToTest - 1);
        String jsonData = gson.toJson(updatedMovie);
        //when
        when(mockMovieRepository.findOne(indexToTest.toString())).thenReturn(updatedMovie);
        when(mockMovieRepository.save(updatedMovie)).thenReturn(updatedMovie);
        //then
        mockMvc.perform(put("/movies/" + indexToTest)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteMovieEndpointDelets() throws Exception {
        //given
        Integer indexToTest = 2;
        //when
        when(mockMovieRepository.findOne(indexToTest.toString())).thenReturn(dummyData.get(indexToTest - 1));
        //then
        mockMvc.perform(delete("/movies/" + indexToTest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getMoviesByRuntimeInBetweenReturnsCorrectMovies() throws Exception {
        //given
        int fromTime = 1;
        int toTime = 4;
        List<Movie> expectedResult = dummyData;
        Movie first = expectedResult.get(0);
        Movie second = expectedResult.get(1);
        //when
        when(mockMovieRepository.findMoviesByRuntimeIsBetween(fromTime, toTime)).thenReturn(expectedResult);
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
    public void getMoviesByTitleOrBlurbContainingKeywordReturnsCorrectMovies() throws Exception {
        //given
        String keyword = "keyword";
        List<Movie> expectedResult = dummyData;
        Movie first = expectedResult.get(0);
        Movie second = expectedResult.get(1);
        //when
        when(mockMovieRepository.findMoviesByTitleContainingOrBlurbContaining(keyword, keyword)).thenReturn(expectedResult);
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
