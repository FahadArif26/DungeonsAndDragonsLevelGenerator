package models;

import dnd.models.Monster;

import java.util.ArrayList;

/*
A passage begins at a door and ends at a door.  It may have many other doors along
the way

You will need to keep track of which door is the "beginning" of the passage
so that you know how to
*/

public class Passage extends Space {
    //these instance variables are suggestions only
    //you can change them if you wish.

    /**Arraylist of all passage section objects that make up the passage.*/
    private ArrayList<PassageSection> thePassage;
    /**Arraylist of all doors in passage section objects that make the passage.*/
    private ArrayList<Door> doorsInSections;

    /**boolean for if an ending condition section is in the chamber, eg. "ends in door to chamber".*/
    private boolean addedFinalPS; //forced in a passage section ending condition

    private int myIndex;

    /*
     Required Methods for that we will test during grading
     */
    /* note:  Some of these methods would normally be protected or private, but because we
    don't want to dictate how you set up your packages we need them to be public
    for the purposes of running an automated test suite (junit) on your code.  */

    /**Constructor with no parameter for passage, to initalize and set up
     * variables and lists.
     */
    public Passage() {
        //allocate memory/ create arraylists
        thePassage = new ArrayList<>();
        doorsInSections = new ArrayList<>();

        //intialize instance varaibles
        addedFinalPS = false;
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

    /**Return all the doors in the chamber.
     * @return - Arraylist of all door objects in the passage
     */
    public ArrayList<Door> getDoors() {
        //gets all of the doors in the entire passage
        return doorsInSections;
    }

    /** Add in a passage section to list of sections.
     * @param toAdd -passageSection object to add
     */
    public void addPassageSection(PassageSection toAdd) {
        //adds the passage section to the passageway
        thePassage.add(toAdd);
        //add a link to door form passage
        if (toAdd.getDescription().contains("Dead End")) {
            addedFinalPS = true;

        } else {
            if (toAdd.getDescription().contains("Door to a Chamber") || toAdd.getDescription().contains("archway (door) to chamber")) {
                addedFinalPS = true;

            } else if (toAdd.getDescription().contains("archway") || toAdd.getDescription().contains("door")) {
                addedFinalPS = false;

            }
            setDoor(toAdd.getDoor());
            doorsInSections.add(toAdd.getDoor()); //add to door to array list
        }
    }

    /** Add in a passage section to list of sections.
     * @param toAdd -passageSection object to add
     * @param index - index in arraylist to add passagesection
     */
    public void addPassageSection(PassageSection toAdd, int index) {
        //adds the passage section to the passageway
        thePassage.add(index, toAdd);
        //add a link to door form passage
        if (toAdd.getDescription().contains("Dead End")) {
            addedFinalPS = true;

        } else {
            if (toAdd.getDescription().contains("Door to a Chamber") || toAdd.getDescription().contains("archway (door) to chamber")) {
                addedFinalPS = true;

            } else if (toAdd.getDescription().contains("archway") || toAdd.getDescription().contains("door")) {
                addedFinalPS = false;

            }
            setDoor(toAdd.getDoor());
            doorsInSections.add(toAdd.getDoor()); //add to door to array list
        }
    }

    /**Setter for a door in the passage.
     * @param newDoor - door to give link to this passage object
     */
    @Override
    public void setDoor(Door newDoor) {
        //should add a door connection to the current Passage Section
        newDoor.setSpaces(this);
    }

    /**Getter for description of the entire passage.
     * @return - string with passage description
     */
    @Override
    public String getDescription() {
        StringBuilder passageDescription = new StringBuilder();

        //Go through arrayList for passage section in this passage and print it out
        if (thePassage.size() > 0) {
            for (int i = 0; i < thePassage.size(); i++) {
                passageDescription.append("\n" + thePassage.get(i).getDescription() + "\n");
            }
        }

        return passageDescription.toString();
    }

    /*
     You can write your own methods too, you aren't limited to the required ones
     */

    /**Function to create the whole passage, by making indivdual passage sections
     * till hit an ending section or have over 10.
     */
    public void genPassageSections() {

        //Add PassageSection for entering door from first Chamber
        PassageSection ps1 = new PassageSection("10FT straight section: Behind you at start of passage there is a door to the chamber previous");
        addPassageSection(ps1);

        PassageSection ps2 = new PassageSection("10FT straight section: Passage ends in Door to a Chamber");
        addPassageSection(ps2);


    }

    /**Getter for all the passage sections.
     * @return - arraylist of passage sections
     */
    public ArrayList<PassageSection> getThePassageSections() {
        return thePassage;
    }


    /**Switch a door object in the specified passage section with the door sent in.
     * @param d - door object being sent in to replace the door in the list
     * @param i - index number of passage section
     */
    public void replacePassageSectionDoor(Door d, int i) {

        doorsInSections.set(i, d);
    }

    /**Add a monster to a specific passage section.
     * @param theMonster - monster object to insert
     * @param i - index of passage section
     */
    public void addMonster(Monster theMonster, int i) {
        // adds a monster to section 'i' of the passage
        thePassage.get(i).addMonster(theMonster);
    }

    /**
     * Get number of monsters in the passage.
     * @return - int value of number of monster
     */
    public int getNumMonsters() {
        int numMonsters = 0;
        for (int i = 0; i < thePassage.size(); i++) {
            if (thePassage.get(i).getMonster() != null) {
                numMonsters++;
            }
        }
        return numMonsters;
    }

    /**Get number of treasures in the passage.
     * @return - int value of number of treasure
     */
    public int getNumTreasures() {
        int numTreasures = 0;
        for (int i = 0; i < thePassage.size(); i++) {
            if (thePassage.get(i).getTreasure() != null) {
                numTreasures++;
            }
        }
        return numTreasures;

    }

}

