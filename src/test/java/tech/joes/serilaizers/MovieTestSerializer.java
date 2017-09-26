package tech.joes.serilaizers;

import com.google.gson.*;
import tech.joes.models.Movie;

import java.lang.reflect.Type;

/**
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

