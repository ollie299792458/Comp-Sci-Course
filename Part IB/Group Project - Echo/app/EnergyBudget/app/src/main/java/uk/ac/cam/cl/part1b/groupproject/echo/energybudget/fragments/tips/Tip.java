package uk.ac.cam.cl.part1b.groupproject.echo.energybudget.fragments.tips;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by oliver on 13/02/18.
 *
 * Basic data class for tips to be displayed, to allow data structures to be build around tips
 *
 * Should be implemented into a database on any future rewrite
 */
public class Tip implements Comparable<Tip>, Serializable {
    private String title = "";
    private String description = "";
    private boolean done;
    private int importance;
    private TipType type;
    private int applianceId;

    /**
     * Up to date constructor
     *
     * @param title Title of the tip
     * @param description Description of the tip
     * @param done Whether or not the tip has been done
     * @param importance How important the tip is, 1 being of high importance
     * @param type What type the tips is
     * @param applianceId The identifier of the appliance associated with the tip
     */
    public Tip(@NonNull String title, @NonNull String description, boolean done, int importance, @NonNull TipType type, int applianceId) {
        this.title = title;
        this.description = description;
        this.done = done;
        this.importance = importance;
        this.type = type;
        this.applianceId = applianceId;
    }

    /**
     * Deprecated constructor, does not include support for applianceId, so will result in duplicate tips, due to the absence of effective equality testing
     */
    @Deprecated
    public Tip(String title, String description, boolean done, int importance, TipType type) {
        this.title = title;
        this.description = description;
        this.done = done;
        this.importance = importance;
        this.type = type;
    }

    /**
     * Deprecated constructor, does not include support for applianceId, so will result in duplicate tips, due to the absence of effective equality testing
     * Also includes the 'ignored' boolean, which is no longer used
     */
    @Deprecated
    public Tip(String title, String description, boolean ignored, boolean done, int importance, TipType type) {
        this.title = title;
        this.description = description;
        this.done = done;
        this.importance = importance;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Deprecated //always returns false
    public boolean isIgnored() {
        return false;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    /**
     * Imposes an order on lists
     * Not Done or Ignored
     * Done
     * Ignored
     *
     * Ties are broken by importance (higher at top), followed by title (alphabetically first at top)
     *
     * Note: All tips are now ignored
     *
     * @param o tip to compare to
     * @return See {@link Comparable#compareTo(Object)}'s return value
     */
    @Override
    public int compareTo(@NonNull Tip o) {
        Tip tip1 = this;
        Tip tip2 = o;
        int less = 1;
        int more = -1;
        if (tip1.isDone() && !tip2.isDone()) {
            return less;
        } else if (!tip1.isDone() && tip2.isDone()) {
            return more;
        } else {
            if (tip1.isIgnored() && !tip2.isIgnored()) {
                return less;
            } else if (!tip1.isIgnored() && tip2.isIgnored()) {
                return more;
            } else {
                if (tip1.isDone() && tip2.isIgnored()) {
                    return less;
                }
                if (tip1.isIgnored() && tip2.isDone()) {
                    return more;
                }
                if (tip1.getImportance() > tip2.getImportance()) {
                    return more;
                }
                if (tip2.getImportance() > tip1.getImportance()) {
                    return less;
                }
                return tip1.getTitle().compareTo(tip2.getTitle());
            }
        }
    }

    /**
     * Equality testing for tips, a tip is equal to another tip iff it is of the same type, and it
     * is associated with the same appliance ID.
     *
     * @param t the object to be compared to
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object t) {
        if (t instanceof  Tip) {
            if (((Tip) t).type == TipType.UPGRADE && type == TipType.UPGRADE) {
                if (applianceId == ((Tip) t).applianceId) {
                    return true;
                }
            }
            if (type == TipType.ADVICE && ((Tip) t).type == TipType.ADVICE) {
                if (applianceId == ((Tip) t).applianceId) {
                    return true;
                }
            }
        }
        return false;
    }

    public TipType getType() {
        return type;
    }

    public void setType(TipType type) {
        this.type = type;
    }

    public int getApplianceId() {
        return applianceId;
    }

    public void setApplianceId(int applianceId) {
        this.applianceId = applianceId;
    }
}
