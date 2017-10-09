import java.util.Map;
import java.util.TreeMap;
import uk.ac.cam.cl.databases.moviedb.MovieDB;
import uk.ac.cam.cl.databases.moviedb.model.*;

public class GenresByActor {
    public static void main(String[] args) {
        try (MovieDB database = MovieDB.open(args[0])) {
            for (Person person : database.getByNamePrefix("Damon, Matt")) {
                TreeMap<String, Integer> genreCount = new TreeMap<>();

                for (Role role : person.getActorIn()) {
                    // Perform a join by looking up the ID of the movie
                    Movie movie = database.getMovieById(role.getMovieId());

                    // Iterate over the genres of that movie, and add them to the map
                    if (movie.getGenres() != null) {
                        for (String genre : movie.getGenres()) {
                            if (genreCount.containsKey(genre)) {
                                genreCount.put(genre, genreCount.get(genre) + 1);
                            } else {
                                genreCount.put(genre, 1);
                            }
                        }
                    }
                }

                // Print out the aggregate counts
                System.out.println(person.getName() + " appears in movies of the following genres:");
                for (Map.Entry<String, Integer> count : genreCount.entrySet()) {
                    System.out.println("    " + count.getKey() + ": " + count.getValue() + " movies");
                }
            }
        }
    }
}
