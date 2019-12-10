import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;

import classes.Movie;
import classes.MovieNightUser;
import classes.UserSimilarity;
import classes.MovieRating;

public class DataController {

    // MovieNightUsers (name is the HashMap key)
    private HashMap<String, MovieNightUser> movieNightUsers;

    // movies and their ratings in the dataset (movieId is the HashMap key)
    // for each movie in the dataset the rating is saved
    // movieId is the 'outer' HashMap key.
    // for the 'inner' HashMap the user id is the key
    private HashMap<Integer, HashMap<Integer, Double>> movieRatings;
    // private HashMap<Integer, HashMap<Integer, PersonRating>> movieRatings;

    // movies and their titles (movieId is the HashMap key)
    private HashMap<Integer, Movie> movies;

    // users and which movies they have rated (userId is the HashMap key)
    private HashMap<Integer, HashMap<Integer, Double>> userRatings;

    // here the individual recommendation lists are saved
    private HashMap<String, PriorityQueue<MovieRating>> userRecommendationLists;

    public HashMap<Integer, HashMap<Integer, Double>> getMovieRatings() {
        return this.movieRatings;
    }

    public HashMap<Integer, Movie> getMovies() {
        return this.movies;
    }

    public HashMap<Integer, HashMap<Integer, Double>> getUserRatings() {
        return this.userRatings;
    }

    public HashMap<String, MovieNightUser> getMovieNightUsers() {
        return this.movieNightUsers;
    }

    public HashMap<String, PriorityQueue<MovieRating>> getUserRecommendationLists() {
        return this.userRecommendationLists;
    }

    public DataController() {
        this.movieRatings = new HashMap<>();
        this.movies = new HashMap<>();
        this.userRatings = new HashMap<>();
        this.movieNightUsers = new HashMap<>();
        this.userRecommendationLists = new HashMap<>();

        // let's load the ratings (userId + movieId + rating)
        loadRatingsData();

        // let's load movie data (movieId + title)
        loadMovieData();

        // let's add a couple of test users for the system
        HashMap<Integer, Double> list1 = new HashMap<>();
        list1.put(1, 2.5); // Toy Story
        list1.put(2, 4.0); // Jumanji
        list1.put(19, 5.0); // Ace Ventura
        list1.put(32, 3.5); // 12 Monkeys
        list1.put(48, 0.5); // Pocahontas
        list1.put(224, 2.0); // Don Juan DeMarco
        list1.put(949, 1.5); // East of Eden

        this.movieNightUsers.put("Lassi", new MovieNightUser("Lassi", list1, "Romance"));

        HashMap<Integer, Double> list2 = new HashMap<>();
        list2.put(1, 4.0); // Toy Story
        list2.put(19, 2.0); // Ace Venture
        list2.put(48, 4.5); // Pocahontas
        list2.put(79132, 3.0); // Inception
        list2.put(193609, 0.5); // Andrew Dice Clay
        list2.put(2085, 4.0); // 101 Dalmatians
        list2.put(2382, 2.5); // Police Academy 5

        this.movieNightUsers.put("Leevi", new MovieNightUser("Leevi", list2, "Drama"));

        HashMap<Integer, Double> list3 = new HashMap<>();
        list3.put(1, 2.5); // Toy Story
        list3.put(19, 3.0); // Ace Venture
        list3.put(189713, 3.5); // BlacKkKlansman
        list3.put(32, 5.0); // 12 Monkeys
        list3.put(104, 5.0); // Happy Gilmore
        list3.put(1721, 0.5); // Titanic
        list3.put(1717, 1.0); // Scream 2

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
        HashSet<Integer> moviesNotSeen = new HashSet<>();
        HashSet<Integer> s = new HashSet<>();

        // let's add all movies the user 'name' has rated to the set 's'
        // for (MovieRating m : this.movieNightUserRatings.get(name)) {
        for (Integer i : this.movieNightUsers.get(name).getMovieRatings().keySet()) {
            s.add(i);
        }

        // let's check which users in the data set have rated the same movies
        // NOTE at least 4 movies have to be the same
        for (Integer i : this.userRatings.keySet()) {
            // 'commonMovies' is a set of movies that are common with the two users
            // 'uncommonMovies' is a set of movies that are not seen by the MovieNightUser
            HashSet<Integer> commonMovies = new HashSet<>();
            HashSet<Integer> uncommonMovies = new HashSet<>();

            // let's add all movies the user(Id) 'i' has rated to the set 'commonMovies' and
            // 'uncommonMovies'
            for (Integer i2 : this.userRatings.get(i).keySet()) {
                commonMovies.add(i2);
                uncommonMovies.add(i2);
            }

            // commonMovies.retainAll(s) removes from set 'commonMovies' all the items that
            // are not included in
            // set 's'
            // so after that 'commonMovies' only has common items between 'commonMovies' and
            // 's'
            // uncommonMovies.removeAll(s) removes from set 'uncommonMovies' all the items
            // that are included in set
            // 's'
            // so after that 'uncommonMovies' only has items that are not seen by the
            // MovieNightUser (this
            // is only
            // done with users that have at least 4 movies in common with the
            // MovieNightUser)
            commonMovies.retainAll(s);
            if (commonMovies.size() > 3) {
                similarUsers.put(i, commonMovies);
                uncommonMovies.removeAll(s);
                // System.out.println("common items " + commonMovies);
                // System.out.println("uncommon items " + uncommonMovies);
                moviesNotSeen.addAll(uncommonMovies);
            }

        }

        // finally we add all the movies not seen by the MovieNightUser to his/her
        // profile
        this.movieNightUsers.get(name).setMoviesNotSeen(moviesNotSeen);

        // for each similar user we calculate the similarity value and
        // these are saved to MovieNightUser in a PriorityQueue
        PriorityQueue<UserSimilarity> pq = new PriorityQueue<>();
        for (int i : similarUsers.keySet()) {
            UserSimilarity u = calculatePearson(name, i, similarUsers.get(i));
            pq.add(u);
        }

        // testing
        // System.out.println("ennen lisäystä jonon koko on " +
        // this.movieNightUsers.get(name).getUserSimilarity().size());
        this.movieNightUsers.get(name).setUserSimilarity(pq);
        // System.out.println("lisäyksen jälkeen jonon koko on " +
        // this.movieNightUsers.get(name).getUserSimilarity().size());

        // testing
        // System.out.println("testausta:");
        // while (!pq.isEmpty()) {
        // System.out.println(pq.poll());
        // }

        // A HashMap is returned with userId-HashSet key-value pairs
        // the HashSet contains the common items between userId and MovieNight user
        // 'name'
        // HashSet is the set 'commonMovies' above
        return similarUsers;
    }

    public UserSimilarity calculatePearson(String name, Integer userId, HashSet<Integer> movies) {

        int numberOfRatings1 = 0;
        double sum1 = 0.0;
        for (Double r1 : this.movieNightUsers.get(name).getMovieRatings().values()) {
            numberOfRatings1++;
            sum1 += r1;
        }
        double average1 = sum1 / numberOfRatings1;

        int numberOfRatings2 = 0;
        double sum2 = 0.0;
        for (Double r2 : userRatings.get(userId).values()) {
            numberOfRatings2++;
            sum2 += r2;
        }
        double average2 = sum2 / numberOfRatings2;

        double sumPearson_1 = 0;
        double sumPearson_a = 0;
        double sumPearson_b = 0;
        for (int i : movies) {
            sumPearson_1 += (this.movieNightUsers.get(name).getMovieRatings().get(i) - average1)
                    * (this.userRatings.get(userId).get(i) - average2);
            sumPearson_a += Math.pow((this.movieNightUsers.get(name).getMovieRatings().get(i) - average1), 2);
            sumPearson_b += Math.pow((this.userRatings.get(userId).get(i) - average2), 2);
        }
        double sim = sumPearson_1 / ((Math.pow(sumPearson_a, 0.5) * Math.pow(sumPearson_b, 0.5)));

        return new UserSimilarity(userId, sim);
    }

    public void calculatePredictions(String movieNightUserName) {
        // int top3 = 0;
        // int top2 = 0;
        // int top1 = 0;
        MovieNightUser movieNightUser = this.movieNightUsers.get(movieNightUserName);
        HashSet<Integer> moviesNotSeen = movieNightUser.getMoviesNotSeen();
        PriorityQueue<UserSimilarity> similarUsers = movieNightUser.getUserSimilarity();
        PriorityQueue<MovieRating> recommendedMovies = new PriorityQueue<>();

        // System.out.println("testausa: ");
        // System.out.println("moviesNotSeen " + moviesNotSeen.size());
        // System.out.println("similarUsers " + similarUsers.size());

        // System.out.println("testausta jono koko on:" + similarUsers.size());
        // while (!similarUsers.isEmpty()) {
        // System.out.println(similarUsers.poll());
        // }

        // first we calculate the average rating given by the MovieNightUser
        int numberOfRatings = 0;
        double sum = 0.0;
        for (Double r : movieNightUser.getMovieRatings().values()) {
            numberOfRatings++;
            sum += r;
        }
        double avg = sum / numberOfRatings;
        //System.out.println("average of " + movieNightUserName + " is: " + avg);

        for (Integer movieId : moviesNotSeen) {
            PriorityQueue<UserSimilarity> copyOfSimilarUsers = new PriorityQueue<>(similarUsers);
            HashSet<UserSimilarity> topThreeSimilarUsers = new HashSet<>();
            // while (topThreeSimilarUsers.size() < 10) {
            // while loop goest through the priority queue until empty OR as long
            // as the similarity value is positive
            while (true) {
                UserSimilarity u = copyOfSimilarUsers.poll();
                if (u == null) {
                    break;
                } else if (u.getPearson() < 0) {
                    break;
                } else {
                    if (this.movieRatings.get(movieId).containsKey(u.getUserId())) {
                        topThreeSimilarUsers.add(u);
                    }
                }
            }

            // testausta
            // nähdään kuinka monta erikokoista ryhmää löytyy
            // if (topThreeSimilarUsers.size() == 1) {
            // top1 ++;
            // } else if (topThreeSimilarUsers.size() == 2) {
            // top2++;
            // } else if (topThreeSimilarUsers.size() == 3) {
            // top3 ++;
            // }
            // System.out.println("3:n ryhmiä " + top3);
            // System.out.println("2:n ryhmiä " + top2);
            // System.out.println("1:n ryhmiä " + top1);

            // lasketaan arvosana niille elokuville, joille löytyy vähintään 10 samanlaista käyttäjää
            // MovieNightUser-käyttäjän kanssa
            if (topThreeSimilarUsers.size() > 9) {
                double dividend = 0.0;
                double denominator = 0.0;
                for (UserSimilarity topUser : topThreeSimilarUsers) {
                    Double similarityValue = topUser.getPearson();
                    Double ratingValue = this.movieRatings.get(movieId).get(topUser.getUserId());

                    int numberOfUserRatings = 0;
                    double sumOfRatings = 0.0;
                    for (Double r : this.userRatings.get(topUser.getUserId()).values()) {
                        numberOfUserRatings++;
                        sumOfRatings += r;
                    }
                    double averagePrediction = sumOfRatings / numberOfUserRatings;
                    dividend += similarityValue * (ratingValue - averagePrediction);
                    denominator += similarityValue;
                }
                // System.out.println();
                // System.out.println("new set, movie: " + i);
                // System.out.println("rating: " + ratingValue);
                // System.out.println("average: " + averagePrediction);
                // System.out.println("similarity: " + similarityValue);
                double prediction = avg + (dividend / denominator);
                if (prediction - 5 > 0) {
                    prediction = 5;
                }
                //System.out.println("prediction for movie " + movieId + " is " + prediction);
                recommendedMovies.add(new MovieRating(movieId, prediction));
            }

        }

        // Here we can list the predictions 
        //System.out.println("recommended movies: " + recommendedMovies.size());
        //int n = 0;
        //while (n < 25) {
        //    if (recommendedMovies.isEmpty()) {
        //        break;
        //   }
        //    MovieRating m = recommendedMovies.poll();
        //    System.out.println("Prediction: " + m.getRating() + " for " + m.getMovieId() + " " + this.movies.get(m.getMovieId()).getGenres());
        //    n++;
        //}

        // let's add the list to the collection of lists
        this.userRecommendationLists.put(movieNightUserName, recommendedMovies);
    }

}
