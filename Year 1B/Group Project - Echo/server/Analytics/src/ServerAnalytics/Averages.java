package ServerAnalytics;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.TreeMap;

public class Averages {

    /* energyTimeUnit maps the time unit id to a treemap , which maps timestamps to the average energy usage over that
        time unit ending at that timestamp
     */
    private static HashMap<Integer, TreeMap<Integer, Double>> energyTimeUnit;
    private static JsonNode customers;
    private static int endTime;

    /* Read in the customer ids from customer_ids.json as soon as the program is run
    Read in the saved averages hashmap using readMap()
     */
    static {
        try {
            customers = new ObjectMapper().readTree(new File("customers_ids.json"));
        } catch(IOException e) {}
        readMap();
    }

    /* Read in a serialised averages hashmap if there is one, otherwise create a new one usinf populateMap()
    */
    private static void populateMap() {
        energyTimeUnit = new HashMap<>();
        energyTimeUnit.put(50, new TreeMap<>());
        energyTimeUnit.put(40, new TreeMap<>());
        energyTimeUnit.put(30, new TreeMap<>());
    }


    private static void readMap() {
        try {
            FileInputStream fis = new FileInputStream("averages");
            ObjectInputStream ois = new ObjectInputStream(fis);

            try {
                energyTimeUnit = (HashMap<Integer, TreeMap<Integer, Double>>) ois.readObject();
                if(energyTimeUnit == null) populateMap();
            } catch (ClassNotFoundException e){}
        } catch(IOException e) {
            populateMap();
        }
    }

    //Calculate the monthly usage
    private static void monthlyAverage() throws IOException {
        energyTimeUnit.get(50).put(endTime, totalEnergy(50, 2635200));
    }

    //Calculate the daily usage
    private static void dailyAverage() throws IOException {
        energyTimeUnit.get(40).put(endTime, totalEnergy(40, 86400));
    }

    //Calculate the hourly usage
    private static void hourlyAverage() throws IOException {
        energyTimeUnit.get(30).put(endTime, totalEnergy(30, 3600));
    }

    /* Calculates the average usage pf the house over the length of time, length, using the time unit, time_units.
    Returns a double that is the average rate of energy usage of the house over this period
     */
    private static double totalEnergy(int time_units, int length) throws IOException {
        double totalEnergy=0;
        endTime = (int) System.currentTimeMillis()/1000;
        System.out.println(endTime);
        long startTime = endTime - length;
        ObjectMapper mapper = new ObjectMapper();
        int custWithData=0;

        for(JsonNode custID : customers) {
            URL custURL = new URL("https://api.energybudget.co.uk/observed_data?customer=" + custID.toString() + "&sts=" + Long.toString(startTime) + "&ets=" +
                    Long.toString(endTime) + "&time_units=" + Integer.toString(time_units));

            JsonNode customer = mapper.readTree(custURL);
            JsonNode data = customer.get("data");

            if (data.get(0) != null) {
                if(data.get(0).get("root_powers") != null) {
                    custWithData++;
                    JsonNode energies = data.get(0).get("root_powers");
                    totalEnergy += energies.get(0).asDouble();
                }
            }
        }

        System.out.println(totalEnergy);

        return totalEnergy/custWithData;
    }

    /* Serializes energyTimeUnit and place it in a file called applianceAverages
     */
    public static void serialize() throws IOException {
        FileOutputStream fos =  new FileOutputStream("averages");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(energyTimeUnit);
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
        try {
            serialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
