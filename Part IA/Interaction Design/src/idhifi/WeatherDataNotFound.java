package idhifi;

/**
 * Exception thrown when weather data cannot be found, for whatever reason
 *
 * Created by ybai on 11/05/2017.
 */
public class WeatherDataNotFound extends Exception {
    public WeatherDataNotFound(String message) {
        super(message);
    }

}
