package uk.ac.cam.cl.part1b.groupproject.echo.energybudget;

import android.util.Log;
import android.util.SparseArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.TreeMap;
import java.util.function.Consumer;

import java.util.Map;

public class UserData {
    private static String TAG = "UserData";
    // Global access to the main user data
    private static UserData user = null;

    public static String CUSTOMER = "0055_UK00000025";

    /*usable IDs: 4 - three fridges, 6, 7, 8 - v.high usage, 9, 12-two TVs,
    15- high outlet load usage but crashes on usage, 16-very high oven use,
    17 - have a heater!!, 18, 19, 21, 24, 25 - heater and PV, good variety,
    26, 27, 28 - only two categories, 29 - outlet load, 30, 31 - have a humidifier!, 32-outlet load,
    33,34,35 - 4 TVs, 36, 37, 39, 40, 41 - use their PV, 42, 43, 44, 49, 50, 53, 55 - heater usage
    (also 4 appliance badges!), 56, 57, 58, 59, 62, 66, 67

    best to use 25, 29, 41, 31, 55?
    */

    /**
     * DEPRECATED: Please avoid using.
     * Returns the user is it exists or null otherwise
     * @return the user
     */
    public static UserData getUserSync() {
        return user;
    }

    /**
     * Fetch the user data through an API request and pass it to callback when ready.
     * @param callback Function to be called when user is ready.
     */
    public static void getUser(Consumer<UserData> callback) {
        if (user == null) {
            ApiRequestTask task = new ApiRequestTask(userObj -> {
                try {
                    user = new UserData(userObj);
                    callback.accept(user);
                    user.fetchRecentUsage();
                } catch (JSONException e) {
                    Log.w(TAG, "Failed to parse user data.");
                }
            }, e -> {
                Log.w(TAG, "Failed to parse user data.");
            });

            String queryString = String.format(Locale.US,
                    "https://api.energybudget.co.uk/get_customer?" +
                            "customer=%s", CUSTOMER);
            try {
                URL url = new URL(queryString);
                task.execute(url);
            } catch (MalformedURLException e) {
                Log.w(TAG, e.getMessage());
                e.printStackTrace();
            }
            return;
        }
        callback.accept(user);
    }

    private String customerId;
    private String postcode; // Only the first 4 digits provided


    public SparseArray<ApplianceData> appliances = new SparseArray<>();
    private UsageData totalUsage = new UsageData();

    /**
     * @param json CM customer json
     * @throws JSONException if any of the required field are missing in the Json
     */
    UserData(JSONObject json) throws JSONException {
        customerId = json.getString("customer");
        postcode = json.getString("postcode");

        JSONArray meters = json.getJSONArray("meters");
        int len = meters.length();

        for (int i = 0; i < len; i += 1) {
            JSONObject meter = meters.getJSONObject(i);
            ApplianceData appliance = new ApplianceData(this, meter);
            appliances.put(appliance.getId(), appliance);
        }
    }

    public void fetchRecentUsage() {
        int now = TimeUnit.nowSec();
        fetchRange(TimeUnit.HOUR, now, 48);
        fetchRange(TimeUnit.DAY, now, 62);
        fetchRange(TimeUnit.MONTH, now, 24);
        fetchRange(TimeUnit.YEAR, now, 2);
    }


    public void fetchRange(TimeUnit unit, int endTime, int numUnits) {
        fetchRange(unit, endTime, numUnits, () -> {});
    }

    /**
     * Will fetch range of data for all appliances, regardless of if it is cached or not.
     */
    public void fetchRange(TimeUnit unit, int endTime, int numUnits, Runnable onSuccess) {
        // We don't support weekly yet
        if (unit == TimeUnit.WEEK) {
            unit = TimeUnit.DAY;
            numUnits *= 7;
            // make sure you get the whole week before
            numUnits += 7;
        }

        int timeUnitId = TimeUnit.toId(unit);

        int unitSecs = TimeUnit.toSec(unit);

        int sts = endTime - (unitSecs * numUnits);
        int ets = endTime + 1;

        String queryString = String.format(Locale.US,
                "https://api.energybudget.co.uk/observed_data?" +
                        "customer=%s&sts=%d&ets=%d&time_units=%d", CUSTOMER, sts, ets, timeUnitId);

        try {
            URL url = new URL(queryString);
            ApiRequestTask task = new ApiRequestTask(dataObj -> {
                try {
                    updateData(dataObj);
                    onSuccess.run();
                } catch (JSONException e) {
                    Log.w(TAG, "Invalid Json received.");
                    Log.w(TAG, e.getMessage());
                }
            }, e -> {});
            task.execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param applianceID
     * @return the ApplianceData, or null if id is invalid
     */
    public ApplianceData getAppliance(int applianceID) {
        return appliances.get(applianceID);
    }

    /**
     * Helper for json parsing int array.
     */
    private int[] convertJsonIntArray(JSONArray json) throws JSONException {
        int len = json.length();

        int[] result = new int[len];
        for (int i = 0; i < len; i += 1) {
            result[i] = json.getInt(i);
        }
        return result;
    }

    /**
     * Helper for json parsing double array.
     */
    private double[] convertJsonDoubleArray(JSONArray json) throws JSONException {
        int len = json.length();

        double[] result = new double[len];
        for (int i = 0; i < len; i += 1) {
            if (json.isNull(i)) {
                result[i] = 0.0;
            } else {
                result[i] = Math.max(0.0, json.getDouble(i));
            }
        }
        return result;
    }

    /**
     * Helper for json parsing to build a map from two arrays
     */
    private TreeMap<Integer, Double> buildUsageMap(int[] timestamps, double[] powers) throws JSONException {
        if (powers.length != timestamps.length) {
            throw new JSONException("Timestamps and data do not match.");
        }

        TreeMap<Integer, Double> usage = new TreeMap<>();
        for (int l = 0; l < powers.length; l += 1) {
            usage.put(timestamps[l], powers[l]);
        }
        return usage;
    }

    /**
     * Update data with a json. Will parse json to extract data.
     * @param json json with the data
     * @throws JSONException if json is not the expected format
     */
    private void updateData(JSONObject json) throws JSONException {
        JSONArray data = json.getJSONArray("data");
        int dataLen = data.length();

        // I don't know why this is an array
        for (int i = 0; i < dataLen; i += 1) {
            JSONObject datum = data.getJSONObject(i);

            TimeUnit unit = TimeUnit.fromId(datum.getInt("time_unit_id"));
            int[] timestamps = convertJsonIntArray(datum.getJSONArray("timestamps"));

            JSONArray types = datum.getJSONArray("appliance_types");
            int typesLen = types.length();

            for (int j = 0; j < typesLen; j += 1) {
                JSONObject type = types.getJSONObject(j);

                JSONArray appliances = type.getJSONArray("appliances");
                int appliancesLen = appliances.length();

                for (int k = 0; k < appliancesLen; k += 1) {
                    JSONObject appliance = appliances.getJSONObject(k);

                    int applianceId = appliance.getInt("appliance_id");
                    if (this.appliances.indexOfKey(applianceId) < 0) {
                        Log.w(TAG, "Unrecognised appliance id. Skipping.");
                        continue;
                    }

                    double[] powers = convertJsonDoubleArray(appliance.getJSONArray("powers"));

                    TreeMap<Integer, Double> usage = buildUsageMap(timestamps, powers);

                    this.appliances.get(applianceId).updateData(unit, usage);
                }
            }

            double[] totalPowers = convertJsonDoubleArray(datum.getJSONArray("root_powers"));
            TreeMap<Integer, Double> totalMap = buildUsageMap(timestamps, totalPowers);

            this.totalUsage.putRange(unit, totalMap);
        }
    }

    /**
     * Get range of data.
     * If endTime is larger then current time it will force fetch.
     * @param unit TimeUnit range in it
     * @param endTime range should end before endTime
     * @param numUnits number of units to pull for the range
     * @param callback the function to call with the resulting range
     */
    public void getRange(TimeUnit unit, int endTime, int numUnits, Consumer<TreeMap<Integer, Double>> callback) {
        int startTime = endTime - numUnits * TimeUnit.toSec(unit);
        if (totalUsage.hasRange(unit, startTime, endTime)) {
            callback.accept(totalUsage.getRange(unit, startTime, endTime));
            return;
        }

        user.fetchRange(unit, endTime, numUnits, () ->
            callback.accept(totalUsage.getRange(unit, startTime, endTime))
        );
    }

    public Map<Integer, Double> percentageChangeAppliance(int timeUnit) {
        return null;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getPostcode() {
        return postcode;
    }

    public SparseArray<ApplianceData> getAppliances() {
        return appliances;
    }

    public UsageData getTotalUsageData() {
        return totalUsage;
    }
}
