package uk.ac.cam.cl.part1b.groupproject.echo.energybudget;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class UsageFragment extends Fragment implements OnChartValueSelectedListener {
    public class ListElement {
        public String time;
        public String usage;

        ListElement(String t, String u) {
            time = t;
            usage = u;
        }
    }

    private ListView listView;
    private LineChart lineChart;
    private RadioGroup timeRadioGroup;

    private TextView changeText;
    private TextView changeValue;
    private ImageView changeArrow;

    private UserData userData;

    private TimeUnit rangeUnit = TimeUnit.DAY;
    private TimeUnit divisionUnit = TimeUnit.HOUR;
    private int numDivisions = 24;

    private int RedColor;
    private int GreenColor;
    private int GreyColor;
    private int BlueColor;
    private int LightBlueColor;
    private Drawable ArrowUpRes;
    private Drawable ArrowDownRes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.usage_fragment, container, false);

        lineChart = view.findViewById(R.id.lineChart);
        timeRadioGroup = view.findViewById(R.id.timeRadioGroup);

        listView = view.findViewById(R.id.usageList);

        RedColor = ContextCompat.getColor(getContext(), R.color.flower_badge);
        GreenColor = ContextCompat.getColor(getContext(), R.color.tree_badge);
        GreyColor = ContextCompat.getColor(getContext(), R.color.greyed_out);
        BlueColor = ContextCompat.getColor(getContext(),R.color.bathroom_icon);
        LightBlueColor = ContextCompat.getColor(getContext(),R.color.bathroom_badge);
        ArrowUpRes = ContextCompat.getDrawable(getContext(), R.drawable.arrow_up);
        ArrowDownRes = ContextCompat.getDrawable(getContext(), R.drawable.arrow_down);

        changeText = view.findViewById(R.id.changeText);
        changeValue = view.findViewById(R.id.changeValue);
        changeArrow = view.findViewById(R.id.changeArrow);
        // Inflate the layout for this fragment
        UserData.getUser(this::init);

        lineChart.setViewPortOffsets(100, 10, 100, 50);
        lineChart.setBackgroundColor(Color.TRANSPARENT);

        // no description text
        lineChart.getDescription().setEnabled(false);

        // enable touch gestures
        lineChart.setTouchEnabled(true);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);

        lineChart.setDrawGridBackground(false);
        lineChart.setMaxHighlightDistance(300);

        XAxis x = lineChart.getXAxis();
//        x.setEnabled(false);
        x.setLabelCount(6);
        x.setTextColor(Color.BLACK);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);

        YAxis y = lineChart.getAxisLeft();
//        y.setEnabled(false);

        lineChart.getLegend().setEnabled(false);

        y.setLabelCount(6, false);
        y.setTextColor(Color.BLACK);
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisMinimum(0.0f);

        y.setValueFormatter((value, ax) -> formatUsage(value));

        lineChart.getAxisRight().setEnabled(false);

        lineChart.setNoDataText("Loading chart data...");
        lineChart.setNoDataTextColor(Color.DKGRAY);

        //set listener
        lineChart.setOnChartValueSelectedListener(this);

        // set time radio group listener so we can change the data on click
        timeRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.dayRadioButton:
                    rangeUnit = TimeUnit.DAY;
                    divisionUnit = TimeUnit.HOUR;
                    numDivisions = 24;
                    break;
                case R.id.weekRadioButton:
                    rangeUnit = TimeUnit.WEEK;
                    divisionUnit = TimeUnit.DAY;
                    numDivisions = 7;
                    break;
                case R.id.monthRadioButton:
                    rangeUnit = TimeUnit.MONTH;
                    divisionUnit = TimeUnit.DAY;
                    numDivisions = 31;
                    break;
                case R.id.yearRadioButton:
                    rangeUnit = TimeUnit.YEAR;
                    divisionUnit = TimeUnit.MONTH;
                    numDivisions = 12;
                    break;
                default: // shouldn't happen
                    Log.i(TAG, "Invalid button provided to checked listener.");
                    return;
            }
            displayData();
        });


        // and with this we don't want the legend anymore
        lineChart.getLegend().setEnabled(false);

        return view;
    }

    private void init(UserData userData) {
        this.userData = userData;
        timeRadioGroup.setVisibility(View.VISIBLE);
        timeRadioGroup.setEnabled(true);
        timeRadioGroup.check(R.id.dayRadioButton);

        displayData();
    }

    /**
     * Formats unix timestamp into a human readable string
     * @param timestamp Unix timestamp(in seconds)
     * @param unit the unit the time should represent
     * @param longFormat whether to be more verbose
     * @return the formated string
     */
    public String formatTime(int timestamp, TimeUnit unit, boolean longFormat) {
        String pattern;
        if (unit.equals(TimeUnit.HOUR)) {
            pattern = longFormat ? "ha EE" : "ha";
        } else if (unit.equals(TimeUnit.DAY)) {
            pattern = longFormat ? "EE dd MMM" : "d/M";
        } else if (unit.equals(TimeUnit.MONTH)) {
            pattern = longFormat ? "MMM yyyy" : "M/y";
        } else {
            pattern = "";
            Log.w(TAG, "Invalid unit range set.");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(Calendar.getInstance().getTimeZone());
        return sdf.format(new Date((long)(timestamp) * 1000L));
    }

    /**
     * Formats the power usage(in Watts) to human readable string.
     * @param usage
     * @return formatted string
     */
    public String formatUsage(double usage) {
        return String.valueOf((int)usage) + "W";
    }

    private LineData createLineData(List<Entry> entries) {
        LineDataSet lineDataSet = new LineDataSet(entries, "daily");
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setCircleColor(Color.MAGENTA);
        lineDataSet.setHighLightColor(Color.GREEN);
        lineDataSet.setColor(BlueColor);
        lineDataSet.setFillColor(LightBlueColor);
        lineDataSet.setFillAlpha(100);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setFillFormatter((dataSet, dataProvider) -> -10);

        LineData lineData = new LineData(lineDataSet);
        lineData.setValueTextSize(9f);
        lineData.setDrawValues(false);

        return lineData;
    }

    private void updateUsageList(ArrayList<ListElement> list) {
        UsageListAdapter adapter;
        if (listView.getAdapter() == null) {
            adapter = new UsageListAdapter(getActivity(), list);
            listView.setAdapter(adapter);
        } else {
            adapter = (UsageListAdapter)listView.getAdapter();
            ArrayList<ListElement> usageList = adapter.getUsageList();
            usageList.clear();
            usageList.addAll(list);
            adapter.notifyDataSetChanged();
            listView.invalidate();
        }
    }

    private void displayData() {
        XAxis x = lineChart.getXAxis();
        x.setValueFormatter((value, ax) -> formatTime((int)value, divisionUnit, false));

        userData.getRange(divisionUnit, TimeUnit.nowSec(), numDivisions, range -> {
            ArrayList<Entry> chartEntries = new ArrayList<>();
            ArrayList<ListElement> listEntries = new ArrayList<>();

            for (Map.Entry<Integer, Double> entry : range.entrySet()) {
                chartEntries.add(new Entry(entry.getKey(), entry.getValue().intValue()));
                listEntries.add(new ListElement(
                        formatTime(entry.getKey(), divisionUnit, true),
                        formatUsage(entry.getValue())));
            }

            if (lineChart.getLineData() != null && lineChart.getLineData().getDataSetCount() > 0) {
                LineDataSet lineDataSet = (LineDataSet)lineChart.getLineData().getDataSetByIndex(0);
                lineDataSet.setValues(chartEntries);

                lineChart.getLineData().notifyDataChanged();
                lineChart.notifyDataSetChanged();
            } else {
                lineChart.setData(createLineData(chartEntries));
            }

            lineChart.invalidate();

            Collections.reverse(listEntries);
            updateUsageList(listEntries);
        });

        Analytics analytics = new Analytics(userData);
        analytics.percentageChangeTotal(TimeUnit.toId(rangeUnit), change -> {
            int changeInt = change.intValue();
            String percentageString = changeInt + "%";
            if (changeInt>0){
                percentageString = "+" + percentageString;
            }

            changeValue.setText(percentageString);
            changeArrow.setVisibility(View.VISIBLE);

            String rangeUnitName = "last " + rangeUnit.toString().toLowerCase();
            if (rangeUnit == TimeUnit.DAY) {
                rangeUnitName = "yesterday";
            }
            String text = text = "Usage compared to " + rangeUnitName + ":";

            int c;
            if (changeInt < 0) {
                c = GreenColor;
                changeArrow.setImageDrawable(ArrowDownRes);
            } else if (changeInt > 0) {
                c = RedColor;
                changeArrow.setImageDrawable(ArrowUpRes);
            } else {
                c = GreyColor;
                text = "No change.";
                changeArrow.setVisibility(View.INVISIBLE);
            }

            changeValue.setTextColor(c);
            changeText.setTextColor(c);
            changeText.setText(text);
            changeArrow.setColorFilter(c);
        });

        lineChart.setViewPortOffsets(100, 10, 100, 50);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
