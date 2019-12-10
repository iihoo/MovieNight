import java.util.Scanner;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.ArrayList;

import classes.Movie;
import classes.MovieNightUser;
import classes.MovieRating;

public class UserInterface {
    private DataController dataController;

    public UserInterface() {
        this.dataController = new DataController();
    }

    public void start() {
        Scanner inputReader = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("-------------------------------------");
            System.out.println("Commands: ");
            System.out.println(" x - stop the application");
            System.out.println(" 1 - show general ratings info");
            System.out.println(" 2 - show list of movies in the database");
            System.out.println(" 3 - show ratings info for a specific movie");
            System.out.println(" 4 - show ratings info for a specific user");
            System.out.println(" 5 - show list of ratings from a specific user");
            System.out.println(" 6 - show ratings info for a specific MovieNight user");
            System.out.println(" 7 - show list of ratings from a specific MovieNight user");
            System.out.println(" 8 - show similar users for a specific MovieNight user");
            System.out.println(" 9 - predict movies");
            System.out.println(" 10 - show individual recommendation lists");
            System.out.println("-------------------------------------");
            System.out.println("");

            System.out.print("Your command: ");
            String command = inputReader.nextLine();
            if (command.equals("x")) {
                break;
            }

            if (command.equals("1")) {
                printRatings();
            } else if (command.equals("2")) {
                printMovies();
            } else if (command.equals("3")) {
                ratingsOfOneMovie(inputReader);
            } else if (command.equals("4")) {
                ratingsOfOneUser(inputReader);
            } else if (command.equals("5")) {
                listOfRaitingsOfOneMovie(inputReader);
            } else if (command.equals("6")) {
                ratingsOfOneMovieNightUser(inputReader);
            } else if (command.equals("7")) {
                listOfRaitingsOfOneMovieNightUser(inputReader);
            } else if (command.equals("8")) {
                System.out.println();
                System.out.println("Which MovieNight user are you interested in?");
                System.out.print("MovieNight user (name): ");

                String name = inputReader.nextLine();
                if (!this.dataController.getMovieNightUsers().containsKey(name)) {
                    System.out.println("There are no users with that name using MovieNight!");
                } else {
                    similarUsers(name);
                }
            } else if (command.equals("9")) {
                System.out.println();
                System.out.println("Which MovieNight user are you interested in?");
                System.out.print("MovieNight user (name): ");

                String name = inputReader.nextLine();
                if (!this.dataController.getMovieNightUsers().containsKey(name)) {
                    System.out.println("There are no users with that name using MovieNight!");
                } else {
                    predictMovies(name);
                }
            } else if (command.equals("10")) {
                calculateRecommendationLists();
            } else {
                System.out.println("Unknown command. Try again. (press 'x' to stop the application)");
            }
        }
    }

    public void printRatings() {
        int n = this.dataController.getMovieRatings().size();

        System.out.println();
        System.out.println("The app contains ratings for " + n + " movies.");
    }

    public void printMovies() {
        HashMap<Integer, Movie> movies = this.dataController.getMovies();

        System.out.println();
        System.out.println("The database contains the following movies:");

        for (Integer i : movies.keySet()) {
            System.out.println(movies.get(i));
        }
    }

    public void ratingsOfOneMovie(Scanner inputReader) {
        HashMap<Integer, Movie> movies = this.dataController.getMovies();

        System.out.println();
        System.out.println("Which movie are you interested in?");
        System.out.print("Movie (id): ");

        String cmd = inputReader.nextLine();
        try {
            int num = Integer.parseInt(cmd);
            if (!dataController.getMovieRatings().containsKey(num)) {
                System.out.println("That movieId is not used in the database!");
            } else {
                int numberOfRatings = 0;
                double sum = 0.0;
                for (Double r : dataController.getMovieRatings().get(num).values()) {
                    numberOfRatings++;
                    sum += r;
                }
                double avg = sum / numberOfRatings;

                System.out.println("There are " + numberOfRatings + " ratings for the movie " + movies.get(num));
                System.out.println("The average score is " + avg);
                System.out.println("The ratings for movie " + movies.get(num).getTitle() + " are the following");

                for (Integer i : dataController.getMovieRatings().get(num).keySet()) {
                    System.out.println(
                            "User " + i + " gave a rating of " + dataController.getMovieRatings().get(num).get(i));

                }
            }
        } catch (NumberFormatException e) {
            System.out.println("You did not enter a number!");
        }
    }

    public void ratingsOfOneUser(Scanner inputReader) {

        System.out.println();
        System.out.println("Which user are you interested in?");
        System.out.print("User (id): ");

        String cmd = inputReader.nextLine();
        try {
            int num = Integer.parseInt(cmd);
            if (!dataController.getUserRatings().containsKey(num)) {
                System.out.println("There are no users with that id in the dataset!");
            } else {
                int numberOfRatings = 0;
                double sum = 0.0;
                for (Double r : dataController.getUserRatings().get(num).values()) {
                    numberOfRatings++;
                    sum += r;
                }
                double avg = sum / numberOfRatings;

                System.out.println("There are " + numberOfRatings + " ratings for the user " + num);
                System.out.println("The average score is " + avg);
            }
        } catch (NumberFormatException e) {
            System.out.println("You did not enter a number!");
        }
    }

    public void listOfRaitingsOfOneMovie(Scanner inputReader) {
        HashMap<Integer, Movie> movies = this.dataController.getMovies();

        System.out.println();
        System.out.println("Which user are you interested in?");
        System.out.print("User (id): ");

        String cmd = inputReader.nextLine();
        try {
            int num = Integer.parseInt(cmd);
            if (!dataController.getUserRatings().containsKey(num)) {
                System.out.println("There are no users with that id in the dataset!");
            } else {
                System.out.println("The ratings for the user " + num + " are the following");
                for (Integer i : dataController.getUserRatings().get(num).keySet()) {
                    double r = dataController.getUserRatings().get(num).get(i);

                    System.out.println(movies.get(i).getTitle() + " was rated " + r);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("You did not enter a number!");
        }
    }

    public void ratingsOfOneMovieNightUser(Scanner inputReader) {

        System.out.println();
        System.out.println("Which MovieNight user are you interested in?");
        System.out.print("MovieNight user (name): ");

        String name = inputReader.nextLine();
        if (!this.dataController.getMovieNightUsers().containsKey(name)) {
            System.out.println("There are no users with that name using MovieNight!");
        } else {
            int numberOfRatings = 0;
            double sum = 0.0;
            for (Double r : this.dataController.getMovieNightUsers().get(name).getMovieRatings().values()) {
                numberOfRatings++;
                sum += r;
            }
            double avg = sum / numberOfRatings;

            System.out.println("There are " + numberOfRatings + " ratings for the MovieNight user " + name);
            System.out.println("The average score is " + avg);
        }
    }

    public void listOfRaitingsOfOneMovieNightUser(Scanner inputReader) {
        HashMap<Integer, Movie> movies = this.dataController.getMovies();

        System.out.println();
        System.out.println("Which MovieNight user are you interested in?");
        System.out.print("MovieNight user (name): ");

        String name = inputReader.nextLine();
        if (!this.dataController.getMovieNightUsers().containsKey(name)) {
            System.out.println("There are no users with that name using MovieNight!");
        } else {
            System.out.println("The ratings for the user " + name + " are the following");
            for (Integer i : this.dataController.getMovieNightUsers().get(name).getMovieRatings().keySet()) {
                double r = this.dataController.getMovieNightUsers().get(name).getMovieRatings().get(i);
                System.out.println(movies.get(i).getTitle() + " was rated " + r);
            }
        }
    }

    public void similarUsers(String name) {
        HashMap<Integer, HashSet<Integer>> similarUsers = this.dataController.getSimilarUsers(name);

        // System.out.println();
        System.out.println(similarUsers.size() + " users have rated at least 4 movies that MovieNight user " + name
                + " also has rated");
        // System.out.println("Similar users for MovieNight user " + name + " are the
        // following");
        // for (Integer i : similarUsers.keySet()) {
        // HashSet<Integer> s = similarUsers.get(i);
        // System.out.println("User " + i + " has rated movies " +
        // Arrays.toString(s.toArray()));
        // }

    }

    public void predictMovies(String name) {
        // first the 'similarUsers' function is used to calculate similar users
        similarUsers(name);
        // then the predictions are calculated for the MovieNightUser
        this.dataController.calculatePredictions(name);
    }

    public void calculateRecommendationLists() {
        HashSet<String> genres = new HashSet<>();
        int amountOfMovies = Integer.MAX_VALUE;
        // we calculate the group recommendation list using borda count
        HashMap<Integer, Integer> groupRecommendationList = new HashMap<>();

        HashMap<String, PriorityQueue<MovieRating>> userRecommendationLists = this.dataController
                .getUserRecommendationLists();

        for (String name : this.dataController.getMovieNightUsers().keySet()) {
            predictMovies(name);
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