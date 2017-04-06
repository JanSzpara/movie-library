package tech.joes.Serilaizers;

import com.google.gson.*;
import tech.joes.Models.Movie;

import java.lang.reflect.Type;

/**
 * Created by joe on 06/04/2017.
 *
 * Serializes the movie object without an ID field so it can be used for posting/putting
 */
public class MovieTestSerializer implements JsonSerializer<Movie> {

    @Override
    public JsonElement serialize(Movie product, Type type, JsonSerializationContext jsc) {
        JsonObject jObj = (JsonObject) new GsonBuilder().create().toJsonTree(product);
        jObj.remove("id");
        return jObj;
    }
}

