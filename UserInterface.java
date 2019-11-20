import java.util.Scanner;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;

import classes.Movie;

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
                ratingsOfOneUser(inputReader);
            } else if (command.equals("4")) {
                ratingsOfOneMovie(inputReader);
            } else if (command.equals("5")) {
                listOfRaitingsOfOneUser(inputReader);
            } else if (command.equals("6")) {
                ratingsOfOneMovieNightUser(inputReader);
            } else if (command.equals("7")) {
                listOfRaitingsOfOneMovieNightUser(inputReader);
            } else if (command.equals("8")) {
                similarUsers(inputReader);
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

    public void ratingsOfOneUser(Scanner inputReader) {
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
                    numberOfRatings ++;
                    sum += r;
                }
                double avg = sum / numberOfRatings;

                System.out.println("There are " + numberOfRatings + " ratings for the movie " + movies.get(num));
                System.out.println("The average score is " + avg);
                System.out.println("The ratings for movie " + movies.get(num).getTitle() + " are the following");

                for (Integer i : dataController.getMovieRatings().get(num).keySet()) {
                    System.out.println("User " + i + " gave a rating of " + dataController.getMovieRatings().get(num).get(i));
                    
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("You did not enter a number!");
        }
    }

    public void ratingsOfOneMovie(Scanner inputReader) {

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
                    numberOfRatings ++;
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

    public void listOfRaitingsOfOneUser(Scanner inputReader) {
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
                numberOfRatings ++;
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

    public void similarUsers(Scanner inputReader) {

        System.out.println();
        System.out.println("Which MovieNight user are you interested in?");
        System.out.print("MovieNight user (name): ");
        
        String name = inputReader.nextLine();
        if (!this.dataController.getMovieNightUsers().containsKey(name)) {
            System.out.println("There are no users with that name using MovieNight!");
        } else {
            HashMap<Integer, HashSet<Integer>> similarUsers = this.dataController.getSimilarUsers(name);
    
            System.out.println();
            System.out.println(similarUsers.size() + " users have rated at least 4 movies that MovieNight user " + name + " also has rated");
            System.out.println("Similar users for MovieNight user " + name + " are the following");
            for (Integer i : similarUsers.keySet()) {
                HashSet<Integer> s = similarUsers.get(i);
                System.out.println("User " + i + " has rated movies " + Arrays.toString(s.toArray()));
            }
        }
    }

}