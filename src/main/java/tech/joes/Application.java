package tech.joes;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tech.joes.Models.Movie;
import tech.joes.Repositories.MovieRepository;

import java.util.Date;

/**
 * Created by joe on 05/04/2017.
 */
@SpringBootApplication
public class Application {



    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner initializeDb(MovieRepository repository){
        return (args) -> {
            repository.deleteAll();
            //Create and insert some Movies with fake data
            for(int i = 0; i < 20; i++) {
                repository.save(new Movie("Test", new Date(), 23223,"Lorem Ipsum"));
            }
        };
    }

}