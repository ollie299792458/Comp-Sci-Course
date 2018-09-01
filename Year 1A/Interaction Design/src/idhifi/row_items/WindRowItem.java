package idhifi.row_items;

import idhifi.WeatherMetrics;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Implementation of AbstractRowItem for wind
 *
 * Created by oliver on 18/05/17.
 */
public class WindRowItem extends AbstractRowItem {

    private int width = 100;
    private int height = 100;
    private int iconWidth = 50;
    private Group item;

    public WindRowItem(int w, int alt) {
        this.width = w;
        this.height = w;
        this.currentAltitude = alt;
    }

    @Override
    public void updateContents(WeatherMetrics contents) {
        this.item = new Group();
        Rectangle bg = new Rectangle(0, 0, this.width, this.height);
        bg.setFill(CellBackgroundColours.windSpeed(contents.getWindSpeed()));
        this.item.getChildren().add(bg);

        Image ind = new Image("idhifi/assets/winddirectionicon.png");
        ImageView indImageView = new ImageView(ind);
        indImageView.setFitHeight(this.iconWidth);
        indImageView.setFitWidth(this.iconWidth);
        indImageView.setLayoutX((this.width - this.iconWidth)/2);
        indImageView.setLayoutY(10);
        indImageView.setRotate(contents.getWindBearing());
        this.item.getChildren().add(indImageView);


        DropShadow ds = new DropShadow();
        ds.setOffsetY(0.3f);
        ds.setSpread(0.8);
        ds.setRadius(4);
        ds.setColor(Color.color(0, 0, 0, 0.5));


        Text windSpeed = new Text(contents.getWindSpeed() + "mph");
        windSpeed.setFill(Paint.valueOf("ffffff"));
        windSpeed.setEffect(ds);
        double tWidth = windSpeed.getLayoutBounds().getWidth();
        double tHeight = windSpeed.getLayoutBounds().getHeight();
        windSpeed.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 12));
        windSpeed.setLayoutY(this.height - tHeight/2);
        windSpeed.setLayoutX((this.width - tWidth)/2);
        this.item.getChildren().add(windSpeed);

    }

    @Override
    public Group getInstance() {
        return this.item;
    }

    @Override
    public RowItemType getType() {
        return RowItemType.WIND;
    }

    @Override
    public void onAltitudeChange(int newAltitude) {
        // do nothing
    }

}
