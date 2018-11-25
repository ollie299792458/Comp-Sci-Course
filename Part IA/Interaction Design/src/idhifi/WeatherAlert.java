package idhifi;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;

/**
 * Class defining a weather alert, to be used in conjunction with the darksky api
 *
 * Created by ybai on 18/05/2017.
 */
public class WeatherAlert {
    private String title;
    private String description;
    private int severity = 0;
    /* 1,2,3
    *  1 - advisory; 2 – watch; 3 – warning;
    *  "advisory" (an individual should be aware of potentially severe weather),
    *  "watch" (an individual should prepare for potentially severe weather),
    *  or "warning" (an individual should take immediate action to protect themselves
    *   and others from potentially severe weather).
    * */
    private String[] regions = null;
    private long issueTm = Long.MIN_VALUE; // issued time
    private long expTm = Long.MIN_VALUE; // expiration time
    private String uri; // An HTTP(S) URI that one may refer to for detailed information about the alert.


    public WeatherAlert(JsonObject jAlert, int offset) {
        JsonString js = jAlert.getJsonString("description");
        if (js != null) description = js.getString();

        js = jAlert.getJsonString("title");
        if (js != null) title = js.getString();

        js = jAlert.getJsonString("uri");
        if (js != null) uri = js.getString();

        js = jAlert.getJsonString("severity");
        if (js != null) {
            switch (js.getString()){
                case "advisory":
                    severity = 1;
                    break;
                case "watch":
                    severity = 2;
                    break;
                case "warning":
                    severity = 3;
                    break;
            }
        }

        JsonArray jRegions = jAlert.getJsonArray("regions");
        if (jRegions != null) {
            regions = new String[jRegions.size()];
            for (int i = 0; i < jRegions.size(); i++) {
                regions[i] = jRegions.getString(i);
            }
        }

        JsonNumber jNum = jAlert.getJsonNumber("time");
        if (jNum != null) issueTm = jNum.longValue() + offset;

        jNum = jAlert.getJsonNumber("expires");
        if (jNum != null) expTm = jNum.longValue() + offset;

    }

    public String toString() {
        return "There aren't currently any warnings.";
    }
}
