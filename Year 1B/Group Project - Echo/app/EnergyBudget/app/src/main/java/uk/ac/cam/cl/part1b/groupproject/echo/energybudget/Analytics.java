package uk.ac.cam.cl.part1b.groupproject.echo.energybudget;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import uk.ac.cam.cl.part1b.groupproject.echo.energybudget.fragments.tips.Tip;
import uk.ac.cam.cl.part1b.groupproject.echo.energybudget.fragments.tips.TipType;

public class Analytics {

    public UserData userData;
    private static SparseArray<Double> efficiencyBenchmark = new SparseArray<>();
    private static SparseArray<Double> usagePercentageIncreases = new SparseArray<>();

    private static HashMap<TimeUnit, TreeMap<Integer, Double>> percentageUsage = new HashMap<>();
    private static SparseArray<TimeUnit> deviceUseTime = new SparseArray<>();
    private static double threshold = 0.5;
    private static double weeklyIncreaseThreshold = 0.15;
    private static double efficiencyComparisonBenchmark = 10.0;

    public static LinkedHashMap<Integer, String> nameMap;

    private static int[] applianceIDs = {5,6,8,9,15,16,20,24,28,30,31,33,34,35,36,37,41,42,43,44,304,997};
    private final static String tree = "tree";
    private final static String flower = "flower";
    private final static String animal = "animal";
    private final static String mountain = "mountain";
    private static final String kitchen = "kitchen";
    private static final String bathroom = "bathroom";
    private static final String entertainment = "entertainment";
    private static final String heating = "heating";
    private static final String light = "light";
    private static final HashMap<String, TimeUnit> houseTimeUnits;
    private static final HashMap<String, Integer> housePercentages;
    public static Map<String, Integer> categoryToId;
    static{
        // ids for the appliance badges
        categoryToId = new HashMap<>();
        categoryToId.put(kitchen, 9);
        categoryToId.put(heating, 41);
        categoryToId.put(light, 304);
        categoryToId.put(bathroom, 43);
        categoryToId.put(entertainment, 30);
        // percentage thresholds for awarding the house badges
        housePercentages = new HashMap<>();
        housePercentages.put(tree, 5);
        housePercentages.put(flower, 5);
        housePercentages.put(animal, 10);
        housePercentages.put(mountain, 10);
        houseTimeUnits = new HashMap<>();
        //time frames for awarding house badges
        houseTimeUnits.put(tree, TimeUnit.DAY);
        houseTimeUnits.put(flower, TimeUnit.WEEK);
        houseTimeUnits.put(animal, TimeUnit.WEEK);
        houseTimeUnits.put(mountain, TimeUnit.MONTH);

        deviceUseTime.put(5, TimeUnit.HOUR);
        deviceUseTime.put(6, TimeUnit.MIN);
        deviceUseTime.put(8, TimeUnit.HOUR);
        deviceUseTime.put(9, TimeUnit.MIN);
        deviceUseTime.put(15, TimeUnit.MIN);
        deviceUseTime.put(16, TimeUnit.HALFHOUR);
        deviceUseTime.put(20, TimeUnit.MIN);
        deviceUseTime.put(24, TimeUnit.MONTH);
        deviceUseTime.put(28, TimeUnit.MIN);
        deviceUseTime.put(30, TimeUnit.HALFHOUR);
        deviceUseTime.put(31, TimeUnit.MIN);
        deviceUseTime.put(33, TimeUnit.HALFHOUR);
        deviceUseTime.put(34, TimeUnit.HOUR);
        deviceUseTime.put(35, TimeUnit.HOUR);
        deviceUseTime.put(36, TimeUnit.HOUR);
        deviceUseTime.put(37, TimeUnit.MIN);
        deviceUseTime.put(41, TimeUnit.HOUR);
        deviceUseTime.put(42, TimeUnit.HOUR);
        deviceUseTime.put(43, TimeUnit.HOUR);
        deviceUseTime.put(44, TimeUnit.MONTH);
        deviceUseTime.put(304, TimeUnit.HALFHOUR);

        for (int i : applianceIDs) {
            efficiencyBenchmark.put(i,efficiencyComparisonBenchmark);
            usagePercentageIncreases.put(i,1 + weeklyIncreaseThreshold);
        }
        nameMap = new LinkedHashMap<>();
        nameMap.put(1, "Air cleaner");
        nameMap.put(2, "Air conditioner");
        nameMap.put(3, "Ceiling fan");
        nameMap.put(4, "Iron");
        nameMap.put(5, "Washing machine");
        nameMap.put(6, "Coffee machine");
        nameMap.put(7, "Desk lamp");
        nameMap.put(8, "Electric heater");
        nameMap.put(9, "Kettle");
        nameMap.put(10, "Electric piano");
        nameMap.put(11, "Electric pot");
        nameMap.put(12, "Floor heating");
        nameMap.put(13, "Food mixer");
        nameMap.put(14, "Gaming");
        nameMap.put(15, "Hair dryer");
        nameMap.put(16, "Humidifier");
        nameMap.put(17, "Scanner");
        nameMap.put(18, "Heated table");
        nameMap.put(19, "Laptop");
        nameMap.put(20, "Microwave");
        nameMap.put(21, "Music player");
        nameMap.put(22, "Overhead light");
        nameMap.put(23, "Printer");
        nameMap.put(24, "Fridge");
        nameMap.put(25, "Rice cooker");
        nameMap.put(26, "Tablet");
        nameMap.put(27, "Telephone");
        nameMap.put(28, "Toaster");
        nameMap.put(29, "Toilet");
        nameMap.put(30, "TV");
        nameMap.put(31, "Vacuum cleaner");
        nameMap.put(32, "Vent fan");
        nameMap.put(33, "Video recorder");
        nameMap.put(34, "Dishwasher");
        nameMap.put(35, "Oven");
        nameMap.put(36, "Bread machine");
        nameMap.put(37, "Inductive Heater");
        nameMap.put(38, "Bath dryer");
        nameMap.put(39, "Ecocute");
        nameMap.put(40, "Heater");
        nameMap.put(41, "Water heater");
        nameMap.put(42, "Electric cooker");
        nameMap.put(43, "Tumble dryer");
        nameMap.put(44, "Freezer");
        nameMap.put(302, "Sewing machine");
        nameMap.put(304, "Light");
        nameMap.put(310, "Outlet load");
        nameMap.put(995, "Main breaker");
        nameMap.put(997, "PV");
    }

    private static Map<String, Boolean> badgesComputed;
    //badge state as integers to indicate levels for house badges
    private static Map<String, Integer> badges;
    public static Map<Integer, Integer> idMap;

    private ApplianceData appDataCache;

    public Analytics(UserData userData) {
        this.userData = userData;

        idMap = new HashMap<>();
        for (int j=0; j<userData.appliances.size(); j++){
            //for each available appliance for the user, map the typeId to actual id
            int key = userData.appliances.keyAt(j);
            int typeId = userData.appliances.get(key).getTypeId();
            idMap.put(typeId, key);
        }
        //initialise badges computed
        badgesComputed = new HashMap<>();
        badgesComputed.put(tree, false);
        badgesComputed.put(flower, false);
        badgesComputed.put(animal, false);
        badgesComputed.put(mountain, false);
        badgesComputed.put(kitchen, false);
        badgesComputed.put(bathroom, false);
        badgesComputed.put(entertainment, false);
        badgesComputed.put(heating, false);
        badgesComputed.put(light, false);

        //initialise badges data
        badges = new HashMap<>();
        badges.put(tree, 0);
        badges.put(flower, 0);
        badges.put(animal, 0);
        badges.put(mountain, 0);
        badges.put(kitchen, 0);
        badges.put(bathroom, 0);
        badges.put(entertainment, 0);
        badges.put(heating, 0);
        badges.put(light, 0);

    }

    /* Badges checks
    * Badges class calls the check badge function for each badge */

    public void checkBadge(String category,  Consumer<Integer> callback) {
        //if badges already computed, no need to fetch data
        if(!badgesComputed.get(category)){
            //if it's a house badge
            if(houseTimeUnits.keySet().contains(category)){
                //fetch data for the corresponding time frame
                percentageChangeTotal(TimeUnit.toId(houseTimeUnits.get(category)), change ->{
                    //if the usage has increased
                    if (change > 0){
                        badges.put(category, 0);
                    } else {
                        //check if they get a level 3 badge
                        if ((Math.abs(change) >= housePercentages.get(category) * 3 || badges.get(category) == 3)
                                && !category.equals(mountain)) {
                            badges.put(category, 3);
                            //or if they get a level 2 badge
                        } else if ((Math.abs(change) >= housePercentages.get(category) * 2 || badges.get(category) == 2)
                                && !category.equals(mountain)) {
                            badges.put(category, 2);
                            //or a level 1 badge
                        } else if ((Math.abs(change) >= housePercentages.get(category) || badges.get(category) == 1)) {
                            badges.put(category, 1);
                            //otherwise no badge earned
                        } else {
                            badges.put(category, 0);
                        }
                    }
                    badgesComputed.put(category, true);
                    callback.accept(badges.get(category));
                });
            } else {
                //get the appliance badge category
                Integer id = categoryToId.get(category);
                if (idMap.containsKey(id)){
                    Integer realId = idMap.get(id);
                    //get the weekly percentage change
                    percentageChangeAppliance(realId, TimeUnit.toId(TimeUnit.WEEK), change -> {
                        //if there is an increase or a decrease of less than 5%, then no badge
                        if (change > -5) {
                            badges.put(category, 0);
                        } else {
                            //if more than 5% give them the badge
                            badges.put(category, 1);
                        }
                        badgesComputed.put(category, true);
                        callback.accept(badges.get(category));
                    });
                } else {
                    badges.put(category, 0);
                    badgesComputed.put(category, true);
                    callback.accept(badges.get(category));
                }
            }
        } else {
            callback.accept(badges.get(category));
        }
    }

    /* Tips checks */


    // Given the usage of a appliance over the last 4 weeks,
    // returns the usage in the last week compared to the 3 weeks before
    // as a fraction - (new usage) / (old usage)
    public double weeklyUsageComparison(TreeMap<Integer, Double> fourWeeksUsage) {
        double oldUsage = 0;
        double newUsage = 0;
        Object[] values = fourWeeksUsage.values().toArray();
        //for last 28 days of data
        for (int i=0; i<28; i++) {
            if (i<21) {
                // first 3 weeks
                oldUsage += (double)values[i];;
            }
            else {
                // most recent week
                newUsage += (double)values[i];
            }
        }
        oldUsage = oldUsage/3; //divide by 3 as 3 weeks measured
        return newUsage/oldUsage;
    }

    private int adviceCounter;
    // Given a consumer/callback of the List of tips generated, it returns a list of tips
    // to this callback.
    // This uses the weeklyUsageComparison method to test for tips
    public void generateAdviceTips(Consumer<List<Tip>> callback) {
        adviceCounter = 0;
        List<Tip> newAdvice = new ArrayList<>();
        // For all of the testable appliances
        for (int applianceID : idMap.keySet()) {
            // Get data for the appliance
            // See if data for appliance is already cached
            int realId = idMap.get(applianceID);
            if (appDataCache == null || appDataCache.getId() != realId) {
                appDataCache = userData.getAppliance(realId);
            }
            ApplianceData applianceData = userData.getAppliance(realId);
            if (applianceData == null) continue;
            //Get last 4 weeks of data
            applianceData.getRange(TimeUnit.DAY, TimeUnit.nowSec(), 28, result->{
                if(!result.isEmpty()) {
                    // If we have results
                    // find comparison to previous weeks
                    double percentageIncrease = weeklyUsageComparison(result);
                    //check if there has been a significant increase
                    if (percentageIncrease > usagePercentageIncreases.get(applianceID)) {
                        //Make the tip
                        String applianceName = nameMap.get(applianceID);
                        String title = "Increased " + applianceName + " Usage";
                        int intIncrease = (int)Math.round(percentageIncrease);
                        String sIncrease = String.format("%.1f", percentageIncrease);
                        String description = "This week you've used your " + applianceName + " " + sIncrease +
                                "% more than in previous weeks";
                        Tip t = new Tip(title, description, false, 1, TipType.ADVICE, applianceID);

                        newAdvice.add(t);
                    }
                }
                adviceCounter++;
                if (adviceCounter == idMap.size()) {
                    callback.accept(newAdvice);
                }
            });


        }
    }

    // Given an appliance id and the usage in the last 12 hours
    // returns the fraction of hourly usage compared to benchmark amount
    public double recentHourlyConsumption(TreeMap<Integer, Double> result, int applianceID) {
        double hourlyUsage = 0;
        //comparing last week with the 3 weeks before it
        Object[] values = result.values().toArray();
        Object[] iDs = result.keySet().toArray();
        for (int i=0; i<12; i++) {
            // Sum consumption in last 12 hours
            hourlyUsage += (double)values[i];;
        }
        double benchmarkAmount = Analytics.efficiencyBenchmark.get(applianceID);
        return hourlyUsage/benchmarkAmount;
    }

    int upgradeCounter;
    // Given a consumer/callback of the List of tips generated, it returns a list of upgrade tips
    // to this callback.
    // This uses the recentHourlyConsumption method to test for tips
    public void generateUpgradeTips(Consumer<List<Tip>> callback) {
        upgradeCounter = 0;
        List<Tip> newUpgrades = new ArrayList<>();
        for (int applianceID : idMap.keySet()) {
            int realId = idMap.get(applianceID);
            // See if data for appliance is already cached
            if (appDataCache == null || appDataCache.getId() != realId) {
                appDataCache = userData.getAppliance(realId);
            }
            ApplianceData applianceData = userData.getAppliance(realId);
            if (applianceData == null) continue;
            applianceData.getRange(TimeUnit.DAY, TimeUnit.nowSec(), 12, result ->{
                if (!result.isEmpty()) {
                    double efficiency = recentHourlyConsumption(result, applianceID);
                    //check if above the benchmark amount
                    if (efficiency > efficiencyBenchmark.get(applianceID)) {
                        //Make the tip
                        String applianceName = nameMap.get(applianceID);
                        String title = "Upgrade " + applianceName;
                        String sUsageComparison = String.format("%.1f", efficiency);
                        String description = "Your " + applianceName + " consumes " + sUsageComparison +
                                " % more than the recommended amount for benchmark devices";
                        Tip t = new Tip(title, description, false, 1, TipType.UPGRADE, applianceID);
                        newUpgrades.add(t);
                    }
                }
                upgradeCounter++;
                if (upgradeCounter == idMap.size()) {
                    callback.accept(newUpgrades);
                }
            });
        }

    }

    /* Returns a callback with the percentage change in energy usage for the house between the last
        two timeUnits. Less than 0 if energy saved
    */
    public void percentageChangeTotal(int timeUnit, Consumer<Double> callback) {
        TimeUnit unit = TimeUnit.fromId(timeUnit);

        userData.getRange(unit, TimeUnit.nowSec(), 2, result ->
                callback.accept(percentageChange(result))
        );
    }

    /* Returns a callback with the percentage change in energy usage for the appliance with the provided
     applianceID between the last two timeUnits. Less than 0 if energy saved
    */
    public void percentageChangeAppliance(int applianceID, int timeUnit, Consumer<Double> callback) {
        TimeUnit unit = TimeUnit.fromId(timeUnit);

        userData.getAppliance(applianceID).getRange(unit, TimeUnit.nowSec(), 2, result -> {
            callback.accept(percentageChange(result));
        });
    }

    /*Does the percentage change calculation */
    private double percentageChange(TreeMap<Integer, Double> times) {
        if(times.size() > 1) {
            int stamp = times.lastKey();
            double thisPeriod = times.get(stamp);
            double lastPeriod = times.get(times.lowerKey(stamp));

            return ((thisPeriod/lastPeriod - 1)*100);
        }
        return 0.0;
    }

    /* Calculates the number of usages and the time used in seconds of the
    appliance for the last day */
    public void calculateUsage(int applianceID) {
        ApplianceData app = userData.getAppliance(applianceID);
        int endStamp = TimeUnit.nowSec();
        TimeUnit t = deviceUseTime.get(applianceID);
        TreeMap<Integer, Integer> numUsagesDaily = app.getNumUsagesDaily();
        TreeMap<Integer, Integer> unitUsagesDaily = app.getTimeUsageDaily();
        if(!numUsagesDaily.isEmpty()) {
            if (endStamp < numUsagesDaily.firstKey() + 86400) {
                return;
            }
        }
        final int end = endStamp;

        Calendar time = new GregorianCalendar();
        time.setTimeZone(TimeZone.getDefault());

        userData.getRange(t, endStamp, 86400/TimeUnit.toSec(t), result -> {
            int numUsages = 0;
            int unitUsages = 0;
            Double[] energies = (Double[]) result.values().toArray();
            double appThreshold = threshold*TimeUnit.toSec(t);
            boolean isOn = false;
            if(energies[0] > appThreshold) {
                isOn = true;
                numUsages++;
                unitUsages++;
            }

            for(int i=1; i<energies.length; i++) {
                if(isOn) {
                    if(energies[i] < appThreshold) isOn = false;
                    else unitUsages++;
                } else {
                    if(energies[i] > appThreshold) {
                        isOn = true;
                        numUsages++;
                        unitUsages++;
                    }
                }
            }

            numUsagesDaily.put(end, numUsages);
            unitUsagesDaily.put(end, unitUsages*TimeUnit.toSec(t));
        });
    }

    /* Returns an estimate of the number of times the device has been used
    for up to a maximum of numDays days returned as a number of uses per day
     */
    public List<Integer> getNumUsage(int applianceID, int numDays) {
        calculateUsage(applianceID);
        ApplianceData app = userData.getAppliance(applianceID);
        List<Integer> usages = new ArrayList<>(app.getNumUsagesDaily().values());
        if(numDays > usages.size()) return usages;
        else { return usages
                .stream()
                .limit(numDays)
                .collect(Collectors.toList());
        }
    }

    /* Returns an estimate of the amount of time the device has been used in seconds
    for up to a maximum of numDays days returned as a list of seconds per day
     */
    public List<Integer> getSecUsage(int applianceID, int numDays) {
        calculateUsage(applianceID);
        ApplianceData app = userData.getAppliance(applianceID);
        List<Integer> usages = new ArrayList<>(app.getTimeUsageDaily().values());
        if(numDays > usages.size()) return usages;
        else { return usages
                .stream()
                .limit(numDays)
                .collect(Collectors.toList());
        }
    }

    /* Recursively computes percentage devices until it has computed it for each device (called once
    per device). This is necessary because we are using callbacks.
     */
    private void computePercentageDevices(int i, int unitID, int numUnits, double total,
                                          HashMap<Integer, Double> percentageByAppliance,
                                          Consumer<HashMap<Integer, Double>> callback) {
        int key = userData.appliances.keyAt(i);
        ApplianceData a = userData.appliances.get(key);

        a.getRange(TimeUnit.fromId(unitID), TimeUnit.nowSec(), numUnits, result -> {
            double totalEnergy = total;
            double totalForA = 0.0;
            for(Double energy : result.values()) {
                totalForA+=energy;
            }
            percentageByAppliance.put(a.getId(), totalForA);
            totalEnergy+=totalForA;
            final double tot = totalEnergy;

            if (i == userData.appliances.size()-1) {
                percentageByAppliance.replaceAll((k, v) -> ((v / tot)*100));
                callback.accept(percentageByAppliance);
            } else {
                computePercentageDevices(i+1, unitID, numUnits, totalEnergy, percentageByAppliance, callback);
            }
        });
    }

    /* Computes the percentage use per device over the number of units, numUnits, of the given time unit, timeUnit,
    and returns a callback, a hashmap mapping applianceID to it's percentage usage
     */
    public void getPercentageDevices(int unitID, int numUnits, Consumer<HashMap<Integer, Double>> callback) {
        HashMap<Integer, Double> percentageByAppliance = new HashMap<>();

        computePercentageDevices(0, unitID, numUnits, 0.0, percentageByAppliance, callback);
    }
}
