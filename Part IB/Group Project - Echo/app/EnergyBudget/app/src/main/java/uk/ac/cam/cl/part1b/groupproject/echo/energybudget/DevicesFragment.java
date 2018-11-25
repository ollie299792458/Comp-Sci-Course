package uk.ac.cam.cl.part1b.groupproject.echo.energybudget;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class DevicesFragment extends Fragment implements OnChartValueSelectedListener{

    //categories
    public static final String kitchen = "kitchen";
    public static final String bathroom = "bathroom";
    public static final String entertainment = "entertainment";
    public static final String housekeeping = "housekeeping";
    public static final String heating = "heating";
    public static final String light = "light";
    public static String currentCategory = "";
    public static TimeUnit currentTimeUnit = TimeUnit.DAY;
    public static int currentTimeButton= R.id.dayRadioButton;

    //keep view count so that the graph is displayed only on first loading of the fragment
    public static int viewCount;

    ListView listView;

    //keep maps of icons, categories, and names of appliances
    public static LinkedHashMap<Integer, Integer> iconMap;
    public static LinkedHashMap<Integer, String> nameMap;
    public static LinkedHashMap<Integer, String> iconCategories;
    public static HashMap<Integer, TimeUnit> timeUnitMap;

    static {
        timeUnitMap = new HashMap<>();
        timeUnitMap.put(R.id.dayRadioButton, TimeUnit.DAY);
        timeUnitMap.put(R.id.weekRadioButton, TimeUnit.WEEK);
        timeUnitMap.put(R.id.monthRadioButton, TimeUnit.MONTH);

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
        iconMap = new LinkedHashMap<>();
        iconMap.put(1, R.drawable.air_cleaner);
        iconMap.put(2, R.drawable.air_conditioner);
        iconMap.put(3, R.drawable.ceiling_fan);
        iconMap.put(4, R.drawable.iron);
        iconMap.put(5, R.drawable.washing_machine);
        iconMap.put(6, R.drawable.coffee_machine);
        iconMap.put(7, R.drawable.desk_lamp);
        iconMap.put(8, R.drawable.electric_heater);
        iconMap.put(9, R.drawable.kettle);
        iconMap.put(10, R.drawable.electric_piano);
        iconMap.put(11, R.drawable.electric_pot);
        iconMap.put(12, R.drawable.floor_heating);
        iconMap.put(13, R.drawable.food_mixer);
        iconMap.put(14, R.drawable.gaming);
        iconMap.put(15, R.drawable.hair_drier);
        iconMap.put(16, R.drawable.humidifier);
        iconMap.put(17, R.drawable.scanner);
        iconMap.put(18, R.drawable.heated_table);
        iconMap.put(19, R.drawable.laptop);
        iconMap.put(20, R.drawable.microwave);
        iconMap.put(21, R.drawable.music_player);
        iconMap.put(22, R.drawable.overhead_light);
        iconMap.put(23, R.drawable.printer);
        iconMap.put(24, R.drawable.fridge);
        iconMap.put(25, R.drawable.rice_cooker);
        iconMap.put(26, R.drawable.tablet);
        iconMap.put(27, R.drawable.telephone);
        iconMap.put(28, R.drawable.toaster);
        iconMap.put(29, R.drawable.toilet);
        iconMap.put(30, R.drawable.tv);
        iconMap.put(31, R.drawable.vacuum_cleaner);
        iconMap.put(32, R.drawable.vent_fan);
        iconMap.put(33, R.drawable.video_recorder);
        iconMap.put(34, R.drawable.dishwasher);
        iconMap.put(35, R.drawable.oven);
        iconMap.put(36, R.drawable.bread_machine);
        iconMap.put(37, R.drawable.inductive_heater);
        iconMap.put(38, R.drawable.drier);
        iconMap.put(39, R.drawable.pv);
        iconMap.put(40, R.drawable.heater);
        iconMap.put(41, R.drawable.heater);
        iconMap.put(42, R.drawable.electric_pot);
        iconMap.put(43, R.drawable.drier);
        iconMap.put(44, R.drawable.freezer);
        iconMap.put(302, R.drawable.sewing_machine);
        iconMap.put(304, R.drawable.light);
        iconMap.put(310, R.drawable.outled_load);
        iconMap.put(995, R.drawable.main_breaker);
        iconMap.put(997, R.drawable.pv);

        iconCategories = new LinkedHashMap<>();
        //populate kitchen icons
        iconCategories.put(R.drawable.coffee_machine, kitchen);
        iconCategories.put(R.drawable.kettle, kitchen);
        iconCategories.put(R.drawable.electric_pot, kitchen);
        iconCategories.put(R.drawable.food_mixer, kitchen);
        iconCategories.put(R.drawable.heated_table, kitchen);
        iconCategories.put(R.drawable.microwave, kitchen);
        iconCategories.put(R.drawable.fridge, kitchen);
        iconCategories.put(R.drawable.rice_cooker, kitchen);
        iconCategories.put(R.drawable.toaster, kitchen);
        iconCategories.put(R.drawable.dishwasher, kitchen);
        iconCategories.put(R.drawable.oven, kitchen);
        iconCategories.put(R.drawable.bread_machine, kitchen);
        iconCategories.put(R.drawable.inductive_heater, kitchen);
        iconCategories.put(R.drawable.freezer, kitchen);
        //populate bathroom icons
        iconCategories.put(R.drawable.iron, bathroom);
        iconCategories.put(R.drawable.washing_machine, bathroom);
        iconCategories.put(R.drawable.hair_drier, bathroom);
        iconCategories.put(R.drawable.toilet, bathroom);
        iconCategories.put(R.drawable.drier, bathroom);
        //populate entertainment icons
        iconCategories.put(R.drawable.electric_piano, entertainment);
        iconCategories.put(R.drawable.gaming, entertainment);
        iconCategories.put(R.drawable.scanner, entertainment);
        iconCategories.put(R.drawable.laptop, entertainment);
        iconCategories.put(R.drawable.music_player, entertainment);
        iconCategories.put(R.drawable.printer, entertainment);
        iconCategories.put(R.drawable.tablet, entertainment);
        iconCategories.put(R.drawable.telephone, entertainment);
        iconCategories.put(R.drawable.tv, entertainment);
        iconCategories.put(R.drawable.video_recorder, entertainment);
        //populate housekeeping icons
        iconCategories.put(R.drawable.air_cleaner, housekeeping);
        iconCategories.put(R.drawable.humidifier, housekeeping);
        iconCategories.put(R.drawable.vacuum_cleaner, housekeeping);
        iconCategories.put(R.drawable.sewing_machine, housekeeping);
        //populate heating icons
        iconCategories.put(R.drawable.ceiling_fan, heating);
        iconCategories.put(R.drawable.electric_heater, heating);
        iconCategories.put(R.drawable.floor_heating, heating);
        iconCategories.put(R.drawable.vent_fan, heating);
        iconCategories.put(R.drawable.heater, heating);
        iconCategories.put(R.drawable.air_conditioner, heating);
        //populate light icons
        iconCategories.put(R.drawable.desk_lamp, light);
        iconCategories.put(R.drawable.overhead_light, light);
        iconCategories.put(R.drawable.light, light);
        iconCategories.put(R.drawable.main_breaker, light);
        iconCategories.put(R.drawable.outled_load, light);
        iconCategories.put(R.drawable.pv, light);
    }

    private PieChart pieChart;
    DeviceCustomListAdapter adapter;
    public UserData userData;
    //stores the data for percentage usage per device, by time unit
    public static HashMap<TimeUnit, HashMap<Integer,Double>> percentageDevices;
    //stores the data for percentage usage per category, by time unit
    public static HashMap<TimeUnit, LinkedHashMap<String, Float>> categoryPercentages;
    private Analytics analytics;

    //list of categories for populating the graph - and other pie chart data
    private List<String> categoryList;
    private List<PieEntry> entries;
    private PieDataSet dataSet;
    private List<Integer>  colors;
    private PieData data;
    //maps from api ids to type ids for devices
    private Map<Integer, Integer> idMap;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.devices_fragment, container, false);

        // Initialise pie chart
        pieChart = view.findViewById(R.id.pieChart);

        // Set pie chart options
        pieChart.setDescription(null);
        //pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(9);
        //pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setRotationAngle(115.0f);
        pieChart.setRotation(0.4f);
        pieChart.setRotationEnabled(true);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setTextColor(Color.DKGRAY);
        legend.setTextSize(15);
        legend.setForm(Legend.LegendForm.CIRCLE);
        //legend.setMaxSizePercent(0.2f);
        legend.setWordWrapEnabled(true);
        pieChart.setExtraBottomOffset(-15);
        pieChart.setHoleRadius(50.0f);
        pieChart.setCenterText("Energy usage per appliance");
        pieChart.setCenterTextColor(Color.DKGRAY);
        pieChart.setCenterTextSize(18);
        pieChart.setNoDataText("Loading chart data...");
        pieChart.setNoDataTextColor(Color.DKGRAY);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(15);
        pieChart.setDrawEntryLabels(false);

        //get user data for appliances
        UserData.getUser(this::init);

        return view;
    }

    public void init(UserData userData) {
        this.userData = userData;
        idMap = new HashMap<>();
        for (int j=0; j<userData.appliances.size(); j++) {
            //for each available appliance for the user, map the typeId to actual id
            int key = userData.appliances.keyAt(j);
            int typeId = userData.appliances.get(key).getTypeId();
            idMap.put(key, typeId);
        }
        analytics = new Analytics(userData);

        //if we need to compute the aggregated category percentages
        if (categoryPercentages==null) {
            analytics.getPercentageDevices(TimeUnit.toId(TimeUnit.DAY), 1, percentageDay -> {
                analytics.getPercentageDevices(TimeUnit.toId(TimeUnit.DAY), 7, percentageWeek -> {
                    analytics.getPercentageDevices(TimeUnit.toId(TimeUnit.MONTH), 1, percentageMonth -> {
                        //intialise the aggregated hashmaps
                        percentageDevices = new HashMap<>();
                        categoryPercentages = new HashMap<>();

                        //calculate monthly aggregated usage
                        LinkedHashMap<String, Float> categoryPercentagesMonth = new LinkedHashMap<>();
                        for (Integer id : percentageMonth.keySet()) {
                            Integer typeId = idMap.get(id);
                            String category = iconCategories.get(iconMap.get(typeId));
                            if (!categoryPercentagesMonth.containsKey(category)) {
                                categoryPercentagesMonth.put(category, percentageMonth.get(id).floatValue());
                            } else {
                                categoryPercentagesMonth.put(category, categoryPercentagesMonth.get(category) +
                                        percentageMonth.get(id).floatValue());

                            }
                        }
                        //remove entry from hashmap if category usage is 0
                        categoryPercentagesMonth.keySet().removeIf(
                                (String key) ->(int)categoryPercentagesMonth.get(key).floatValue() == 0);

                        //add to the main maps
                        percentageDevices.put(TimeUnit.MONTH, percentageMonth);
                        categoryPercentages.put(TimeUnit.MONTH, categoryPercentagesMonth);

                        //repeat for monthly
                        LinkedHashMap<String, Float> categoryPercentagesWeek = new LinkedHashMap<>();
                        for (Integer id : percentageWeek.keySet()) {
                            Integer typeId = idMap.get(id);
                            String category = iconCategories.get(iconMap.get(typeId));
                            if (!categoryPercentagesWeek.containsKey(category)) {
                                categoryPercentagesWeek.put(category, percentageWeek.get(id).floatValue());
                            } else {
                                categoryPercentagesWeek.put(category, categoryPercentagesWeek.get(category) +
                                        percentageWeek.get(id).floatValue());

                            }
                        }
                        categoryPercentagesWeek.keySet().removeIf(
                                (String key) ->(int)categoryPercentagesWeek.get(key).floatValue() == 0);

                        percentageDevices.put(TimeUnit.WEEK, percentageWeek);
                        categoryPercentages.put(TimeUnit.WEEK, categoryPercentagesWeek);

                        //repeat for daily
                        LinkedHashMap<String, Float> categoryPercentagesDay = new LinkedHashMap<>();
                        for (Integer id : percentageDay.keySet()) {
                            Integer typeId = idMap.get(id);
                            String category = iconCategories.get(iconMap.get(typeId));
                            if (!categoryPercentagesDay.containsKey(category)) {
                                categoryPercentagesDay.put(category, percentageDay.get(id).floatValue());
                            } else {
                                categoryPercentagesDay.put(category, categoryPercentagesDay.get(category) +
                                        percentageDay.get(id).floatValue());

                            }
                        }
                        categoryPercentagesDay.keySet().removeIf(
                                (String key) ->(int)categoryPercentagesDay.get(key).floatValue() == 0);

                        percentageDevices.put(TimeUnit.DAY, percentageDay);
                        categoryPercentages.put(TimeUnit.DAY, categoryPercentagesDay);

                        //then call all the other things that still need to be in the call back
                        nonCallbackOnCreateView();

                    });

                });

            });
        } else {
            //call the necessary pie chart displaying etc outside the callback as we have all the data
            nonCallbackOnCreateView();
        }
    }

    public void nonCallbackOnCreateView(){

        //initialise a list with deviceNames to populate the adapter
        ArrayList<String> deviceNames = new ArrayList<>(nameMap.values());
        //set the listview adapter
        adapter = new DeviceCustomListAdapter(getActivity(), userData, deviceNames, percentageDevices.get(TimeUnit.DAY));
        listView = view.findViewById(R.id.list);
        listView.setAdapter(adapter);

        //set listview onclicklistener for graph display
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setVisibleChartIndex(i);
            }
        });

        RadioGroup timeRadioGroup = view.findViewById(R.id.timeRadioGroup);
        //set a listener for the time button
        timeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //set the current time unit and the current clicked button variables
                currentTimeUnit = timeUnitMap.get(i);
                currentTimeButton = i;

                //initialise all the data for the pie chart according to the time unit
                categoryList = new ArrayList<>(categoryPercentages.get(currentTimeUnit).keySet());
                entries = new ArrayList<>();
                for (String category : categoryList) {
                    PieEntry entry = new PieEntry(categoryPercentages.get(currentTimeUnit).get(category), category);
                    entries.add(entry);
                }
                dataSet = new PieDataSet(entries, "");
                //set colours
                colors = new ArrayList<>();
                for (String category : categoryList) {
                    colors.add(ContextCompat.getColor(getContext(), colourSelection(category)));
                }
                dataSet.setColors(colors);
                dataSet.setValueTextSize(15);
                dataSet.setValueTextColor(Color.WHITE);
                dataSet.setValueFormatter(new PieChartValueFormatter());

                data = new PieData(dataSet);
                pieChart.setData(data);
                pieChart.invalidate();

                //restore selection from previous visiting of the fragment
                Highlight highlight;
                if(categoryList.contains(currentCategory)) {
                    float x = categoryList.indexOf(currentCategory);
                    PieEntry entry = entries.get(categoryList.indexOf(currentCategory));
                    highlight = new Highlight(x, entry.getValue(), 0);
                    pieChart.highlightValue(highlight);
                } else {
                    pieChart.highlightValue(null);
                    currentCategory = "";
                }

                //set the currentMap for the adapter (daily vs weekly vs monthly)
                adapter.currentMap = percentageDevices.get(currentTimeUnit);
                adapter.notifyDataSetChanged();
            }

        });

        //select the time button
        timeRadioGroup.check(currentTimeButton);

        //set listener
        pieChart.setOnChartValueSelectedListener(this);
        if (currentCategory.equals("") && viewCount == 1) {
            pieChart.animateX(1800, Easing.EasingOption.Linear);
        }

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        String label = ((PieEntry)e).getLabel();
        switch (label) {
            case kitchen:
                currentCategory = kitchen;
                break;
            case bathroom:
                currentCategory = bathroom;
                break;
            case entertainment:
                currentCategory = entertainment;
                break;
            case housekeeping:
                currentCategory = housekeeping;
                break;
            case heating:
                currentCategory = heating;
                break;
            case light:
                currentCategory = light;
                break;
            default:
                Log.i(TAG, "The selected category is not recognised." +
                        "Check that the label names match with the case statement.");
        }
        adapter.updateCurrentCategory();
    }

    @Override
    public void onNothingSelected() {
        //reset selection
        currentCategory = "";
        adapter.updateCurrentCategory();
    }

    public static void setIconColour(ImageView imageView, int colour){
        //Set the colour for the particular icon
        imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), colour),
                PorterDuff.Mode.SRC_ATOP);
    }

    public static int colourSelection(String category){
        //get colour depending on category
        int colour = 0;
        switch(category){
            case kitchen:
                colour = R.color.kitchen_icon;
                break;
            case bathroom:
                colour = R.color.bathroom_icon;
                break;
            case entertainment:
                colour = R.color.entertainment_icon;
                break;
            case housekeeping:
                colour = R.color.housekeeping_icon;
                break;
            case heating:
                colour = R.color.heating_icon;
                break;
            case light:
                colour = R.color.light_icon;
                break;
        }
        return colour;
    }
}
