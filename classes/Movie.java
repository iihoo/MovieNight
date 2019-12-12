package classes;

import java.util.ArrayList;

// Each movie has a title and a list of genres
public class Movie {
    private String title;
    private ArrayList<String> genres;

    public Movie(String title, ArrayList<String> genres) {
        this.title = title;
        this.genres = genres;
    }

    public void setTitle(String t) {
        this.title = t;
    }

    public String getTitle() {
        return this.title;
    }

    public ArrayList<String> getGenres() {
        return this.genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return this.title;
    }
}