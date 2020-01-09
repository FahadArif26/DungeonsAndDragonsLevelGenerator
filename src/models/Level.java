package models;

import dnd.die.D20;

public class Level implements java.io.Serializable {

    /**Int to store roll value of dice.*/
    private int rollVal;
    /**string to hold description of level.*/
    private String description;

    /**No parameter level contructor, initialize values.
     */
    public Level() {
        rollVal = 0;

    }

    /**Method to set description of level based on roll table RANDOMLY.
     */
    public void setLevel() {
        //get a random roll value
        D20 die20 = new D20(); //is roll 0-20, ro 1-20
        rollVal = die20.roll();

        if (rollVal <= 11) {
            setLevelOnRolls1();

        } else {
            setLevelOnRolls2();
        }
    }

    /**Method to set description of level based on roll table RANDOMLY.
     */
    private void setLevelOnRolls1() {
        if (rollVal < 3) {
            //passage goes straight for 10ft
            description = "passage goes straight for 10 ft";
        } else if (rollVal >= 3 && rollVal <= 5) {
            //passage ends in a door to a chambers
            description = "passage ends in Door to a Chamber";
        } else if (rollVal >= 6 && rollVal <= 7) {
            //archway (door) to right, main passage continues straight for 10 ft
            description = "archway (door) to right (main passage continues straight for 10 ft)";
        } else if (rollVal >= 8 && rollVal <= 9) {
            //archway (door) to left, main passage continues straight for 10 ft
            description = "archway (door) to left (main passage continues straight for 10 ft)";
        } else if (rollVal >= 10 && rollVal <= 11) {
            //passage turns to left and continues straight for 10 ft
            description = "passage turns to left and continues for 10 ft";
        }
    }

    /**Method to set description of level based on roll table RANDOMLY.
     */
    private void setLevelOnRolls2() {
         if (rollVal >= 12 && rollVal <= 13) {
            //passage turns to right and continues straight for 10 ft
            description = "passage turns to right and continues for 10 ft";
        } else if (rollVal >= 14 && rollVal <= 16) {
            //passage ends in archway (door) to chamber
            description = "passage ends in archway (door) to chamber";
        } else if (rollVal == 17) {
            // Stairs, passage continues straight for 10 ft
            description = "Stairs, (passage continues straight for 10 ft)";
        } else if (rollVal >= 18 && rollVal <= 19) {
            //Dead End
            description = "Dead End";
        } else if (rollVal == 20) {
            //Wandering Monster (passage continues straight for 10 ft)
            description = "Wandering Monster (passage continues straight for 10 ft)";
        }
    }

    /**Getter for level description.
     * @return - string of level description
     */
    public String getDescription() {

        return description;
    }

}
