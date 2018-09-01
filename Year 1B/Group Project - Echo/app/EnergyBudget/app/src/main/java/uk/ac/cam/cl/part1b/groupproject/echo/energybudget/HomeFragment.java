package uk.ac.cam.cl.part1b.groupproject.echo.energybudget;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import uk.ac.cam.cl.part1b.groupproject.echo.energybudget.fragments.tips.Tip;
import uk.ac.cam.cl.part1b.groupproject.echo.energybudget.fragments.tips.TipDatabase;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    private Analytics analytics;

    private ConstraintLayout mLandscape;

    private ImageView mSun;
    private ImageView mMoon;
    private View mGrass;
    private ImageView mPines;
    private ImageView mPoppy;
    private ImageView mPoppy2;
    private ImageView mTulip;
    private ImageView mTulip2;
    private ImageView mTree1;
    private ImageView mTree2;
    private ImageView mSheep;
    private ImageView mFruitTree;
    private ImageView mMountain;
    private ImageView mBigDipper;
    private ImageView mCloud;
    private ImageView mPlant;
    private ImageView mFlowerPot;
    private ImageView mPalmTree;
    private ImageView mFox;
    private ImageView mRabbit;
    private ImageView mRedFlower1;
    private ImageView mRedFlower2;
    private ImageView mYellowFlower1;
    private ImageView mYellowFlower2;
    private ImageView mPinkFlower1;
    private ImageView mPinkFlower2;

    private TextView mEnergyText;
    private TextView mCostText;
    private TextView mCoalText;

    private TextView mTipBody;

    private double rootWidth;
    private double rootHeight;

    private Calendar cal;

    private final static String tree = "tree";
    private final static String flower = "flower";
    private final static String animal = "animal";
    private final static String mountain = "mountain";
    private static final String kitchen = "kitchen";
    private static final String bathroom = "bathroom";
    private static final String entertainment = "entertainment";
    private static final String heating = "heating";
    private static final String light = "light";

    private static final double POWER_BUDGET = 2000.0;

    private int RedColor;
    private int GreenColor;

    UserData user;
    TimeUnit timeUnit = TimeUnit.DAY;
    ProgressBar progressBar;
    TextView progressPercentage;
    TextView progressText;
    RadioGroup timeRadioGroup;

    //Given kWh of electricity, outputs equivalent KG of coal
    public static double findKGofCoal(double kWh) {
        // coal produces roughly 2.46kWh/Kilo
        return kWh/2.46;
    }

    //Given kWh of electricity, outputs cost in pounds
    public static double findMoney (double kWh) {
        // Using average electricity prices in UK for estimate
        return kWh*0.13;
    }

    // UNLOCKS
    // Functions using the badge checking from analytics

    // Different trees require different levels
    private void hasUnlockedNormalTree(Consumer<Boolean> callback) {
        analytics.checkBadge(tree, level ->{
            callback.accept(level>0);
        });
    }

    private void hasUnlockedFruitTree(Consumer<Boolean> callback) {
        analytics.checkBadge(tree, level ->{
            callback.accept(level>1);
        });
    }

    private void hasUnlockedPalmTree(Consumer<Boolean> callback) {
        analytics.checkBadge(tree, level ->{
            callback.accept(level>2);
        });
    }

    private void getFlowerLevel(Consumer<Integer> callback) {
        analytics.checkBadge(flower, callback);
    }

    private void getAnimalLevel(Consumer<Integer> callback) {
        analytics.checkBadge(animal, callback);
    }

    private void hasUnlockedMountain(Consumer<Boolean> callback) {
        analytics.checkBadge(mountain, level ->{
            callback.accept(level>0);
        });
    }

    // DRAWING

    private void moveSunAndMoon() {
        //Defining size of elipse the sun moves through
        double minorAxis = 0.4*rootHeight;
        double majorAxis = 0.35*rootWidth;

        //Finding the time
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        double time = hours+minutes/60;

        // finding parametric t
        // t should change from 0 to 2π during the course of the day
        // 0  at sunrise, π at sunset
        // using 7am as sunrise and 7pm as sunset to allow uniform motion
        double t = (Math.PI/12)*(time-7);
        t = t%(2*Math.PI);

        //position on elipse
        double xDelta = majorAxis*Math.cos(t);
        double yDelta = -minorAxis*Math.sin(t);

        mSun.animate().xBy((float)xDelta).setDuration(0);
        mSun.animate().yBy((float)yDelta).setDuration(0);
        mMoon.animate().xBy((float)-xDelta).setDuration(0);
        mMoon.animate().yBy((float)-yDelta).setDuration(0);
    }

    private void colourSky() {
        //Finding the time
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int time = 60*hours+minutes; //time of day in minutes

        // Colour sky
        int skyColour;
        if (time < 310 || time > 1265) {
            // Middle of the night
            skyColour = R.color.night;
            mBigDipper.setVisibility(View.VISIBLE);
        }
        else if (time >= 310 && time < 355 || time <= 1265 && time >1220) {
            skyColour = R.color.astronomical_twilight;
        }
        else if (time >= 355 && time < 395 || time <= 1220 && time >1180) {
            skyColour = R.color.nautical_twilight;
        }
        else if (time >= 395 && time < 420 || time <= 1180 && time >1140) {
            skyColour = R.color.civil_twilight;
        }
        else {
            skyColour = R.color.daylight;
        }
        mLandscape.setBackgroundColor(ResourcesCompat.getColor(getResources(), skyColour, null));
    }

    // houseAreasScore - checks the different areas of the house where specific badges can be
    //  unlocked and sums up number of badges
    int houseAreasScore;
    private void getCloudColour(Consumer<Integer> callback) {
        houseAreasScore = 0;
        // For all the different badge areas
        analytics.checkBadge(kitchen, kitchenLevel->{
            analytics.checkBadge(bathroom, bathroomLevel->{
                analytics.checkBadge(entertainment, entertainmentLevel->{
                    analytics.checkBadge(heating, heatingLevel->{
                        analytics.checkBadge(light, lightLevel->{
                            // sum the number unlocked
                            houseAreasScore += kitchenLevel;
                            houseAreasScore += bathroomLevel;
                            houseAreasScore += entertainmentLevel;
                            houseAreasScore += heatingLevel;
                            houseAreasScore += lightLevel;
                            // Colour of the cloud depends on number of
                            //  specific badges unlocked
                            if (houseAreasScore < 2) {
                                callback.accept(R.color.sadCloud);
                            }
                            else if (houseAreasScore < 4) {
                                callback.accept(R.color.okCloud);
                            } else {
                                callback.accept(R.color.happyCloud);
                            }
                        });
                    });
                });
            });
        });


    }

    private void moveCloud() {

        // Set as happy as standard so it doesn't appear pitch black if waiting for
        //  asynchronous calls
        setIconColour(mCloud, R.color.happyCloud);
        // Colour Cloud
        getCloudColour(colour ->{
            setIconColour(mCloud,colour);}
        );

        // Move the cloud across the screen
        //  Cloud will move across the screen between quarter past the hours and
        //  quarter to the hour.

        // Finding minutes past the hours
        int mins = cal.get(Calendar.MINUTE);
        // make a translation factor - in range [-1,1)
        double translationFactor = ((double)mins-30)/30;
        // don't want it always on the screen so use time multiplier
        double xDelta = translationFactor*rootWidth;
        // move
        mCloud.animate().xBy((float)xDelta).setDuration(0);
    }

    // A couple of very basic animations to provide a little more visual interest.
    private void performAnimations() {
        // the times in miliseconds between starting an the animations happening
        int[] times = {1000,5000,10000,20000};

        // shuffling the time so animations do not always occur in same order/time
        int index, temp;
        Random random = new Random();
        for (int i=times.length-1; i>0; i--) {
            index = random.nextInt(i + 1);
            temp = times[index];
            times[index] = times[i];
            times[i] = temp;
        }

        //Tree 1
        ObjectAnimator flipTree1 = ObjectAnimator.ofFloat(mTree1, "rotationY", 0f, 180f);
        // delay by first time in shuffled array of times
        flipTree1.setStartDelay(times[0]);
        flipTree1.start();

        //Fruit Tree
        ObjectAnimator flipFruitTree = ObjectAnimator.ofFloat(mFruitTree, "rotationY", 0f, 180f);
        // delay by second time in shuffled array of times
        flipFruitTree.setStartDelay(times[1]);
        flipFruitTree.start();
    }

    private void drawLandscape() {
        moveSunAndMoon();
        colourSky();
        moveCloud();

        //Trees
        hasUnlockedNormalTree(bool->{
            if (bool){
                mTree1.setVisibility(View.VISIBLE);
            }
        });

        hasUnlockedFruitTree(bool->{
            if (bool){
                mFruitTree.setVisibility(View.VISIBLE);
            }
        });

        hasUnlockedPalmTree(bool->{
            if (bool){
                mPalmTree.setVisibility(View.VISIBLE);
            }
        });

        // Plants/Flower

        getFlowerLevel(flowerLevel ->{
            // Setting which flowers show at differnt levels
            if (flowerLevel > 0) {
                mPlant.setVisibility(View.VISIBLE);
                mYellowFlower2.setVisibility(View.VISIBLE);
                mPinkFlower2.setVisibility(View.VISIBLE);
                if (flowerLevel >1) {
                    mYellowFlower1.setVisibility(View.VISIBLE);
                    mRedFlower1.setVisibility(View.VISIBLE);
                    if (flowerLevel > 2) {
                        mFlowerPot.setVisibility(View.VISIBLE);
                        mPinkFlower1.setVisibility(View.VISIBLE);
                        mRedFlower2.setVisibility(View.VISIBLE);
                    }
                }
            }
        } );



        // Animals
        getAnimalLevel(animalLevel ->{
            // Setting which animals show at different levels
            if (animalLevel > 0) {
                mSheep.setVisibility(View.VISIBLE);
                if (animalLevel > 1) {
                    mRabbit.setVisibility(View.VISIBLE);
                    if (animalLevel > 2) {
                        mFox.setVisibility(View.VISIBLE);
                    }
                }
            }
        } );


        // Mountain
        hasUnlockedMountain(bool ->{
            if(bool){
                mMountain.setVisibility(View.VISIBLE);
            }
        }
        );

        performAnimations();
    }

    // CONSUMPTION

    // Calculating and displaying the 3 main metrics shown
    private void displayConsumptions(double energyAmount) {
        String energyPrefix = "Energy:  ";
        String costPrefix = "Cost:      £";
        String coalPrefix = "Coal:      ";

        String energySuffix = " kWh";
        String coalSuffix = " KG";

        // energyAmount is the amount of energy used in the time period
        double costAmount = findMoney(energyAmount);
        double coalAmount  = findKGofCoal(energyAmount);

        //Convert sensible precision strings
        String sEnergyAmount = String.format("%.2f", energyAmount);
        String sCostAmount = String.format("%.2f", costAmount);
        String sCoalAmount = String.format("%.2f", coalAmount);

        String energyText = energyPrefix + sEnergyAmount + energySuffix;
        String costText = costPrefix + sCostAmount;
        String coalText = coalPrefix + sCoalAmount + coalSuffix;

        mEnergyText.setText(energyText);
        mCostText.setText(costText);
        mCoalText.setText(coalText);
    }

    // TIP

    // Extracts the top advice tip from TipDB. If no advice then appliance upgrade tip
    private void writeTip() {
        String tip;
        //get list of advice tips
        List<Tip> advice = TipDatabase.getInstance(getContext()).getAdvice();
        if (advice.size() > 0) {
            //get description of first tip
            tip = advice.get(0).getDescription();
        }
        else {
            // If no advice tips, get appliance upgrades
            List<Tip> upgrades = TipDatabase.getInstance(getContext()).getUpgrades();
            if (upgrades.size() > 0) {
                tip = upgrades.get(0).getDescription();
            }
            else {
                // Placeholder tip if no tips found from either list
                tip = "You will consume less energy if you only put the amount of water you need " +
                        "in the kettle, rather than boiling a whole kettle.";
            }
        }
        mTipBody.setText(tip);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);

        this.mLandscape = rootView.findViewById(R.id.house_frame);

        //Finding the time
        Date date = new Date();
        this.cal = Calendar.getInstance();
        cal.setTime(date);

        //Find dimensions of the house frame
        ConstraintLayout layout = (ConstraintLayout) mLandscape;
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int width  = layout.getMeasuredWidth();
                int height = layout.getMeasuredHeight();
                HomeFragment.this.rootWidth = width;
                HomeFragment.this.rootHeight = height;

                UserData.getUser(user -> {
                    analytics = new Analytics(user);
                    // Drawlandscape placed in here as it needs to know landscape dimensions to
                    //  move certain components - hence the global layout must be decided
                    drawLandscape();
                    writeTip();
                });
            }
        });

        mSun = rootView.findViewById( R.id.SunView );
        mMoon = rootView.findViewById( R.id.Moon );
        mGrass = rootView.findViewById(R.id.grass);
        mPines = rootView.findViewById(R.id.Pines);
        mPoppy = rootView.findViewById(R.id.Poppy);
        mPoppy2 = rootView.findViewById(R.id.Poppy2);
        mTulip = rootView.findViewById(R.id.Tulip);
        mTulip2 = rootView.findViewById(R.id.Tulip2);
        mTree1 = rootView.findViewById(R.id.Tree1);
        mTree2 = rootView.findViewById(R.id.Tree2);
        mSheep = rootView.findViewById(R.id.Sheep);
        mFruitTree = rootView.findViewById(R.id.FruitTree);
        mMountain = rootView.findViewById(R.id.Mountain);
        mBigDipper = rootView.findViewById(R.id.BigDipper);
        mBigDipper.setVisibility(View.GONE);
        mCloud = rootView.findViewById(R.id.Cloud);
        mPlant = rootView.findViewById(R.id.Plant1);
        mFlowerPot = rootView.findViewById(R.id.FlowerPot);
        mPalmTree = rootView.findViewById(R.id.PalmTree);
        mFox = rootView.findViewById(R.id.Fox);
        mRabbit = rootView.findViewById(R.id.Rabbit);
        mRedFlower1 = rootView.findViewById(R.id.RedFlower1);
        mRedFlower2 = rootView.findViewById(R.id.RedFlower2);
        mYellowFlower1 = rootView.findViewById(R.id.YellowFlower1);
        mYellowFlower2 = rootView.findViewById(R.id.YellowFlower2);
        mPinkFlower1 = rootView.findViewById(R.id.PinkFlower1);
        mPinkFlower2 = rootView.findViewById(R.id.PinkFlower2);

        mEnergyText = rootView.findViewById(R.id.EnergyAmount);
        mCostText = rootView.findViewById(R.id.CostAmount);
        mCoalText = rootView.findViewById(R.id.CoalAmount);

        mTipBody = rootView.findViewById(R.id.TipOfTheDayBody);

        RedColor = ContextCompat.getColor(getContext(), R.color.flower_badge);
        GreenColor = ContextCompat.getColor(getContext(), R.color.tree_badge);

        progressBar = rootView.findViewById(R.id.progressBar);
        progressPercentage = rootView.findViewById(R.id.progressPercentage);
        progressText = rootView.findViewById(R.id.progressText);
        timeRadioGroup = rootView.findViewById(R.id.homeTimeRadioGroup);

        UserData.getUser(this::init);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void init(UserData userData) {
        user = userData;
        displayUsage();

        // set time radio group listener so we can change the data on click
        timeRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.dayRadioButton:
                    timeUnit = TimeUnit.DAY;
                    break;
                case R.id.weekRadioButton:
                    timeUnit = TimeUnit.WEEK;
                    break;
                case R.id.monthRadioButton:
                    timeUnit = TimeUnit.MONTH;
                    break;
                case R.id.yearRadioButton:
                    timeUnit = TimeUnit.YEAR;
                    break;
                default: // shouldn't happen
                    Log.w(TAG, "Invalid button provided to checked listener.");
            }
            displayUsage();
        });
    }

    public static void setIconColour(ImageView imageView, int colour){
        //Set the colour for the particular icon
        imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), colour),
                PorterDuff.Mode.SRC_ATOP);
    }

    // Gives the consumption so far in the time period and informs the user if they are on track.
    public void displayUsage() {
        // set a day in the future to get current period
        int ets = TimeUnit.nowSec() + TimeUnit.toSec(timeUnit) - 1;
        user.getRange(timeUnit, ets, 2, range -> {
            if (range.isEmpty()) return;

            int now = TimeUnit.nowSec();
            if (now < range.firstKey()) {
                Log.w(TAG,"No usage for this period.");
                return;
            }
            int t = range.floorKey(now);

            int duration = TimeUnit.nowSec() - t;

            double power = range.get(t);

            double energy = power * ((double)duration / 3600.0);

            // :D
            double energyBudget = POWER_BUDGET * (double)TimeUnit.toSec(timeUnit) / 3600.0;
            // :D

            double energyLeft = (energyBudget - energy) / 1000.0;
            int progress = (int)(Math.min(1.0, energy / energyBudget) * 100.0);
            progressBar.setProgress(progress);

            String percentageString = progress + "%";
            progressPercentage.setText(percentageString);

            String progressString;
            if (energyLeft <= 0.0) {
                progressString = "You have used your energy budget :(";
            } else {
                String left =  new DecimalFormat("#.##").format(energyLeft);
                progressString = "You have " + left + "kWh left. ";
                if (power <= POWER_BUDGET) {
                    progressString += "You are on track! :)";
                    progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.energy_progress_green, getContext().getTheme()));
                } else {
                    progressString += "You are not on track.";
                    progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.energy_progress_red, getContext().getTheme()));
                }
            }
            progressText.setText(progressString);

            //Finding actual kWh used
            double kWh = energy/1000;
            displayConsumptions(kWh);
        });


    }
}
