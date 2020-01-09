package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditTreasurePopup extends EditPopup {

    private ComboBox<String> passageSectionsList;
    private int selectedSectionNumber;
    private Stage editWindow;
    private Controller myController;
    private int mySpaceType;
    private ComboBox<String> treasureOptions;

    /**Constructor for popup, initialize instance variables and
     * stage for window.
     * @param theController - controller object
     */
    public EditTreasurePopup(Controller theController) {
        treasureOptions = new ComboBox<>();
        editWindow = new Stage();
        editWindow.initModality(Modality.APPLICATION_MODAL);
        editWindow.setTitle("Edit Treasure");
        myController = theController;
        selectedSectionNumber = -1;
    }

    /**Create basic window for popup and set up window's nodes .
     * @param spaceType - space number for the space door is connected to
     */
    @Override
    public void createWindow(int spaceType) {
        //if spacetype = 0 set for chamber
        //if spacetype = 1 set for passage
        HBox layout = new HBox();
        mySpaceType = spaceType;

        layout.setStyle("-fx-padding: 10;"
                + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;"
                + "-fx-border-insets: 15;"
                + "-fx-border-radius: 5;"
                + "-fx-border-color: PALETURQUOISE;"
                + "-fx-background-color: LIGHTGOLDENRODYELLOW;");

        layout.setSpacing(10);

        //Add treasure to chamber button
        Button addTreausure = new Button("Add Treasure");
        addTreausure.setOnAction(e -> {
                addWindow();
        });

        //Add treasure to chamber button
        Button removeTreasure = new Button("Remove Treasure");
        removeTreasure.setOnAction(e -> {
                remWindow();
        });


        layout.getChildren().addAll(addTreausure, removeTreasure);
        layout.setAlignment(Pos.CENTER);

        Scene doorScene = new Scene(layout, 300, 150);
        editWindow.setScene(doorScene);

        editWindow.showAndWait();

    }

    /**
     * Create popup window to select and enter a roll.
     * for treasure to add in chamber
     */
    @Override
    public void addWindow() {
        Stage treasureWindow = new Stage();
        VBox layout = new VBox();

        layout.setStyle("-fx-padding: 10;"
                + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;"
                + "-fx-border-insets: 15;"
                + "-fx-border-radius: 5;"
                + "-fx-border-color: THISTLE;"
                + "-fx-background-color: TEAL;");

        treasureWindow.initModality(Modality.APPLICATION_MODAL);
        treasureWindow.setTitle("Add Treasure");

        Label prompt = new Label("Select the type of treasure to add from below:");
        prompt.setTextFill(Color.WHITE);

        Label response = new Label();
        response.setTextFill(Color.WHITE);
        response.setWrapText(true);

        //create combobox for treasure list
        treasureOptions = createOptionsList();

        Button submit = createAddSubmitButton(response);

        if (mySpaceType == 1) {
            //gen passage window
            passageSectionsList = fillPSectionsList();
            Label prompt2 = new Label("Select passage section to add treasure to:");
            prompt2.setTextFill(Color.WHITE);
            layout.getChildren().addAll(prompt, treasureOptions, prompt2, passageSectionsList, submit, response);
        } else {
            layout.getChildren().addAll(prompt, treasureOptions, submit, response);
        }

        //add nodes
        layout.setSpacing(10);

        //show window
        Scene scene = new Scene(layout, 380, 250);
        treasureWindow.setScene(scene);
        treasureWindow.showAndWait();
    }

    /**create drop down menu for treasures.
     * @return - drop down menu with options
     */
    @Override
    public ComboBox<String> createOptionsList() {
        ComboBox<String> treasures = new ComboBox<>();
        treasures.setPromptText("Treasure Options");
        treasures.getItems().addAll(
                "1,000 copper pieces",
                "1,000 silver pieces",
                "750 electrum pieces",
                "250 gold pieces",
                "100 platinum pieces",
                "1-4 gems",
                "1 piece jewelry"
        );

        return treasures;
    }

    /**create and set button action to submit an adding edit.
     * @param response - feedback for if successful or error
     * @return - button
     */
    @Override
    public Button createAddSubmitButton(Label response) {
        //action 0 = add
        //action 1 = remove
        Button submit = new Button("Confirm Edit");
        submit.setOnAction(e -> {
            int rollVal = parseOption();
            if (rollVal < 0) {
                response.setText("Please choose a treasure from the above menu");
            } else {
                if (mySpaceType == 0) {
                    //chamber add treasure
                    //rollResult = validateInput(enterRoll.getText(), 1, 100);
                    response.setText("Treasure added, select another and press submit to add another or close window to exit");
                    myController.addTreasure(rollVal, -1);

                } else {
                    if (selectedSectionNumber < 0) {
                        //eror check section
                        response.setText("ERROR: Please choose a passage section number");
                    } else {
                        myController.addTreasure(rollVal, selectedSectionNumber);
                        response.setText("Treasure added, select another and press submit to add another or close window to exit");
                    }
                }
            }
        });

        return submit;
    }


    /**Create submit button with action to call method to remove conent.
     * @param enterRoll - section to remove from
     * @param response - feedback for if successful
     * @return - submit button
     */
    @Override
    public Button createRemSubmitButton(TextField enterRoll, Label response) {
        Button submit = new Button("Confirm Edit");
        submit.setOnAction(e -> {
            if (mySpaceType == 0) {
                //chamber rem treasure
                int max = myController.getChamberTreasureSize();
                int min;
                if (myController.getChamberTreasureSize() == 0) {
                    max = -1;
                    min = -1;
                } else {
                    min = 1;
                }

                int result = validateInput(enterRoll.getText(), min, max);
                if (result > 0) {
                    response.setText("Treasure removed, enter another number and press submit to remove another or close window to exit");
                    myController.removeTreasure(result, -1);
                } else {
                    response.setText("ERROR: Please enter a valid treasure number for this chamber");

                }
            } else {
            if (selectedSectionNumber < 0) {
                //eror check section
                response.setText("ERROR: Please choose a passage section number");
            } else {
                myController.removeTreasure(0, selectedSectionNumber);
                response.setText("Treasure removed, select another section and press submit to remove another or close window to exit");
            }
        }
        });

        return submit;
    }

    /**
     * setup pop window to remove the content.
     */
    @Override
    public void remWindow() {
        Stage treasureWindow = new Stage();
        VBox layout = new VBox();

        layout.setStyle("-fx-padding: 10;"
                + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;"
                + "-fx-border-insets: 15;"
                + "-fx-border-radius: 5;"
                + "-fx-border-color: PALETURQUOISE;"
                + "-fx-background-color: PALEVIOLETRED;");

        treasureWindow.initModality(Modality.APPLICATION_MODAL);
        treasureWindow.setTitle("Remove Treasure");

        Label prompt = new Label("Enter Treasure Number to remove");
        prompt.setTextFill(Color.WHITE);

        Label response = new Label();
        response.setTextFill(Color.WHITE);
        response.setWrapText(true);

        TextField enterRoll = new TextField();
        enterRoll.setPromptText("Treasure Number");
        enterRoll.setMaxWidth(100);

        Button submit = createRemSubmitButton(enterRoll, response);

        if (mySpaceType == 1) {
            //gen passage window
            passageSectionsList = fillPSectionsList();
            Label prompt2 = new Label("Select passage section to remove treasure from:");
            prompt2.setTextFill(Color.WHITE);
            layout.getChildren().addAll(prompt2, passageSectionsList, submit, response);
        } else {
            layout.getChildren().addAll(prompt, enterRoll, submit, response);
        }

        //add nodes
        layout.setSpacing(10);

        //show window
        Scene scene = new Scene(layout, 380, 250);
        treasureWindow.setScene(scene);
        treasureWindow.showAndWait();

    }

    /**from treasure drop down menu assign a rule base on selection.
     * @return - roll value
     */
    @Override
    public int parseOption() {
        int selectedIndex = treasureOptions.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            return -1;
        }

        if (selectedIndex == 0) {
            return 1;
        } else if (selectedIndex == 1) {
            return 26;
        } else if (selectedIndex == 2) {
            return 51;
        } else if (selectedIndex == 3) {
            return 66;
        } else if (selectedIndex == 4) {
            return 81;
        } else if (selectedIndex == 5) {
            return 91;
        } else {
            return 95;
        }
    }

    /**create menu with all the passage section options.
     * @return - the menu
     */
    @Override
    public ComboBox<String> fillPSectionsList() {
        ComboBox<String> psList = new ComboBox<>();
        psList.setPromptText("Sections List");
        String indexDescription = "P Section ";
        int numSections = myController.getNumSectionsInPassage();
        for (int i = 1; i < numSections + 1; i++) {
            psList.getItems().add(indexDescription + i);
        }

        psList.setOnAction(e -> selectedSectionNumber = psList.getSelectionModel().getSelectedIndex());

        return psList;
    }

}
