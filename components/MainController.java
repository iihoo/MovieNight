package components;

import classes.Movie;
import classes.MovieNightUser;
import classes.UserSimilarity;
import classes.MovieRating;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Comparator;

public class MainController {
    private DataController dataController;

    // MovieNightUsers (name is the HashMap key)
    private HashMap<String, MovieNightUser> movieNightUsers;

    // here the individual recommendation lists are saved
    private HashMap<String, PriorityQueue<MovieRating>> userRecommendationLists;
    
    // these are used in different calculations
    // default values are initialized but the user is provided with the possibility to adjust them

    // at least how many movies have to be in common with the MovieNightUser (default value = 4)
    // at least how many similar users each to-be-recommended movies should have (default value = 10) 
    // what is the weighing value that used when calculating the genre-weighted group recommendation list (default value = 0.5)
    // how many movies do you wish to see in the list (default value = 20)
    private int commonMovies;
    private int minimumNumberOfSimilarUsers;
    private double genreCoefficient;
    private int listSize;

    public MainController() {
        this.dataController = new DataController();
        this.movieNightUsers = new HashMap<>();
        this.userRecommendationLists = new HashMap<>();
        this.commonMovies = 4;
        this.minimumNumberOfSimilarUsers = 10;
        this.genreCoefficient = 0.5;
        this.listSize = 20;
    }

    public void loadData() {
        // let's load the ratings
        this.dataController.loadRatingsData();

        // let's load movie data
        this.dataController.loadMovieData();
    }

    public HashMap<String, MovieNightUser> getMovieNightUsers() {
        return this.movieNightUsers;
    }

    public void setMovieNightUsers(HashMap<String, MovieNightUser> users) {
        this.movieNightUsers = users;
    }

    public HashMap<String, PriorityQueue<MovieRating>> getUserRecommendationLists() {
        return this.userRecommendationLists;
    }

    public void setUserRecommendationLists(HashMap<String, PriorityQueue<MovieRating>> lists) {
        this.userRecommendationLists = lists;
    }

    public int getCommonMovies() {
        return this.commonMovies;
    }

    public void setCommonMovies(int n) {
        this.commonMovies = n;
    }

    public int getMinimumNumberOfSimilarUsers() {
        return this.minimumNumberOfSimilarUsers;
    }

    public void setMinimumNumberOfSimilarUsers(int n) {
        this.minimumNumberOfSimilarUsers = n;
    }

    public double getGenreCoefficient() {
        return this.genreCoefficient;
    }

    public void setGenreCoefficient(double c) {
        this.genreCoefficient = c;
    }

    public int getListSize() {
        return this.listSize;
    }

    public void setListSize(int s) {
        this.listSize = s;
    }

    public HashMap<Integer, HashMap<Integer, Double>> getMovieRatings() {
        return this.dataController.getMovieRatings();
    }

    public HashMap<Integer, Movie> getMovies() {
        return this.dataController.getMovies();
    }

    public void calculateSimilarUsers(String name) {
        HashMap<Integer, HashMap<Integer, Double>> userRatings = this.dataController.getUserRatings();
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
        for (Integer i : userRatings.keySet()) {
            // 'commonMovies' is a set of movies that are common with the two users
            // 'uncommonMovies' is a set of movies that are not seen by the MovieNightUser
            HashSet<Integer> commonMovies = new HashSet<>();
            HashSet<Integer> uncommonMovies = new HashSet<>();

            // let's add all movies the user(Id) 'i' has rated to the set 'commonMovies' and
            // 'uncommonMovies'
            for (Integer i2 : userRatings.get(i).keySet()) {
                commonMovies.add(i2);
                uncommonMovies.add(i2);
            }

            // - commonMovies.retainAll(s) removes from set 'commonMovies' all the items that
            // are not included in set 's'
            // so after that 'commonMovies' only has common items between 'commonMovies' and 's'
            // - uncommonMovies.removeAll(s) removes from set 'uncommonMovies' all the items that
            // are included in set 's'
            // so after that 'uncommonMovies' only has items that are not seen by the
            // MovieNightUser (this is only done with users that have at least 'commonMovies' amount movies
            // in common with the MovieNightUser)
            commonMovies.retainAll(s);
            if (commonMovies.size() >= this.commonMovies) {
                similarUsers.put(i, commonMovies);
                uncommonMovies.removeAll(s);
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

        this.movieNightUsers.get(name).setUserSimilarity(pq);
    }

    public UserSimilarity calculatePearson(String name, Integer userId, HashSet<Integer> movies) {
        HashMap<Integer, HashMap<Integer, Double>> userRatings = this.dataController.getUserRatings();

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
                    * (userRatings.get(userId).get(i) - average2);
            sumPearson_a += Math.pow((this.movieNightUsers.get(name).getMovieRatings().get(i) - average1), 2);
            sumPearson_b += Math.pow((userRatings.get(userId).get(i) - average2), 2);
        }
        double sim = sumPearson_1 / ((Math.pow(sumPearson_a, 0.5) * Math.pow(sumPearson_b, 0.5)));

        return new UserSimilarity(userId, sim);
    }

    public void calculatePredictions(String movieNightUserName) {
        HashMap<Integer, HashMap<Integer, Double>> userRatings = this.dataController.getUserRatings();
        HashMap<Integer, HashMap<Integer, Double>> movieRatings = this.dataController.getMovieRatings();
        MovieNightUser movieNightUser = this.movieNightUsers.get(movieNightUserName);
        HashSet<Integer> moviesNotSeen = movieNightUser.getMoviesNotSeen();
        PriorityQueue<UserSimilarity> similarUsers = movieNightUser.getUserSimilarity();
        PriorityQueue<MovieRating> recommendedMovies = new PriorityQueue<>();

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
            // while loop goes through the priority queue until empty OR as long
            // as the similarity value is positive
            while (true) {
                UserSimilarity u = copyOfSimilarUsers.poll();
                if (u == null) {
                    break;
                } else if (u.getPearson() < 0) {
                    break;
                } else {
                    if (movieRatings.get(movieId).containsKey(u.getUserId())) {
                        topThreeSimilarUsers.add(u);
                    }
                }
            }

            // lasketaan arvosana niille elokuville, joille löytyy vähintään 10 samanlaista
            // käyttäjää
            // MovieNightUser-käyttäjän kanssa
            if (topThreeSimilarUsers.size() >= this.minimumNumberOfSimilarUsers) {
                double dividend = 0.0;
                double denominator = 0.0;
                for (UserSimilarity topUser : topThreeSimilarUsers) {
                    Double similarityValue = topUser.getPearson();
                    Double ratingValue = movieRatings.get(movieId).get(topUser.getUserId());

                    int numberOfUserRatings = 0;
                    double sumOfRatings = 0.0;
                    for (Double r : userRatings.get(topUser.getUserId()).values()) {
                        numberOfUserRatings++;
                        sumOfRatings += r;
                    }
                    double averagePrediction = sumOfRatings / numberOfUserRatings;
                    dividend += similarityValue * (ratingValue - averagePrediction);
                    denominator += similarityValue;
                }
                double prediction = avg + (dividend / denominator);
                if (prediction - 5 > 0) {
                    prediction = 5;
                }
                recommendedMovies.add(new MovieRating(movieId, prediction));
            }

        }

         //Here we can list the predictions
         //System.out.println("recommended movies: " + recommendedMovies.size());
         //int n = 0;
         //while (n < 25) {
         //if (recommendedMovies.isEmpty()) {
         //break;
         //}
         //MovieRating m = recommendedMovies.poll();
         //System.out.println("Prediction: " + m.getRating() + " for " + m.getMovieId()
         //+ " " + this.dataController.getMovies().get(m.getMovieId()).getGenres());
         //n++;
         //}

        // let's add the list to the collection of lists
        this.userRecommendationLists.put(movieNightUserName, recommendedMovies);
    }

    public void calculateRecommendationLists() {
        HashMap<Integer, Movie> movies = this.dataController.getMovies();
        
        System.out.println("\n*** *** *** Calculating recommendation lists.... *** *** ***");

        HashSet<String> genres = new HashSet<>();
        // max 50 movies are used from each individual recommendation list
        int amountOfMovies = 50;

        // we calculate the group recommendation list using borda count
        HashMap<Integer, Integer> groupRecommendationList = new HashMap<>();

        HashMap<String, PriorityQueue<MovieRating>> userRecommendationLists = this.userRecommendationLists;

        for (String name : this.movieNightUsers.keySet()) {
            // first the 'similarUsers' function is used to calculate similar users
            calculateSimilarUsers(name);

            // then the predictions are calculated for the MovieNightUser
            calculatePredictions(name);
            genres.add(this.movieNightUsers.get(name).getGenre());
        }

        for (String name : userRecommendationLists.keySet()) {
            PriorityQueue<MovieRating> x = this.userRecommendationLists.get(name);
            //System.out.println("recommended movies for " + name + ": " + x.size());
            if (x.size() < amountOfMovies) {
                amountOfMovies = x.size();
            }
        }
        //System.out.println("smallest list:" + amountOfMovies);

        for (String name : userRecommendationLists.keySet()) {
            PriorityQueue<MovieRating> x = this.userRecommendationLists.get(name);
            int n = 0;
            while (n < amountOfMovies) {
                if (x.isEmpty()) {
                    break;
                }
                MovieRating m = x.poll();
                if (groupRecommendationList.keySet().contains(m.getMovieId())) {
                    int oldValue = groupRecommendationList.get(m.getMovieId());
                    oldValue += (amountOfMovies - n);
                    groupRecommendationList.put(m.getMovieId(), oldValue);
                } else {
                    groupRecommendationList.put(m.getMovieId(), (amountOfMovies - n));
                }
                n++;
            }
        }

        HashMap<Integer, Integer> groupRecommendationListWithGenres = new HashMap<>(groupRecommendationList);

        for (int movieId : groupRecommendationListWithGenres.keySet()) {
            double extraPoints = 0;
            ArrayList<String> movieGenres = movies.get(movieId).getGenres();
            for (String s : movieGenres) {
                if (genres.contains(s)) {
                    extraPoints += this.genreCoefficient * amountOfMovies;
                }
            }

            if (extraPoints > 0) {
                int oldValue = groupRecommendationListWithGenres.get(movieId);
                oldValue += Math.round(extraPoints);
                groupRecommendationListWithGenres.put(movieId, oldValue);
            } else {
                int oldValue = groupRecommendationListWithGenres.get(movieId);
                oldValue -= Math.round(this.genreCoefficient * amountOfMovies);
                groupRecommendationListWithGenres.put(movieId, oldValue);
            }

        }

        HashMap<Integer, Integer> orderedList = sortGroupRecommendationList(groupRecommendationList);
        HashMap<Integer, Integer> orderedListWithGenres = sortGroupRecommendationList(groupRecommendationListWithGenres);
        GroupLists(orderedList, orderedListWithGenres);
    }

    public HashMap<Integer, Integer> sortGroupRecommendationList(HashMap<Integer, Integer> groupRecommendationList) {
        LinkedHashMap<Integer, Integer> orderedGroupRecommendationList = new LinkedHashMap<>();
        groupRecommendationList.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> orderedGroupRecommendationList.put(x.getKey(), x.getValue()));
        return orderedGroupRecommendationList;
    }

    public void GroupLists(HashMap<Integer, Integer> withoutGenre,
            HashMap<Integer, Integer> withGenre) {

        System.out.println("\n*** The calculation coefficients were: ");
        System.out.println(" * Minimum number of common movies rated between a MovieNightUser and a user X in the database: "+ this.commonMovies);
        System.out.println(" * Minimum number of similar users for each to-be-recommended movie: " + this.minimumNumberOfSimilarUsers);
        System.out.println(" * Genre coefficient for group recommendation list calculations: " + this.genreCoefficient);
        System.out.println(" * Recommendation list size (for printing): " + this.listSize);
        
        System.out.println("\n*** The MovieNight users and their genre preferences were:");
        for (String name : this.movieNightUsers.keySet()) {
            System.out.println(" * " + name + " prefers " + this.movieNightUsers.get(name).getGenre());
        }

        System.out.println("\n*** GROUP RECOMMENDATION LIST (method: borda count, genre-weighting: NO):");
        printList(withoutGenre);

        System.out.println("\n*** GROUP RECOMMENDATION LIST (method: borda count, genre-weighting: YES):");
        printList(withGenre);
    }

    public void printList(HashMap<Integer, Integer> list) {
        HashMap<Integer, Movie> movies = this.dataController.getMovies();
        int n = 1;
        for (int x : list.keySet()) {
            System.out.println("[" + n + "] " + list.get(x) + " points for "
                    + movies.get(x) + " " + movies.get(x).getGenres());
            if (n == this.listSize) {
                break;
            }
            n++;
        }
    }

}
