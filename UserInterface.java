import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

public class UserInterface {
    // movies and their ratings in the dataset (movieId is the HasHMap key)
    private HashMap<Integer, ArrayList<PersonRating>> movieRatings;
    // movies and their titles (movieId is the HashMap key)
    private HashMap<Integer, String> movies;
    // users and which movies they have rated (userId is the HashMap key)
    private HashMap<Integer, ArrayList<MovieRating>> userRatings;

    // MovieNight users and which movies they have rated (name is the HashMap key)
    private HashMap<String, ArrayList<MovieRating>> movieNightUserRatings;

    public UserInterface() {
        this.movieRatings = new HashMap<>();
        this.movies = new HashMap<>();
        this.userRatings = new HashMap<>();
        this.movieNightUserRatings = new HashMap<>();

        // let's load the ratings (userId + movieId + rating)
        loadRatingsData();

        // let's load movie data (movieId + title)
        loadMovieData();

        // let's add a couple of test users for the system
        ArrayList list1 = new ArrayList<>();
        list1.add(new MovieRating(1, 2.5));
        list1.add(new MovieRating(2, 4));
        list1.add(new MovieRating(19, 5));
        list1.add(new MovieRating(32, 3.5));
        list1.add(new MovieRating(48, 0.5));
        this.movieNightUserRatings.put("Lassi", list1);

        ArrayList list2 = new ArrayList<>();
        list2.add(new MovieRating(1, 4));
        list2.add(new MovieRating(19, 2));
        list2.add(new MovieRating(48, 4.5));
        list2.add(new MovieRating(26, 3));
        list2.add(new MovieRating(193609, 0.5));
        this.movieNightUserRatings.put("Leevi", list2);

        ArrayList list3 = new ArrayList<>();
        list3.add(new MovieRating(1, 2.5));
        list3.add(new MovieRating(19, 3));
        list3.add(new MovieRating(189713, 3.5));
        list3.add(new MovieRating(32, 5));
        list3.add(new MovieRating(193609, 5));
        list3.add(new MovieRating(179135, 4.5));
        this.movieNightUserRatings.put("Karvinen", list3);

    }

    public void loadRatingsData() {
        // let's create a Scanner to load the data from File
        // the data includes movie ratings in following way (whitespace/tab separated)
        // user id item id rating time stamp
        // (we are not interested in the time stamp)
        try (Scanner dataReader = new Scanner(new File("ratings.csv"))) {
            // let's read the file line by line
            // but we will ignore the first one as it contains the "column names"
            dataReader.nextLine();
            while (dataReader.hasNextLine()) {
                String[] parts = dataReader.nextLine().split(",");

                // System.out.println(parts[0] + " and " + parts[1] + " and " + parts[2] + " and
                // " + parts[3]);

                int personId = Integer.parseInt(parts[0]);
                int itemId = Integer.parseInt(parts[1]);
                double rating = Double.parseDouble(parts[2]);

                // let's add the rating to movieRatings
                if (this.movieRatings.containsKey(itemId)) {
                    ArrayList list = this.movieRatings.get(itemId);
                    list.add(new PersonRating(personId, rating));
                    this.movieRatings.put(itemId, list);
                } else {
                    ArrayList list = new ArrayList<>();
                    list.add(new PersonRating(personId, rating));
                    this.movieRatings.put(itemId, list);
                }

                // let's link the movie to the user (userRatings)
                if (this.userRatings.containsKey(personId)) {
                    ArrayList list = this.userRatings.get(personId);
                    list.add(new MovieRating(itemId, rating));
                    this.userRatings.put(personId, list);
                } else {
                    ArrayList list = new ArrayList<>();
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
        // movie id movie title etc etc
        // (at the moment we are only interested in the movie id and movie title)
        try (Scanner dataReader = new Scanner(new File("movies.csv"))) {
            // let's read the file line by line
            // but we will ignore the first one as it contains the "column names"
            dataReader.nextLine();
            while (dataReader.hasNextLine()) {
                String[] parts = dataReader.nextLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                // System.out.println(parts[0] + " and " + parts[1]);

                int movieId = Integer.parseInt(parts[0]);
                String title = parts[1];

                this.movies.put(movieId, title);
            }
            System.out.println();
            System.out.println("File read ('movies.csv').");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void printRatings() {
        System.out.println();
        System.out.println("The app contains ratings for " + this.movieRatings.size() + " movies.");
    }

    public void printMovies() {
        System.out.println("The database contains the following " + this.movieRatings.size() + " movies:");
        for (Integer i : this.movies.keySet()) {
            System.out.println(this.movies.get(i));
        }
    }

    public void ratingsOfOneUser(Scanner inputReader) {
        System.out.println();
        System.out.println("Which movie are you interested in? (Use the movie id)");
        String cmd = inputReader.nextLine();
        try {
            int num = Integer.parseInt(cmd);
            if (!this.movieRatings.containsKey(num)) {
                System.out.println("That movieId is not used in the database!");
            } else {
                ArrayList<PersonRating> listOfRatings = this.movieRatings.get(num);
                int numberOfRatings = listOfRatings.size();
                int sum = 0;
                for (PersonRating r : listOfRatings) {
                    sum += r.getRating();
                }
                double avg = new Double(sum) / numberOfRatings;
                System.out.println("There are " + numberOfRatings + " ratings for the movie " + this.movies.get(num));
                System.out.println("The average score is " + avg);
                System.out.println("The ratings for movie " + this.movies.get(num) + " are the following");
                for (PersonRating r : listOfRatings) {
                    System.out.println(r);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("You did not enter a number!");
        }
    }

    public void ratingsOfOneMovie(Scanner inputReader) {
        System.out.println();
        System.out.println("Which user are you interested in? (Use the user id)");
        String cmd = inputReader.nextLine();
        try {
            int num = Integer.parseInt(cmd);
            if (num > this.userRatings.size()) {
                System.out.println("There are not that many users in the system!");
            } else if (num == 0) {
                System.out.println("You entered '0'!");
            } else {
                ArrayList<MovieRating> listOfRatings = this.userRatings.get(num);
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
        System.out.println();
        System.out.println("Which user are you interested in? (Use the user id)");
        String cmd = inputReader.nextLine();
        try {
            int num = Integer.parseInt(cmd);
            if (num > this.userRatings.size()) {
                System.out.println("There are not that many users in the system!");
            } else if (num == 0) {
                System.out.println("You entered '0'!");
            } else {
                ArrayList<MovieRating> listOfRatings = this.userRatings.get(num);
                System.out.println("The ratings for the user " + num + " are the following");
                for (MovieRating r : listOfRatings) {
                    int movie = r.getMovie();
                    System.out.println(this.movies.get(movie) + " was rated " + r.getRating());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("You did not enter a number!");
        }
    }

    public void ratingsOfOneMovieNightUser(Scanner inputReader) {
        System.out.println();
        System.out.println("Which MovieNight user are you interested in? (Use the user name)");
        String name = inputReader.nextLine();
        if (!this.movieNightUserRatings.containsKey(name)) {
            System.out.println("There are no users with that name using MovieNight!");
        } else {
            ArrayList<MovieRating> listOfRatings = this.movieNightUserRatings.get(name);
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
        System.out.println();
        System.out.println("Which MovieNight user are you interested in? (Use the user name)");
        String name = inputReader.nextLine();
        if (!this.movieNightUserRatings.containsKey(name)) {
            System.out.println("There are no users with that name using MovieNight!");
        } else {
            ArrayList<MovieRating> listOfRatings = this.movieNightUserRatings.get(name);
            System.out.println("The ratings for the user " + name + " are the following");
            for (MovieRating r : listOfRatings) {
                int movie = r.getMovie();
                System.out.println(this.movies.get(movie) + " was rated " + r.getRating());
            }
        }
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
            System.out.println(" 8 - do something else");
            System.out.println("-------------------------------------");
            System.out.println("");

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
                System.out.println("do something..");
            } else {
                System.out.println("Unknown command. Try again. (press 'x' to stop the application)");
            }
        }
    }
}