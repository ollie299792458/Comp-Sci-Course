package uk.ac.cam.cl.part1b.groupproject.echo.energybudget;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UsageListAdapter extends ArrayAdapter<UsageFragment.ListElement> {
    private final Activity context;

    private ArrayList<UsageFragment.ListElement> usageList;

    public UsageListAdapter(Activity context, ArrayList<UsageFragment.ListElement> usageList) {
        super(context, R.layout.usage_list_item, usageList);
        this.context = context;
        this.usageList = usageList;
    }

    public ArrayList<UsageFragment.ListElement> getUsageList() {
        return usageList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View rowView, @NonNull ViewGroup parent) {
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.usage_list_item, parent, false);
        }

        TextView dateText = rowView.findViewById(R.id.dateText);
        TextView usageText = rowView.findViewById(R.id.usageText);

        dateText.setText(usageList.get(position).time);
        usageText.setText(usageList.get(position).usage);

        return rowView;
    }
}