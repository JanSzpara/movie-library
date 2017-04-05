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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "release_date", nullable = false)
    Date releaseDate;

    @Column(name = "runtime", nullable = false)
    int runtime;

    @Column(name = "blurb", nullable = false)
    String blurb;


    //Default constructor for hibernate
    public Movie() {
    }

    public Movie(String title, Date releaseDate, int runtime, String blurb) {
        this.title = title;
        this.releaseDate = releaseDate;
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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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
