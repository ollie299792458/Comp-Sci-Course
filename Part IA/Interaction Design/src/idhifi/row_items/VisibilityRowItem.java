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
 * Implementation of AbstractRowItem for visibility
 *
 * Created by oliver on 18/05/17.
 */
public class VisibilityRowItem extends AbstractRowItem {

    private Group item;
    private int height = 100;
    private int width = 100;

    public VisibilityRowItem(int w) {
        this.width = w;
        this.height = w;
    }

    @Override
    public void updateContents(WeatherMetrics weather) {
        this.item = new Group();
        Rectangle bg = new Rectangle(0, 0, this.width, this.height);
        bg.setFill(CellBackgroundColours.visibility(weather.getVisibility()));
        this.item.getChildren().add(bg);

        DropShadow ds = new DropShadow();
        ds.setOffsetY(0.3f);
        ds.setSpread(0.8);
        ds.setRadius(4);
        ds.setColor(Color.color(0, 0, 0, 0.5));

        Text visibility = new Text(weather.getVisibility() + "");
        visibility.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 20));
        visibility.setFill(Paint.valueOf("ffffff"));
        visibility.setEffect(ds);
        double tWidth = visibility.getLayoutBounds().getWidth();
        double tHeight = visibility.getLayoutBounds().getHeight();
        visibility.setLayoutY(19 + (this.height - tHeight)/2);
        visibility.setLayoutX((this.width - tWidth)/2);
        this.item.getChildren().add(visibility);
    }

    @Override
    public Group getInstance() {
        return this.item;
    }

    @Override
    public RowItemType getType() {
        return RowItemType.VISIBILITY;
    }

    @Override
    public void onAltitudeChange(int newAltitude) {
        // Nothing required.
    }
}
