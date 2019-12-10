import java.util.Scanner;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Arrays;

import classes.Movie;
import classes.MovieNightUser;
import classes.MovieRating;

public class UserInterface {
    private DataController dataController;

    public UserInterface() {
        this.dataController = new DataController();
    }

    public void start() {

        // let's load the ratings
        this.dataController.loadRatingsData();

        // let's load movie data
        this.dataController.loadMovieData();

        // let's add a couple of test users for the system
        HashMap<String, MovieNightUser> movieNightUsers = new HashMap<>();

        HashMap<Integer, Double> list1 = new HashMap<>();
        list1.put(1, 2.5); // Toy Story
        list1.put(2, 4.0); // Jumanji
        list1.put(19, 5.0); // Ace Ventura
        list1.put(32, 3.5); // 12 Monkeys
        list1.put(48, 0.5); // Pocahontas
        list1.put(224, 2.0); // Don Juan DeMarco
        list1.put(949, 1.5); // East of Eden
        movieNightUsers.put("Tupu", new MovieNightUser("Tupu", list1, "Romance"));

        HashMap<Integer, Double> list2 = new HashMap<>();
        list2.put(1, 4.0); // Toy Story
        list2.put(19, 2.0); // Ace Venture
        list2.put(48, 4.5); // Pocahontas
        list2.put(79132, 3.0); // Inception
        list2.put(193609, 0.5); // Andrew Dice Clay
        list2.put(2085, 4.0); // 101 Dalmatians
        list2.put(2382, 2.5); // Police Academy 5
        movieNightUsers.put("Hupu", new MovieNightUser("Hupu", list2, "Drama"));

        HashMap<Integer, Double> list3 = new HashMap<>();
        list3.put(1, 2.5); // Toy Story
        list3.put(19, 3.0); // Ace Venture
        list3.put(189713, 3.5); // BlacKkKlansman
        list3.put(32, 5.0); // 12 Monkeys
        list3.put(104, 5.0); // Happy Gilmore
        list3.put(1721, 0.5); // Titanic
        list3.put(1717, 1.0); // Scream 2
        movieNightUsers.put("Lupu", new MovieNightUser("Lupu", list3, "Comedy"));

        this.dataController.setMovieNightUsers(movieNightUsers);

        // let's start the program
        Scanner inputReader = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("-----------------------------------------------");
            System.out.println("Commands: ");
            System.out.println(" x - stop the application");
            System.out.println(" 1 - show general info");
            System.out.println(" 2 - show ratings info for a specific movie");
            System.out.println(" 3 - show recommendation lists");
            System.out.println("-----------------------------------------------");
            System.out.println("");

            System.out.print("Your command: ");
            String command = inputReader.nextLine();
            if (command.equals("x")) {
                break;
            }

            if (command.equals("1")) {
                generalInfo(inputReader);
            } else if (command.equals("2")) {
                ratingsOfOneMovie(inputReader);
            } else if (command.equals("3")) {
                calculateRecommendationLists();
            } else {
                System.out.println("Unknown command. Try again. (press 'x' to stop the application)");
            }
        }
    }

    public void generalInfo(Scanner inputReader) {
        int n = this.dataController.getMovieRatings().size();

        System.out.println("\n *** The app contains ratings for " + n + " movies. \n");
        System.out.println("Do you want to list all the movies? (type 'yes' to continue)");
        String cmd = inputReader.nextLine();
        if (cmd.equals("yes")) {
            for (Integer movieId : this.dataController.getMovies().keySet()) {
                System.out.println("MovieId " + movieId + ": " + this.dataController.getMovies().get(movieId) + " --- GENRES:" + this.dataController.getMovies().get(movieId).getGenres());
            }
        }
    }

    public void ratingsOfOneMovie(Scanner inputReader) {
        HashMap<Integer, Movie> movies = this.dataController.getMovies();
        HashMap<Integer, HashMap<Integer, Double>> movieRatings = this.dataController.getMovieRatings();

        System.out.println();
        System.out.println("Which movie are you interested in?");
        System.out.print("Movie (id): ");

        String cmd = inputReader.nextLine();
        try {
            int movieId = Integer.parseInt(cmd);
            if (!movieRatings.containsKey(movieId)) {
                System.out.println("That movieId is not used in the database!");
            } else {
                int numberOfRatings = 0;
                double sumOfRatings = 0.0;
                for (Double rating : movieRatings.get(movieId).values()) {
                    numberOfRatings++;
                    sumOfRatings += rating;
                }
                double avg = sumOfRatings / numberOfRatings;

                System.out.println("\n*** MovieId " + movieId + " refers to: " + movies.get(movieId));
                System.out.println("*** There are " + numberOfRatings + " ratings for the movie ");
                System.out.println("*** The average score is " + String.format(Locale.US, "%.2f", avg));
                System.out.println("*** The ratings are the following:");

                for (Integer i : movieRatings.get(movieId).keySet()) {
                    System.out.println(
                            " * User " + i + " gave a rating of " + movieRatings.get(movieId).get(i));
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("You did not enter a number!");
        }
    }

    public void similarUsers(String name) {
        HashMap<Integer, HashSet<Integer>> similarUsers = this.dataController.getSimilarUsers(name);

        System.out.println("\n*** " + similarUsers.size() + " users have rated at least 4 movies that MovieNight user " + name + " also has rated");
        System.out.println("*** Similar users and their ratings are the following:");
        for (Integer i : similarUsers.keySet()) {
            HashSet<Integer> s = similarUsers.get(i);
            System.out.println(" * User " + i + " has rated movies " + Arrays.toString(s.toArray()));
        }

    }

    public void calculateRecommendationLists() {
        HashSet<String> genres = new HashSet<>();
        int amountOfMovies = Integer.MAX_VALUE;

        // we calculate the group recommendation list using borda count
        HashMap<Integer, Integer> groupRecommendationList = new HashMap<>();

        HashMap<String, PriorityQueue<MovieRating>> userRecommendationLists = this.dataController
                .getUserRecommendationLists();

        for (String name : this.dataController.getMovieNightUsers().keySet()) {
            // first the 'similarUsers' function is used to calculate similar users
            similarUsers(name);
            // then the predictions are calculated for the MovieNightUser
            this.dataController.calculatePredictions(name);
            genres.add(this.dataController.getMovieNightUsers().get(name).getGenre());
        }

        for (String name : userRecommendationLists.keySet()) {
            PriorityQueue<MovieRating> x = this.dataController.getUserRecommendationLists().get(name);
            System.out.println("recommended movies for " + name + ": " + x.size());
            if (x.size() < amountOfMovies) {
                amountOfMovies = x.size();
            }
        }
        System.out.println("smallest list:" + amountOfMovies);

        for (String name : userRecommendationLists.keySet()) {
            PriorityQueue<MovieRating> x = this.dataController.getUserRecommendationLists().get(name);
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

                System.out.println("Prediction: " + m.getRating() + " for " + m.getMovieId() + " "
                        + this.dataController.getMovies().get(m.getMovieId()).getGenres());
                n++;
            }
        }

        HashMap<Integer, Integer> groupRecommendationListWithGenres = new HashMap<>(groupRecommendationList);

        for (int movieId : groupRecommendationListWithGenres.keySet()) {
            double extraPoints = 0;
            ArrayList<String> movieGenres = this.dataController.getMovies().get(movieId).getGenres();
            for (String s : movieGenres) {
                if (genres.contains(s)) {
                    extraPoints += 0.5 * amountOfMovies;
                }
            }

            if (extraPoints > 0) {
                int oldValue = groupRecommendationListWithGenres.get(movieId);
                oldValue += Math.round(extraPoints);
                groupRecommendationListWithGenres.put(movieId, oldValue);
            } else {
                int oldValue = groupRecommendationListWithGenres.get(movieId);
                oldValue -= Math.round(0.5 * amountOfMovies);
                groupRecommendationListWithGenres.put(movieId, oldValue);
            }

        }

        LinkedHashMap<Integer, Integer> orderedGroupRecommendationList = new LinkedHashMap<>();
        groupRecommendationList.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> orderedGroupRecommendationList.put(x.getKey(), x.getValue()));

        LinkedHashMap<Integer, Integer> orderedGroupRecommendationListWithGenres = new LinkedHashMap<>();
        groupRecommendationListWithGenres.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> orderedGroupRecommendationListWithGenres.put(x.getKey(), x.getValue()));

        for (int x : orderedGroupRecommendationList.keySet()) {
            System.out.println("group prediction " + orderedGroupRecommendationList.get(x) + " for movie " + x + " "
                    + this.dataController.getMovies().get(x).getGenres());
        }

        for (int x : orderedGroupRecommendationListWithGenres.keySet()) {
            System.out.println("group prediction " + orderedGroupRecommendationListWithGenres.get(x) + " for movie " + x
                    + " " + this.dataController.getMovies().get(x).getGenres());
        }
        // System.out.println(orderedGroupRecommendationList);
    }

}