package models;

import dnd.die.Die;
import dnd.models.Monster;
import dnd.models.Treasure;

/* Represents a 10 ft section of passageway */

public class PassageSection implements java.io.Serializable {

    /*
     Required Methods for that we will test during grading
     */
    /* note:  Some of these methods would normally be protected or private, but because we
    don't want to dictate how you set up your packages we need them to be public
    for the purposes of running an automated test suite (junit) on your code.  */

    /**Monster object in the passageSection.*/
    private Monster myMonster;
    /**Level object in the passageSection to base description off of.*/
    private Level lvl;
    /**Stringbuilder of passageSection description.*/
    private StringBuilder lvlDescript;
    /**Door object in the passageSection.*/
    private Door door;
    private String myDescription;
    private Treasure myTreasure;


    /**No Parameter contructor for passage section, intialize all variables,
     * call functions to generate doors, monsters and etc. based on level description.
     */
    public PassageSection() {
        //sets up the 10 foot section with default settings

        lvl = new Level();
        lvl.setLevel();
        lvlDescript = new StringBuilder();
        lvlDescript.append(lvl.getDescription()); //add description from lvl
        //Function find out if theres a door, monster or etc. and create them
        parseDescription();

    }

    /**Contructor for passage section, with a section description being specified.
     * Initialize all variables, call functions to generate doors, monsters
     * and etc. based on level description.
     * @param description -  string of description for the level
     */
    public PassageSection(String description) {
        //sets up a specific passage based on the values sent in from
        //modified table 1
        myDescription = description;
        lvlDescript = new StringBuilder();
        lvlDescript.append(description);
        parseDescription();
        myMonster = null;

    }

    /**Getter for the door in the passage section.
     * @return - retrun the door object in the section
     */
    public Door getDoor() {
        //returns the door that is in the passage section, if there is one
        return door;
    }

    /**Getter for passage section description.
     * @return - string with description of the section
     */
    public String getDescription() {
        lvlDescript.delete(0, lvlDescript.length());
        lvlDescript.append(myDescription);
        if (myMonster != null) {
            lvlDescript.append("\n  Monster in passage is " + myMonster.getDescription());
        }
        if (myTreasure != null) {
            lvlDescript.append("\n  Treasure in passage is " + myTreasure.getDescription());
        }
        return lvlDescript.toString();
    }

    /**Pare the description the level object gave, and find out if there is
     * a monster or door and create it.
     */
    private void parseDescription() {
        //find if monster
        if (lvlDescript.toString().contains("Monster")) {
            createMonster();
        }

        //if door in passage section, create it
        if (lvlDescript.toString().contains("Door") || lvlDescript.toString().contains("door")) {
            //check if door is an archway
            if (lvlDescript.toString().contains("Archway") || lvlDescript.toString().contains("archway")) {
                addArchway();
            } else {
                addRandomDoor();
            }
        }

    }

    private void addArchway() {
        door = new Door();
        //set door as an archway
        door.setArchway(true);
    }

    private void addRandomDoor() {
        door = new Door();
        //set door type
        door.setDoorType();
    }

    private void createMonster() {
        //generate monster
        Monster monster = new Monster();
        monster.setType(Die.percentile());

        addMonster(monster);
    }

    /**If inserting a monster in from the passage class.
     * @param theMonster - monster to add
     */
    public void addMonster(Monster theMonster) {

        //Make monster sent set to instance variable
        myMonster = theMonster;

        //Append to monster description to description
        lvlDescript.append("\n  Monster in passage is " + theMonster.getDescription());
    }

    /**Remove monster from this section, ie. set to null.
     */
    public void removeMonster() {
        //make the monster null
        myMonster = null;
    }

    /**If inserting a monster in from the passage class.
     * @param theTreasure - treasure object to add
     */
    public void addTreasure(Treasure theTreasure) {
        //Make monster sent set to instance variable
        myTreasure = theTreasure;
    }

    /**Remove treasure from this section, ie. set to null.
     */
    public void removeTreasure() {
        //make the monster null
        myTreasure = null;
    }

    /**get the monster of this section.
     * @return - monster object
     */
    public Monster getMonster() {
        return myMonster;
    }

    /**get the treasure of this section.
     * @return - treasure object
     */
    public Treasure getTreasure() {
        return myTreasure;
    }
}
