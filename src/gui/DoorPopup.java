package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class DoorPopup {

    private String spaceString;
    private GUI myGui;
    private Stage doorWindow;

    /**door popup contructor, initalize stage and load instance variables.
     * @param theGui - main gui object
     */
    public DoorPopup(GUI theGui) {
        myGui = theGui;
        doorWindow = new Stage();

    }

    /**Create basic window for popup.
     * @param doorNum - door index pop is for
     * @param doorDetails - door description
     * @param otherRoomDescription - description of space it is connected to
     * @param spaceNum - space number for the space door is connected to
     */
    public void createWindow(int doorNum, String doorDetails, String otherRoomDescription, int spaceNum) {
        VBox layout =  new VBox();
        this.spaceString = otherRoomDescription;

        layout.setStyle("-fx-padding: 10;"
                + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;"
                + "-fx-border-insets: 15;"
                + "-fx-border-radius: 5;"
                + "-fx-border-color: PINK;"
                + "-fx-background-color: DARKTURQUOISE;");

        layout.setSpacing(10);

        doorWindow.initModality(Modality.APPLICATION_MODAL);
        doorWindow.setTitle("Door Information");

        Text doorNumDesc = new Text("Door # " + (doorNum + 1));
        Text doorDescription = new Text("Door Description: \n"
                                            + doorDetails
                                            + "\n\n On the otherside of this door is: "
                                            + otherRoomDescription
                                            + " "
                                            + spaceNum);

        //doorNumDesc.setStyle("-fx-border-color: MEDIUMVIOLETRED; -fx-padding:3px;");
        doorDescription.setWrappingWidth(250);
        doorDescription.setTextAlignment(TextAlignment.CENTER);

        //Setting the stroke color
        doorNumDesc.setStroke(Color.PINK);
        doorDescription.setStroke(Color.WHITE);

        Button jump = createSwitchButton(spaceNum);

        layout.getChildren().addAll(doorNumDesc, doorDescription, jump);
        layout.setAlignment(Pos.TOP_CENTER);

        Scene doorScene = new Scene(layout, 350, 270);
        doorWindow.setScene(doorScene);
        doorWindow.show();
    }

    /**create button to jump to next space.
     * @param spaceNum - space number in list
     * @return - button object created
     */
    private Button createSwitchButton(int spaceNum) {
        Button switchSpace = new Button("Go to that space");
        switchSpace.setOnAction(e -> setSwitchButtonAction(spaceNum));
        return switchSpace;
    }

    /**Action for when switch button pressed to change selected.
     * @param spaceNum - space number in list
     * space index. and close popup
     */
    private void setSwitchButtonAction(int spaceNum) {
        if (spaceString == "Chamber") {
            myGui.selectChamberIndex(spaceNum - 1);
        } else {
            myGui.selectPassageIndex(spaceNum - 1);
        }
        doorWindow.close();
    }

}
