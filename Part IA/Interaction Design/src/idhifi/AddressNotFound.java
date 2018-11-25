package idhifi;

/**
 * Exception thrown when the address cannot be found by GeocodeParsing
 *
 * Created by ybai on 11/05/2017.
 */
public class AddressNotFound extends Exception {
    public AddressNotFound(String message) {
        super(message);
    }

}
