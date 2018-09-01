package uk.ac.cam.cl.part1b.groupproject.echo.energybudget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class BadgeAdapter extends BaseAdapter {
    private Context mContext;
    private String mType;
    private Analytics analytics;
    private final static String tree = "tree";
    private final static String flower = "flower";
    private final static String animal = "animal";
    private final static String mountain = "mountain";
    private static final String kitchen = "kitchen";
    private static final String bathroom = "bathroom";
    private static final String entertainment = "entertainment";
    private static final String housekeeping = "housekeeping";
    private static final String heating = "heating";
    private static final String light = "light";
    //strings to display if badges are locked
    private static final String daily_usage =
            "You can unlock this badge by saving more energy on a daily basis. Be careful, you can lose the badge if your usage increases again!";
    private static final String weekly_usage =
            "You can unlock this badge by saving more energy on a weekly basis. Be careful, you can lose the badge if your usage increases again!";
    private static final String monthly_usage =
            "You can unlock this badge by saving more energy on a monthly basis. Be careful, you can lose the badge if your usage increases again!";


    // references to device icons
    private static Integer[] deviceBadgeIcons = {
            R.drawable.kettle_badge,
            R.drawable.drier_badge,
            R.drawable.tv_badge,
            R.drawable.heater_badge,
            R.drawable.light_badge
    };

    //array that holds the icons for the current adapter
    private Integer[] currentIcons;

    // references to house icons
    private static Integer[] houseBadgeIcons = {
            R.drawable.tree1_badge,
            R.drawable.tree2_badge,
            R.drawable.tree3_badge,
            R.drawable.flower1_badge,
            R.drawable.flower2_badge,
            R.drawable.flower3_badge,
            R.drawable.sheep_badge,
            R.drawable.rabbit_badge,
            R.drawable.fox_badge,
            R.drawable.mountain_badge,
    };

    private static Map<Integer, Integer> badgeLevels;
    private static Map<Integer, String> badgeCategoryMap;
    private static Map<Integer, String> badgeTitles;
    private static Map<Integer, String> badgeEnabledContent;
    private static Map<Integer, String> badgeDisabledContent;
    public static Map<String, Integer> categoryToId;
    static{
        // ids for the appliance badges
        categoryToId = new HashMap<>();
        categoryToId.put(kitchen, 9);
        categoryToId.put(heating, 41);
        categoryToId.put(light, 304);
        categoryToId.put(bathroom, 43);
        categoryToId.put(entertainment, 30);
        // available levels for the house badges
        badgeLevels = new HashMap<>();
        badgeLevels.put(R.drawable.tree1_badge, 1);
        badgeLevels.put(R.drawable.tree2_badge, 2);
        badgeLevels.put(R.drawable.tree3_badge, 3);
        badgeLevels.put(R.drawable.flower1_badge, 1);
        badgeLevels.put(R.drawable.flower2_badge, 2);
        badgeLevels.put(R.drawable.flower3_badge, 3);
        badgeLevels.put(R.drawable.sheep_badge, 1);
        badgeLevels.put(R.drawable.rabbit_badge, 2);
        badgeLevels.put(R.drawable.fox_badge, 3);
        badgeLevels.put(R.drawable.mountain_badge, 1);
        badgeLevels.put(R.drawable.kettle_badge, 1);
        badgeLevels.put(R.drawable.drier_badge, 1);
        badgeLevels.put(R.drawable.tv_badge, 1);
        badgeLevels.put(R.drawable.heater_badge, 1);
        badgeLevels.put(R.drawable.light_badge, 1);
        //categories of each badge icon
        badgeCategoryMap = new HashMap<>();
        badgeCategoryMap.put(R.drawable.tree1_badge, tree);
        badgeCategoryMap.put(R.drawable.tree2_badge, tree);
        badgeCategoryMap.put(R.drawable.tree3_badge, tree);
        badgeCategoryMap.put(R.drawable.flower1_badge, flower);
        badgeCategoryMap.put(R.drawable.flower2_badge, flower);
        badgeCategoryMap.put(R.drawable.flower3_badge, flower);
        badgeCategoryMap.put(R.drawable.sheep_badge, animal);
        badgeCategoryMap.put(R.drawable.rabbit_badge, animal);
        badgeCategoryMap.put(R.drawable.fox_badge, animal);
        badgeCategoryMap.put(R.drawable.mountain_badge, mountain);
        badgeCategoryMap.put(R.drawable.kettle_badge, kitchen);
        badgeCategoryMap.put(R.drawable.drier_badge, bathroom);
        badgeCategoryMap.put(R.drawable.tv_badge, entertainment);
        badgeCategoryMap.put(R.drawable.heater_badge, heating);
        badgeCategoryMap.put(R.drawable.light_badge, light);
        //displayed upon clicking on each icon
        badgeTitles = new HashMap<>();
        badgeTitles.put(R.drawable.tree1_badge, "Tree planter");
        badgeTitles.put(R.drawable.tree2_badge, "Environment enthusiast");
        badgeTitles.put(R.drawable.tree3_badge, "Planet saviour");
        badgeTitles.put(R.drawable.flower1_badge, "Flowers lover");
        badgeTitles.put(R.drawable.flower2_badge, "Amateur gardener");
        badgeTitles.put(R.drawable.flower3_badge, "Professional florist");
        badgeTitles.put(R.drawable.sheep_badge, "Shepherd");
        badgeTitles.put(R.drawable.rabbit_badge, "Rabbit owner");
        badgeTitles.put(R.drawable.fox_badge, "Fox owner");
        badgeTitles.put(R.drawable.mountain_badge, "Mountaineer");
        badgeTitles.put(R.drawable.kettle_badge, "Fill it up to half!");
        badgeTitles.put(R.drawable.drier_badge, "Dry in the sun!");
        badgeTitles.put(R.drawable.tv_badge, "Get up from the couch!");
        badgeTitles.put(R.drawable.heater_badge, "Cool it down!");
        badgeTitles.put(R.drawable.light_badge, "Turn the lights off!");
        //extra information displayed upon clicking enabled badges
        badgeEnabledContent = new HashMap<>();
        badgeEnabledContent.put(R.drawable.tree1_badge,
                "You have unlocked a new tree for your main house!");
        badgeEnabledContent.put(R.drawable.tree2_badge,
                "You have unlocked another tree for your main house! Now you have two extra trees to take care of.");
        badgeEnabledContent.put(R.drawable.tree3_badge,
                "You have unlocked the final tree for your main house! Now you have three extra trees to take care of.");
        badgeEnabledContent.put(R.drawable.flower1_badge,
                "You have unlocked a new flower for your main house!");
        badgeEnabledContent.put(R.drawable.flower2_badge,
                "You have unlocked another flower for your main house! Now you have two extra flowers to take care of.");
        badgeEnabledContent.put(R.drawable.flower3_badge,
                "You have unlocked the final flower for your main house! Now you have three extra trees to take care of.");
        badgeEnabledContent.put(R.drawable.sheep_badge,
                "You have unlocked a sheep for your main house!");
        badgeEnabledContent.put(R.drawable.rabbit_badge,
                "You have unlocked another animal for your main house - a rabbit! Now you have two animals to take care of.");
        badgeEnabledContent.put(R.drawable.fox_badge,
                "You have unlocked the last animal for your main house - a fox! Now you have three animals to take care of.");
        badgeEnabledContent.put(R.drawable.mountain_badge,
                "You have unlocked a mountain for your main house!");
        badgeEnabledContent.put(R.drawable.kettle_badge,
                "You saved energy by using the kettle less often - well done!");
        badgeEnabledContent.put(R.drawable.drier_badge,
                "You saved energy by using the tumble dryer less often - well done!");
        badgeEnabledContent.put(R.drawable.tv_badge,
                "You saved energy by turning the TV off more often - well done!");
        badgeEnabledContent.put(R.drawable.heater_badge,
                "You saved energy by turning the heating off more often - well done!");
        badgeEnabledContent.put(R.drawable.light_badge,
                "You saved energy by turning the lights off more often - well done!");
        //information displayed upon clicking disabled badges
        badgeDisabledContent = new HashMap<>();
        badgeDisabledContent.put(R.drawable.tree1_badge, daily_usage);
        badgeDisabledContent.put(R.drawable.tree2_badge, daily_usage);
        badgeDisabledContent.put(R.drawable.tree3_badge, daily_usage);
        badgeDisabledContent.put(R.drawable.flower1_badge, weekly_usage);
        badgeDisabledContent.put(R.drawable.flower2_badge, weekly_usage);
        badgeDisabledContent.put(R.drawable.flower3_badge, weekly_usage);
        badgeDisabledContent.put(R.drawable.sheep_badge, weekly_usage);
        badgeDisabledContent.put(R.drawable.rabbit_badge, weekly_usage);
        badgeDisabledContent.put(R.drawable.fox_badge, weekly_usage);
        badgeDisabledContent.put(R.drawable.mountain_badge, monthly_usage);
        badgeDisabledContent.put(R.drawable.kettle_badge,
                "You can unlock this badge by saving more energy on kettle usage.");
        badgeDisabledContent.put(R.drawable.drier_badge,
                "You can unlock this badge by saving more energy on tumble dryer usage.");
        badgeDisabledContent.put(R.drawable.tv_badge,
                "You can unlock this badge by saving more energy on tv usage.");
        badgeDisabledContent.put(R.drawable.heater_badge,
                "You can unlock this badge by saving more energy on heating usage.");
        badgeDisabledContent.put(R.drawable.light_badge,
                "You can unlock this badge by saving more energy on light usage.");
    }


    public Map<Integer, Integer> idMap;


    public BadgeAdapter(Context c, String type) {
        mContext = c;
        //keep a type of adapter - either house or device
        mType = type;

        UserData.getUser(user -> {
            analytics = new Analytics(user);
        });

        //keep a map from typeIds to keys
        idMap = new HashMap<>();
        for (int j=0; j<BadgesFragment.userData.appliances.size(); j++){
            //for each available appliance for the user, map the typeId to actual id
            int key = BadgesFragment.userData.appliances.keyAt(j);
            int typeId = BadgesFragment.userData.appliances.get(key).getTypeId();
            idMap.put(typeId, key);
        }

        updateHiddenPositions();
    }

    //hardcoded for now
    public void calculateBadge(String category, Consumer<Integer> callback) {

        analytics.checkBadge(category, level -> {
            callback.accept(level);
        }
        );
    }


    public int getCount() {
        if (mType.equals(BadgesFragment.device)){
            return deviceBadgeIcons.length - hiddenPositions.size();
        } else {
            return houseBadgeIcons.length;
        }
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    private ArrayList<Integer> hiddenPositions = new ArrayList<>();
    private void updateHiddenPositions(){
        //add an id to hidden position if the user has no data for this appliance
        //or if the category is different than the selected category
        hiddenPositions.clear();

        for(int i= 0; i<deviceBadgeIcons.length; i++){
            int icon = deviceBadgeIcons[i];
            String category = badgeCategoryMap.get(icon);
            Integer id = categoryToId.get(category);
            if (!idMap.containsKey(id)){
                hiddenPositions.add(i);
            }
        }
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        //hide items as necessary
        if (mType.equals(BadgesFragment.device)) {
            for (Integer hiddenIndex : hiddenPositions) {
                if (hiddenIndex <= position) {
                    position = position + 1;
                }
            }
        }
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels / 4;
            int height = width;
            imageView.setLayoutParams(new GridView.LayoutParams(width, height));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setId(position);
        } else {
            imageView = (ImageView) convertView;
        }

        //set which icons are used
        if (mType.equals(BadgesFragment.house)){
            currentIcons = houseBadgeIcons;
        } else {
            currentIcons = deviceBadgeIcons;
        }

        //get the icon and category
        Integer icon = currentIcons[position];
        imageView.setImageResource(icon);
        String category = badgeCategoryMap.get(icon);

        //fetch the badge level asynchronously
        calculateBadge(category, level->{
            //make it grey if disabled
            if(level<badgeLevels.get(icon)){
                imageView.clearColorFilter();
            } else {
                //otherwise recolour it according to category
                setIconColour(imageView, colourSelection(category));
            }

            //if the badge is clicked by the user
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    //inflate the view for the popupwindow
                    View popupView = inflater.inflate(R.layout.popup, null);

                    int colour;
                    String content;
                    //set colours and content depending if enabled or disabled
                    if (level<badgeLevels.get(icon)) {
                        colour = R.color.greyed_out;
                        content = badgeDisabledContent.get(icon);
                    } else {
                        colour = colourSelection(category);
                        content = badgeEnabledContent.get(icon);
                    }

                    //set the border color
                    LinearLayout border = popupView.findViewById(R.id.popup_border);
                    border.setBackground(new ColorDrawable(ContextCompat.getColor(popupView.getContext(), colour)));

                    //set the title
                    TextView title = popupView.findViewById(R.id.title);
                    title.setText(badgeTitles.get(icon));
                    title.setTextColor(ContextCompat.getColor(popupView.getContext(), colour));

                    TextView contentView = popupView.findViewById(R.id.content);
                    contentView.setText(content);

                    //set the maximum size
                    DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
                    int width = displayMetrics.widthPixels * 2 / 3;
                    int height = displayMetrics.heightPixels * 2 / 3;
                    //get the measured size of the content
                    int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
                    int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
                    //create a popup view of whichever size is smaller(mesured or maximum)
                    popupView.measure(widthMeasureSpec, heightMeasureSpec);
                    int popupHeight = popupView.getMeasuredHeight();
                    int popupWidth = popupView.getMeasuredWidth();

                    //create a popup window
                    PopupWindow popupWindow = new PopupWindow(popupView, popupWidth, popupHeight);
                    popupWindow.setTouchable(true);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setBackgroundDrawable(new ColorDrawable());
                    popupWindow.setTouchInterceptor(new View.OnTouchListener(){
                        @Override public boolean onTouch(View v, MotionEvent event){
                            popupWindow.dismiss();
                            return true; } });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                }
                }
        );

        });

        return imageView;
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
            case tree:
                colour = R.color.tree_badge;
                break;
            case flower:
                colour = R.color.flower_badge;
                break;
            case animal:
                colour = R.color.animal_badge;
                break;
            case mountain:
                colour = R.color.mountain_badge;
                break;
            case kitchen:
                colour = R.color.kitchen_badge;
                break;
            case bathroom:
                colour = R.color.bathroom_badge;
                break;
            case entertainment:
                colour = R.color.entertainment_badge;
                break;
            case housekeeping:
                colour = R.color.housekeeping_badge;
                break;
            case heating:
                colour = R.color.heating_badge;
                break;
            case light:
                colour = R.color.light_badge;
                break;
        }
        return colour;
    }
}
