import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

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
        list1.add(new MovieRating(1, 2.5));     // Toy Story
        list1.add(new MovieRating(2, 4));       // Jumanji
        list1.add(new MovieRating(19, 5));      // Ace Ventura
        list1.add(new MovieRating(32, 3.5));    // 12 Monkeys
        list1.add(new MovieRating(48, 0.5));    // Pocahontas
        list1.add(new MovieRating(224, 2.0));   // Don Juan DeMarco
        list1.add(new MovieRating(949, 1.5));   // East of Eden
        this.movieNightUserRatings.put("Lassi", list1);
        this.movieNightUserGenres.put("Lassi", "Romance");

        ArrayList<MovieRating> list2 = new ArrayList<>();
        list2.add(new MovieRating(1, 4));           // Toy Story
        list2.add(new MovieRating(19, 2));          // Ace Venture
        list2.add(new MovieRating(48, 4.5));        // Pocahontas
        list2.add(new MovieRating(79132, 3));       // Inception
        list2.add(new MovieRating(193609, 0.5));    // Andrew Dice Clay
        list2.add(new MovieRating(2085, 4));        // 101 Dalmatians
        list2.add(new MovieRating(2382, 2.5));      // Police Academy 5
        this.movieNightUserRatings.put("Leevi", list2);
        this.movieNightUserGenres.put("Leevi", "Drama");

        ArrayList<MovieRating> list3 = new ArrayList<>();
        list3.add(new MovieRating(1, 2.5));         // Toy Story
        list3.add(new MovieRating(19, 3));          // Ace Venture
        list3.add(new MovieRating(189713, 3.5));    // BlacKkKlansman
        list3.add(new MovieRating(32, 5));          // 12 Monkeys
        list3.add(new MovieRating(104, 5));         // Happy Gilmore
        list3.add(new MovieRating(1721, 4.5));      // Titanic
        list3.add(new MovieRating(1717, 5.0));      // Scream 2
        this.movieNightUserRatings.put("Karvinen", list3);
        this.movieNightUserGenres.put("Karvinen", "Drama");
    }

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

    public HashMap<Integer, HashSet<Integer>> getSimilarUsers(String name) {
        HashMap<Integer, HashSet<Integer>> similarUsers = new HashMap<>();
        HashSet<Integer> s = new HashSet<>();

        // let's add all movies the user 'name' has rated to the set 's'
        for (MovieRating m : this.movieNightUserRatings.get(name)) {
            s.add(m.getMovieId());
        }

        // let's check which users in the data set have rated the same movies
        // NOTE at least 4 movies have to be the same 
        for (Integer i : this.userRatings.keySet()) {
            HashSet<Integer> s2 = new HashSet<>();

            // let's add all movies the user(Id) 'i' has rated to the set 's2'
            for (MovieRating m2 : this.userRatings.get(i)) {
                s2.add(m2.getMovieId());
            }

            // s2.retainAll(s) removes from set 's2' all the items that are not included in set 's'
            // so after that s2 only has common items between 's2' and 's'
            s2.retainAll(s);
            if (s2.size() > 3) {
                similarUsers.put(i, s2);
            }
        }

        // A HashMap is returned with userId-HashSet key-value pairs
        // the HashSet contains the common items between userId and MovieNight user 'name'
        // HashSet is the set 's2' above
        return similarUsers;
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
