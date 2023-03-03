// https://github.com/SantiagoVira/MovieProject

public class MovieCollectionRunner {
  public static void main(String arg[]) {
    MovieCollection myCollection = new MovieCollection("movies_data.csv");
    myCollection.menu();
  }
}