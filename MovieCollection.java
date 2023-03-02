import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.List;

public class MovieCollection {
  private ArrayList<Movie> movies;
  private Scanner scanner;
  private HashMap<String, ArrayList<Movie>> castList;
  private HashMap<String, ArrayList<Movie>> genreList;

  public MovieCollection(String fileName) {
    importMovieList(fileName);
    scanner = new Scanner(System.in);
    loadCastList();
    loadGenreList();
  }

  public ArrayList<Movie> getMovies() {
    return movies;
  }

  public void loadCastList() {
    castList = new HashMap<String, ArrayList<Movie>>();
    for (Movie m : movies) {
      String[] cast = m.getCast().split("\\|");
      for (String name : cast) {
        if (castList.containsKey(name)) {
          // Add this m to this list
          ArrayList<Movie> newMovies = new ArrayList<Movie>(castList.get(name));
          newMovies.add(m);
          castList.put(name, newMovies);
        } else {
          castList.put(name, new ArrayList<Movie>(Arrays.asList(m)));
        }
      }
    }
  }

  public void loadGenreList() {
    genreList = new HashMap<String, ArrayList<Movie>>();
    for (Movie m : movies) {
      String[] genres = m.getGenres().split("\\|");
      for (String genre : genres) {
        if (genreList.containsKey(genre)) {
          // Add this m to this list
          ArrayList<Movie> newMovies = new ArrayList<Movie>(genreList.get(genre));
          newMovies.add(m);
          genreList.put(genre, newMovies);
        } else {
          genreList.put(genre, new ArrayList<Movie>(Arrays.asList(m)));
        }
      }
    }
  }

  public void menu() {
    String menuOption = "";

    System.out.println("Welcome to the movie collection!");
    System.out.println("Total: " + movies.size() + " movies");

    while (!menuOption.equals("q")) {
      System.out.println("------------ Main Menu ----------");
      System.out.println("- search (t)itles");
      System.out.println("- search (k)eywords");
      System.out.println("- search (c)ast");
      System.out.println("- search (g)enre");
      System.out.println("- see all movies of a (gen)re");
      System.out.println("- list top 50 (r)ated movies");
      System.out.println("- list top 50 (h)igest revenue movies");
      System.out.println("- (q)uit");
      System.out.print("Enter choice: ");
      menuOption = scanner.nextLine();

      if (!menuOption.equals("q")) {
        processOption(menuOption);
      }
    }
  }

  private void processOption(String option) {
    if (option.equals("t")) {
      searchTitles();
    } else if (option.equals("c")) {
      searchCast();
    } else if (option.equals("k")) {
      searchKeywords();
    } else if (option.equals("g")) {
      searchGenres();
    } else if (option.equals("gen")) {
      listGenres();
    } else if (option.equals("r")) {
      listHighestRated();
    } else if (option.equals("h")) {
      listHighestRevenue();
    } else {
      System.out.println("Invalid choice!");
    }
  }

  private void searchTitles() {
    System.out.print("Enter a title search term: ");
    String searchTerm = scanner.nextLine();

    // prevent case sensitivity
    searchTerm = searchTerm.toLowerCase();

    // arraylist to hold search results
    ArrayList<Movie> results = new ArrayList<Movie>();

    // search through ALL movies in collection
    for (int i = 0; i < movies.size(); i++) {
      String movieTitle = movies.get(i).getTitle();
      movieTitle = movieTitle.toLowerCase();

      if (movieTitle.indexOf(searchTerm) != -1) {
        // add the Movie object to the results list
        results.add(movies.get(i));
      }
    }

    // sort the results by title
    sortResults(results);

    // now, display them all to the user
    for (int i = 0; i < results.size(); i++) {
      String title = results.get(i).getTitle();

      // this will print index 0 as choice 1 in the results list; better for user!
      int choiceNum = i + 1;

      System.out.println("" + choiceNum + ". " + title);
    }

    System.out.println("Which movie would you like to learn more about?");
    System.out.print("Enter number: ");

    int choice = scanner.nextInt();
    scanner.nextLine();

    Movie selectedMovie = results.get(choice - 1);

    displayMovieInfo(selectedMovie);

    System.out.println("\n ** Press Enter to Return to Main Menu **");
    scanner.nextLine();
  }

  private void sortResults(ArrayList<Movie> listToSort) {
    for (int j = 1; j < listToSort.size(); j++) {
      Movie temp = listToSort.get(j);
      String tempTitle = temp.getTitle();

      int possibleIndex = j;
      while (possibleIndex > 0 && tempTitle.compareTo(listToSort.get(possibleIndex - 1).getTitle()) < 0) {
        listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
        possibleIndex--;
      }
      listToSort.set(possibleIndex, temp);
    }
  }

  private void displayMovieInfo(Movie movie) {
    System.out.println();
    System.out.println("Title: " + movie.getTitle());
    System.out.println("Tagline: " + movie.getTagline());
    System.out.println("Runtime: " + movie.getRuntime() + " minutes");
    System.out.println("Year: " + movie.getYear());
    System.out.println("Directed by: " + movie.getDirector());
    System.out.println("Cast: " + movie.getCast());
    System.out.println("Overview: " + movie.getOverview());
    System.out.println("User rating: " + movie.getUserRating());
    System.out.println("Box office revenue: " + movie.getRevenue());
  }

  private void searchCast() {
    System.out.print("Who do you want to search for? ");
    String searchTerm = scanner.nextLine();

    // prevent case sensitivity
    searchTerm = searchTerm.toLowerCase();

    ArrayList<String> matchesSearch = new ArrayList<String>();
    ArrayList<String> sortedCast = new ArrayList<String>(castList.keySet());
    Collections.sort(sortedCast);
    int castIdx = 1;
    for (String name : sortedCast) {
      if (name.toLowerCase().contains(searchTerm)) {
        System.out.println(castIdx + ". " + name);
        matchesSearch.add(name);
        castIdx++;
      }
    }

    int choice;
    do {
      System.out.print("What is your choice? ");
      choice = scanner.nextInt();
    } while (choice < 1 || choice > matchesSearch.size());

    ArrayList<Movie> results = new ArrayList<Movie>(castList.get(matchesSearch.get(choice - 1)));
    sortResults(results);

    for (int i = 0; i < results.size(); i++) {
      String title = results.get(i).getTitle();

      // this will print index 0 as choice 1 in the results list; better for user!
      int choiceNum = i + 1;

      System.out.println("" + choiceNum + ". " + title);
    }

    System.out.println("Which movie would you like to learn more about?");
    System.out.print("Enter number: ");

    choice = scanner.nextInt();
    scanner.nextLine();

    Movie selectedMovie = results.get(choice - 1);

    displayMovieInfo(selectedMovie);

    System.out.println("\n ** Press Enter to Return to Main Menu **");
    scanner.nextLine();
  }

  private void searchGenres() {
    // Choose Genre
    ArrayList<String> genres = listGenres();
    int choice;
    do {
      System.out.print("Which genre would you like to look into? ");
      choice = scanner.nextInt();
    } while (choice < 1 || choice > genres.size());

    String genreChoice = genres.get(choice - 1);

    // List out all movies
    ArrayList<Movie> movies = new ArrayList<Movie>(genreList.get(genreChoice));
    sortResults(movies);

    for (int i = 0; i < movies.size(); i++) {
      String title = movies.get(i).getTitle();
      System.out.println(i + 1 + ". " + title);
    }

    // Choose movie
    System.out.println("Which movie would you like to learn more about?");
    System.out.print("Enter number: ");

    choice = scanner.nextInt();
    scanner.nextLine();

    Movie selectedMovie = movies.get(choice - 1);

    displayMovieInfo(selectedMovie);

    System.out.println("\n ** Press Enter to Return to Main Menu **");
    scanner.nextLine();
  }

  private void searchKeywords() {
    System.out.print("Enter a keyword search term: ");
    String searchTerm = scanner.nextLine();

    // prevent case sensitivity
    searchTerm = searchTerm.toLowerCase();

    // arraylist to hold search results
    ArrayList<Movie> results = new ArrayList<Movie>();

    // search through ALL movies in collection
    for (int i = 0; i < movies.size(); i++) {
      String keywords = movies.get(i).getKeywords();
      keywords = keywords.toLowerCase();

      if (keywords.indexOf(searchTerm) != -1) {
        // add the Movie object to the results list
        results.add(movies.get(i));
      }
    }

    // sort the results by title
    sortResults(results);

    // now, display them all to the user
    for (int i = 0; i < results.size(); i++) {
      String title = results.get(i).getTitle();

      // this will print index 0 as choice 1 in the results list; better for user!
      int choiceNum = i + 1;

      System.out.println("" + choiceNum + ". " + title);
    }

    System.out.println("Which movie would you like to learn more about?");
    System.out.print("Enter number: ");

    int choice = scanner.nextInt();
    scanner.nextLine();

    Movie selectedMovie = results.get(choice - 1);

    displayMovieInfo(selectedMovie);

    System.out.println("\n ** Press Enter to Return to Main Menu **");
    scanner.nextLine();
  }

  private ArrayList<String> listGenres() {
    ArrayList<String> sortedGenres = new ArrayList<String>(genreList.keySet());
    Collections.sort(sortedGenres);
    int idx = 1;
    for (String name : sortedGenres) {
      System.out.println(idx + ". " + name);
      idx++;
    }

    return sortedGenres;
  }

  private void listHighestRated() {
     ArrayList<Movie> listToSort = new ArrayList<Movie>(movies);
    for (int j = 1; j < listToSort.size(); j++) {
      Movie temp = listToSort.get(j);
      double tempRev = temp.getUserRating();

      int possibleIndex = j;
      while (possibleIndex > 0 && tempRev > listToSort.get(possibleIndex - 1).getUserRating()) {
        listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
        possibleIndex--;
      }
      listToSort.set(possibleIndex, temp);
    }
    for (int i = 1; i < 51; i++) {
      System.out.println(i + ". " + listToSort.get(i - 1).getTitle() + " - " + listToSort.get(i - 1).getUserRating());
    }

    System.out.println("Which movie would you like to learn more about?");
    System.out.print("Enter number: ");

    int choice = scanner.nextInt();
    scanner.nextLine();

    Movie selectedMovie = listToSort.get(choice - 1);

    displayMovieInfo(selectedMovie);

    System.out.println("\n ** Press Enter to Return to Main Menu **");
    scanner.nextLine();
  }

  private void listHighestRevenue() {
    ArrayList<Movie> listToSort = new ArrayList<Movie>(movies);
    for (int j = 1; j < listToSort.size(); j++) {
      Movie temp = listToSort.get(j);
      int tempRev = temp.getRevenue();

      int possibleIndex = j;
      while (possibleIndex > 0 && tempRev > listToSort.get(possibleIndex - 1).getRevenue()) {
        listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
        possibleIndex--;
      }
      listToSort.set(possibleIndex, temp);
    }
    for (int i = 1; i < 51; i++) {
      System.out.println(i + ". " + listToSort.get(i - 1).getTitle() + " - $" + listToSort.get(i - 1).getRevenue());
    }

    System.out.println("Which movie would you like to learn more about?");
    System.out.print("Enter number: ");

    int choice = scanner.nextInt();
    scanner.nextLine();

    Movie selectedMovie = listToSort.get(choice - 1);

    displayMovieInfo(selectedMovie);

    System.out.println("\n ** Press Enter to Return to Main Menu **");
    scanner.nextLine();
  }

  private void importMovieList(String fileName) {
    try {
      FileReader fileReader = new FileReader(fileName);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line = bufferedReader.readLine();

      movies = new ArrayList<Movie>();

      while ((line = bufferedReader.readLine()) != null) {
        String[] movieFromCSV = line.split(",");

        String title = movieFromCSV[0];
        String cast = movieFromCSV[1];
        String director = movieFromCSV[2];
        String tagline = movieFromCSV[3];
        String keywords = movieFromCSV[4];
        String overview = movieFromCSV[5];
        int runtime = Integer.parseInt(movieFromCSV[6]);
        String genres = movieFromCSV[7];
        double userRating = Double.parseDouble(movieFromCSV[8]);
        int year = Integer.parseInt(movieFromCSV[9]);
        int revenue = Integer.parseInt(movieFromCSV[10]);

        Movie nextMovie = new Movie(title, cast, director, tagline, keywords, overview, runtime, genres, userRating,
            year, revenue);
        movies.add(nextMovie);
      }
      bufferedReader.close();
    } catch (IOException exception) {
      // Print out the exception that occurred
      System.out.println("Unable to access " + exception.getMessage());
    }
  }
}