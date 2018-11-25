package uk.ac.cam.cl.part1b.groupproject.echo.energybudget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BadgesFragment extends Fragment {
    public final static String device = "device";
    public final static String house = "house";

    public static UserData userData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.badges_fragment, container, false);

        //get user data for appliances
        UserData.getUser(userData ->{
            this.userData = userData;
        });

        // use expandable gridview so both gridviews can be made non-scrollable
        // and put inside a scrollview
        ExpandableGridView deviceGridview = view.findViewById(R.id.device_badges_grid);
        deviceGridview.setExpanded(true);
        deviceGridview.setAdapter(new BadgeAdapter(getContext(), device));
        ExpandableGridView houseGridview = view.findViewById(R.id.house_badges_grid);
        houseGridview.setAdapter(new BadgeAdapter(getContext(), house));
        houseGridview.setExpanded(true);
        return view;
    }
}
