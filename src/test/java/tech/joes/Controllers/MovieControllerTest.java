package tech.joes.Controllers;


import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tech.joes.Application;
import tech.joes.Models.Movie;
import tech.joes.Repositories.MovieRepository;
import tech.joes.Serilaizers.MovieTestSerializer;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class MovieControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    MovieRepository mockMovieRepository;

    @InjectMocks
    private MovieController movieController;

    private MockMvc mockMvc;

    private Gson gson;
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

    /*
    * Generate numItems random Movie objects
    * */
    private ArrayList<Movie> getDummyData(int numItems) {
        Faker faker = new Faker();
        ArrayList<Movie> dummyData = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            dummyData.add(new Movie(faker.lorem().word(), faker.number().numberBetween(1970, 2017), faker.number().numberBetween(1, 9999), faker.lorem().paragraph()));
        }

        return dummyData;
    }


    /*
    *   Tests that calling /movies/ returns all available movies in the repository
    * */
    @Test
    public void test_movies_endpoint_returns_all_available() throws Exception {

        int numDummyData = 5;

        ArrayList<Movie> dummyData = getDummyData(numDummyData);
        when(mockMovieRepository.findAll()).thenReturn(dummyData);


        mockMvc.perform(get("/movies/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(numDummyData)));

        //Clear the movie repo for next test
        mockMovieRepository.deleteAll();
    }

    /*
    *   Tests that calling /movies/{id} returns the correct item from the repository
    * */
    @Test
    public void test_single_movie_access_returns_correct_movie() throws Exception {

        int numDummyData = 5;
        int indexToTest = 2;
        ArrayList<Movie> dummyData = getDummyData(numDummyData);

        Movie expectedResult = dummyData.get(indexToTest);


        when(mockMovieRepository.findOne(indexToTest)).thenReturn(expectedResult);


        mockMvc.perform(get("/movies/" + indexToTest))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id", is(expectedResult.getId())))
                .andExpect(jsonPath("$.title", is(expectedResult.getTitle())))
                .andExpect(jsonPath("$.blurb", is(expectedResult.getBlurb())))
                .andExpect(jsonPath("$.releaseYear", is(expectedResult.getReleaseYear())))
                .andExpect(jsonPath("$.runtime", is(expectedResult.getRuntime())));

        //Clear the movie repo for next test
        mockMovieRepository.deleteAll();
    }

    /*
    *   Tests that calling /movies/releaseYear/{year} returns the correct items from the repository
    * */
    @Test
    public void test_movies_by_year() throws Exception {

        int numDummyData = 5;
        ArrayList<Movie> dummyData = getDummyData(numDummyData);

        int year = 1965; // As we cannot guarantee the random years we will get - use one that is outside random range

        Movie first = dummyData.get(2);
        Movie second = dummyData.get(3);

        first.setReleaseYear(year); // Set two of the dummies to have the desired year
        second.setReleaseYear(year);


        ArrayList<Movie> expectedResult = new ArrayList<>();
        expectedResult.add(first);
        expectedResult.add(second);

        when(mockMovieRepository.findMoviesByReleaseYear(year)).thenReturn(expectedResult);


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

        //Clear the movie repo for next test
        mockMovieRepository.deleteAll();
    }

    /*
    *   Tests that adding a new Movie object works
    * */
    @Test
    public void test_create_new_movie() throws Exception {

        Movie newMovie = getDummyData(1).get(0);
        final String jsonData = gson.toJson(newMovie);

        when(mockMovieRepository.save(newMovie)).thenReturn(newMovie);
        mockMvc.perform(post("/movies/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isCreated());


        //Clear the movie repo for next test
        mockMovieRepository.deleteAll();
    }

    /*
    *   Tests that updating the attributes of a movie works
    * */
    @Test
    public void test_update_existing_movie() throws Exception {
        int numDummyData = 5;
        int indexToTest = 2;
        ArrayList<Movie> dummyData = getDummyData(numDummyData);
        Movie expectedResult = dummyData.get(indexToTest);

        expectedResult.setReleaseYear(1940);
        expectedResult.setRuntime(1234);
        expectedResult.setBlurb("Lorem Ipsum");
        expectedResult.setTitle("Altered title");

        String jsonData = gson.toJson(dummyData.get(indexToTest));



        when(mockMovieRepository.findOne(indexToTest)).thenReturn(expectedResult);


        mockMvc.perform(put("/movies/" + indexToTest)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id", is(expectedResult.getId())))
                .andExpect(jsonPath("$.title", is(expectedResult.getTitle())))
                .andExpect(jsonPath("$.blurb", is(expectedResult.getBlurb())))
                .andExpect(jsonPath("$.releaseYear", is(expectedResult.getReleaseYear())))
                .andExpect(jsonPath("$.runtime", is(expectedResult.getRuntime())));

        //Clear the movie repo for next test
        mockMovieRepository.deleteAll();

    }

}
