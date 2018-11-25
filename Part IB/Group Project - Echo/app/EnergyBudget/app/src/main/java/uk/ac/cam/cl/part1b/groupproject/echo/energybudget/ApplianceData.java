package uk.ac.cam.cl.part1b.groupproject.echo.energybudget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;

public class ApplianceData {
    private UserData user;

    private int id; // id of the appliance
    private int typeId;
    private String typeName; // human-readable

    private int channelId; //
    private int startTime; // Unix timestamp of first usage
    private int endTime; // Unix timestamp of last recorded usage(negative if still recording)

    private String manufacturer; // Will probably be empty
    private String modelNumber; // Also probably empty
    private String mac; // sensor mac
    private TreeMap<Integer, Integer> numUsagesDaily = new TreeMap<>(Comparator.reverseOrder());
    private TreeMap<Integer, Integer> timeUsageDaily = new TreeMap<>(Comparator.reverseOrder());

    //For each time unit put timestamp and average usage
    private UsageData usage = new UsageData();

    /**
     * @param json Customer Metadata meter json
     */
    public ApplianceData(UserData user, JSONObject json) throws JSONException {
        this.user = user;
        id = json.getInt("appliance_id");

        JSONObject typeInfo = json.getJSONObject("observation_target");
        typeId = typeInfo.getInt("appliance_type_id");
        typeName = typeInfo.getString("name");

        channelId = json.getInt("channel_id");
        startTime = json.getInt("start_timestamp");
        endTime = json.isNull("end_datetime") ? -1 : json.getInt("end_datetime");

        manufacturer = json.isNull("manufacturer") ? "" : json.getString("manufacturer");
        modelNumber = json.isNull("model_number") ? "" : json.getString("model_number");
        mac = json.getString("mac");
    }

    public void updateData(TimeUnit timeUnit, TreeMap<Integer, Double> usage) {
        this.usage.putRange(timeUnit, usage);
    }

    /**
     * Get range of usage.
     * If endTime is larger then current time it will force fetch.
     * @param unit
     * @param endTime
     * @param numUnits
     * @param callback
     */
    public void getRange(TimeUnit unit, int endTime, int numUnits, Consumer<TreeMap<Integer, Double>> callback) {
        int startTime = endTime - numUnits * TimeUnit.toSec(unit);
        if (usage.hasRange(unit, startTime, endTime)) {
            callback.accept(usage.getRange(unit, startTime, endTime));
            return;
        }

        user.fetchRange(unit, endTime, numUnits, () ->
            callback.accept(usage.getRange(unit, startTime, endTime))
        );
    }

    public int getId() {
        return id;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getChannelId() {
        return channelId;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public String getMac() {
        return mac;
    }

    public UsageData getUsageData() {
        return usage;
    }

    public TreeMap<Integer, Integer> getNumUsagesDaily() { return numUsagesDaily; }

    public TreeMap<Integer, Integer> getTimeUsageDaily() { return timeUsageDaily; }
}
