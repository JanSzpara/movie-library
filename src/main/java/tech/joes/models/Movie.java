package tech.joes.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "library", type = "movies")
public class Movie {

    @Id
    private String id;

    private String title;

    private int releaseYear;

    private int runtime;

    private String blurb;

    public Movie(){
    }

    public Movie(String title, int releaseYear, int runtime, String blurb) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.runtime = runtime;
        this.blurb = blurb;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }
}
