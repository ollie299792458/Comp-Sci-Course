package idhifi.row_items;

import idhifi.WeatherMetrics;
import javafx.scene.Group;

/**
 * Abstract class used to graphically define cells in the table of weather
 *
 * Created by oliver on 18/05/17.
 */
public abstract class AbstractRowItem {
    /**
     * Current altitude as set by user
     */
    protected int currentAltitude;

    /**
     * Update the contents of the current cell by pulling data from the WeatherMetrics object supplied
     * @param contents Weather metrics
     */
    public abstract void updateContents(WeatherMetrics contents);

    /**
     * Returns an instance of the JavaFX group, should be implemented so that two consecutive calls return the same group
     * @return
     */
    public abstract Group getInstance();

    /**
     * Get the type of the cell
     * @return Type of the cell
     */
    public abstract RowItemType getType();

    /**
     * Update the weather metrics on the tile when the altitude is changed by the user without requiring a complete update of everything
     * @param newAltitude the most recent altitude selected by the user/application
     */
    public abstract void onAltitudeChange(int newAltitude);
}
