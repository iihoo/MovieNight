package classes;

import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.HashMap;

public class MovieNightUser {
    private String nimi;                                    // -- name of the user
    private String genre;                                   // -- the preferred genre
    private HashMap<Integer, MovieRating> movieratings;     // -- rated movies, movie id is the key for hashmap
    private PriorityQueue<UserSimilarity> userSimilarity;   // -- for similar users the calculated similarity value is saved here in order
    private HashSet<Integer> moviesNotSeen;                 // -- all the movies that MovieNight user has not seen 
                                                            //    (from the set of movies that his/her similar users have rated)
    private PriorityQueue<MovieRating> recommendedMovies;   // -- the movies from moviesNotSeen with predicted ratings for the MovieNightUser

    public MovieNightUser(String nimi, HashMap<Integer, MovieRating> movieratings, String genre) {
        this.nimi = nimi;
        this.movieratings = movieratings;
        this.genre = genre;
        this.userSimilarity = new PriorityQueue<>();
        this.moviesNotSeen = new HashSet<>();
        this.recommendedMovies = new PriorityQueue<>();
    }

    // lisää funktiot getNimi, setNimi, getLista, setLista jne
    public String getNimi() {
        return this.nimi;
    }

    public HashMap<Integer, MovieRating> getMovieRatings() {
        return this.movieratings;
    }

    public String getGenre() {
        return this.genre;
    }

    public PriorityQueue<UserSimilarity> getUserSimilarity() {
        return this.userSimilarity;
    }

    public void setUserSimilarity(PriorityQueue<UserSimilarity> u) {
        this.userSimilarity = u;
    }

    public HashSet<Integer> getMoviesNotSeen() {
        return this.moviesNotSeen;
    }

    public PriorityQueue<MovieRating> getRecommendedMovies() {
        return this.recommendedMovies;
    }

}