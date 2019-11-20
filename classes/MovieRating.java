package classes;

// Each MovieRating includes the movie id and a rating
public class MovieRating implements Comparable<MovieRating>{
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

    @Override
    public int compareTo(MovieRating m) {
        if (this.rating > m.rating) {
            return -1;
        } else if (this.rating < m.rating) {
            return 1;
        } else {
            return 0;
        }
        
    }

    @Override
    public String toString() {
        return this.movieId + " was rated as " + this.rating;
    }
}