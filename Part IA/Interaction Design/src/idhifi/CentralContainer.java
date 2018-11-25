package idhifi;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Central UI component of the app, the table in which all non static weather metrics are displayed, impleneted as a
 * wrapper around a javafx group
 *
 * Created by Harri on 16/05/2017.
 */
public class CentralContainer {

    private Group previous;
    private List<WeatherMetrics> data;
    private Main parent;

    /**
     * Get a table of weather metrics (AbstractRowItems in CentralColumns)
     * @param addr current address (to be used to get weather metrics)
     * @param alt current altitude (as set by the user)
     * @param baseAlt current base altitude (the altitude at which the weather metrics for the current address are
     *                accurate)
     * @return group containing a a table made up of CentralColumns
     */
    public Group getInstance(Address addr, int alt, int baseAlt) {

        try {
            Group container = new Group();
            container.setStyle("-fx-border-width: 0px;-fx-border-insets: 0px;");

            this.data = DarkSkyAPIParsing.getWeather(addr);

            // get this from ollie
            CentralColumn reference = new CentralColumn(this.parent);
            int colWidth = reference.getWidth();
            int index = 0;
            CentralColumn cc;
            Group col;
            for(WeatherMetrics dataSlice : data) {

                cc = new CentralColumn(this.parent);
                if(index == 0) cc.setFirstColumn(true);
                cc.updateWeather(dataSlice, alt, baseAlt);
                col = cc.getInstance();
                col.setLayoutX(index * colWidth);
                col.setLayoutY(0);
                container.getChildren().add(col);
                index++;
            }

            container.getChildren().get(0).toFront();
            container.prefWidth(colWidth * data.size());
            return container;

        }
        catch(WeatherDataNotFound e) {
            if(this.previous != null) return this.previous;
            else return new Group();
        }


    }

    /**
     * Set the UI root of the container
     * @param m the ui root
     */
    public void setParent(Main m) {
        this.parent = m;
    }


}
