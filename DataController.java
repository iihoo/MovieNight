import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

import classes.Movie;
import classes.MovieRating;
import classes.PersonRating;

public class DataController {

    // movies and their ratings in the dataset (movieId is the HasHMap key)
    private HashMap<Integer, ArrayList<PersonRating>> movieRatings;

    // movies and their titles (movieId is the HashMap key)
    private HashMap<Integer, Movie> movies;

    // users and which movies they have rated (userId is the HashMap key)
    private HashMap<Integer, ArrayList<MovieRating>> userRatings;

    // MovieNight users and which movies they have rated (name is the HashMap key)
    private HashMap<String, ArrayList<MovieRating>> movieNightUserRatings;

    // MovieNight users and the genres they prefer (name is the HashMap key)
    private HashMap<String, String> movieNightUserGenres;

    public DataController() {
        this.movieRatings = new HashMap<>();
        this.movies = new HashMap<>();
        this.userRatings = new HashMap<>();
        this.movieNightUserRatings = new HashMap<>();
        this.movieNightUserGenres = new HashMap<>();

        // let's load the ratings (userId + movieId + rating)
        loadRatingsData();

        // let's load movie data (movieId + title)
        loadMovieData();

        // let's add a couple of test users for the system
        ArrayList<MovieRating> list1 = new ArrayList<>();
        list1.add(new MovieRating(1, 2.5));
        list1.add(new MovieRating(2, 4));
        list1.add(new MovieRating(19, 5));
        list1.add(new MovieRating(32, 3.5));
        list1.add(new MovieRating(48, 0.5));
        this.movieNightUserRatings.put("Lassi", list1);
        this.movieNightUserGenres.put("Lassi", "Romance");

        ArrayList<MovieRating> list2 = new ArrayList<>();
        list2.add(new MovieRating(1, 4));
        list2.add(new MovieRating(19, 2));
        list2.add(new MovieRating(48, 4.5));
        list2.add(new MovieRating(26, 3));
        list2.add(new MovieRating(193609, 0.5));
        this.movieNightUserRatings.put("Leevi", list2);
        this.movieNightUserGenres.put("Leevi", "Drama");

        ArrayList<MovieRating> list3 = new ArrayList<>();
        list3.add(new MovieRating(1, 2.5));
        list3.add(new MovieRating(19, 3));
        list3.add(new MovieRating(189713, 3.5));
        list3.add(new MovieRating(32, 5));
        list3.add(new MovieRating(193609, 5));
        list3.add(new MovieRating(179135, 4.5));
        this.movieNightUserRatings.put("Karvinen", list3);
        this.movieNightUserGenres.put("Karvinen", "Drama");
    }

    public void loadRatingsData() {
        // let's create a Scanner to load the data from File
        // the data includes movie ratings in following way
        // 'userid' 'item' 'id' 'rating' 'time stamp'
        // (we are not interested in the time stamp)
        try (Scanner dataReader = new Scanner(new File("ratings.csv"))) {
            // we will ignore the first one as it contains the "column names"
            dataReader.nextLine();

            // let's read the file line by line
            while (dataReader.hasNextLine()) {
                String[] parts = dataReader.nextLine().split(",");

                // System.out.println(parts[0] + " and " + parts[1] + " and " + parts[2] + " and
                // " + parts[3]);

                int personId = Integer.parseInt(parts[0]);
                int itemId = Integer.parseInt(parts[1]);
                double rating = Double.parseDouble(parts[2]);

                // let's add the rating to movieRatings
                if (this.movieRatings.containsKey(itemId)) {
                    ArrayList<PersonRating> list = this.movieRatings.get(itemId);
                    list.add(new PersonRating(personId, rating));
                    this.movieRatings.put(itemId, list);
                } else {
                    ArrayList<PersonRating> list = new ArrayList<>();
                    list.add(new PersonRating(personId, rating));
                    this.movieRatings.put(itemId, list);
                }

                // let's link the movie to the user (userRatings)
                if (this.userRatings.containsKey(personId)) {
                    ArrayList<MovieRating> list = this.userRatings.get(personId);
                    list.add(new MovieRating(itemId, rating));
                    this.userRatings.put(personId, list);
                } else {
                    ArrayList<MovieRating> list = new ArrayList<>();
                    list.add(new MovieRating(itemId, rating));
                    this.userRatings.put(personId, list);
                }
            }
            System.out.println();
            System.out.println("File read ('ratings.csv').");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void loadMovieData() {
        // let's create a Scanner to load the data from File
        // the data includes movie info in following way (whitespace/tab separated)
        // 'movie-id' 'movie title' 'genres'
        // (at the moment we are only interested in the movie id, movie title and movie genres)
        try (Scanner dataReader = new Scanner(new File("movies.csv"))) {
            // we will ignore the first one as it contains the column names
            dataReader.nextLine();
            
            // let's read the file line by line
            while (dataReader.hasNextLine()) {
                String[] parts = dataReader.nextLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                //System.out.println(parts[0] + " and " + parts[1] + " and " + parts[2]);

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

    public HashMap<Integer, ArrayList<PersonRating>> getMovieRatings() {
        return this.movieRatings;
    }

    public HashMap<Integer, Movie> getMovies() {
        return this.movies;
    }

    public HashMap<Integer, ArrayList<MovieRating>> getUserRatings() {
        return this.userRatings;
    } 

    public HashMap<String, ArrayList<MovieRating>> getMovieNightUserRatings() {
        return this.movieNightUserRatings;
    } 
}
