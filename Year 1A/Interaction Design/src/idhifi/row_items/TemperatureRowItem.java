package idhifi.row_items;

import idhifi.Main;
import idhifi.WeatherMetrics;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Implementation of AbstractRowItem for temperature
 *
 * Created by oliver on 18/05/17.
 */
public class TemperatureRowItem extends AbstractRowItem {

    private int width;
    private int height;
    private int altitude;
    private int baseAltitude;
    private double unadjustedTemp;
    private Main root;
    private Group item;
    private Text temperature;
    private Rectangle bg;


    public TemperatureRowItem(int w, int alt, int baseAlt, Main m) {
        this.width = w;
        this.height = w;
        this.root = m;
        this.altitude = alt;
        this.baseAltitude = baseAlt;
        this.root.subscribeToAltitudeChange(this);
    }

    @Override
    public void updateContents(WeatherMetrics contents) {
        this.item = new Group();
        this.bg = new Rectangle(0, 0, this.width, this.height);
        this.bg.setFill(CellBackgroundColours.temperature(this.adjustTemperatureForAltitude(contents.getTemperature())));
        this.item.getChildren().add(this.bg);

        DropShadow ds = new DropShadow();
        ds.setOffsetY(0.3f);
        ds.setSpread(0.8);
        ds.setRadius(4);
        ds.setColor(Color.color(0, 0, 0, 0.5));

        this.unadjustedTemp = contents.getTemperature();
        this.temperature = new Text(this.adjustTemperatureForAltitude(contents.getTemperature()) + "°C");
        this.temperature.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 22));
        this.temperature.setFill(Paint.valueOf("ffffff"));
        this.temperature.setEffect(ds);
        double tWidth = this.temperature.getLayoutBounds().getWidth();
        double tHeight = this.temperature.getLayoutBounds().getHeight();
        this.temperature.setLayoutY(19 + (this.height - tHeight)/2);
        this.temperature.setLayoutX((this.width - tWidth)/2);
        this.item.getChildren().add(this.temperature);
    }

    @Override
    public Group getInstance() {
        return this.item;
    }

    @Override
    public RowItemType getType() {
        return RowItemType.TEMPERATURE;
    }

    @Override
    public void onAltitudeChange(int newAltitude) {
        this.altitude = newAltitude;
        this.temperature.setText(this.adjustTemperatureForAltitude(this.unadjustedTemp) + "°C");
        this.bg.setFill(CellBackgroundColours.temperature(this.adjustTemperatureForAltitude(this.unadjustedTemp)));
    }

    private int adjustTemperatureForAltitude(double temp) {
        return (new Double(temp - 0.82*((this.altitude - this.baseAltitude)/100))).intValue();
    }
}
