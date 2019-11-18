import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

import classes.Movie;
import classes.MovieRating;
import classes.PersonRating;

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
        HashMap<Integer, ArrayList<PersonRating>> movieRatings = dataController.getMovieRatings();
        HashMap<Integer, Movie> movies = this.dataController.getMovies();

        System.out.println();
        System.out.println("Which movie are you interested in?");
        System.out.print("Movie (id): ");

        String cmd = inputReader.nextLine();
        try {
            int num = Integer.parseInt(cmd);
            if (!movieRatings.containsKey(num)) {
                System.out.println("That movieId is not used in the database!");
            } else {
                ArrayList<PersonRating> listOfRatings = movieRatings.get(num);
                int numberOfRatings = listOfRatings.size();
                int sum = 0;
                for (PersonRating r : listOfRatings) {
                    sum += r.getRating();
                }
                double avg = new Double(sum) / numberOfRatings;

                System.out.println("There are " + numberOfRatings + " ratings for the movie " + movies.get(num));
                System.out.println("The average score is " + avg);
                System.out.println("The ratings for movie " + movies.get(num).getTitle() + " are the following");

                for (PersonRating r : listOfRatings) {
                    System.out.println(r);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("You did not enter a number!");
        }
    }

    public void ratingsOfOneMovie(Scanner inputReader) {
        HashMap<Integer, ArrayList<MovieRating>> userRatings = dataController.getUserRatings();

        System.out.println();
        System.out.println("Which user are you interested in?");
        System.out.print("User (id): ");

        String cmd = inputReader.nextLine();
        try {
            int num = Integer.parseInt(cmd);
            if (!userRatings.containsKey(num)) {
                System.out.println("There are no users with that id in the dataset!");
            } else {
                ArrayList<MovieRating> listOfRatings = userRatings.get(num);
                int numberOfRatings = listOfRatings.size();
                int sum = 0;
                for (MovieRating r : listOfRatings) {
                    sum += r.getRating();
                }
                double avg = new Double(sum) / numberOfRatings;

                System.out.println("There are " + numberOfRatings + " ratings for the user " + num);
                System.out.println("The average score is " + avg);
            }
        } catch (NumberFormatException e) {
            System.out.println("You did not enter a number!");
        }
    }

    public void listOfRaitingsOfOneUser(Scanner inputReader) {
        HashMap<Integer, ArrayList<MovieRating>> userRatings = dataController.getUserRatings();
        HashMap<Integer, Movie> movies = this.dataController.getMovies();

        System.out.println();
        System.out.println("Which user are you interested in?");
        System.out.print("User (id): ");

        String cmd = inputReader.nextLine();
        try {
            int num = Integer.parseInt(cmd);
            if (!userRatings.containsKey(num)) {
                System.out.println("There are no users with that id in the dataset!");
            } else {
                ArrayList<MovieRating> listOfRatings = userRatings.get(num);
                System.out.println("The ratings for the user " + num + " are the following");
                for (MovieRating r : listOfRatings) {
                    int movie = r.getMovie();

                    System.out.println(movies.get(movie).getTitle() + " was rated " + r.getRating());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("You did not enter a number!");
        }
    }

    public void ratingsOfOneMovieNightUser(Scanner inputReader) {
        HashMap<String, ArrayList<MovieRating>> movieNightUserRatings = this.dataController.getMovieNightUserRatings();

        System.out.println();
        System.out.println("Which MovieNight user are you interested in?");
        System.out.print("MovieNight user (name): ");

        String name = inputReader.nextLine();
        if (!movieNightUserRatings.containsKey(name)) {
            System.out.println("There are no users with that name using MovieNight!");
        } else {
            ArrayList<MovieRating> listOfRatings = movieNightUserRatings.get(name);
            int numberOfRatings = listOfRatings.size();
            int sum = 0;
            for (MovieRating r : listOfRatings) {
                sum += r.getRating();
            }
            double avg = new Double(sum) / numberOfRatings;

            System.out.println("There are " + numberOfRatings + " ratings for the MovieNight user " + name);
            System.out.println("The average score is " + avg);
        }
    }

    public void listOfRaitingsOfOneMovieNightUser(Scanner inputReader) {
        HashMap<String, ArrayList<MovieRating>> movieNightUserRatings = this.dataController.getMovieNightUserRatings();
        HashMap<Integer, Movie> movies = this.dataController.getMovies();

        System.out.println();
        System.out.println("Which MovieNight user are you interested in?");
        System.out.print("MovieNight user (name): ");
        
        String name = inputReader.nextLine();
        if (!movieNightUserRatings.containsKey(name)) {
            System.out.println("There are no users with that name using MovieNight!");
        } else {
            ArrayList<MovieRating> listOfRatings = movieNightUserRatings.get(name);
            System.out.println("The ratings for the user " + name + " are the following");
            for (MovieRating r : listOfRatings) {
                int movie = r.getMovie();
                System.out.println(movies.get(movie).getTitle() + " was rated " + r.getRating());
            }
        }
    }

}