import java.util.ArrayList;

public class Movie {
    private String title;
    private ArrayList<String> genres;

    public Movie(String title, ArrayList<String> genres) {
        this.title = title;
        this.genres = genres;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String t) {
        this.title = t;
    }

    public ArrayList<String> getGenres() {
        return this.genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String toString() {
        String g = "";
        for (String s : this.genres) {
            g += s + " ";
        }
        return this.title + " contains the following genres: " + g;
    }
}