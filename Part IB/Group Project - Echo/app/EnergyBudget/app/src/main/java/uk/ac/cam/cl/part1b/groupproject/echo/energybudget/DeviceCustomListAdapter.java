package uk.ac.cam.cl.part1b.groupproject.echo.energybudget;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DeviceCustomListAdapter extends ArrayAdapter<String> implements Filterable {
    public static final String TAG = "DeviceCustomListAdapter";

    private int selectedDeviceItem = -1;
    private final Activity context;
    //keep a hidden positions list in order to dynamically adjust displayed content
    private ArrayList<Integer> hiddenPositions = new ArrayList<>();
    private ArrayList<Integer> visiblePositions = new ArrayList<>();
    //initialise displayedCategory to empty strng
    private String displayedCategory;
    private TimeUnit displayedTime;

    private List<Integer> idList;
    private Map<Integer, Integer> idMap;
    private Map<Integer, List<Integer>> typeIdMap;
    public HashMap<Integer, Double> currentMap;
    public UserData userData;

    public static HashMap<TimeUnit, Integer> chartUnits = new HashMap<>();
    public static HashMap<TimeUnit, TimeUnit> chartUnitConvert = new HashMap<>();
    static {
        chartUnitConvert.put(TimeUnit.DAY, TimeUnit.HOUR);
        chartUnitConvert.put(TimeUnit.WEEK, TimeUnit.DAY);
        chartUnitConvert.put(TimeUnit.MONTH, TimeUnit.DAY);
        chartUnits.put(TimeUnit.DAY, 24);
        chartUnits.put(TimeUnit.WEEK, 7);
        chartUnits.put(TimeUnit.MONTH, 31);
    }


    public DeviceCustomListAdapter(Activity context, UserData userData, List<String> deviceNames, HashMap<Integer, Double> percentageDevicesDay) {
        super(context, R.layout.custom_list, deviceNames);
        this.context=context;
        this.userData = userData;

        //map from typeIds which are used in iconMap and nameMap to actual Ids in user data
        idMap = new HashMap<>();
        //map from typeIds to applianceIds - in case user has more than one of some appliances
        typeIdMap = new HashMap<>();
        for (int j=0; j<userData.appliances.size(); j++){
            //for each available appliance for the user, map the typeId to actual id
            int key = userData.appliances.keyAt(j);
            int typeId = userData.appliances.get(key).getTypeId();
            idMap.put(key, typeId);
            if (!typeIdMap.containsKey(typeId)){
                ArrayList<Integer> applianceIdList = new ArrayList<>();
                applianceIdList.add(key);
                typeIdMap.put(typeId, applianceIdList);
            } else {
                typeIdMap.get(typeId).add(key);
            }
        }

        displayedCategory = "";
        displayedTime = TimeUnit.DAY;
        currentMap = percentageDevicesDay;
        //get a list of ids
        idList = new ArrayList<>(typeIdMap.keySet());
        sortIdList();
        updateHiddenPositions();
        notifyDataSetChanged();
    }

    private void updateHiddenPositions(){
        //add an id to hidden position if the user has no data for this appliance
        //or if the category is different than the selected category
        hiddenPositions.clear();
        visiblePositions.clear();
        for(int i=0; i<idList.size(); i++){
            String category = DevicesFragment.iconCategories.get(DevicesFragment.iconMap.get(idList.get(i)));
            if (!displayedCategory.equals(category)
                    && !displayedCategory.isEmpty()){
                hiddenPositions.add(i);
            } else {
                visiblePositions.add(i);
            }
        }
    }

    private void sortIdList(){
        //sort the list of appliances in list view
        idList.sort(new Comparator<Integer>(){
            public int compare(Integer id1,Integer id2){
                //compare if both are in user data
                //if not in user data treat as smaller
                double usage1 = 0;
                double usage2 = 0;
                //aggregate usage over all appliances with the typeId id1
                for (Integer applianceId : typeIdMap.get(id1)){
                    usage1 += currentMap.get(applianceId);
                }
                //aggregate usage over all appliances with the typeId id2
                for (Integer applianceId : typeIdMap.get(id2)){
                    usage2 += currentMap.get(applianceId);
                }
                //sort in decreasing order
                if (usage2-usage1>0){
                    return 1;
                } else if (usage2-usage1<0) {
                    return -1;
                } else {
                    return 0;
                }
            }});
    }

    //get count of visible objects
    public int getCount() {
        return typeIdMap.keySet().size() - hiddenPositions.size();
    }

    public void updateCurrentCategory(){
        //update the list
        if (!displayedCategory.equals(DevicesFragment.currentCategory)
                || !(displayedTime == DevicesFragment.currentTimeUnit)){
            displayedCategory = DevicesFragment.currentCategory;
            displayedTime = DevicesFragment.currentTimeUnit;
            sortIdList();
            updateHiddenPositions();
            notifyDataSetChanged();
        }
    }

    //TODO: use recycled view (see fragment tips adapter)
    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        updateCurrentCategory();

        //hide items as necessary
        for(Integer hiddenIndex : hiddenPositions) {
            if(hiddenIndex <= position) {
                position = position + 1;
            }
        }

        //inflate the template layout
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_list, null,true);
        TextView title = rowView.findViewById(R.id.item);
        ImageView image = rowView.findViewById(R.id.icon);
        TextView extra = rowView.findViewById(R.id.textView);
        LineChart lineChart = rowView.findViewById(R.id.lineChart);

        //get the icon and name
        int id = idList.get(position);
        int icon = DevicesFragment.iconMap.get(id);
        String text = DevicesFragment.nameMap.get(id);
        if (typeIdMap.get(id).size()>1){
            text += " x" + typeIdMap.get(id).size();
        }
        title.setText(text);
        image.setImageResource(icon);

        //set text colour
        String category = DevicesFragment.iconCategories.get(icon);
        int colour = DevicesFragment.colourSelection(category);
        title.setTextColor(ContextCompat.getColor(getContext(),colour));
        //set icon colour
        DevicesFragment.setIconColour(image,colour);

        //set line chart data
        lineChart.setTouchEnabled(false);

        //show no axes
        lineChart.getXAxis().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);

        //show no desctiption
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);

        //if no data, make the text match colour to icon
        lineChart.setNoDataTextColor(ContextCompat.getColor(getContext(),colour));


        ArrayList<Entry> values = new ArrayList<>();
        for (Integer applianceId : typeIdMap.get(id)){
            ApplianceData applianceData = userData.getAppliance(applianceId);
            //only draw chart if we have actual data - otherwise breaks
            if (currentMap.get(applianceId).floatValue() >= 0.05){
                //get appliance data for the device
                //reuse call from usage fragment
                applianceData.getRange(chartUnitConvert.get(displayedTime), TimeUnit.nowSec(), chartUnits.get(displayedTime), range -> {
                    int count=0;
                    //accumulate values for each appliance data for that typeId
                    for (Map.Entry<Integer, Double> entry : range.entrySet()) {
                        if (values.size()<range.entrySet().size()) {
                            values.add(new Entry(entry.getKey(), entry.getValue().floatValue()));
                        } else {
                            Entry oldEntry = values.get(count);
                            oldEntry.setY(oldEntry.getY()+entry.getValue().floatValue());
                        }
                        count++;
                    }
                    LineDataSet lineDataSet = new LineDataSet(values, "");
                    lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    lineDataSet.setCubicIntensity(0.2f);
                    lineDataSet.setDrawFilled(true);
                    lineDataSet.setDrawCircles(false);
                    lineDataSet.setLineWidth(1.8f);
                    lineDataSet.setColor(ContextCompat.getColor(getContext(),colour));
                    lineDataSet.setFillColor(Color.WHITE);
                    lineDataSet.setFillAlpha(100);
                    lineDataSet.setDrawHorizontalHighlightIndicator(false);
                    lineDataSet.setFillFormatter((dataSet, dataProvider) -> -10);

                    LineData lineData = new LineData(lineDataSet);
                    lineData.setDrawValues(false);

                    lineChart.setData(lineData);
                    lineChart.invalidate();
                });
            }
        }


        //if (clickedItemIndex != -1 && position == visiblePositions.get(clickedItemIndex)) {
        if (id == selectedDeviceItem){
            lineChart.setVisibility(View.VISIBLE);
            lineChart.invalidate();

            //timeRadioGroup.setVisibility(View.VISIBLE);
        }

        float usage = 0;
        //aggregate usage over all appliances with that typeId
        for(Integer applianceId : typeIdMap.get(id)){
            usage += currentMap.get(applianceId);
        }
        String extraText = String.format(Locale.getDefault(),"%,.1f", usage)+"%";
        extra.setText(extraText);
        notifyDataSetChanged();
        return rowView;
    }

    public void setVisibleChartIndex(int i) {
        //if graph already showing, hide on click
        if (selectedDeviceItem == idList.get(visiblePositions.get(i))) {
            selectedDeviceItem = -1;
        }else{
            //show graph on selecting a list item
            selectedDeviceItem = idList.get(visiblePositions.get(i));
        }
        notifyDataSetChanged();
    }
}