package idhifi.row_items;

import idhifi.WeatherMetrics;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Implementation of AbstractRowItem for title (the first cell of the 'gap' rows)
 *
 * Created by oliver on 18/05/17.
 */
public class TitleRowItem extends AbstractRowItem {
    private Text title = new Text();
    private Group item = new Group();
    private int height = 30;
    private int width = 80;

    public TitleRowItem(int w) {
        this.width = w;
    }

    @Override
    public void updateContents(WeatherMetrics contents) {}

    public void updateTitle(String t) {

        if(t == null) t = "";

        Rectangle bg = new Rectangle(0, 0, this.width, this.height);
        bg.setFill(Paint.valueOf("ffffff"));

        this.title.setText(t.toUpperCase());
        this.title.setFont(Font.font("Trebuchet MS", FontWeight.BLACK, 12));
        this.title.setLayoutX(5);
        this.title.setFill(Paint.valueOf("13383e"));
        double textHeight = this.title.getLayoutBounds().getHeight();
        this.title.setLayoutY((this.height - textHeight/2 - 3));
        this.item.getChildren().add(bg);
        this.item.getChildren().add(this.title);
    }

    @Override
    public Group getInstance() {
        return this.item;
    }

    @Override
    public RowItemType getType() {
        return RowItemType.TITLE;
    }

    @Override
    public void onAltitudeChange(int newAltitude) {
        // Nothing required
    }
}
