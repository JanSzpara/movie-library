package tech.joes.Models;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by joe on 05/04/2017.
 */

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "release_date", nullable = false)
    int releaseYear;

    @Column(name = "runtime", nullable = false)
    int runtime;

    @Column(name = "blurb", nullable = false)
    String blurb;


    //Default constructor for hibernate
    public Movie() {
    }

    public Movie(String title, int releaseYear, int runtime, String blurb) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.runtime = runtime;
        this.blurb = blurb;
    }

    public int getId() {
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
