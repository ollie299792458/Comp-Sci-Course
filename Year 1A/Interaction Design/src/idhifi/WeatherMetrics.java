package idhifi;

import javax.json.JsonNumber;
import javax.json.JsonObject;

/**
 * Created by ybai on 18/05/2017.
 */

/*
* order:
* wind speed and bearing
* temp (degree celsius)
* precipitationProb
* precipitationInten (Millimeters per hour)
* visibility (in miles)
* humidity
* cloud cover (percentage)
* */

public class WeatherMetrics {
    private double windSpeed = Double.NaN;
    private int windBearing = Integer.MIN_VALUE;
    private double temperature = Double.NaN;
    private double preticpProb = Double.NaN;
    private double preticpInten = Double.NaN;
    private double visibility = Double.NaN;
    private double humidity = Double.NaN;
    private double cloudCover = Double.NaN;
    private long time = Long.MIN_VALUE;

    // the tag names for double values in the hour object
    private static final String[] JSON_FIELDS_DOUBLES = {
            "windSpeed"
            ,"apparentTemperature"
            ,"precipProbability"
            ,"precipIntensity"
            ,"visibility"
            ,"humidity"
            ,"cloudCover"};

    /*
    * if some of the data requested is unavailable,
    * depending on their types
    * return null WeatherAlert
    * return NaN Double
    * return MIN_VALUE Long
    * */
    public WeatherMetrics(JsonObject jData, int offset) {
        double[] doubles = new double[JSON_FIELDS_DOUBLES.length];
        for (int i = 0; i < JSON_FIELDS_DOUBLES.length; i++) {
            JsonNumber jn = jData.getJsonNumber(JSON_FIELDS_DOUBLES[i]);
            if (jn == null) doubles[i] = Double.NaN;
            else doubles[i] = jn.doubleValue();
        }
        // handle int: wind bearing separately
        JsonNumber jn= jData.getJsonNumber("windBearing");
        int windBearing = Integer.MIN_VALUE;
        if (jn != null) windBearing = jn.intValue();

        // handle long: time separately
        jn = jData.getJsonNumber("time");
        long time = Long.MIN_VALUE;
        if (jn != null) time = jn.longValue() + offset;


        this.windSpeed = doubles[0];
        this.windBearing = windBearing;
        this.temperature = doubles[1];
        this.preticpProb = doubles[2];
        this.preticpInten = doubles[3];
        this.visibility = doubles[4];
        this.humidity = doubles[5];
        this.cloudCover = doubles[6];
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getPreticpProb() {
        return preticpProb;
    }

    public double getPreticpInten() {
        return preticpInten;
    }

    public int getWindBearing() {
        return windBearing;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getVisibility() {
        return visibility;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getCloudCover() {
        return cloudCover;
    }

    public long getTime() {
        return time;
    }
}
