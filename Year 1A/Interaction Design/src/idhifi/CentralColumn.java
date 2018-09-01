package idhifi;

import idhifi.row_items.*;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper class defining the columns in the central table, each column displaying weather metrics for a single period
 * in time, contains a group which is made up of various AbstractRowItems
 *
 * Created by oliver on 18/05/17.
 */
public class CentralColumn {

    private int width = 80;
    private Group column = new Group();

    private boolean isFirstColumn = false;
    private int timeHeight = 40;
    private int gap = 30;
    private int rowItemHeight = width;
    private List<String> titles = new ArrayList<>();
    private WeatherMetrics weather;

    private AbstractRowItem timeRowItem;
    private List<AbstractRowItem> columnContents;

    private Main root;

    /**
     * Create the column wrapper, providing it with a UI root
     * @param m
     */
    public CentralColumn(Main m) {
        this.root = m;
    }

    /**
     * Please note that calling this method will override anything previously in the column, and data will need to be
     * resupplied
     * @return An instance of the column
     */
    public Group getInstance() {
        timeRowItem = new TimeRowItem(this.width);
        timeRowItem.updateContents(weather);
        Group triGroup = timeRowItem.getInstance();
        column.getChildren().add(triGroup);
        triGroup.setLayoutX(0);
        triGroup.setLayoutY(0);
        triGroup.prefWidth(width);
        triGroup.prefHeight(timeHeight);


        int currentY = timeHeight;
        int currentTitle = 0;
        for (AbstractRowItem rowItem : this.columnContents) {

            TitleRowItem titleRI = new TitleRowItem(this.width);
            Group titleGroup = titleRI.getInstance();
            titleGroup.setLayoutY(currentY);
            titleGroup.setLayoutX(0);
            titleGroup.prefWidth(width);
            titleGroup.prefHeight(gap);
            column.getChildren().add(titleGroup);

            //if is first column add titles in gaps
            if (this.isFirstColumn) {
                titleRI.updateTitle(titles.get(currentTitle));
                currentTitle++;
            } else {
                titleRI.updateTitle("");
            }

            Group riGroup = rowItem.getInstance();
            column.getChildren().add(riGroup);
            riGroup.setLayoutX(0);
            riGroup.setLayoutY(currentY + gap);
            riGroup.prefWidth(width);
            riGroup.prefHeight(rowItemHeight);

            currentY += rowItemHeight + gap;
        }

        return column;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return timeHeight + (gap+rowItemHeight)*(columnContents.size()-1);
    }

    public boolean isFirstColumn() {
        return isFirstColumn;
    }

    /**
     * Must be edited before getInstance() is called
     * @param firstColumn
     */
    public void setFirstColumn(boolean firstColumn) {
        isFirstColumn = firstColumn;
    }

    /**
     * Update the contents of the column, this will involve sending the weather metrics supplied to all UI children of
     * the column (AbstractRowItems)
     * @param weather current weather
     * @param alt current altitude
     * @param baseAlt altitude weather is provided at
     */
    public void updateWeather(WeatherMetrics weather, int alt, int baseAlt) {

        this.weather = weather;
        this.columnContents = new ArrayList<>();

        WindRowItem wind = new WindRowItem(this.width, alt);
        wind.updateContents(weather);
        this.columnContents.add(wind);
        this.titles.add("Wind");

        VisibilityRowItem visibilty = new VisibilityRowItem(this.width);
        visibilty.updateContents(weather);
        this.columnContents.add(visibilty);
        this.titles.add("Visibility (Kilometers)");

        TemperatureRowItem temp = new TemperatureRowItem(this.width, alt, baseAlt, this.root);
        temp.updateContents(weather);
        this.columnContents.add(temp);
        this.titles.add("Temperature");

        PrecipitationRowItem precipitation = new PrecipitationRowItem(this.width);
        precipitation.updateContents(weather);
        this.columnContents.add(precipitation);
        this.titles.add("Precipitation");

        HumidityRowItem humidity = new HumidityRowItem(this.width);
        humidity.updateContents(weather);
        this.columnContents.add(humidity);
        this.titles.add("Humidity");

        CloudCoverRowItem cloudCover = new CloudCoverRowItem(this.width);
        cloudCover.updateContents(weather);
        this.columnContents.add(cloudCover);
        this.titles.add("Cloud Cover");
    }
}
