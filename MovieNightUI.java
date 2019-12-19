import java.util.Scanner;
import java.util.HashMap;
import java.util.Locale;

import components.MainController;

import classes.Movie;
import classes.MovieNightUser;

public class MovieNightUI {
    private MainController controller;

    public MovieNightUI() {
        this.controller = new MainController();
    }

    public void start() {
        // let's load the ratings data
        this.controller.loadData();

        // let's initialize the system with predetermined test users
        initializeMovieNightUsers();

        // let's start the program
        Scanner inputReader = new Scanner(System.in);
        while (true) {

            System.out.println("\n================================================");
            System.out.println("*            MOVIE NIGHT application           *");
            System.out.println("------------------------------------------------");
            System.out.println("Commands: ");
            System.out.println(" x - stop the application");
            System.out.println(" 0 - add MovieNight users and ratings");
            System.out.println(" 1 - show general info");
            System.out.println(" 2 - show ratings info for a specific movie");
            System.out.println(" 3 - show ratings info by the MovieNight users");
            System.out.println(" 4 - show recommendation lists");
            System.out.println("================================================");
            System.out.print("\nYour command: ");

            String command = inputReader.nextLine();

            if (command.equals("x")) {
                break;
            } else if (command.equals("0")) {
                addMovieNightUsers(inputReader);
            } else if (command.equals("1")) {
                generalInfo(inputReader);
            } else if (command.equals("2")) {
                ratingsOfOneMovie(inputReader);
            } else if (command.equals("3")) {
                ratingsOfMovieNightUsers();
            } else if (command.equals("4")) {
                calculateRecommendationLists(inputReader);
            } else {
                System.out.println("Unknown command. Try again. (press 'x' to stop the application)");
            }
        }
    }

    // Add MovieNight users to the system
    public void addMovieNightUsers(Scanner inputReader) {
        System.out.println("\nDo you want to initialize the system with predetermined users? (yes/no)");
        System.out.println("NOTE: 'yes' will remove any existing MovieNight users from the system.");
        System.out.print("Your command: ");
        String command = inputReader.nextLine();
        if (command.equals("yes")) {
            initializeMovieNightUsers();
        } else if (command.equals("no")) {
            while (true) {
                System.out.println("\nFor who do you want to add a rating or genre preference?");
                System.out.println(" - if you want to see existing MovieNight user rating for reference, press 0");
                System.out.println(" - if you done with adding MovieNight user ratings, press x");
                System.out.print("\nInsert name: ");
                String name = inputReader.nextLine();
                if (name.equals("0")) {
                    ratingsOfMovieNightUsers();
                } else if (name.equals("x")) {
                    break;
                } else {
                    while (true) {
                        System.out.println("\n... adding ratings for MovieNight user " + name);
                        System.out.print("Insert movie (movieId) of press 'g' for genre addition (or 'x' to stop): ");
                        String cmd = inputReader.nextLine();
                        if (cmd.equals("x")) {
                            break;
                        } else if (cmd.equals("g")) {
                            System.out.println("Allowed genres: Action/Adventure/Animation/Children's/Comedy/Crime/Documentary/Drama/Fantasy/Film-Noir/Horror/Musical/Mystery/Romance/Sci-Fi/Thriller/War/Western");
                            System.out.print("Insert genre: ");
                            String genre = inputReader.nextLine();
                            this.controller.addMovieNightUserGenre(name, genre);
                        } else {
                            try {
                                int movieId = Integer.parseInt(cmd);
                                System.out.print("Insert rating (0.5-5.0 with half-star increments): ");
                                double rating = Double.parseDouble(inputReader.nextLine());
                                this.controller.addMovieNightUserRating(name, movieId, rating);
                            } catch (NumberFormatException e) {
                                System.out.println("You did not enter a valid number for movieId or rating!");
                            }
                        }
                    }
                }
            }
        }
    }

    // Show genereal info about the movie data
    public void generalInfo(Scanner inputReader) {
        int n = this.controller.getMovieRatings().size();

        System.out.println("\n *** The app contains ratings for " + n + " movies. \n");
        System.out.println("Do you want to list all the movies? (type 'yes' to continue)");

        String command = inputReader.nextLine();

        if (command.equals("yes")) {
            for (int movieId : this.controller.getMovies().keySet()) {
                System.out.println("MovieId " + movieId + ": " + this.controller.getMovies().get(movieId)
                        + " --- GENRES:" + this.controller.getMovies().get(movieId).getGenres());
            }
        }
    }

    // Show more specific info about one particular movie
    public void ratingsOfOneMovie(Scanner inputReader) {
        HashMap<Integer, Movie> movies = this.controller.getMovies();
        HashMap<Integer, HashMap<Integer, Double>> movieRatings = this.controller.getMovieRatings();

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

    // Show ratings info by the MovieNight users
    public void ratingsOfMovieNightUsers() {
        HashMap<String, MovieNightUser> movieNightUsers = this.controller.getMovieNightUsers();
        HashMap<Integer, Movie> movies = this.controller.getMovies();

        for (String name : movieNightUsers.keySet()) {
            HashMap<Integer, Double> ratings = movieNightUsers.get(name).getMovieRatings();
            System.out.println("\n*** MovieNight user " + name + " has rated the following movies: ");
            for (int movieId : ratings.keySet()) {
                System.out.println(" * Rating " + ratings.get(movieId) + " for movie: " + movies.get(movieId));
            }
            System.out.println(" * " + name + "'s genre preference: " + movieNightUsers.get(name).getGenre());
        }
    }

    public void calculateRecommendationLists(Scanner inputReader) {

        System.out.println("\nDo you want to use the default values for calculations (yes/no)?");
        System.out.println("Press 'x' to abort");
        System.out.print("\nYour command: ");

        String command = inputReader.nextLine();

        if (command.equals("yes")) {
            // initialize default coefficients
            this.controller.setNumberOfMoviesInCommon(4);
            this.controller.setMinimumNumberOfSimilarUsers(10);
            this.controller.setGenreCoefficient(0.5);
            this.controller.setListSize(20);
            // ... and calculate recommendation lists
            this.controller.calculateRecommendationLists();
        } else if (command.equals("no")) {
            // adjust values ...
            adjustCalculationCoefficients(inputReader);
            // ... and calculate recommendation lists
            this.controller.calculateRecommendationLists();
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

            int numberOfMoviesInCommon = Integer.parseInt(inputReader.nextLine());

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

            // if all parameters have been successfully selected, they are saved
            this.controller.setNumberOfMoviesInCommon(numberOfMoviesInCommon);
            this.controller.setMinimumNumberOfSimilarUsers(minimumNumberOfSimilarUsers);
            this.controller.setGenreCoefficient(genreCoefficient);
            this.controller.setListSize(listSize);
        } catch (NumberFormatException e) {
            System.out.println("\nYou did not enter a number!");
            System.out.println("Default coefficients will be used.");
        }
    }

    public void initializeMovieNightUsers() {
        // first let's remove the existing MovieNight users if there are any
        this.controller.setMovieNightUsers(new HashMap<>());

        // let's initialize the system with predetermined MovieNight users and their
        // ratings
        HashMap<String, MovieNightUser> movieNightUsers = new HashMap<>();

        HashMap<Integer, Double> ratings1 = new HashMap<>();
        ratings1.put(1, 2.5); // Toy Story
        ratings1.put(2, 4.0); // Jumanji
        ratings1.put(19, 5.0); // Ace Ventura
        ratings1.put(32, 3.5); // 12 Monkeys
        ratings1.put(48, 0.5); // Pocahontas
        ratings1.put(224, 2.0); // Don Juan DeMarco
        ratings1.put(949, 1.5); // East of Eden
        movieNightUsers.put("Tupu", new MovieNightUser("Tupu", ratings1, "Romance"));

        HashMap<Integer, Double> ratings2 = new HashMap<>();
        ratings2.put(1, 4.0); // Toy Story
        ratings2.put(19, 2.0); // Ace Venture
        ratings2.put(48, 4.5); // Pocahontas
        ratings2.put(79132, 3.0); // Inception
        ratings2.put(193609, 0.5); // Andrew Dice Clay
        ratings2.put(2085, 4.0); // 101 Dalmatians
        ratings2.put(2382, 2.5); // Police Academy 5
        movieNightUsers.put("Hupu", new MovieNightUser("Hupu", ratings2, "Drama"));

        HashMap<Integer, Double> ratings3 = new HashMap<>();
        ratings3.put(1, 2.5); // Toy Story
        ratings3.put(19, 3.0); // Ace Venture
        ratings3.put(189713, 3.5); // BlacKkKlansman
        ratings3.put(32, 5.0); // 12 Monkeys
        ratings3.put(104, 5.0); // Happy Gilmore
        ratings3.put(1721, 0.5); // Titanic
        ratings3.put(1717, 1.0); // Scream 2
        movieNightUsers.put("Lupu", new MovieNightUser("Lupu", ratings3, "Comedy"));

        HashMap<Integer, Double> ratings4 = new HashMap<>();
        ratings4.put(6711, 4.5); // Lost in Translation
        ratings4.put(19, 2.5); // Ace Venture
        ratings4.put(189713, 4.0); // BlacKkKlansman
        ratings4.put(32, 3.0); // 12 Monkeys
        ratings4.put(6373, 1.5); // Bruce Almighty
        ratings4.put(1721, 2.0); // Titanic
        ratings4.put(7439, 2.5); // Punisher
        movieNightUsers.put("Aku", new MovieNightUser("Aku", ratings4, "Crime"));

        this.controller.setMovieNightUsers(movieNightUsers);
    }

}