package classes;

import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.HashMap;

public class MovieNightUser {
    private String nimi;                                    // -- name of the user
    private String genre;                                   // -- the preferred genre
    private HashMap<Integer, Double> movieratings;          // -- movie ratings by the user, movie id is the key for hashmap with ratings as values
    private PriorityQueue<UserSimilarity> userSimilarity;   // -- for similar users the calculated similarity value is saved here in descending order
    private HashSet<Integer> moviesNotSeen;                 // -- all the movies that MovieNight user has not seen 
                                                            //    (from the set of movies that his/her similar users have rated)
    private PriorityQueue<MovieRating> recommendedMovies;   // -- the movies from moviesNotSeen with predicted ratings for the MovieNightUser in descending order

    public MovieNightUser(String nimi, HashMap<Integer, Double> movieratings, String genre) {
        this.nimi = nimi;
        this.movieratings = movieratings;
        this.genre = genre;
        this.userSimilarity = new PriorityQueue<>();
        this.moviesNotSeen = new HashSet<>();
        this.recommendedMovies = new PriorityQueue<>();
    }

    public MovieNightUser(String nimi) {
        this.nimi = nimi;
        this.movieratings = new HashMap<>();
        this.userSimilarity = new PriorityQueue<>();
        this.moviesNotSeen = new HashSet<>();
        this.recommendedMovies = new PriorityQueue<>();
    }

    public void setNimi(String name) {
        this.nimi = name;
    }

    public String getNimi() {
        return this.nimi;
    }

    public void setMovieRatings(HashMap<Integer, Double> ratings) {
        this.movieratings = ratings;
    } 

    public HashMap<Integer, Double> getMovieRatings() {
        return this.movieratings;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setUserSimilarity(PriorityQueue<UserSimilarity> u) {
        this.userSimilarity = u;
    }

    public PriorityQueue<UserSimilarity> getUserSimilarity() {
        return this.userSimilarity;
    }

    public void setMoviesNotSeen(HashSet<Integer> moviesNotSeen) {
        this.moviesNotSeen = moviesNotSeen;
    }

    public HashSet<Integer> getMoviesNotSeen() {
        return this.moviesNotSeen;
    }

    public void setRecommendedMovies(PriorityQueue<MovieRating> movies) {
        this.recommendedMovies = movies;
    }

    public PriorityQueue<MovieRating> getRecommendedMovies() {
        return this.recommendedMovies;
    }

    public void addRating(int movieId, double rating) {
        this.movieratings.put(movieId, rating);
    }

}