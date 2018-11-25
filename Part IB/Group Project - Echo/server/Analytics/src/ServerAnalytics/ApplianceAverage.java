package ServerAnalytics;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.TreeMap;

public class ApplianceAverage {

    /* timeUnitDeviceEnergies maps the time unit ids to a HashMap which maps appliance ids to a TreeMap which maps
    Unix timestamps to an average energy computed for that time unit for that device ending at that timestamp
     */
    private static HashMap<Integer, HashMap<Integer, TreeMap<Integer, Double>>> timeUnitDeviceEnergies;
    private static JsonNode customers;
    private static int endTime = (int) (System.currentTimeMillis()/1000L);

    /* Read in the customer ids from customer_ids.json as soon as the program is run
    Read in the saved appliance averages hashmap using readMap()
     */
    static {
        try {
            customers = new ObjectMapper().readTree(new File("customer_ids.json"));
        } catch(IOException e) {}
        readMap();
    }

    /* If there is no saved averages appliance hashmap then create a new one, and put in the time units */
    private static void populateMap() {
        timeUnitDeviceEnergies = new HashMap<>();
        timeUnitDeviceEnergies.put(50, new HashMap<>());
        timeUnitDeviceEnergies.put(40, new HashMap<>());
        timeUnitDeviceEnergies.put(30, new HashMap<>());
    }

    /* Read in a serialised averages hashmap if there is one, otherwise create a new one usinf populateMap()
    */
    private static void readMap() {
        try {
            FileInputStream fis = new FileInputStream("applianceAverages");
            ObjectInputStream ois = new ObjectInputStream(fis);

            try {
                timeUnitDeviceEnergies = (HashMap<Integer, HashMap<Integer, TreeMap<Integer, Double>>>) ois.readObject();
                if(timeUnitDeviceEnergies == null) populateMap();
            } catch (ClassNotFoundException e){}
        } catch(IOException e) {
            populateMap();
        }
    }

    /* Get the average usage for each device over the last month (ending at endstamp). Then place these into
    timeUnitDeviceEnergies
     */
    private static void monthlyAverage() throws IOException {
        HashMap<Integer, Double> deviceAverage = totalEnergy(50, 2635200);
        HashMap<Integer, TreeMap<Integer, Double>> deviceEnergiesDay = timeUnitDeviceEnergies.get(50);

        for(Integer id : deviceAverage.keySet()) {
            TreeMap<Integer, Double> applianceEnergies = deviceEnergiesDay.computeIfAbsent(id, t -> new TreeMap<>());
            applianceEnergies.put(endTime, deviceAverage.get(id));
        }
    }

    /* Get the average usage for each device over the last day (ending at endstamp). Then place these into
    timeUnitDeviceEnergies
     */
    private static void dailyAverage() throws IOException {
        HashMap<Integer, Double> deviceAverage = totalEnergy(40, 86400);
        HashMap<Integer, TreeMap<Integer, Double>> deviceEnergiesDay = timeUnitDeviceEnergies.get(40);

        for(Integer id : deviceAverage.keySet()) {
            TreeMap<Integer, Double> applianceEnergies = deviceEnergiesDay.computeIfAbsent(id, t -> new TreeMap<>());
            applianceEnergies.put(endTime, deviceAverage.get(id));
        }
    }

    /* Get the average usage for each device over the last hour (ending at endstamp). Then place these into
    timeUnitDeviceEnergies
     */
    private static void hourlyAverage() throws IOException {
        HashMap<Integer, Double> deviceAverage = totalEnergy(30, 3600);
        HashMap<Integer, TreeMap<Integer, Double>> deviceEnergiesHour = timeUnitDeviceEnergies.get(30);

        for(Integer id : deviceAverage.keySet()) {
            TreeMap<Integer, Double> applianceEnergies = deviceEnergiesHour.computeIfAbsent(id, t -> new TreeMap<>());
            applianceEnergies.put(endTime, deviceAverage.get(id));
        }
    }

    /* Calculates the average usage per device over the length of time, length, using the time unit, time_units.
    Returns a hashmap of appliance IDs mapped to the average rate of energy consumption over that time period
     */
    private static HashMap<Integer, Double> totalEnergy(int time_units, int length) throws IOException {
        HashMap<Integer, Double> deviceTotalEnergies = new HashMap<>();
        HashMap<Integer, Integer> totalDevicesByID = new HashMap<>();
        long startTime = endTime - length;
        ObjectMapper mapper = new ObjectMapper();

        for(JsonNode custID : customers) {
            URL custURL = new URL("https://api.energybudget.co.uk/observed_data?customer=" + custID.toString()
                    .replace("\"", "") + "&sts=" + Long.toString(startTime) + "&ets=" +
                    Long.toString(endTime) + "&time_units=" + Integer.toString(time_units));

            JsonNode customer = mapper.readTree(custURL);
            JsonNode data = customer.get("data");

            if (data.get(0) != null) {
                if(data.get(0).get("appliance_types") != null) {
                    JsonNode appliances = data.get(0).get("appliance_types");
                    for(JsonNode appliance : appliances) {
                        int applianceID = appliance.get("appliance_type_id").asInt();
                        if(appliance.get("appliances") != null) {
                            for(JsonNode app : appliance.get("appliances")) {
                                totalDevicesByID.merge(applianceID, 1, (a, b) -> a+b);
                                deviceTotalEnergies.merge(applianceID, app.get("powers").get(0).asDouble(), (a,b) -> a+b);
                            }
                        }
                    }
                }
            }
        }

        HashMap<Integer, Double> devicePercentageEnergies = new HashMap<>();
        for(Integer id : deviceTotalEnergies.keySet()) {
            devicePercentageEnergies.put(id, deviceTotalEnergies.get(id)/totalDevicesByID.get((id)));
        }

        return devicePercentageEnergies;
    }

    /* Serializes timeUnitDeviceEnergies and place it in a file called applianceAverages
     */
    public static void serialize() throws IOException {
        FileOutputStream fos =  new FileOutputStream("applianceAverages");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(timeUnitDeviceEnergies);
        fos.close();
        oos.close();
    }

    /* Take command line arguments to decide whether to compute hourly, daily or mothly averages
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            try {
                monthlyAverage();
                dailyAverage();
                hourlyAverage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String in = args[0];
            try {
                switch (in) {
                    case "m":
                        monthlyAverage();
                        break;
                    case "d":
                        dailyAverage();
                        break;
                    case "h":
                        hourlyAverage();
                        break;
                }
            } catch (IOException e) {}
        }
        System.out.println(timeUnitDeviceEnergies);
        try {
            serialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
