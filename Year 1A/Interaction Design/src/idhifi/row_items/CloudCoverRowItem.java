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
 * Implementation of AbstractRowItem for cloud cover
 *
 * Created by oliver on 18/05/17.
 */
public class CloudCoverRowItem extends AbstractRowItem {

    private int width, height;
    private Group item;
    private Rectangle bg;
    private Text cloudCover;

    public CloudCoverRowItem(int w) {
        this.width = w;
        this.height = w;
    }

    @Override
    public void updateContents(WeatherMetrics contents) {
        this.item = new Group();
        this.bg = new Rectangle(0, 0, this.width, this.height);
        this.bg.setFill(CellBackgroundColours.cloudCover(100*contents.getCloudCover()));
        this.item.getChildren().add(this.bg);

        DropShadow ds = new DropShadow();
        ds.setOffsetY(0.3f);
        ds.setSpread(0.8);
        ds.setRadius(4);
        ds.setColor(Color.color(0, 0, 0, 0.5));

        this.cloudCover = new Text((new Double(100*contents.getCloudCover()).intValue()) + "%");
        this.cloudCover.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 22));
        this.cloudCover.setFill(Paint.valueOf("ffffff"));
        this.cloudCover.setEffect(ds);
        double tWidth = this.cloudCover.getLayoutBounds().getWidth();
        double tHeight = this.cloudCover.getLayoutBounds().getHeight();
        this.cloudCover.setLayoutY(19 + (this.height - tHeight)/2);
        this.cloudCover.setLayoutX((this.width - tWidth)/2);
        this.item.getChildren().add(this.cloudCover);
    }

    @Override
    public Group getInstance() {
        return this.item;
    }

    @Override
    public RowItemType getType() {
        return RowItemType.CLOUDCOVER;
    }

    @Override
    public void onAltitudeChange(int newAltitude) {
        // Nothing to do.
    }
}
