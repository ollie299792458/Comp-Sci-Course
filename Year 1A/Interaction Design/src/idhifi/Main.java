package idhifi;

import com.jfoenix.animation.JFXAnimationManager;
import com.jfoenix.controls.JFXTextField;
import idhifi.row_items.AbstractRowItem;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    private int scaleFactor = 1;
    private int height = 600;
    private int width = 350;

    private int topBarHeight = 40;
    private int bottomBarHeight = 50;
    private int altitudeLabelWidth = 60;
    private int iconWidth = 22;
    private int iconHorizontalPadding = 10;

    private int maxAltitude = 6000;
    private int currentAltitude;

    private boolean drawerOpen = false;

    private Color topBarColour = Color.valueOf("13383e");
    private Color hazeColour = Color.valueOf("135550");
    private Color altLabelBackgroundColour = Color.valueOf("121f2d");
    private Color altLabelTextColour = Color.valueOf("ffffff");

    private Label altLabel;
    private JFXTextField field;
    private Label sunriseLabel, sunsetLabel, nearestStorm, nearestRain, warning;
    private ImageView drawerArrow;

    private Address currentAddress;
    private Address WGBAddress = new Address("William Gates Building, 15 JJ Thomson Ave, Cambridge CB3 0FD, UK", "CB3 0FD", 52.210878, 0.091668,19);
    private WeatherMetricsStatic staticWeatherData;

    private CentralContainer centralContainer;
    private ScrollPane scrollPane;
    private ScrollBar scroller;

    private ArrayList<AbstractRowItem> subscribers = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {


        // Set mock current location
        this.currentAddress = WGBAddress;

        primaryStage.setTitle("Interaction Design");

        Group mainGroup = new Group();

        /* */
        this.scrollPane = new ScrollPane();
        this.scrollPane.setLayoutX(18);
        this.scrollPane.setLayoutY(this.topBarHeight);
        this.scrollPane.setStyle("-fx-background-color:transparent;");
        this.scrollPane.setPrefSize(this.width - 18, this.height - this.topBarHeight - this.bottomBarHeight);
        this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.scrollPane.setFitToHeight(true);
        this.centralContainer = new CentralContainer();
        this.centralContainer.setParent(this);
        this.updateCentralContainer(this.WGBAddress);
        /* */

        mainGroup.getChildren().addAll(this.topBar(), this.sideScroller(), this.scrollPane, this.bottomBar());

        this.setCurrentLocation();
        this.staticWeatherData = DarkSkyAPIParsing.getWeatherStatic(this.currentAddress);
        this.updateStaticWeatherLabels();

        Scene mScene = new Scene(mainGroup, width, height);
        mScene.getStylesheets().add("idhifi/App.css");
        primaryStage.setScene(mScene);
        primaryStage.setResizable(false);
        primaryStage.show();

        JFXAnimationManager jfxAniMng = new JFXAnimationManager();
    }

    private Group topBar() {

        Group topBar = new Group();
        topBar.setLayoutX(0);
        topBar.setLayoutY(0);

        // background bar
        Rectangle bg = new Rectangle(0, 0, this.width, this.topBarHeight);
        bg.setFill(this.topBarColour);
        topBar.getChildren().add(bg);

        //left altitude label
        Rectangle alt = new Rectangle(0, 0, this.altitudeLabelWidth, this.topBarHeight);
        alt.setFill(this.altLabelBackgroundColour);
        topBar.getChildren().add(alt);

        this.altLabel = new Label();
        this.altLabel.setTextFill(this.altLabelTextColour);
        topBar.getChildren().add(this.altLabel);
        this.currentAltitude = 0;
        this.setAltitudeLabel();

        Image myLocation = new Image("idhifi/assets/mylocation.png");
        ImageView myLocationImageView = new ImageView(myLocation);
        myLocationImageView.setFitHeight(this.iconWidth);
        myLocationImageView.setFitWidth(this.iconWidth);
        myLocationImageView.setLayoutX(this.altitudeLabelWidth + this.iconHorizontalPadding);
        myLocationImageView.setLayoutY((this.topBarHeight - this.iconWidth)/2);
        topBar.getChildren().add(myLocationImageView);
        myLocationImageView.setOnMouseClicked(e -> this.setCurrentLocation());

        Image searchIcon = new Image("idhifi/assets/search.png");
        ImageView searchIconImageView = new ImageView(searchIcon);
        searchIconImageView.setFitHeight(this.iconWidth);
        searchIconImageView.setFitWidth(this.iconWidth);
        searchIconImageView.setLayoutX(this.altitudeLabelWidth + 2*this.iconHorizontalPadding + this.iconWidth);
        searchIconImageView.setLayoutY((this.topBarHeight - this.iconWidth)/2);
        topBar.getChildren().add(searchIconImageView);
        searchIconImageView.setOnMouseClicked(e -> this.searchLocation());

        int xInputStart = this.altitudeLabelWidth + 3*this.iconHorizontalPadding + 2*this.iconWidth;
        Group searchDropdown = new Group();
        this.field = new JFXTextField();
        this.field.setLabelFloat(false);
        this.field.setStyle("-fx-text-fill: white;");
        this.field.setPromptText("Location");
        searchDropdown.getChildren().add(this.field);
        searchDropdown.setLayoutX(xInputStart);
        searchDropdown.setLayoutY(6);
        this.field.setMinWidth(this.width - xInputStart - this.iconHorizontalPadding);
        topBar.getChildren().add(searchDropdown);

        return topBar;
    }

    private Group sideScroller() {
        Group sideScroller = new Group();
        sideScroller.getStyleClass().add("scrollview");
        sideScroller.setLayoutX(-1);
        sideScroller.setLayoutY(this.topBarHeight);

        this.scroller = new ScrollBar();
        this.scroller.setMax(this.maxAltitude);
        this.scroller.setMin(0);
        this.scroller.setValue(0);
        this.scroller.setUnitIncrement(50);
        this.scroller.setBlockIncrement(50);
        this.scroller.setMaxHeight(this.height - this.topBarHeight - this.bottomBarHeight);
        this.scroller.setMinHeight(this.height - this.topBarHeight - this.bottomBarHeight);
        this.scroller.setOrientation(Orientation.VERTICAL);
        this.scroller.valueProperty().addListener((ov, old_val, new_val) -> {
            this.currentAltitude = new_val.intValue();
            this.setAltitudeLabel();
        });
        this.scroller.setOnMouseReleased(e -> this.scrollerDragDone());
        this.scroller.setRotate(180);

        sideScroller.getChildren().add( this.scroller);
        return sideScroller;
    }

    private Group bottomBar() {
        Group bottomBar = new Group();
        bottomBar.setLayoutX(0);
        bottomBar.setLayoutY(0);
        bottomBar.maxHeight(this.height);
        bottomBar.minHeight(this.height);

        Group innerRectangeGroup = new Group();

        Rectangle bg = new Rectangle(0, this.height - this.bottomBarHeight, this.width, this.height);
        bg.setFill(this.topBarColour);

        Rectangle haze = new Rectangle(0, this.height, this.width, this.height);
        haze.setFill(this.hazeColour);
        haze.setOpacity(0);

        innerRectangeGroup.getChildren().add(haze);
        innerRectangeGroup.getChildren().add(bg);
        bottomBar.getChildren().add(innerRectangeGroup);

        innerRectangeGroup.setOnMouseClicked(e -> this.toggleDrawer(e));

        // Labels
        Group drawerLabels = new Group();


        // Top Row
        Image sunset = new Image("idhifi/assets/sunset.png");
        ImageView sunsetImageView = new ImageView(sunset);
        sunsetImageView.setFitHeight(20);
        sunsetImageView.setFitWidth(48);
        sunsetImageView.setLayoutY(this.height - this.bottomBarHeight + 15);
        sunsetImageView.setLayoutX(15);
        drawerLabels.getChildren().add(sunsetImageView);

        this.sunsetLabel = new Label();
        this.sunsetLabel.setTextFill(Paint.valueOf("ffffff"));
        this.sunsetLabel.setLayoutY(this.height - this.bottomBarHeight + 17);
        this.sunsetLabel.setLayoutX(15 + 10 + 48);
        drawerLabels.getChildren().add(this.sunsetLabel);

        Image nearestStormImg = new Image("idhifi/assets/neareststorm.png");
        ImageView nearestStormImageView = new ImageView(nearestStormImg);
        nearestStormImageView.setFitHeight(20);
        nearestStormImageView.setFitWidth(41);
        nearestStormImageView.setLayoutY(this.height - this.bottomBarHeight + 15);
        nearestStormImageView.setLayoutX(15 + 48 + 15 + 60);
        drawerLabels.getChildren().add(nearestStormImageView);

        this.nearestStorm = new Label();
        this.nearestStorm.setTextFill(Paint.valueOf("ffffff"));
        this.nearestStorm.setLayoutY(this.height - this.bottomBarHeight + 17);
        this.nearestStorm.setLayoutX(15 + 48 + 10 + 48 + 60 + 15);
        drawerLabels.getChildren().add(this.nearestStorm);

        Image drawerArrow = new Image("idhifi/assets/uparrow.png");
        ImageView drawerArrowImageView = new ImageView(drawerArrow);
        drawerArrowImageView.setFitHeight(10);
        drawerArrowImageView.setFitWidth(16);
        drawerArrowImageView.setLayoutY(this.height - this.bottomBarHeight + 20);
        drawerArrowImageView.setLayoutX(this.width - 15 - 16);
        this.drawerArrow = drawerArrowImageView;
        drawerLabels.getChildren().add(drawerArrowImageView);


        //Bottom Row
        Image sunrise = new Image("idhifi/assets/sunrise.png");
        ImageView sunriseImageView = new ImageView(sunrise);
        sunriseImageView.setFitHeight(20);
        sunriseImageView.setFitWidth(48);
        sunriseImageView.setLayoutY(this.height + 15);
        sunriseImageView.setLayoutX(15);
        drawerLabels.getChildren().add(sunriseImageView);


        this.sunriseLabel = new Label();
        this.sunriseLabel.setTextFill(Paint.valueOf("ffffff"));
        this.sunriseLabel.setLayoutY(this.height + 17);
        this.sunriseLabel.setLayoutX(15 + 10 + 48);
        drawerLabels.getChildren().add(this.sunriseLabel);

        Image nearestRain = new Image("idhifi/assets/raindrops.png");
        ImageView nearestRainImageView = new ImageView(nearestRain);
        nearestRainImageView.setFitHeight(20);
        nearestRainImageView.setFitWidth(16);
        nearestRainImageView.setLayoutY(this.height + 15);
        nearestRainImageView.setLayoutX(15 + 48 + 10 + 60 + 25);
        drawerLabels.getChildren().add(nearestRainImageView);

        this.nearestRain = new Label();
        this.nearestRain.setTextFill(Paint.valueOf("ffffff"));
        this.nearestRain.setLayoutY(this.height + 17);
        this.nearestRain.setLayoutX(15 + 48 + 10 + 48 + 60 + 15);
        drawerLabels.getChildren().add(this.nearestRain);


        this.warning = new Label("There aren't currently any warnings.");
        this.warning.setTextFill(Paint.valueOf("ffe700"));
        this.warning.setLayoutY(this.height + 17 + 60);
        this.warning.setLayoutX(15);
        drawerLabels.getChildren().add(this.warning);



        innerRectangeGroup.getChildren().add(drawerLabels);

        return bottomBar;
    }

    private void toggleDrawer(MouseEvent e) {

        Group g = (Group)e.getSource();
        double labelsDisplacement = 150;

        // Animate main drawer
        Rectangle drawer = (Rectangle)g.getChildren().get(1);
        ScaleTransition st = new ScaleTransition(Duration.millis(200), drawer);
        if(!this.drawerOpen) st.setByY(0.5f);
        else st.setByY(-0.5f);
        st.play();

        Group labels = (Group)g.getChildren().get(2);
        TranslateTransition lst = new TranslateTransition(Duration.millis(200), labels);
        if(!this.drawerOpen) lst.setByY(-labelsDisplacement);
        else lst.setByY(labelsDisplacement);
        lst.play();

        if(!this.drawerOpen) this.drawerArrow.setRotate(180);
        else this.drawerArrow.setRotate(0);

        Rectangle haze = (Rectangle)g.getChildren().get(0);
        FadeTransition ft = new FadeTransition(Duration.millis(200), haze);
        if(!this.drawerOpen) {
            haze.setY(0);
            haze.setHeight(this.height);
            ft.setToValue(0.8);
        }
        else {

            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            Runnable task = () -> { haze.setY(this.height); haze.setHeight(this.height); };
            executor.schedule(task, 200, TimeUnit.MILLISECONDS);
            ft.setToValue(0.0);
        }

        ft.play();

        this.drawerOpen = !this.drawerOpen;
    }

    private void setAltitudeLabel()  {
        Text text = new Text(this.currentAltitude + "m");
        double width = text.getLayoutBounds().getWidth();
        double height = text.getLayoutBounds().getHeight();

        this.altLabel.setText(text.getText());
        this.altLabel.setLayoutY((this.topBarHeight - height)/2);
        this.altLabel.setLayoutX((this.altitudeLabelWidth - width)/2);
    }

    private void setCurrentLocation() {
        try {
            this.currentAddress = this.WGBAddress;
            this.currentAltitude = (new Double(this.currentAddress.getAltitude())).intValue();
            this.scroller.setValue(this.currentAltitude);
            this.field.setText(this.currentAddress.getmName());
            this.staticWeatherData = DarkSkyAPIParsing.getWeatherStatic(this.currentAddress);
            this.subscribers = new ArrayList<>();
            this.updateCentralContainer(this.currentAddress);
            this.updateStaticWeatherLabels();
            this.setAltitudeLabel();
        }
        catch(WeatherDataNotFound e) {

        }
    }

    private void searchLocation() {
        try {
            List<Address> lstAddr = GeocodeParsing.getAddress(this.field.getText());
            this.currentAddress = lstAddr.get(0);
            this.currentAltitude = (new Double(this.currentAddress.getAltitude())).intValue();
            this.scroller.setValue(this.currentAltitude);
            this.field.setText(this.currentAddress.getmName());
            this.staticWeatherData = DarkSkyAPIParsing.getWeatherStatic(this.currentAddress);
            this.subscribers = new ArrayList<>();
            this.updateCentralContainer(this.currentAddress);
            this.updateStaticWeatherLabels();
            this.setAltitudeLabel();
        }
        catch (AddressNotFound e){
            System.out.println("Error: Address not found.");
            this.setCurrentLocation();
        }
        catch (WeatherDataNotFound e2) {
            System.out.println("Error: Weather Data Not Found.");
        }
    }

    private void scrollerDragDone() {
        for(AbstractRowItem r : this.subscribers) r.onAltitudeChange(this.currentAltitude);
    }

    private void updateCentralContainer(Address addr) {
        this.scrollPane.setContent(this.centralContainer.getInstance(addr, this.currentAltitude, (new Double(this.currentAddress.getAltitude())).intValue()));
    }

    private void updateStaticWeatherLabels() {
        DateFormat dft = new SimpleDateFormat("HH:mm");
        Date sunrise = new Date(this.staticWeatherData.getSunriseTime() * 1000);
        Date sunset = new Date(this.staticWeatherData.getSunsetTime() * 1000);

        if(sunrise != null) this.sunriseLabel.setText(dft.format(sunrise));
        else this.sunriseLabel.setText("N/A");

        if(sunset != null) this.sunsetLabel.setText(dft.format(sunset));
        else this.sunsetLabel.setText("N/A");


        double nearestStormVal = this.staticWeatherData.getNearestStormDistance();
        if(nearestStormVal <= 0.0) this.nearestStorm.setText("N/A");
        this.nearestStorm.setText(String.valueOf(Math.round(nearestStormVal * 100)/100) + " miles");

        double nearestRainVal = this.staticWeatherData.getNearestStormDistance();
        if(nearestRainVal <= 0.0) this.nearestRain.setText("N/A");
        this.nearestRain.setText(String.valueOf(Math.round(nearestRainVal * 100)/100) + " miles");

        if(this.staticWeatherData.getAlert() != null) this.warning.setText(this.staticWeatherData.getAlert().toString());
        else this.warning.setText("There aren't currently any warnings.");
    }

    public void subscribeToAltitudeChange(AbstractRowItem r) {
        this.subscribers.add(r);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
