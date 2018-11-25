package uk.ac.cam.cl.part1b.groupproject.echo.energybudget;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class PieChartValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public PieChartValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if(value >= 3) {
            // write your logic here
            return mFormat.format(value) + "%"; // append a percentage
        }
        else {
            //((PieEntry)entry).setLabel("");
            return "";
        }
    }
}