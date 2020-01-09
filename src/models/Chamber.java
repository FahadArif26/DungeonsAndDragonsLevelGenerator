package models;

import dnd.models.ChamberContents;
import dnd.models.ChamberShape;
import dnd.models.Monster;
import dnd.models.Treasure;
import java.util.ArrayList;
import dnd.die.Die;
import dnd.exceptions.NotProtectedException;
import dnd.exceptions.UnusualShapeException;


public class Chamber extends Space {

    /**Instance variable for the contents class of the chamber. */
    private ChamberContents myContents;
    /**Instance variable for the chamberShape class of the chamber. */
    private ChamberShape mySize;

    /**Instance variable for an arraylist of all the doors in the chamber. */
    private ArrayList<Door> doorsInChamber;
    /**Instance variable for an arraylist of all the treasures in the chamber. */
    private ArrayList<Treasure> myTreasures;
    /**Instance variable for an arraylist of all the monsters in the chamber. */
    private ArrayList<Monster> myMonsters;

    /**Instance variable, check for if an entry door was inserted to the list of door,
     * then prints the rest after door2, ie. door 1 is the door entered through .
     * */

    private StringBuilder chamberDescription;
    private int numExits;
    private int myIndex;

    /*
     Required Methods for that we will test during grading

    note:  Some of these methods would normally be protected or private, but because we
    don't want to dictate how you set up your packages we need them to be public
    for the purposes of running an automated test suite (junit) on your code.  */


    /** No parameter constructor to generate a chamber,
     * initialize all the instance variables and set descriptions
     * for the chamberShape and ChamberContents class, then call
     * the functions to process and make chamber description.
     */
    public Chamber() {
        //create/allocate memory for classes
        myContents = new ChamberContents();

        //initialize array
        doorsInChamber = new ArrayList<>();
        myMonsters = new ArrayList<>();
        myTreasures = new ArrayList<>();


        /*create a random content/shape description*/
        genShape();
        genContents(myContents);

        findOutWhatsInChamberC(myContents); //process contents
        createDoorsOnExits(); //make doors based on exits
    }

    /**initialize all the instance variables and set descriptions
     * for the chamberShape and ChamberContents class based on objects sent in,
     *  then call the functions to process and make chamber description.
     *
     * @param theShape - object of chamberShape sent in to base chamber size and shape on
     * @param theContents - object of chamberContents sent in to base chamber contents on
     */
    public Chamber(ChamberShape theShape, ChamberContents theContents) {
        //this.myContents = theContents;
        setContents(theContents);
        setShape(theShape);

        doorsInChamber = new ArrayList<>();
        myMonsters = new ArrayList<>();
        myTreasures = new ArrayList<>();


        findOutWhatsInChamberC(myContents);
        createDoorsOnExits();
    }

    /**Set the index of the chamber in the arraylist of chambers in main.
     * @param index - index int value chamber is in arraylist
     */
    @Override
    public void setMyNum(int index) {
        myIndex = index;
    }

    /**getter for the index this chamber is in the arraylist of chambers in main.
     * @return - int value of chmabers index
     */
    @Override
    public int getMyNum() {
        return myIndex;
    }


    private void setContents(ChamberContents theContents) {

        myContents = theContents;
    }

    private void genContents(ChamberContents theContents) {
        theContents.chooseContents(Die.d20());
    }

    /**Set the shape of chamber based on the object for shape sent in.
     *  - object of chamberShape sent in to set instance variable to
     */
    private void genShape() {

        int roll = Die.d20();
        //restrict rolls to only be square/rectangles
        while (roll > 14 || roll == 7 || roll == 8) {
            roll = Die.d20();
        }
        setShape(ChamberShape.selectChamberShape(roll));
    }

    /**Set the shape of chamber based on the object for shape sent in.
     * @param theShape - object of chamberShape sent in to set instance variable to
     */
    private void setShape(ChamberShape theShape) {
        mySize = theShape;
    }

    /**get the list of door objects in the chamber.
     * @return - ArrayList of doors in chamber
     */
    public ArrayList<Door> getDoors() {
        return doorsInChamber;
    }

    /**Get description of chamber, and all its contents, eg. doors, monsters etc.
     * @return - string of description
     */
    @Override
    public String getDescription() {

        chamberDescription = new StringBuilder();
        getChamberShapeDescription();

        //print out monster description
        if (myMonsters.size() > 0) {
            chamberDescription.append("\n------------------");
            chamberDescription.append("\nThe monster(s) in the chamber are: ");
            chamberDescription.append(getMonsterDescription());
        }

        //print out treasure description
        if (myTreasures.size() > 0) {
            chamberDescription.append("\n------------------");
            chamberDescription.append("\nThe treasure(s) in the chamber are: ");
            chamberDescription.append(getTreasureDescription());
        }

        //if chamber is empty print that
        if (myContents.getDescription() == "empty" && myTreasures.size() == 0 && myMonsters.size() == 0) {
            chamberDescription.append("\nThe chamber is empty");
        }
        return chamberDescription.toString();
    }

    /**Add a new door to the chamber arrayList of doors and set its space with this (chamber).
     */
    private void addDoor() {
        //should add a door connection to this room
        Door door = new Door();
        door.setDoorType();
        doorsInChamber.add(door);
        setDoor(door);
    }

    /**Add a new door to the chamber arrayList of doors and set its space with this (chamber).
     * @param theDoor - door to be added
     */
    @Override
    public void setDoor(Door theDoor) {
        //should add a door connection to this room
        theDoor.setSpaces(this);
    }

    /** Get the number of exits ie. doors in chamber.
     * @return - number of exits in chamber
     */
    public int getNumExits() {

        return numExits;
    }

    /** get lenght of chamber shape.
     * @return - int value of length
     */
    public int getChamberLength() {
        return mySize.getLength();
    }

    /** get width of chamber shape.
     * @return - int value of width
     */
    public int getChamberWidth() {
        return mySize.getWidth();
    }

    /*
     You can write your own methods too, you aren't limited to the required ones
     */


    /**Set/ process all the contents of chamber and generate objects accordingly.
     * @param theContents - contents object to base contents on
     */
    private void findOutWhatsInChamberC(ChamberContents theContents) {

        //Check what chamber content was generated then call appropriate method
        if (theContents.getDescription() == "monster only") {
            setMonster(); //call function to generate a monster

        } else if (theContents.getDescription() == "treasure") {
            setTreasure(); //call function to generate treasure

        }  else if (theContents.getDescription() == "monster and treasure") {
            setMonster(); //generate monster
            setTreasure(); //generate a treasure

        } /*else if (theContents.getDescription() == "stairs") {
            //genStairs(userChoosing);
            //DONT WORRY FOR NOW!!

        } else if (theContents.getDescription() == "trap") {
            //genTrap(userChoosing);
            //DONT WORRY FOR NOW!!
        } */
    }

    /**Add a monster to the chamber.
     * @param theTreasure - treasure object to be added
     */
    public void addTreasure(Treasure theTreasure) {
        myTreasures.add(theTreasure);
    }

    /**Get all the treasures in the chamber.
     * @return -  arrayList of treasures in chamber
     */
    public ArrayList<Treasure> getTreasureList() {
        return myTreasures;
    }

    /**Generate a treasure, set its description, container.
     */
    private void setTreasure() {
        Treasure myTreasure = new Treasure();
        myTreasure.chooseTreasure(Die.d20());
        myTreasure.setContainer(Die.d20());

        addTreasure(myTreasure);
    }

    private String getTreasureDescription() {
        StringBuilder treasureDescription = new StringBuilder();
        for (int i = 0; i < myTreasures.size(); i++) {
            try {
                //Try if guarded
                treasureDescription.append("\nTreasure " + (i + 1) + ": " + myTreasures.get(i).getDescription() + ", inside " + myTreasures.get(i).getContainer() + ", and guarded by " + myTreasures.get(i).getProtection());
            } catch (NotProtectedException e) {
                treasureDescription.append("\nTreasure " + (i + 1) + ": "  + myTreasures.get(i).getDescription() + ", inside " + myTreasures.get(i).getContainer() + ", and is not guarded.");
            }
        }

        return treasureDescription.toString();
    }

    /**Add a monster to the chamber.
     * @param theMonster - monster object to be added
     */
    public void addMonster(Monster theMonster) {
        myMonsters.add(theMonster);
    }

    /**Get  all the monsters in the chamber.
     * @return -  arrayList of monsters in chamber
     */
    public ArrayList<Monster> getMonsters() {
        return myMonsters;
    }

    /** create a monster object and Set the monster type.
     */
    private void setMonster() {
        //Gen randomly
        Monster myMonster = new Monster();
        myMonster.setType(Die.percentile());
        addMonster(myMonster);
    }

    private String getMonsterDescription() {
        StringBuilder monsterDescription = new StringBuilder();

        for (int i = 0; i < myMonsters.size(); i++) {
            chamberDescription.append("\nMonster " + (i + 1) + ": (" + myMonsters.get(i).getMinNum() + "-" + myMonsters.get(i).getMaxNum() + ") " + myMonsters.get(i).getDescription());
        }
        return monsterDescription.toString();
    }

    private void setNumExits() {
        mySize.setNumExits(Die.d20());

        //error check roll so dont get 5 exits
        while (mySize.getNumExits() > 4) {
            mySize.setNumExits(Die.d20());
        }
        numExits = mySize.getNumExits();
    }

    /**Create door objects based on exits in the chamber.
     */
    private void createDoorsOnExits() {
        //get exits and if not set, getExits() will set it
        setNumExits();

        //loop through all exits and set them to a door add that door to the arraylist
        for (int i = 0; i < numExits; i++) {
            //create a new door for every exit
            addDoor();
        }
    }

    private void getChamberShapeDescription() {
        //print chamber shape
        chamberDescription.append("This chamber's shape is a " + mySize.getShape());
        //print out chamber dimensions/area
        try {
            chamberDescription.append("\nRoom Size (Length x Width): " + mySize.getLength() + " x " + mySize.getWidth());
            chamberDescription.append("\nRoom Area: " + mySize.getArea());
        } catch (UnusualShapeException e) {
            chamberDescription.append("\nRoom Area: " + mySize.getArea());
        }

    }




}
