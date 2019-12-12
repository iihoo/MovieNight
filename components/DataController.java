package components;

import classes.Movie;

import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class DataController {

    // movies and their titles and genres (movie id is the HashMap key)
    private HashMap<Integer, Movie> movies;

    // movies and their ratings in the dataset
    // contains movies and which users have rated the movie (movie id is the 'outer' HashMap key)
    // for the 'inner' HashMap the user id is the key and the rating of the user is the value
    private HashMap<Integer, HashMap<Integer, Double>> movieRatings;

    // users and their ratings in the dataset
    // contains users and which movies they have rated (user id is the 'outer' HashMap key)
    // for the 'inner' HashMap the movie id is the key and the rating for that movie is the value
    private HashMap<Integer, HashMap<Integer, Double>> userRatings;

    public DataController() {
        this.movieRatings = new HashMap<>();
        this.movies = new HashMap<>();
        this.userRatings = new HashMap<>();
    }

    public void setMovieRatings(HashMap<Integer, HashMap<Integer, Double>> ratings) {
        this.movieRatings = ratings;
    }

    public HashMap<Integer, HashMap<Integer, Double>> getMovieRatings() {
        return this.movieRatings;
    }

    public void setMovies(HashMap<Integer, Movie> movies) {
        this.movies = movies;
    }

    public HashMap<Integer, Movie> getMovies() {
        return this.movies;
    }

    public void setUserRatings(HashMap<Integer, HashMap<Integer, Double>> ratings) {
        this.userRatings = ratings;
    }

    public HashMap<Integer, HashMap<Integer, Double>> getUserRatings() {
        return this.userRatings;
    }

    // this function loads the ratings from the database (userId + movieId + rating)
    public void loadRatingsData() {
        // let's create a Scanner to load the data from File
        // the data includes movie ratings in following way (comma-separated)
        // 'userid' 'item' 'id' 'rating' 'time stamp' (we are not interested in the time stamp)
        try (Scanner dataReader = new Scanner(new File("data/ratings.csv"))) {
            // we will ignore the first line as it contains the "column names"
            dataReader.nextLine();

            // let's read the file line by line
            while (dataReader.hasNextLine()) {
                String[] parts = dataReader.nextLine().split(",");

                int userId = Integer.parseInt(parts[0]);
                int movieId = Integer.parseInt(parts[1]);
                double rating = Double.parseDouble(parts[2]);

                // let's add the rating to movieRatings
                if (this.movieRatings.containsKey(movieId)) {
                    // if the HasHMap already includes the movie id, the rating is added for that movie
                    HashMap<Integer, Double> ratingsOfMovie = this.movieRatings.get(movieId);
                    ratingsOfMovie.put(userId, rating);
                    this.movieRatings.put(movieId, ratingsOfMovie);
                } else {
                    // if the HashMap does not include the movie id,
                    // a new key-value (userId-rating) pair is added to the HashMap
                    HashMap<Integer, Double> ratingsOfMovie = new HashMap<>();
                    ratingsOfMovie.put(userId, rating);
                    this.movieRatings.put(movieId, ratingsOfMovie);
                }

                // let's link the movie to the user (userRatings)
                if (this.userRatings.containsKey(userId)) {
                    // if the HasHMap already includes the userd id, the rating is added for that user
                    HashMap<Integer, Double> ratingsOfUser = this.userRatings.get(userId);
                    ratingsOfUser.put(movieId, rating);
                    this.userRatings.put(userId, ratingsOfUser);
                } else {
                    // if the HashMap does not include the user id,
                    // a new key-value (movieId-rating) pair is added to the HashMap
                    HashMap<Integer, Double> ratingsOfUser = new HashMap<>();
                    ratingsOfUser.put(movieId, rating);
                    this.userRatings.put(userId, ratingsOfUser);
                }
            }
            System.out.println();
            System.out.println("File read ('ratings.csv').");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // let's load movie data (movieId + title)
    public void loadMovieData() {
        // let's create a Scanner to load the data from File
        // the data includes movie info in following way (whitespace/tab separated)
        // 'movie-id' 'movie title' 'genres'
        try (Scanner dataReader = new Scanner(new File("data/movies.csv"))) {
            // we will ignore the first one as it contains the column names
            dataReader.nextLine();

            // let's read the file line by line
            while (dataReader.hasNextLine()) {
                // The movie names might include commas, but in such instances the movie name is in double quotes
                String[] parts = dataReader.nextLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                int movieId = Integer.parseInt(parts[0]);
                String title = parts[1];
                // the genres are separated by vertical bar symbol, and the are collected in to an ArrayList
                String[] g = parts[2].split("\\|");
                ArrayList<String> genres = new ArrayList<>(Arrays.asList(g));
                Movie movie = new Movie(title, genres);
                this.movies.put(movieId, movie);
            }
            System.out.println();
            System.out.println("File read ('movies.csv').");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}