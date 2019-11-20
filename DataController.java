import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;

import classes.Movie;
import classes.MovieRating;
import classes.PersonRating;
import classes.MovieNightUser;
import classes.UserSimilarity;

public class DataController {

    // MovieNightUsers (name is the HashMap key)
    private HashMap<String, MovieNightUser> movieNightUsers;

    // movies and their ratings in the dataset (movieId is the HashMap key)
    // for each movie in the dataset the rating is saved
    // movieId is the 'outer' HashMap key.
    // for the 'inner' HashMap the user id is the key
    private HashMap<Integer, HashMap<Integer, PersonRating>> movieRatings;

    // movies and their titles (movieId is the HashMap key)
    private HashMap<Integer, Movie> movies;

    // users and which movies they have rated (userId is the HashMap key)
    private HashMap<Integer, HashMap<Integer, MovieRating>> userRatings;

    // MovieNight users and which movies they have rated (name is the HashMap key)
    // private HashMap<String, ArrayList<MovieRating>> movieNightUserRatings;

    // MovieNight users and the genres they prefer (name is the HashMap key)
    // private HashMap<String, String> movieNightUserGenres;

    public DataController() {
        this.movieRatings = new HashMap<>();
        this.movies = new HashMap<>();
        this.userRatings = new HashMap<>();
        //this.movieNightUserRatings = new HashMap<>();
        //this.movieNightUserGenres = new HashMap<>();
        this.movieNightUsers = new HashMap<>();

        // let's load the ratings (userId + movieId + rating)
        loadRatingsData();

        // let's load movie data (movieId + title)
        loadMovieData();

        // let's add a couple of test users for the system
        HashMap<Integer, MovieRating> list1 = new HashMap<>();
        list1.put(1, new MovieRating(1, 2.5)); // Toy Story
        list1.put(2, new MovieRating(2, 4)); // Jumanji
        list1.put(19, new MovieRating(19, 5)); // Ace Ventura
        list1.put(32, new MovieRating(32, 3.5)); // 12 Monkeys
        list1.put(48, new MovieRating(48, 0.5)); // Pocahontas
        list1.put(224, new MovieRating(224, 2.0)); // Don Juan DeMarco
        list1.put(949, new MovieRating(949, 1.5)); // East of Eden
        //this.movieNightUserRatings.put("Lassi", list1);
        //this.movieNightUserGenres.put("Lassi", "Romance");

        this.movieNightUsers.put("Lassi", new MovieNightUser("Lassi", list1, "Romance"));

        HashMap<Integer, MovieRating> list2 = new HashMap<>();
        list2.put(1, new MovieRating(1, 4)); // Toy Story
        list2.put(19, new MovieRating(19, 2)); // Ace Venture
        list2.put(48, new MovieRating(48, 4.5)); // Pocahontas
        list2.put(79132, new MovieRating(79132, 3)); // Inception
        list2.put(193609, new MovieRating(193609, 0.5)); // Andrew Dice Clay
        list2.put(2085, new MovieRating(2085, 4)); // 101 Dalmatians
        list2.put(2382, new MovieRating(2382, 2.5)); // Police Academy 5
        //this.movieNightUserRatings.put("Leevi", list2);
        //this.movieNightUserGenres.put("Leevi", "Drama");

        this.movieNightUsers.put("Leevi", new MovieNightUser("Leevi", list2, "Drama"));

        HashMap<Integer, MovieRating> list3 = new HashMap<>();
        list3.put(1, new MovieRating(1, 2.5)); // Toy Story
        list3.put(19, new MovieRating(19, 3)); // Ace Venture
        list3.put(189713, new MovieRating(189713, 3.5)); // BlacKkKlansman
        list3.put(32, new MovieRating(32, 5)); // 12 Monkeys
        list3.put(104, new MovieRating(104, 5)); // Happy Gilmore
        list3.put(1721, new MovieRating(1721, 4.5)); // Titanic
        list3.put(1717, new MovieRating(1717, 5.0)); // Scream 2
        //this.movieNightUserRatings.put("Karvinen", list3);
        //this.movieNightUserGenres.put("Karvinen", "Drama");

        this.movieNightUsers.put("Karvinen", new MovieNightUser("Karvinen", list3, "Drama"));
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
                    HashMap<Integer, PersonRating> persons = this.movieRatings.get(itemId);
                    persons.put(personId, new PersonRating(personId, rating));
                    this.movieRatings.put(itemId, persons);
                } else {
                    HashMap<Integer, PersonRating> persons = new HashMap<>();
                    persons.put(personId, new PersonRating(personId, rating));
                    this.movieRatings.put(itemId, persons);
                }

                // let's link the movie to the user (userRatings)
                if (this.userRatings.containsKey(personId)) {
                    HashMap<Integer, MovieRating> movies = this.userRatings.get(personId);
                    movies.put(itemId, new MovieRating(itemId, rating));
                    this.userRatings.put(personId, movies);
                } else {
                    HashMap<Integer, MovieRating> movies = new HashMap<>();
                    movies.put(itemId, new MovieRating(itemId, rating));
                    this.userRatings.put(personId, movies);
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

    public HashMap<Integer, HashSet<Integer>> getSimilarUsers(String name) {
        HashMap<Integer, HashSet<Integer>> similarUsers = new HashMap<>();
        HashSet<Integer> s = new HashSet<>();

        // let's add all movies the user 'name' has rated to the set 's'
        // for (MovieRating m : this.movieNightUserRatings.get(name)) {
        for (MovieRating m : this.movieNightUsers.get(name).getMovieRatings().values()) {
            s.add(m.getMovieId());
        }

        // let's check which users in the data set have rated the same movies
        // NOTE at least 4 movies have to be the same
        for (Integer i : this.userRatings.keySet()) {
            HashSet<Integer> s2 = new HashSet<>();

            // let's add all movies the user(Id) 'i' has rated to the set 's2'
            for (MovieRating m2 : this.userRatings.get(i).values()) {
                s2.add(m2.getMovieId());
            }

            // s2.retainAll(s) removes from set 's2' all the items that are not included in
            // set 's'
            // so after that s2 only has common items between 's2' and 's'
            s2.retainAll(s);
            if (s2.size() > 3) {
                similarUsers.put(i, s2);
            }
        }   

        PriorityQueue<UserSimilarity> pq = new PriorityQueue<>();
        for (int i : similarUsers.keySet()) {
            UserSimilarity u = calculatePearson(name, i, similarUsers.get(i));
            pq.add(u);
        }
        this.movieNightUsers.get(name).setUserSimilarity(pq);

        while(!pq.isEmpty()) { 
            System.out.println(pq.poll()); 
        } 

        // A HashMap is returned with userId-HashSet key-value pairs
        // the HashSet contains the common items between userId and MovieNight user
        // 'name'
        // HashSet is the set 's2' above
        return similarUsers;
    }

    public UserSimilarity calculatePearson(String name, Integer userId, HashSet<Integer> movies) {

        int numberOfRatings1 = 0;
        int sum1 = 0;
        for (MovieRating r1 : this.movieNightUsers.get(name).getMovieRatings().values()) {
            numberOfRatings1 ++;
            sum1 += r1.getRating();
            }
        double average1 = new Double(sum1) / numberOfRatings1;

        int numberOfRatings2 = 0;
        int sum2 = 0;
        for (MovieRating r2 : userRatings.get(userId).values()) {
            numberOfRatings2 ++;
            sum2 += r2.getRating();
            }
        double average2 = new Double(sum2) / numberOfRatings2; 

        double sumPearson_1 = 0;
        double sumPearson_a = 0;
        double sumPearson_b = 0;
        for (int i : movies) {
            sumPearson_1 += (this.movieNightUsers.get(name).getMovieRatings().get(i).getRating() - average1) * (this.userRatings.get(userId).get(i).getRating() - average2);
            sumPearson_a += Math.pow((this.movieNightUsers.get(name).getMovieRatings().get(i).getRating() - average1), 2) ;
            sumPearson_b += Math.pow((this.userRatings.get(userId).get(i).getRating() - average2), 2);
        }
        double sim = sumPearson_1 / ( (Math.pow(sumPearson_a, 0.5) * Math.pow(sumPearson_b, 0.5)) ); 
        
        return new UserSimilarity(userId, sim);
    }

    public HashMap<Integer, HashMap<Integer, PersonRating>> getMovieRatings() {
        return this.movieRatings;
    }

    public HashMap<Integer, Movie> getMovies() {
        return this.movies;
    }

    public HashMap<Integer, HashMap<Integer, MovieRating>> getUserRatings() {
        return this.userRatings;
    }

    public HashMap<String, MovieNightUser> getMovieNightUsers() {
        return this.movieNightUsers;
    }

    //public HashMap<String, ArrayList<MovieRating>> getMovieNightUserRatings() {
    //    return this.movieNightUserRatings;
    //}
}
