package models;
import dnd.die.Die;
import dnd.models.Trap;

import java.util.ArrayList;

public class Door implements java.io.Serializable {

    /** Boolean for if door is open or closed, true = opened, false = closed.*/
    private boolean doorIsOpen;
    /** Boolean for if door is trapped or not, true = trapped, false = not trapped.*/
    private boolean doorIsTrapped;
    /** Boolean for if door is locked or unlocked, true = locked, false = unlocked.*/
    private boolean isLocked;
    /** Boolean for if door is archway or not, true = archway, false = not.*/
    private boolean doorIsArchway;

    /** Arraylist of spaces door has link to.*/
    private ArrayList<Space> connectedSpaces;
    /** Description of door.*/
    private String doorDescription;

    /** Trap object for if door is trapped.*/
    private Trap doorTrap;

    /** Arraylist of chamber door has target to.*/
    private ArrayList<Chamber> targetChambers;

    /**Door constructor with no parameters.
     * initialize all instance varaibles
     */
    public Door() {
        //needs to set defaults
        connectedSpaces = new ArrayList<>();
        doorDescription = new String();

        targetChambers = new ArrayList<>();

        //intialize basic variable booleans
        doorIsOpen = true;
        doorIsTrapped = false;
        isLocked = false;
        doorIsArchway = false;
    }

    /*
     Required Methods for that we will test during grading
     note:  Some of these methods would normally be protected or private, but because we
     don't want to dictate how you set up your packages we need them to be public
     for the purposes of running an automated test suite (junit) on your code.  */

    /**Find out if the door is trapped, and make a random trap or based on roll.
     * @param flag - if door has trap == true, else false
     */
    private void setTrapped(boolean flag) {
        // true == trapped.  Trap must be rolled if no integer is given
        doorIsTrapped = flag;

        if (doorIsTrapped) {
            doorTrap = new Trap(); // create trap object
            //no roll sent in
            //doorTrap.setDescription();
            doorTrap.chooseTrap(Die.d20());
            //Set description
            doorDescription = "The door is trapped, the trap is " + getTrapDescription();
        }

    }

    /**Set door to be an open door.
     * @param flag - true if door is open, if closed its false
     */
    private void setOpen(boolean flag) {
        //true == open
        doorIsOpen = flag;
        //if its an archaway door must be open regardless
        if (doorIsArchway) {
            doorIsOpen = true;
            isLocked = false;
        } else {
            //its not an archway so just make sure door is open or closed based on flag
            if (doorIsOpen) {
                //door cannot be opened and locked
                isLocked = false;
                doorDescription = ("The door is opened");
            } else {
                doorDescription = ("The door is closed");
            }
        }
    }

    /**Set door to be an archway and also unlocked, open.
     * @param flag - true = archway, false = not an archway
     */
    public void setArchway(boolean flag) {
        //true == is archway
        doorIsArchway = flag;

        if (doorIsArchway) {
            doorDescription = ("This is an archway");

            //archway cant be locked, or closed or trapped
            isLocked = false;
            doorIsOpen = true;
            setTrapped(false);
        }
    }


    /**Return trap description of door.
     * @return - string with description of trap
     */
    private String getTrapDescription() {
        return doorTrap.getDescription();
    }

    /**Return the spaces a door has.
     * @return - arrayList of spaces door has a reference tos
     */
    public ArrayList<Space> getSpaces() {
        //returns the two spaces that are connected by the door
        return connectedSpaces;
    }

    /**return door description.
     * @return - string of door's descritpion
     */
    public String getDescription() {
        return doorDescription;
    }

    /*
     You can write your own methods too, you aren't limited to the required ones
     */

    /**Add link to a single space object by adding it to the arraylist of spaces.
     * @param spaceOne - space object to link
     */
    public void setSpaces(Space spaceOne) {
        //link door to only where you came from not where going to cause not building that
        connectedSpaces.add(spaceOne);
    }

    /** Add a chamber object as a target for this door.
     * @param target - chamber object to add in list
     */
    public void setTargets(Chamber target) {

        targetChambers.add(target);
    }

    /**Share list of targets this door has.
     * @return - arraylist of chambers
     */
    public ArrayList<Chamber> getTargets() {

        return  targetChambers;
    }


    /**Set if door is locked.
     * @param flag - true == door is locked, false == door unlocked
     */
    private void setLocked(boolean flag) {
        //true == is locked
        isLocked = flag;

        if (isLocked) {
            //if its locked make sure door is closed
            doorIsOpen = false;
            doorDescription = ("The door is locked");
        }
    }


    /**Set door type based on roll table provided.
     */
    public void setDoorType() {
        //get a random roll value
        int rollVal = Die.d20();

        if (rollVal < 3) {
            //Door is locked
            setLocked(true);

        } else if (rollVal == 3) {
            //door is trapped
            setTrapped(true);

        } else if (rollVal >= 4 && rollVal <= 5) {
            //door is an archway
            setArchway(true);

        } else if (rollVal >= 6 && rollVal <= 12) {
            //door is just closed
            setOpen(false);

        } else {
            //door is just opened
            setOpen(true);

        }

    }
}
