package gui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;



public class GUI extends Application {

    private Controller theController;
    private BorderPane root;  //the root element of this GUI
    private Popup descriptionPane;
    private Stage primaryStage;  //The stage that is passed in on initialization
    private Label spaceDescription;
    private ComboBox<String> doorsMenu;
    private ComboBox<String> chamberMenu;
    private ComboBox<String> passageMenu;
    private boolean selectingChamber;
    private StackPane theDisplay;


    /**Start gui with this.
     * @param stage - main stage
     */
    @Override
    public void start(Stage stage) {
        theController = new Controller(this);
        chamberMenu = new ComboBox<>();
        passageMenu = new ComboBox<>();
        theDisplay = new StackPane();

        primaryStage = stage;
        /*Border Panes have  top, left, right, center and bottom sections */
        root = setUpRoot();
        Scene scene = new Scene(root, 1090, 600);
        primaryStage.setTitle("Dungeons & Dragons Level Generator");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private BorderPane setUpRoot() {
        BorderPane main = new BorderPane();

        main.setBackground(new Background(new BackgroundFill(Color.POWDERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        //create a menu bar and add file menu to save/load file
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(createFileMenu());
        //in boarderpane set menu bar as the top
        main.setTop(menuBar);
        main.setLeft(createChamberMenu());
        main.setRight(createDoorsMenu());
        main.setCenter(createCenter());


        /*create combobox for doors list and chambers stuff*/

        return main;
    }

    private VBox createDoorsMenu() {
        VBox right = new VBox();
        right.setSpacing(220);
        right.setMaxWidth(180);
        right.setStyle("-fx-padding: 10;"
                + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;"
                + "-fx-border-insets: 20;"
                + "-fx-border-radius: 5;"
                + "-fx-border-color: blue;");

        Label legend = new Label();
        legend.setWrapText(true);
        legend.setText("\n\n\n\nGraphic Display Note:\n A treasure chest picture, or monster picture"
                        + " represents there is a is a monster or treasure in the passage or chamber not how many"
                        + " or in which section. Refer to description on left for that :)");
        legend.setTextFill(Color.LIGHTSLATEGRAY);

        doorsMenu = new ComboBox<>();
        doorsMenu.setPromptText("Doors List");
        doorsMenu.setOnAction(e -> {
            if (selectingChamber) {
                theController.genDoorPopup(selectedDoorIndex(), selectedChamberIndex(), 0);
            } else {
                theController.genDoorPopup(selectedDoorIndex(), selectedPassageIndex(), 1);
            }
        });
        right.getChildren().addAll(doorsMenu, legend);

        return right;
    }

    /**get the doordropdwon menu to parse.
     * @return - door menu
     */
    public ComboBox<String> getDoorsDropMenu() {
        return doorsMenu;
    }

    private VBox createChamberMenu() {
        VBox left = new VBox();

        left.setStyle("-fx-padding: 10;"
                + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;"
                + "-fx-border-insets: 20;"
                + "-fx-border-radius: 5;"
                + "-fx-border-color: blue;");

        left.setSpacing(35);


        //fill dropdown menu with list of chambers
        setChamberMenu();
        //change center description and  doors list
        chamberMenu.setOnAction(e -> {
            selectingChamber = true;
            theController.chamberDrawing(selectedChamberIndex());
            theController.printDescription(0);
            theController.adjustDoorsList(chamberMenu, 0);
        });

        //fill dropdown menu with list of chambers
        setPassageMenu();
        //change center description and  doors list
        passageMenu.setOnAction(e -> {
            selectingChamber = false;
            theController.passageDrawing(selectedPassageIndex());
            theController.printDescription(1);
            theController.adjustDoorsList(passageMenu, 1);
        });

        Button editTreasure = new Button("Edit Treasure");
        Button editMonster = new Button("Edit Monster");

        setEditButtonActions(editMonster, editTreasure);

        left.getChildren().addAll(chamberMenu, passageMenu, editTreasure, editMonster);

        return left;
    }

    /**Alter description in center text description box.
     * @param theDescription - string of space description
     */
    public void setCenterText(String theDescription) {
        spaceDescription.setTextFill(Color.PALEVIOLETRED);
        spaceDescription.setText(theDescription);
    }

    private HBox createCenter() {
        HBox center = new HBox();
        theDisplay.setMinWidth(450);
        theDisplay.setMaxWidth(350);

        theDisplay.setStyle("-fx-padding: 10;"
                + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;"
                + "-fx-border-insets: 20;"
                + "-fx-border-radius: 5;"
                + "-fx-border-color: lightgoldenrodyellow;");

        center.getChildren().addAll(createSpaceDescriptionBox(), theDisplay);

        return center;
    }

    /**
     * Create the main graphic panel by drawing contents, doors and floor plan.
     * @param length - length of space
     * @param width - width of space
     * @param numDoors - doors in the space
     * @param treasure - if there is a treasure
     * @param monster -if there is a monster
     */
    public void createDrawingBoard(int length, int width, int numDoors, int treasure, int monster) {
        theDisplay.getChildren().clear();
        ImageView imageFloor;
        Image image;

        image = new Image(getClass().getResourceAsStream("/res/floors.png"));
        imageFloor = new ImageView(image);

        //scale floor image based on size
        imageFloor.setFitHeight(length);
        imageFloor.setFitWidth(width);

        ObservableList list = theDisplay.getChildren();
        list.add(imageFloor);

        //draw the doors
        for (int i = 0; i < numDoors; i++) {
            drawTheDoors(list, i, width, length);
        }

        list.add(drawContents(width, length, treasure, monster));

    }

    private void drawTheDoors(ObservableList list, int i, int width, int length) {
        if (i == 0) {
            GridPane temp = setUpGridToDraw(width, length, true);
            temp.setAlignment(Pos.TOP_CENTER);
            list.add(temp);
        }
        if (i == 1) {
            GridPane temp = setUpGridToDraw(width, length, true);
            temp.add(addDoorToDisplay(), 0, 0);
            temp.setAlignment(Pos.BOTTOM_CENTER);
            list.add(temp);
        }
        if (i == 2) {
            GridPane temp = setUpGridToDraw(width, length, true);
            temp.setAlignment(Pos.CENTER_RIGHT);
            list.add(temp);
        }
        if (i == 3) {
            GridPane temp = setUpGridToDraw(width, length, true);
            temp.setAlignment(Pos.CENTER_LEFT);
            list.add(temp);
        }
    }

    private GridPane setUpGridToDraw(int width, int length, boolean forDoor) {
        GridPane temp = new GridPane();
        temp.setMinSize(width + 40, length + 40);
        temp.setMaxSize(width + 40, length + 40);
        if (forDoor) {
            temp.add(addDoorToDisplay(), 0, 0);
        }
        return temp;
    }

    private GridPane drawContents(int width, int length, int treasure, int monster) {
        GridPane temp = setUpGridToDraw(width, length, false);
        if (treasure > 0) {
            ImageView chestPic = getTreasurePic();
            temp.add(chestPic, 0, 0);
        }
        if (monster > 0) {
            ImageView monsterPic = getMonsterPic();
            temp.add(monsterPic, 0, 1);
        }
        temp.setAlignment(Pos.CENTER);

        return temp;
    }

    private ImageView getMonsterPic() {
        ImageView imageMonster;
        Image image;

        image = new Image(getClass().getResourceAsStream("/res/monster.png"));
        imageMonster = new ImageView(image);
        imageMonster.setFitHeight(30);
        imageMonster.setFitWidth(30);
        return imageMonster;
    }

    private ImageView getTreasurePic() {
        ImageView imageTreasure;
        Image image;

        image = new Image(getClass().getResourceAsStream("/res/treasure.png"));
        imageTreasure = new ImageView(image);
        imageTreasure.setFitHeight(30);
        imageTreasure.setFitWidth(30);

        return imageTreasure;
    }

    private ImageView addDoorToDisplay() {
        ImageView imageDoor;
        Image image;

        image = new Image(getClass().getResourceAsStream("/res/door.png"));
        imageDoor = new ImageView(image);
        imageDoor.setFitHeight(40);
        imageDoor.setFitWidth(40);

        return imageDoor;
    }

    private VBox createSpaceDescriptionBox() {
        VBox temp = new VBox();
        temp.setMinWidth(250);
        temp.setMaxWidth(250);

        spaceDescription = new Label();
        spaceDescription.setWrapText(true);
        temp.setStyle("-fx-padding: 10;"
                + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;"
                + "-fx-border-insets: 20;"
                + "-fx-border-radius: 5;"
                + "-fx-border-color: lightgoldenrodyellow;");

        temp.getChildren().add(spaceDescription);
        return temp;
    }

    private Menu createFileMenu() {
        Menu fileMenu = new Menu("File");
        MenuItem saveFile = new MenuItem("Save File");
        fileMenu.getItems().add(saveFile);
        saveFile.setOnAction(e -> theController.saveGame());

        MenuItem loadFile = new MenuItem("Load File");
        fileMenu.getItems().add(loadFile);
        loadFile.setOnAction(e -> theController.loadGame());


        return fileMenu;
    }

    private void setEditButtonActions(Button editMonster, Button editTreasure) {
        editTreasure.setOnAction(e -> {
            EditTreasurePopup editTreasurePopup = new EditTreasurePopup(theController);
            //if a chamber or passage are selected then allow edit buttons to work
            if (selectedPassageIndex() > -1 || selectedChamberIndex() > -1) {
                if (selectingChamber) {
                    editTreasurePopup.createWindow(0);
                } else {
                    editTreasurePopup.createWindow(1);
                }
            }
        });
        //monster edit button actions
        editMonster.setOnAction(e -> {
            EditMonsterPopup editMonsterPopup = new EditMonsterPopup(theController);
            //if a chamber or passage are selected then allow edit buttons to work
            if (selectedPassageIndex() > -1 || selectedChamberIndex() > -1) {
                if (selectingChamber) {
                    editMonsterPopup.createWindow(0);
                } else {
                    editMonsterPopup.createWindow(1);
                }
            }
        });
    }


    private void setChamberMenu() {
        chamberMenu.setPromptText("Chambers List");
        String indexDescription = "Chamber ";
        for (int i = 1; i < 6; i++) {
            chamberMenu.getItems().add(indexDescription + i);
        }
    }

    private void setPassageMenu() {
        passageMenu.setPromptText("Passages List");
        String indexDescription = "Passage ";
        int size = theController.getNumPassages();
        for (int i = 1; i < size + 1; i++) {
            passageMenu.getItems().add(indexDescription + i);
        }
    }

    /**Get the selected chamber index from the drop down menu.
     * @return - int value of chamber
     */
    public int selectedChamberIndex() {
        return chamberMenu.getSelectionModel().getSelectedIndex();
    }

    /**Get the selected passage index from the drop down menu.
     * @return - int value of passage
     */
    public int selectedPassageIndex() {
        return passageMenu.getSelectionModel().getSelectedIndex();
    }

    /**Get the selected door index from the drop down menu.
     * @return - int value of door
     */
    public int selectedDoorIndex() {
        return doorsMenu.getSelectionModel().getSelectedIndex();
    }

    /**select a chamber index in the drop down menu.
     * @param index  - index of chamber
     */
    public void selectChamberIndex(int index) {
        clearGraphicalDisplay();
        doorsMenu.getSelectionModel().clearSelection();
        passageMenu.getSelectionModel().clearSelection();
        chamberMenu.getSelectionModel().clearAndSelect(index);
    }

    /**select a passage index in the drop down menu.
     * @param index  - index of passage
     */
    public void selectPassageIndex(int index) {
        clearGraphicalDisplay();
        doorsMenu.getSelectionModel().clearSelection();
        chamberMenu.getSelectionModel().clearSelection();
        passageMenu.getSelectionModel().clearAndSelect(index);
    }

    /**remove previous space's drawing.
     */
    public void clearGraphicalDisplay() {
        theDisplay.getChildren().clear();
    }

    /** the main.
     * @param args - args for main
     */
    public static void main(String[] args) {
        launch(args);
    }
}
