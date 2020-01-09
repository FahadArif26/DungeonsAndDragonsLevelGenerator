package gui;

import models.Chamber;
import models.DungeonBuilder;
import models.Passage;
import dnd.die.Die;
import dnd.models.Monster;
import dnd.models.Treasure;
import javafx.scene.control.ComboBox;


import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class Controller {

    private DungeonBuilder theDungeon;
    private GUI myGui;

    private ArrayList<Chamber> theChambers;
    private ArrayList<Passage> thePassages;


    /**Constructor to setup controller, load instance variables.
     * set basic requirements for model interaction
     * @param theGui - main gui object
     */
    public Controller(GUI theGui) {
        myGui = theGui;
        theDungeon = new DungeonBuilder();
        theDungeon.makeLevel();
        theChambers = theDungeon.getChambersList();
        thePassages = theDungeon.getPassagesList();
    }

    /**Method to save the current game state to a file of choice.
     */
    public void saveGame() {
        try {
            final JFileChooser fileChooser = new JFileChooser();
            int retValue = fileChooser.showSaveDialog(null);

            if (retValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                file.createNewFile(); //create the actual file
                FileOutputStream outFile = new FileOutputStream(file.getName());
                ObjectOutputStream outObject = new ObjectOutputStream(outFile);
                outObject.writeObject(theDungeon);
                //close the streams
                outFile.close();
                outObject.close();
                myGui.setCenterText("SUCCESSFULLY SAVED DUNGEON FILE!!");
                myGui.clearGraphicalDisplay();

            }
        } catch (Exception e) {
            myGui.setCenterText("ERROR IN SAVING FILE!!");
        }
    }

    /**Method to load the current game state from a file of choice.
     */
    public void loadGame() {
        try {
            final JFileChooser fileChooser = new JFileChooser();
            int retValue = fileChooser.showOpenDialog(null);

            if (retValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                //open file
                FileInputStream inFile = new FileInputStream(file.getName());
                ObjectInputStream inObject = new ObjectInputStream(inFile);
                theDungeon = (DungeonBuilder) inObject.readObject();
                //set instance vars
                theChambers = theDungeon.getChambersList();
                thePassages = theDungeon.getPassagesList();

                //close the streams
                inFile.close();
                inObject.close();

                myGui.setCenterText("SUCCESSFULLY LOADED DUNGEON FILE!!");
                myGui.clearGraphicalDisplay();

            }
        } catch (Exception e) {
            myGui.setCenterText("ERROR IN LOADING SAVED FILE!!");

        }
    }

    /**Populate doors list according to the space's doors.
     * @param spaceList - menu of space to get space details from
     * @param type - space type, 0 = chamber, 1 = passage
     */
    public void adjustDoorsList(ComboBox<String> spaceList, int type) {
        ComboBox<String> doorsList = myGui.getDoorsDropMenu();
        int index = spaceList.getSelectionModel().getSelectedIndex();
        int numDoors;
        String indexDescription = "Door ";

        if (index > -1) {
            if (type == 0) {
                //chamber
                numDoors = theChambers.get(index).getDoors().size();
            } else {
                //passage
                numDoors = thePassages.get(index).getDoors().size();
            }

            doorsList.getItems().clear();

            for (int i = 0; i < numDoors; i++) {
                doorsList.getItems().add(indexDescription + (i + 1));
            }
        }
    }

    /**Create the door popup send in the doors description and what
     * space its conected to.
     * @param doorIndex - door index in list
     * @param index - index of space in space list
     * @param type - chamber = 0, passage = 1
     */
    public void genDoorPopup(int doorIndex, int index, int type) {
        if (doorIndex >= 0) {
            DoorPopup doorPopup = new DoorPopup(myGui);
            int spaceNum;
            if (type == 0) {
                spaceNum = (theChambers.get(index).getDoors().get(doorIndex).getSpaces().get(1).getMyNum() + 1);
                doorPopup.createWindow(doorIndex, theChambers.get(index).getDoors().get(doorIndex).getDescription(), "Passage", spaceNum);
            } else {
                spaceNum = (thePassages.get(index).getDoors().get(doorIndex).getSpaces().get(0).getMyNum() + 1);
                doorPopup.createWindow(doorIndex, thePassages.get(index).getDoors().get(doorIndex).getDescription(), "Chamber", spaceNum);
            }
        }
    }

    /**Get description for the space for the center box., then send it to gui.
     * @param type - if chamber = 0, passage = 1
     */
    public void printDescription(int type) {
        if (type == 0) {
            int chamberIndex = myGui.selectedChamberIndex();
            if (chamberIndex > -1) {
                myGui.setCenterText(theChambers.get(chamberIndex).getDescription());

            }
        } else {
            int passageIndex = myGui.selectedPassageIndex();
            if (passageIndex > -1) {
                myGui.setCenterText(thePassages.get(passageIndex).getDescription());
            }
        }
    }

    /**get the size and contents details to draw the chamber.
     * @param index - chamber index in the list
     */
    public void chamberDrawing(int index) {
        if (index > -1) {
            int len = theChambers.get(index).getChamberLength() * 10;
            int width = theChambers.get(index).getChamberWidth() * 10;
            int numDoors = theChambers.get(index).getNumExits();
            int monster = getChamberMonsterSize();
            int treasure = getChamberTreasureSize();
            myGui.createDrawingBoard(len, width, numDoors, treasure, monster);
        }
    }

    /**get the size and contents details to draw the passage.
     * @param index - passage index in the list
     */
    public void passageDrawing(int index) {
        if (index > -1) {
            int len = getNumSectionsInPassage() * 100;
            int width = 100;
            int numDoors = thePassages.get(index).getDoors().size();
            int monster = thePassages.get(index).getNumMonsters();
            int treasure = thePassages.get(index).getNumTreasures();
            myGui.createDrawingBoard(len, width, numDoors, treasure, monster);
        }
    }

    /**Add treasure to dnd model based on edit popup.
     * @param rollValue - roll value to select treasure
     * @param sectionNumber - passage section number to add to, chamber = 0
     */
    public void addTreasure(int rollValue, int sectionNumber) {
        //sectionNumber is negative this is fro a chamber not passage
        Treasure treasureAdd = new Treasure();
        treasureAdd.chooseTreasure(rollValue);
        treasureAdd.setContainer(Die.d20());

        if (sectionNumber < 0) {
            //chamber
            theChambers.get(myGui.selectedChamberIndex()).addTreasure(treasureAdd);
            //update center description
            myGui.setCenterText(theChambers.get(myGui.selectedChamberIndex()).getDescription());
            chamberDrawing(myGui.selectedChamberIndex());

        } else {
            //passage add
            thePassages.get(myGui.selectedPassageIndex()).getThePassageSections().get(sectionNumber).addTreasure(treasureAdd);
            myGui.setCenterText(thePassages.get(myGui.selectedPassageIndex()).getDescription());
            passageDrawing(myGui.selectedPassageIndex());
        }

    }

    /**Get the size of treasures list in the chamber.
     * @return - selected chambers, number of treasures
     */
    public int getChamberTreasureSize() {
        return theChambers.get(myGui.selectedChamberIndex()).getTreasureList().size();
    }

    /**Remove treasure from dnd model based on edit popup.
     * @param index - space index from list to remove from
     * @param sectionNumber - passage section number to add to, chamber = 0
     */
    public void removeTreasure(int index, int sectionNumber) {
        //sectionnumber < 0 = chamber otherwise passage
        if (sectionNumber < 0) {
            //chamber
            ArrayList<Treasure> treasureList = theChambers.get(myGui.selectedChamberIndex()).getTreasureList();

            treasureList.remove(index - 1);
            myGui.setCenterText(theChambers.get(myGui.selectedChamberIndex()).getDescription());
            chamberDrawing(myGui.selectedChamberIndex());

        } else {
            //passage
            thePassages.get(myGui.selectedPassageIndex()).getThePassageSections().get(sectionNumber).removeTreasure();
            myGui.setCenterText(thePassages.get(myGui.selectedPassageIndex()).getDescription());
            passageDrawing(myGui.selectedPassageIndex());
        }
    }

    /**Add monster to dnd model based on edit popup.
     * @param rollValue - roll value to select monster based on edit
     * @param sectionNumber - passage section number to add to, chamber = 0
     */
    public void addMonster(int rollValue, int sectionNumber) {
        //sectionNumber is negative this is fro a chamber not passage
        Monster monsterAdd = new Monster();
        monsterAdd.setType(rollValue);

        if (sectionNumber < 0) {
            //chamber
            theChambers.get(myGui.selectedChamberIndex()).addMonster(monsterAdd);
            //update center description
            myGui.setCenterText(theChambers.get(myGui.selectedChamberIndex()).getDescription());
            chamberDrawing(myGui.selectedChamberIndex());

        } else {
            //passage add
            thePassages.get(myGui.selectedPassageIndex()).addMonster(monsterAdd, sectionNumber);
            myGui.setCenterText(thePassages.get(myGui.selectedPassageIndex()).getDescription());
            passageDrawing(myGui.selectedPassageIndex());
        }
    }

    /**Get the size of monsters list in the chamber.
     * @return - selected chambers, number of monsters
     */
    public int getChamberMonsterSize() {
        return theChambers.get(myGui.selectedChamberIndex()).getMonsters().size();
    }

    /**Remove monster from dnd model based on edit popup.
     * @param index - space index from list to remove from
     * @param sectionNumber - passage section number to add to, chamber = 0
     */
    public void removeMonster(int index, int sectionNumber) {
        //sectionnumber < 0 = chamber otherwise passage
        if (sectionNumber < 0) {
            //chamber
            ArrayList<Monster> monsterList = theChambers.get(myGui.selectedChamberIndex()).getMonsters();

            monsterList.remove(index - 1);
            myGui.setCenterText(theChambers.get(myGui.selectedChamberIndex()).getDescription());
            chamberDrawing(myGui.selectedChamberIndex());

        } else {
            //passage
            thePassages.get(myGui.selectedPassageIndex()).getThePassageSections().get(sectionNumber).removeMonster();
            myGui.setCenterText(thePassages.get(myGui.selectedPassageIndex()).getDescription());
            passageDrawing(myGui.selectedPassageIndex());
        }
    }

    /**get how many passages are in the level.
     * @return -  int value of number of passages in the level
     */
    public int getNumPassages() {
        return thePassages.size();
    }

    /**get how many passages sections are in the currently selected passage.
     * @return -  int value of number of passage sections in the level
     */
    public int getNumSectionsInPassage() {
        return thePassages.get(myGui.selectedPassageIndex()).getThePassageSections().size();
    }

}
