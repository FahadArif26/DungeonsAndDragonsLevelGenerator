package models;


public abstract class Space implements java.io.Serializable {

    /**Abstract method to get the description of a space (chamber or passage).
     * @return - string with the description of the space
     */
    public abstract String getDescription();

    /**Abstract method to set the door, give it a link to the space it is in.
     * @param theDoor - door object to link for
     */
    public abstract void setDoor(Door theDoor);

    /**Set the index of the chamber in the arraylist of chambers in main.
     * @param index - index int value chamber is in arraylist
     */
    public abstract void setMyNum(int index);

    /**getter for the index this chamber is in the arraylist of chambers in main.
     * @return - int value of chmabers index
     */
    public abstract int getMyNum();

}
