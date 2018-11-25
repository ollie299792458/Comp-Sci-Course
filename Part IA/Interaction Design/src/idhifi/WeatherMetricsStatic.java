package idhifi;

import javafx.scene.control.Alert;

import javax.json.JsonNumber;
import javax.json.JsonObject;

/**
 * Similar to WeatherMetrics, but for metrics that don't change throughout the day
 *
 * Created by ybai on 18/05/2017.
 */
public class WeatherMetricsStatic {
    private long sunriseTime = Long.MIN_VALUE;
    private long sunsetTime = Long.MIN_VALUE;

    private double nearestStormDistance = Double.NaN;
    private int nearestStormBearing = Integer.MIN_VALUE;
    private WeatherAlert alert = null;

    private long time = Long.MIN_VALUE;

    public WeatherMetricsStatic(JsonObject jO, int offset) {
        JsonObject jCrt = jO.getJsonObject("currently");

        JsonNumber jn = jCrt.getJsonNumber("nearestStormDistance");
        if (jn != null) nearestStormDistance = jn.doubleValue();

        jn = jCrt.getJsonNumber("nearestStormBearing");
        if (jn != null) nearestStormBearing = jn.intValue();

        jn = jCrt.getJsonNumber("time");
        if (jn != null) time = jn.longValue() + offset;

        JsonObject jAlerts = jO.getJsonObject("alerts");
        if (jAlerts != null) alert = new WeatherAlert(jAlerts,offset);

        JsonObject jDaily = jO.getJsonObject("daily")
                .getJsonArray("data").getJsonObject(0);
        jn = jDaily.getJsonNumber("sunriseTime");
        if (jn != null) sunriseTime = jn.longValue() + offset;

        jn = jDaily.getJsonNumber("sunsetTime");
        if (jn != null) sunsetTime = jn.longValue() + offset;

    }

    public long getSunriseTime() {
        return sunriseTime;
    }

    public long getSunsetTime() {
        return sunsetTime;
    }

    public double getNearestStormDistance() {
        return nearestStormDistance;
    }

    public int getNearestStormBearing() {
        return nearestStormBearing;
    }

    public WeatherAlert getAlert() {
        return alert;
    }

    public long getTime() {
        return time;
    }
}
