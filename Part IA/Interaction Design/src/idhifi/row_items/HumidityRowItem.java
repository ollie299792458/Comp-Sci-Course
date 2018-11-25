package idhifi.row_items;

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
 * Implementation of AbstractRowItem for Humidity
 *
 * Created by oliver on 18/05/17.
 */
public class HumidityRowItem extends AbstractRowItem {

    private int width, height;
    private Group item;
    private Rectangle bg;
    private Text humidity;

    public HumidityRowItem(int w) {
        this.width = w;
        this.height = w;
    }

    @Override
    public void updateContents(WeatherMetrics contents) {
        this.item = new Group();
        this.bg = new Rectangle(0, 0, this.width, this.height);
        this.bg.setFill(CellBackgroundColours.humidity(100*contents.getHumidity()));
        this.item.getChildren().add(this.bg);

        DropShadow ds = new DropShadow();
        ds.setOffsetY(0.3f);
        ds.setSpread(0.8);
        ds.setRadius(4);
        ds.setColor(Color.color(0, 0, 0, 0.5));

        this.humidity = new Text((new Double(100*contents.getHumidity()).intValue()) + "%");
        this.humidity.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 22));
        this.humidity.setFill(Paint.valueOf("ffffff"));
        this.humidity.setEffect(ds);
        double tWidth = this.humidity.getLayoutBounds().getWidth();
        double tHeight = this.humidity.getLayoutBounds().getHeight();
        this.humidity.setLayoutY(19 + (this.height - tHeight)/2);
        this.humidity.setLayoutX((this.width - tWidth)/2);
        this.item.getChildren().add(this.humidity);
    }

    @Override
    public Group getInstance() {
        return this.item;
    }

    @Override
    public RowItemType getType() {
        return RowItemType.HUMIDITY;
    }

    @Override
    public void onAltitudeChange(int newAltitude) {
        // Nothing to do.
    }
}
