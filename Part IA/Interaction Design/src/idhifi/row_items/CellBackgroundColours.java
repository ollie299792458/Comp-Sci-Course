package idhifi.row_items;

import javafx.scene.paint.Paint;

/**
 * Get JavaFX paint objects for weather metrics, with differeing colours for safe - moderate - dangerous
 *
 * Created by Harri on 20/05/2017.
 */
public class CellBackgroundColours {

    private static Paint safe = Paint.valueOf("addad3");
    private static Paint moderate = Paint.valueOf("fad0b8");
    private static Paint unsafe = Paint.valueOf("f37923");

    /**
     * Safe moderate or dangerous paint (Same for following methods for appropriate metrics)
     * @param speed Current wind speed
     * @return Paint with which to paint the cell
     */
    public static Paint windSpeed(double speed) {
        if(speed >= 20) return unsafe;
        else if (speed >= 12) return moderate;
        else return safe;
    }

    public static Paint temperature(double temperature) {
        if(temperature < 3 || temperature > 30) return unsafe;
        else if (temperature < 7 || temperature > 25) return moderate;
        else return safe;
    }

    public static Paint visibility(double visibility) {
        if(visibility < 0.05) return unsafe;
        else if (visibility < 0.1) return moderate;
        else return safe;
    }

    public static Paint humidity(double humidity) {
        if(humidity > 85) return unsafe;
        else if (humidity < 5 || humidity > 70) return moderate;
        else return safe;
    }

    public static Paint precipitation(double precipitation) {
        if(precipitation > 70) return unsafe;
        else if (precipitation > 40) return moderate;
        else return safe;
    }

    public static Paint cloudCover(double cc) {
        if (cc < 5 || cc > 90) return moderate;
        else return safe;
    }
}
