package idhifi;

import javax.json.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Static utility class to get data from the darksky api (https://darksky.net/dev/)
 *
 * Created by ybai on 18/05/2017.
 */
public class DarkSkyAPIParsing {
    private static final String KEY = "7114a6476717cec973d305b76e0597a3/";
    private static final String DARKSKY_ADDR = "https://api.darksky.net/forecast/";
    private static final String EXCLUDE_BLOCKS = "?exclude=[daily,minutely]";
    private static final String EXCLUDE_BLOCKS_STATIC = "?exclude=[hourly,minutely]";
    private static final String OPTIONS = "&units=uk2";

     /**
     * the static method getWeather returns
     * within a range of time
     * the first element in the list is the weather metrics at the sharp hour just before
     * the second element is the current weather metrics
     * the following 48 are 48 hours' data upcoming
     * @param a the address at which you want weather
     * @return a list of Weather collections
     * @throws WeatherDataNotFound in the event of a connection or other failure
     */
    public static List<WeatherMetrics> getWeather(Address a) throws WeatherDataNotFound{
        JsonObject jsobj;
        try {
            jsobj = APIRead.HTTPToJSON(DARKSKY_ADDR + KEY +
                    a.getLatitude() + "," + a.getLongtitude() + EXCLUDE_BLOCKS + OPTIONS);

        }
        catch (IOException e){
            System.out.printf("fail to connect dark sky");
            throw new WeatherDataNotFound("fail to connect dark sky");
        }
        // time zone offset

        JsonNumber jOffset = jsobj.getJsonNumber("offset");
        int offset = 0;
        if (jOffset != null) offset = jOffset.intValue() * 3600;

        // 1 for the o'clock just before
        // 1 for current, 48 for the future
        List<WeatherMetrics> output = new ArrayList<>(50);

        // handle the currently block in the json object
        // get the data at the current time
        JsonObject currentObj = jsobj.getJsonObject("currently");
        WeatherMetrics currentMetrics = new WeatherMetrics(currentObj,offset);

        // add the weathermetrics at the  o'clock just before the current time first,
        // than the current weathermetrics
        JsonArray ja = jsobj.getJsonObject("hourly").getJsonArray("data");
        JsonObject hrObj = ja.getJsonObject(0);
        output.add(new WeatherMetrics(hrObj,offset));

        output.add(currentMetrics);

        for (int i = 1; i < ja.size(); i++) {
            hrObj = ja.getJsonObject(i);
            output.add(new WeatherMetrics(hrObj,offset));
        }

        return output;
    }


    public static WeatherMetricsStatic getWeatherStatic(Address a) throws WeatherDataNotFound{
        JsonObject jsobj;
        try {
            jsobj = APIRead.HTTPToJSON(DARKSKY_ADDR + KEY +
                    a.getLatitude() + "," + a.getLongtitude() + EXCLUDE_BLOCKS_STATIC + OPTIONS);

        }
        catch (IOException e){
            System.out.printf("fail to connect dark sky");
            throw new WeatherDataNotFound("fail to connect dark sky");
        }
        JsonNumber jOffset = jsobj.getJsonNumber("offset");
        int offset = 0;
        if (jOffset != null) offset = jOffset.intValue() * 3600;

        return new WeatherMetricsStatic(jsobj,offset);
    }


    /**
     * Test method
     * @param args not required
     */
    public static void main(String[] args) {
        try{
            List<WeatherMetrics> weatherMetrics = getWeather(GeocodeParsing
                    .getAddress("Trinity College Cambridge").get(0));
        }
        catch (AddressNotFound e){

        }
        catch (WeatherDataNotFound e){

        }
    }
}
