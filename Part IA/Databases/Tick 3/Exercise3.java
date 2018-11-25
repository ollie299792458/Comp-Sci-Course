import java.util.Map;
import java.util.TreeMap;
import uk.ac.cam.cl.databases.moviedb.MovieDB;
import uk.ac.cam.cl.databases.moviedb.model.*;

public class Exercise3 {
    public static void main(String[] args) {
        try (MovieDB database = MovieDB.open(args[0])) {
            int requiredPosition = Integer.parseInt(args[1]);
            Map<Integer, String> peopleByMovies = new TreeMap<>();
            for (Person person : database.getByNamePrefix("")) {
                if (person.getActorIn() != null)
                for (Role role : person.getActorIn()) {
                    if (role.getPosition() != null)
                    if (role.getPosition() == requiredPosition) {
                        if (peopleByMovies.containsKey(role.getMovieId())) {
                            if (person.getName().compareTo(peopleByMovies.get(role.getMovieId())) > 0) {
                                String name1 = peopleByMovies.get(role.getMovieId());
                                String name2 = person.getName();
                                String movieTitle = database.getMovieById(role.getMovieId()).getTitle();

                                System.out.println(name1
                                        + " and "
                                        + name2
                                        + " both have position "
                                        + String.valueOf(requiredPosition)
                                        + " in "
                                        + movieTitle);
                            }
                        } else {
                            peopleByMovies.put(role.getMovieId(), person.getName());
                        }
                    }
                }
            }
        }
    }
}
