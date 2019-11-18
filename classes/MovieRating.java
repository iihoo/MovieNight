package classes;

public class MovieRating {
    private int movieId;
    private double rating;

    public MovieRating(int m, double r) {
        this.movieId = m;
        this.rating = r;
    }

    public double getRating() {
        return this.rating;
    }

    public int getMovieId() {
        return this.movieId;
    }

    public void setRating(int r) {
        this.rating = r;
    }

    public void setMovieId(int p) {
        this.movieId = p;
    }

    public String toString() {
        return this.movieId + " was rated as " + this.rating;
    }
}