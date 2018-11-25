package idhifi;

import javax.json.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Get the current address from the incomplete address provided by the user
 *
 * Created by ybai on 11/05/2017.
 */
public class GeocodeParsing {
    private final static String GEOCODE_KEY = "&key=AIzaSyDZjXzJlYQLbRhRA4TTcccG1j0RshDQ5oU";
    private final static String GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/";
    private final static String GEOCODE_ADDR_PREFIX = "json?address=";
    private final static String ELEVATION_KEY = "&key=AIzaSyCTjFzh_VMLK-BUxO64UFDeKczaYIOznF4";
    private final static String ELEVATION_URL = "https://maps.googleapis.com/maps/api/elevation/";
    private final static String ELEVATION_LOCAT_PREFIX = "json?locations=";



    public static List<Address> getAddress (String inputAddr) throws AddressNotFound {
        // input sanitisation
        inputAddr = inputAddr.replace(' ','+');

        JsonObject geocodeJObj;
        try {
            geocodeJObj = APIRead.HTTPToJSON(GEOCODE_URL + GEOCODE_ADDR_PREFIX + inputAddr + GEOCODE_KEY);
        }
        catch (IOException e){
            System.out.printf("fail to connect to geocode");
            throw new AddressNotFound("bad connection to geocode");
        }

        if (geocodeJObj.getString("status").equals("OK")){
            JsonArray results = geocodeJObj.getJsonArray("results");
            List<Address> lstAddr = new ArrayList<>(results.size());
            for (int i = 0; i < results.size(); i++) {
                JsonObject place = results.getJsonObject(i);
                JsonObject laLong = place.getJsonObject("geometry").getJsonObject("location");
                JsonArray zipcodeJAry = place.getJsonArray("address_components");
                String name = place.getString("formatted_address");
                double latitude = laLong.getJsonNumber("lat").doubleValue();
                double longtitude = laLong.getJsonNumber("lng").doubleValue();
                String zipCode = "000000"; // initialised zip code
                for (int j = zipcodeJAry.size()-1; j > 0; j--) {
                    JsonObject jCmpnt = zipcodeJAry.getJsonObject(j);
                    if (jCmpnt.getJsonArray("types").getJsonString(0).getString().equals("postal_code")){
                        zipCode = jCmpnt.getJsonString("long_name").getString();
                        break;
                    }
                }

                // get the altitude
                double altitude = 0; // initialise altitude to 0
                JsonObject elevationJObj;
                try {
                    elevationJObj = APIRead.HTTPToJSON(ELEVATION_URL + ELEVATION_LOCAT_PREFIX
                            + latitude + "," + longtitude + ELEVATION_KEY);
                }
                catch (IOException e){
                    System.out.printf("fail to connect to elevation");
                    throw new AddressNotFound("bad connection to elevation");
                }
                if (elevationJObj.getString("status").equals("OK")){
                    altitude = elevationJObj.getJsonArray("results")
                            .getJsonObject(0).getJsonNumber("elevation").doubleValue();
                }
                // else if not "OK", use initialised 0 altitude

                Address addr = new Address(name,zipCode,latitude,longtitude,altitude);

                lstAddr.add(addr);
            }
            return lstAddr;
        }
        else throw new AddressNotFound(inputAddr);

    }


    /**
     * Test method (unit test)
     * @param args not required
     */
    public static void main(String[] args) {
        try{
            List<Address> lstAddr = getAddress("CB3 0FD, UK");
            System.out.println(lstAddr.get(0));
        }
        catch (AddressNotFound e){
            System.out.println("addr not found ");
        }
    }
}
