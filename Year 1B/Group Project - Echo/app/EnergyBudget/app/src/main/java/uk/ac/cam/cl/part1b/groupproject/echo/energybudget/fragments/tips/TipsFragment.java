package uk.ac.cam.cl.part1b.groupproject.echo.energybudget.fragments.tips;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.cl.part1b.groupproject.echo.energybudget.Analytics;
import uk.ac.cam.cl.part1b.groupproject.echo.energybudget.R;
import uk.ac.cam.cl.part1b.groupproject.echo.energybudget.UserData;

/**
 * Fragment for displaying the tips listview, and the tabs above to select tip type
 */
public class TipsFragment extends Fragment {

    private static final String TIPS_DATABASE = "tips_database";
    private CustomListAdapterTip listAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TipDatabase tipDatabase;
    private List<Tip> tipsDisplayed;
    private TipType tabSelected = TipType.UPGRADE;
    private Analytics analytics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //todo add tips to saved instance state
        super.onCreate(savedInstanceState);

        //Test case tips
        //Tip t1 = new Tip("hi","hi",false,1,TipType.UPGRADE,20);
        //Tip t2 = new Tip("hi","hi",false,1,TipType.UPGRADE,20);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tips_fragment, container, false);


        //start tip database
        tipDatabase = TipDatabase.getInstance(getContext());
        tipsDisplayed = new ArrayList<>();
        tipsDisplayed.addAll(tipDatabase.getUpgrades());

        //initialise objects
        ListView listView = view.findViewById(R.id.tips_listview);
        listAdapter = new CustomListAdapterTip(tipsDisplayed, getContext());
        listView.setAdapter(listAdapter);
        UserData.getUser(userData ->{
            analytics = new Analytics(userData);
        });

        //set up swipe to refresh layout (allows refreshing by swipe gesture)
        swipeRefreshLayout = view.findViewById(R.id.tipsswiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this::refresh);

        //set up tab layout, allows selecting and filtering tips by type
        TabLayout tabLayout = view.findViewById(R.id.tips_tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //if upgrades
                if (tab.getPosition() == 0) {
                    tabSelected = TipType.UPGRADE;
                    tipsDisplayed.clear();
                    refresh();
                }

                //if advice
                if (tab.getPosition() == 1) {
                    tabSelected = TipType.ADVICE;
                    tipsDisplayed.clear();
                    refresh();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //todo do more efficiently with two listViews
                tipsDisplayed.clear();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //unused, maybe set scroll position to top, or return to list if tip expanded
            }
        });

        return view;
    }

    @Override
    public void onResume() {

        //on resume, set tab type to default, and refresh
        tabSelected = TipType.UPGRADE;
        refresh();

        super.onResume();
    }

    /**
     * Refreshes the contents of the listview and tipsdatabase, by making use of the analytics class.
     *
     * Calls inside this class use {@link #finishRefresh(TipType)} as callback
     */
    private void refresh() {
        //on refresh generate both kinds of tips, putting them in the database on callback
        analytics.generateAdviceTips(tips -> {tipDatabase.setAdviceTips(tips,getContext());finishRefresh(TipType.ADVICE);});
        analytics.generateUpgradeTips(tips -> {tipDatabase.setUpgradeTips(tips,getContext());finishRefresh(TipType.UPGRADE);});

    }

    /**
     * Called when the advice/upgrade tips have been generated and loaded into the database.
     * Sets the listview to point at the right kind of tips
     * @param type the type of tip that was just refreshed
     */
    private void finishRefresh(TipType type) {
        //if the callback type is not the current tab, ignore
        if (tabSelected == type) {
            tipsDisplayed.clear();
            //select the correct type of tips
            if (tabSelected == TipType.UPGRADE) {
                tipsDisplayed.addAll(tipDatabase.getUpgrades());
            } else if (tabSelected == TipType.ADVICE) {
                tipsDisplayed.addAll(tipDatabase.getAdvice());
            }
            listAdapter.notifyDataSetChanged();
        }
        //end the refresh animation
        swipeRefreshLayout.setRefreshing(false);
    }
}
