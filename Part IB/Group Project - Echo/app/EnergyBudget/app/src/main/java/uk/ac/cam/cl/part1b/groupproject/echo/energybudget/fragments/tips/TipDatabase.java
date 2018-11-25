package uk.ac.cam.cl.part1b.groupproject.echo.energybudget.fragments.tips;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ListView;

/**
 * Basic database for storing tips, and writing their state to disk
 *
 * Note: Bad behaviour is possible by editing tip objects that are returned from this, but would not
 * recommend doing so.
 *
 * Created by henrymattinson on 20/02/2018.
 * Updated by oliver on 26/02/2018
 */
public class TipDatabase implements Serializable{

    private final List<Tip> upgrades, advice;
    private static final String FILENAME = "data.tipdatabase";
    private static final String TAG = "TipDatabase";
    // Singleton instance
    private static TipDatabase instance;

    /**
     * Private constructor
     */
    private TipDatabase() {
        this.upgrades = new LinkedList<>();
        this.advice = new LinkedList<>();
    }

    /**
     * Static method that returns an instance of tip database, ensuring only one exists at any one time.
     *
     * @param context context in which to attempt to read old tip data from disk
     * @return the instance of tipDatabase
     */
    public static synchronized TipDatabase getInstance(Context context) {
        if(TipDatabase.instance == null) {
            TipDatabase.instance = new TipDatabase();
            instance.readInstanceFromDisk(context);
        }
        return TipDatabase.instance;
    }

    /**
     * Adds a single tip to the database, and writes to disk.
     * It writes every time, so will be slow if adding multiple tips (use {@link #addTips(List, Context)})
     *
     * Add tip semantics, will add the tip unless it is already in the database, in which case it'll
     * update it's title and description. If you wish to update it's done state, use {@link #updateTip(Tip, boolean, Context)}.
     *
     * @param t tip to add
     * @param context context in which to save state to disk
     */
    public void addTip(Tip t, Context context) {
        addTip(t);
        writeInstanceToDisk(context);
    }

    //Adding tips, used by addTips for efficiency
    private void addTip(Tip t) {
        if (t.getType()==TipType.UPGRADE) {
            int i = upgrades.indexOf(t);
            if (i != -1) {
                t.setDone(upgrades.get(i).isDone());
                upgrades.set(i, t);
            } else {
                upgrades.add(t);
            }
        } else if (t.getType()==TipType.ADVICE) {
            int i = advice.indexOf(t);
            if (i != -1) {
                t.setDone(advice.get(i).isDone());
                advice.set(i, t);
            } else {
                advice.add(t);
            }
        }
    }

    /**
     * Updates the tip ins
     *
     * Note: the tip passed must be the same instance as is retrieved from the tips database
     *
     * @param t tip to be edited, must have been previously returned by this class
     * @param done the done state to put it in
     * @param context the context in which to save it to disk
     */
    public void updateTip(Tip t, boolean done, Context context) {
        t.setDone(done);
        writeInstanceToDisk(context);
    }

    /**
     * Adds multiple tips, see {@link #addTip(Tip, Context)} for details, only writes after all have been
     * added.
     *
     * @param ts list of tips to add
     * @param context context in which to save state to disk
     */
    public void addTips(List<Tip> ts, Context context) {
        for (Tip t :ts) {
            addTip(t);
        }
        writeInstanceToDisk(context);
    }

    /**
     * A wrapper for {@link #addTips(List, Context)}, with logging of how many tips were added,
     * it trusts you give it an array of only Upgrade tips
     *
     * @param tips list of tips to add
     * @param context context in which to save state to disk
     */
    public void setUpgradeTips(List<Tip> tips, Context context) {
        //todo doesn't properly delete outdated upgrades
        addTips(tips,context);
        Log.v(TAG, "Added "+tips.size()+" upgrade tips");
    }

    /**
     * A wrapper for {@link #addTips(List, Context)}, with logging of how many tips were added,
     * it trusts you give it an array of only Advice tips
     *
     * @param tips list of tips to add
     * @param context context in which to save state to disk
     */
    public void setAdviceTips(List<Tip> tips, Context context) {
        addTips(tips,context);
        Log.v(TAG, "Added "+tips.size()+" advice tips");
    }

    /**
     * Deprecated as it just calls {@link #getAdvice()} and {@link #getUpgrades()}
     * @return all the tips in the database
     */
    //Getting tips
    @Deprecated
    public List<Tip> getAllTips() {
        List<Tip> tips = new LinkedList<>();
        tips.addAll(upgrades);
        tips.addAll(advice);
        return tips;
    }

    /**
     * @return all the advice tips in the database
     */
    public List<Tip> getAdvice() {
        synchronized (advice) {
            return this.advice;
        }
    }

    /**
     * @return all the upgrade tips in the database
     */
    public List<Tip> getUpgrades() {
        synchronized (upgrades) {
            return this.upgrades;
        }
    }

    /**
     * Get tips that are neither complete nor ignored, use {@link #getIncomplete(List)} instead
     *
     * @return tips that have not been completed
     */
    @Deprecated
    public List<Tip> getIncompleteTips() {
        List<Tip> incompleteTips = new ArrayList<>();
        List<Tip> tips = getAllTips();
        for (Tip t : tips) {
            if (!(t.isDone()) && !(t.isIgnored())){
                incompleteTips.add(t);
            }
        }
        return incompleteTips;
    }

    /**
     * Removes all completed tips from a list of tips
     *
     * @param tips tips to be filtered
     * @return tips filtered so only incomplete ones remain
     */
    public List<Tip> getIncomplete(List<Tip> tips) {
        List<Tip> incompleteTips = new ArrayList<>();
        for (Tip t : tips) {
            if (!(t.isDone()) && !(t.isIgnored())){
                incompleteTips.add(t);
            }
        }
        return incompleteTips;
    }

    /* DISK */

    private synchronized void writeInstanceToDisk(Context context)  {
        if (context == null) {
            Log.w(TAG, "Null context. Skipping write to disk.");
            return;
        }

        try {
            FileOutputStream f = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(instance);

            o.close();
            f.close();
            Log.v(TAG, "Wrote tips to disk");
        } catch (IOException e) {
            Log.e(TAG, "Failure writing database");
        }
    }

    private synchronized void readInstanceFromDisk(Context context) {
        if (context == null) {
            Log.w(TAG, "Null context. Skipping read from disk.");
            return;
        }

        try {
            FileInputStream fi = context.openFileInput(FILENAME);
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            instance = (TipDatabase) oi.readObject();

            oi.close();
            fi.close();
            Log.v(TAG, "Read tips from disk");
        } catch (IOException e) {
            Log.e(TAG, "Failure reading database - could be empty");
        } catch (ClassNotFoundException e) {
            Log.e(TAG , "Corrupt database");
        }
    }

    /* DISK END */
}
