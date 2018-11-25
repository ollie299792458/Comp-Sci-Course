package idhifi.row_items;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import idhifi.WeatherMetrics;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.w3c.dom.css.Rect;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of AbstractRowItem for time (top row of the interface)
 *
 * Created by oliver on 18/05/17.
 */
public class TimeRowItem extends AbstractRowItem {
    private Group item;
    private int width = 80;
    private int height = 40;

    public TimeRowItem(int w) {
        this.width = w;
        this.height = 40;
    }

    @Override
    public void updateContents(WeatherMetrics data) {

        Date now = new Date();
        Date date = new Date(data.getTime() * 1000 - 3600000);
        DateFormat dft = new SimpleDateFormat("HH:mm");
        DateFormat dfd = new SimpleDateFormat("dd/MM");
        String contents = dft.format(date);
        String dateString = dfd.format(date);

        this.item = new Group();
        this.item.prefHeight(this.height);
        this.item.prefWidth(this.width);

        Rectangle bg = new Rectangle(0, 0, this.width, this.height);
        bg.setFill(Color.valueOf("f6fafb"));
        this.item.getChildren().add(bg);

        Rectangle bottomBorder = new Rectangle(0, this.height - 1, this.width, 1);
        bottomBorder.setFill(Paint.valueOf("093c3d"));
        this.item.getChildren().add(bottomBorder);


        if(Math.abs(getDateDiff(date, now, TimeUnit.SECONDS)) < 10) {
            // Is 'Now'
            Text l = new Text("Now");
            l.setFont(Font.font("Trebuchet MS", FontWeight.BLACK, 15));
            double textWidth = l.getLayoutBounds().getWidth();
            double textHeight = l.getLayoutBounds().getHeight();
            l.setFill(Paint.valueOf("093c3d"));
            l.setLayoutY(14 + (this.height - textHeight) / 2);
            l.setLayoutX((this.width - textWidth) / 2);

            this.item.getChildren().add(l);
        }

        else {


            Text l = new Text(contents);
            l.setFont(Font.font("Trebuchet MS", FontWeight.BLACK, 15));
            double textWidth = l.getLayoutBounds().getWidth();
            double textHeight = l.getLayoutBounds().getHeight();
            l.setFill(Paint.valueOf("093c3d"));
            l.setLayoutY(19 + (this.height - textHeight) / 2);
            l.setLayoutX((this.width - textWidth) / 2);

            Text l_d = new Text(dateString);
            l_d.setFont(Font.font("Trebuchet MS", FontWeight.LIGHT, 10));
            double textWidth_d = l_d.getLayoutBounds().getWidth();
            double textHeight_d = l_d.getLayoutBounds().getHeight();
            l_d.setFill(Paint.valueOf("2f737e"));
            l_d.setLayoutY(2 + (this.height - textHeight_d) / 2);
            l_d.setLayoutX((this.width - textWidth_d) / 2);


            this.item.getChildren().add(l);
            this.item.getChildren().add(l_d);
        }

    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    @Override
    public Group getInstance() {
        return item;
    }

    @Override
    public RowItemType getType() {
        return RowItemType.TIME;
    }

    @Override
    public void onAltitudeChange(int newAltitude) {
        // Nothing required.
    }
}
