package tech.joes.generators;


import com.github.javafaker.Faker;
import tech.joes.models.Movie;

import java.util.ArrayList;

public class MovieFakeDataGenerator {
    public static ArrayList<Movie> getDummyData(int numItems) {
        Faker faker = new Faker();
        ArrayList<Movie> dummyData = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            dummyData.add(new Movie(faker.lorem().word(), faker.number().numberBetween(1970, 2017), faker.number().numberBetween(1, 9999), faker.lorem().paragraph()));
        }
        return dummyData;
    }
}
