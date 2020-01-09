package gui;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public abstract class EditPopup {

    /**create menu with all the passage section options.
     * @return - the menu
     */
    public abstract ComboBox<String> fillPSectionsList();

    /**from treasure drop down menu assign a rule base on selection.
     * @return - roll value
     */
    public abstract int parseOption();

    /**Create submit button with action to call method to remove conent.
     * @param enterRoll - section to remove from
     * @param response - feedback for if successful
     * @return - submit button
     */
    public abstract Button createRemSubmitButton(TextField enterRoll, Label response);

    /**setup pop window to remove the content.
     */
    public abstract void remWindow();

    /**create and set button action to submit an adding edit.
     * @param response - feedback for if successful or error
     * @return - button
     */
    public abstract Button createAddSubmitButton(Label response);

    /**create drop down menu for treasures.
     * @return - drop down menu with options
     */
    public abstract ComboBox<String> createOptionsList();

    /**
     * Create popup window to select and enter a roll.
     * for content to add in chamber
     */
    public abstract void addWindow();

    /**Create basic window for popup and set up window's nodes .
     * @param spaceType - space number for the space door is connected to
     */
    public abstract void createWindow(int spaceType);


    /**Check if input provided by user fits ranges.
     * @param rollInput - string of user input
     * @param min - min range
     * @param max - max range
     * @return - return the input as an int or -1 if failed
     */
    public int validateInput(String rollInput, int min, int max) {

        //check oif typed int and if did make sure its in roll
        try {
            int rollValue =  Integer.parseInt(rollInput);
            if (rollValue >= min && rollValue <= max) {
                return rollValue;
            } else {
                return -1;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
