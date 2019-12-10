import java.util.Scanner;
import java.util.HashMap;
import java.util.Locale;

import classes.Movie;
import classes.MovieNightUser;

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

        HashMap<Integer, Double> list4 = new HashMap<>();
        list4.put(6711, 4.5); // Lost in Translation
        list4.put(19, 2.5); // Ace Venture
        list4.put(189713, 4.0); // BlacKkKlansman
        list4.put(32, 3.0); // 12 Monkeys
        list4.put(6373, 1.5); // Bruce Almighty
        list4.put(1721, 2.0); // Titanic
        list4.put(7439, 2.5); // Punisher
        movieNightUsers.put("Aku", new MovieNightUser("Lupu", list4, "Crime"));

        this.dataController.setMovieNightUsers(movieNightUsers);

        // let's start the program
        Scanner inputReader = new Scanner(System.in);
        while (true) {

            System.out.println("\n-----------------------------------------------");
            System.out.println("Commands: ");
            System.out.println(" x - stop the application");
            System.out.println(" 1 - show general info");
            System.out.println(" 2 - show ratings info for a specific movie");
            System.out.println(" 3 - show recommendation lists");
            System.out.println("-----------------------------------------------\n");
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
                calculateRecommendationLists(inputReader);
            } else {
                System.out.println("Unknown command. Try again. (press 'x' to stop the application)");
            }
        }
    }

    // Show genereal info about the movie data
    public void generalInfo(Scanner inputReader) {
        int n = this.dataController.getMovieRatings().size();

        System.out.println("\n *** The app contains ratings for " + n + " movies. \n");
        System.out.println("Do you want to list all the movies? (type 'yes' to continue)");

        String command = inputReader.nextLine();

        if (command.equals("yes")) {
            for (int movieId : this.dataController.getMovies().keySet()) {
                System.out.println("MovieId " + movieId + ": " + this.dataController.getMovies().get(movieId)
                        + " --- GENRES:" + this.dataController.getMovies().get(movieId).getGenres());
            }
        }
    }

    // Show more specific info about one particular movie
    public void ratingsOfOneMovie(Scanner inputReader) {
        HashMap<Integer, Movie> movies = this.dataController.getMovies();
        HashMap<Integer, HashMap<Integer, Double>> movieRatings = this.dataController.getMovieRatings();

        System.out.println("\nWhich movie are you interested in?");
        System.out.print("Movie (id): ");

        try {
            int movieId = Integer.parseInt(inputReader.nextLine());

            if (!movieRatings.containsKey(movieId)) {
                System.out.println("That movieId is not used in the database!");
            } else {
                int numberOfRatings = 0;
                double sumOfRatings = 0.0;

                for (Double rating : movieRatings.get(movieId).values()) {
                    numberOfRatings++;
                    sumOfRatings += rating;
                }

                double average = sumOfRatings / numberOfRatings;

                System.out.println("\n*** MovieId " + movieId + " refers to: " + movies.get(movieId));
                System.out.println("*** There are " + numberOfRatings + " ratings for the movie ");
                System.out.println("*** The average score is " + String.format(Locale.US, "%.2f", average));
                System.out.println("*** The ratings are the following:");

                for (int userId : movieRatings.get(movieId).keySet()) {
                    System.out.println(
                            " * User " + userId + " gave a rating of " + movieRatings.get(movieId).get(userId));
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("You did not enter a number!");
        }
    }

    public void calculateRecommendationLists(Scanner inputReader) {

        System.out.println("\nDo you want to use the default values for calculations (yes/no)?");
        System.out.println("Press 'x' to abort");
        System.out.print("\nYour command: ");

        String command = inputReader.nextLine();

        if (command.equals("yes")) {
            // initialize default coefficients
            this.dataController.setCommonMovies(4);
            this.dataController.setMinimumNumberOfSimilarUsers(10);
            this.dataController.setGenreCoefficient(0.5);
            this.dataController.setListSize(20);
            // ... and calculate recommendation lists
            this.dataController.calculateRecommendationLists();
        } else if (command.equals("no")) {
            // adjust values ...
            adjustCalculationCoefficients(inputReader);
            // ... and calculate recommendation lists
            this.dataController.calculateRecommendationLists();
        } else {
            System.out.println("Unknown command. Try again. (press 'x' to abort)");
        }

    }

    public void adjustCalculationCoefficients(Scanner inputReader) {
        try {

            System.out.println(
                    "\nHow many movies do a user X in the database have to have in common with a MovieNightUser to be considered when calculating similar users?");
            System.out.println("Default value is 4.");
            System.out.print("\nYour command: ");

            int commonMovies = Integer.parseInt(inputReader.nextLine());

            System.out.println("\nAt least how many similar users each to-be-recommended movie should have?");
            System.out.println("Default value is 10.");
            System.out.print("\nYour command: ");

            int minimumNumberOfSimilarUsers = Integer.parseInt(inputReader.nextLine());

            System.out.println(
                    "\nWhat should be the weighing coefficient when calculating the genre-weighted group recommendation list?");
            System.out.println("Default value is 0.5.");
            System.out.print("\nYour command: ");

            double genreCoefficient = Double.parseDouble(inputReader.nextLine());

            System.out.println("\nHow many movies do you wish to see in the final group recommendation lists?");
            System.out.println("Default value is 20.");
            System.out.print("\nYour command: ");

            int listSize = Integer.parseInt(inputReader.nextLine());

            this.dataController.setCommonMovies(commonMovies);
            this.dataController.setMinimumNumberOfSimilarUsers(minimumNumberOfSimilarUsers);
            this.dataController.setGenreCoefficient(genreCoefficient);
            this.dataController.setListSize(listSize);
        } catch (NumberFormatException e) {
            System.out.println("\nYou did not enter a number!");
            System.out.println("Default coefficients will be used.");
        }
    }

}