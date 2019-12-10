package components;

import classes.Movie;

import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class DataController {

    // movies and their ratings in the dataset (movieId is the HashMap key)
    // for each movie in the dataset the rating is saved
    // movieId is the 'outer' HashMap key.
    // for the 'inner' HashMap the user id is the key
    private HashMap<Integer, HashMap<Integer, Double>> movieRatings;

    // movies and their titles (movieId is the HashMap key)
    private HashMap<Integer, Movie> movies;

    // users and which movies they have rated (userId is the HashMap key)
    private HashMap<Integer, HashMap<Integer, Double>> userRatings;
    
    public DataController() {
        this.movieRatings = new HashMap<>();
        this.movies = new HashMap<>();
        this.userRatings = new HashMap<>();
    }

    public HashMap<Integer, HashMap<Integer, Double>> getMovieRatings() {
        return this.movieRatings;
    }

    public void setMovieRatings(HashMap<Integer, HashMap<Integer, Double>> ratings) {
        this.movieRatings = ratings;
    }

    public HashMap<Integer, Movie> getMovies() {
        return this.movies;
    }

    public void setMovies(HashMap<Integer, Movie> movies) {
        this.movies = movies;
    }

    public HashMap<Integer, HashMap<Integer, Double>> getUserRatings() {
        return this.userRatings;
    }

    public void setUserRatings(HashMap<Integer, HashMap<Integer, Double>> ratings) {
        this.userRatings = ratings;
    }

    // this function loads the ratings (userId + movieId + rating)
    public void loadRatingsData() {
        // let's create a Scanner to load the data from File
        // the data includes movie ratings in following way
        // 'userid' 'item' 'id' 'rating' 'time stamp'
        // (we are not interested in the time stamp)
        try (Scanner dataReader = new Scanner(new File("data/ratings.csv"))) {
            // we will ignore the first one as it contains the "column names"
            dataReader.nextLine();

            // let's read the file line by line
            while (dataReader.hasNextLine()) {
                String[] parts = dataReader.nextLine().split(",");

                int personId = Integer.parseInt(parts[0]);
                int itemId = Integer.parseInt(parts[1]);
                double rating = Double.parseDouble(parts[2]);

                // let's add the rating to movieRatings
                if (this.movieRatings.containsKey(itemId)) {
                    HashMap<Integer, Double> persons = this.movieRatings.get(itemId);
                    persons.put(personId, rating);
                    this.movieRatings.put(itemId, persons);
                } else {
                    HashMap<Integer, Double> persons = new HashMap<>();
                    persons.put(personId, rating);
                    this.movieRatings.put(itemId, persons);
                }

                // let's link the movie to the user (userRatings)
                if (this.userRatings.containsKey(personId)) {
                    HashMap<Integer, Double> movies = this.userRatings.get(personId);
                    movies.put(itemId, rating);
                    this.userRatings.put(personId, movies);
                } else {
                    HashMap<Integer, Double> movies = new HashMap<>();
                    movies.put(itemId, rating);
                    this.userRatings.put(personId, movies);
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
        // (at the moment we are only interested in the movie id, movie title and movie
        // genres)
        try (Scanner dataReader = new Scanner(new File("data/movies.csv"))) {
            // we will ignore the first one as it contains the column names
            dataReader.nextLine();

            // let's read the file line by line
            while (dataReader.hasNextLine()) {
                String[] parts = dataReader.nextLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                int movieId = Integer.parseInt(parts[0]);
                String title = parts[1];

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