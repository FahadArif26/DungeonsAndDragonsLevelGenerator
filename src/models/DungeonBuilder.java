package models;

import java.util.ArrayList;
import java.util.HashMap;

public class DungeonBuilder implements java.io.Serializable {

    private HashMap<Door, ArrayList<Chamber>> doorsMap;
    private ArrayList<Chamber> theChambers; //array list for 5 chambers
    private ArrayList<Passage> thePassages;

    /**Main constructor, initialize instance variables.
     */
    public DungeonBuilder() {
        doorsMap = new HashMap<>();
        theChambers = new ArrayList<>(); //array list for 5 chambers
        thePassages = new ArrayList<>();
    }

    /**Class main function, Contains main algorithm to generate 5 passages.
     */
    public void makeLevel() {

        int exit = 0;
        int doorIndexToDoubleConnect  = 0;
        int nxtChamberHasMe = 0;

        //create 5 chambers and add them to the arrayList
        createChambersList();
        sortChambersList();
        numberChambersInList();

        /*MAIN ALGORITHM STARTS HERE!!!!!!!!!!!!!!!!*/
        for (int i = 0; i < theChambers.get(0).getNumExits(); i++) {
            //connect chamber 0's doors to the first door in the next/each chamber
            setDoorTargets(theChambers.get(0), theChambers.get(i + 1), i, 0);
            addToHashMap(theChambers.get(0), i);
        }

        //loop for all chambers
        for (int i = 1; i < 5; i++) {
            for (int j = 0; j < theChambers.get(i).getNumExits(); j++) {
                //if door has no previous targets
                if (theChambers.get(i).getDoors().get(j).getTargets().size() == 0) {
                    //need to connect a door from a different chamber
                    exit = 0;
                    //for every chamber thats not ME! check the doors
                    for (int k = i + 1; exit == 0 && k < 5; k++) {
                        //find out if any chamber i  door have k as any targets
                        nxtChamberHasMe = 0;
                        for (int l = 0; l < theChambers.get(k).getNumExits() && nxtChamberHasMe == 0; l++) { //loop all dors in next chamber
                            if (theChambers.get(k).getDoors().get(l).getTargets().contains(theChambers.get(i))) { //if already connected break
                                nxtChamberHasMe = 1;

                            } else {
                                if (theChambers.get(k).getDoors().get(l).getTargets().size() == 0) { //connect to this if not in use
                                    setDoorTargets(theChambers.get(i), theChambers.get(k), j, l);
                                    addToHashMap(theChambers.get(i), j);
                                    nxtChamberHasMe = 1;
                                    exit = 1;
                                }
                            }
                        }
                    }

                    //if finish without exit = 1 that mean door not connected and need to force 1/double up a target
                    if (exit == 0) {
                        //Loop throguht the chambers,
                        for (int k = 0; exit == 0 && k < 5; k++) {
                            if (k == i) {
                                k++;
                            }

                            //cycle through doors in chamber
                            nxtChamberHasMe = 0;
                            for (int l = 0; l < theChambers.get(k).getNumExits() && nxtChamberHasMe == 0; l++) { //next chamer's doors

                                //if that door does already have chamber i as target add to it, break
                                if (theChambers.get(k).getDoors().get(l).getTargets().contains(theChambers.get(i))) {
                                    nxtChamberHasMe = 1;

                                } else {
                                    doorIndexToDoubleConnect = l;
                                }
                            }
                            if (nxtChamberHasMe == 0) {
                                setDoorTargets(theChambers.get(i), theChambers.get(k), j, doorIndexToDoubleConnect);
                                //add this door to the hashmap
                                addToHashMap(theChambers.get(i), j);
                                exit = 1;
                            }
                        }
                    }
                }  else {
                    //Add door to hashMap since this door already has a target
                    addToHashMap(theChambers.get(i), j);
                }
            }
        } //end of for i

        //printHashMap();

        connectPassages();
        //printDoorsPassages();
    }

    /**Get the list of the chambers in the dungeon.
     * @return - arraylist of chambers
     */
    public ArrayList<Chamber> getChambersList() {
        return theChambers;
    }

    /**Get the list of the passages in the dungeon.
     * @return - arraylist of passages
     */
    public ArrayList<Passage> getPassagesList() {
        return thePassages;
    }

    private void createChambersList() {
        for (int i = 0; i < 5; i++) {
            Chamber chamber = new Chamber();
            theChambers.add(chamber);
        }
    }

    private void createPassagesList() {
        for (int i = 0; i < 25; i++) {
            Passage p = new Passage();
            p.genPassageSections();
            p.setMyNum(i);
            thePassages.add(p);
        }
    }

    private void numberChambersInList() {
        for (int i = 0; i < 5; i++) {
            theChambers.get(i).setMyNum(i);
        }
    }

    /**Method to sort all the chambers in the arraylist byt the num exits in descending order.
     */
    private void sortChambersList() {
        for (int i = 0; i < 5; i++) {
            //loop through other indexes
            for (int j = 0; j < 5 - i - 1; j++) {
                //if the next chamber has more exits swap it to come before
                if (theChambers.get(j).getNumExits() < theChambers.get(j + 1).getNumExits()) {
                    // swap the chambers, so greater one comes first in arrayList
                    Chamber tempChamber = theChambers.get(j);
                    theChambers.set(j, theChambers.get(j + 1));
                    theChambers.set(j + 1, tempChamber);
                }
            }
        }
    }

    /**Method to make target chambers and doors know.
     * @param c1 - orginal chamber setting target from
     * @param c2 - target chamber
     * @param c1Door -door originally setting target to
     * @param c2Door - door to have set c1 as target
     */
    public void setDoorTargets(Chamber c1, Chamber c2, int c1Door, int c2Door) {
        //set target for door in current chamber to the next availible chamber ie. c2
        c1.getDoors().get(c1Door).setTargets(c2);
        //set c2 to have c1 as a target as well now
        c2.getDoors().get(c2Door).setTargets(c1);
    }

    /**Method to add door and its target chambers to hashmap.
     * @param theChamber -chamber with door object in it
     * @param doorIndex - door index to add from
     */
    public void addToHashMap(Chamber theChamber, int doorIndex) {
        doorsMap.put(theChamber.getDoors().get(doorIndex), theChamber.getDoors().get(doorIndex).getTargets());
    }

    /**Link the doors of the chambers to passages based on connections.
     */
    private void connectPassages() {
        int passageIndex = 0;
        boolean passageConnected = false;
        //make Passages
        createPassagesList();
        //loop through chambers
        for (int i = 0; i < 5; i++) {
            ArrayList<Chamber> targetsList;
            //loop thru doors
            for (int doorIndex = 0; doorIndex < theChambers.get(i).getNumExits(); doorIndex++) {
                //if only 1 target for door
                //if hasnt been linked to a passage, only has a chamber
                if (theChambers.get(i).getDoors().get(doorIndex).getSpaces().size() == 1) {
                    //for this doors's target chamber get door that has this chamber as target, set space for same passage
                    targetsList = theChambers.get(i).getDoors().get(doorIndex).getTargets();

                    //now find door in target chamber that has chamber i as its target
                    for (int d = 0; d < targetsList.get(0).getDoors().size(); d++) {
                        //find door that has chamber i as target
                        if (targetsList.get(0).getDoors().get(d).getTargets().contains(theChambers.get(i))) {

                            //if target door is not connected to anything yet then connect this door to passage and target chamber's dppr
                            if (targetsList.get(0).getDoors().get(d).getSpaces().size() == 1 && targetsList.get(0).getDoors().get(d).getTargets().size() == 1) {
                                //set chamber i's door space
                                theChambers.get(i).getDoors().get(doorIndex).setSpaces(thePassages.get(passageIndex)); //link passage with set spaces
                                //replacedoor for sec 1
                                thePassages.get(passageIndex).replacePassageSectionDoor(theChambers.get(i).getDoors().get(doorIndex), 0);

                                //replace door for sec 2 with target chamber door
                                thePassages.get(passageIndex).replacePassageSectionDoor(targetsList.get(0).getDoors().get(d), 1);
                                targetsList.get(0).getDoors().get(d).setSpaces(thePassages.get(passageIndex));
                                passageConnected = true;

                            }
                        }
                    }

                }
                if (theChambers.get(i).getDoors().get(doorIndex).getTargets().size() > 1) {
                    //chamber door is connected to 2+ targets

                    //set chamber i's door space for itself in the passage
                    theChambers.get(i).getDoors().get(doorIndex).setSpaces(thePassages.get(passageIndex)); //link passage with set spaces
                    //replacedoor for sec 1
                    thePassages.get(passageIndex).replacePassageSectionDoor(theChambers.get(i).getDoors().get(doorIndex), 0);
                    int temp = 0;
                    //add other targets till last
                    for (int j = 0; j < theChambers.get(i).getDoors().get(doorIndex).getTargets().size() - 1; j++) {
                        PassageSection junctionSection = new PassageSection("10FT straight section with Door to other chamber");
                        thePassages.get(passageIndex).addPassageSection(junctionSection, j + 1);
                        targetsList = theChambers.get(i).getDoors().get(doorIndex).getTargets();
                        //now find door in target chamber that has chamber i as its target
                        for (int d = 0; d < targetsList.get(j).getDoors().size(); d++) {
                            if (targetsList.get(j).getDoors().get(d).getTargets().contains(theChambers.get(i))) {
                                //replace door for sec 2
                                thePassages.get(passageIndex).replacePassageSectionDoor(targetsList.get(j).getDoors().get(d), j + 1);
                                targetsList.get(j).getDoors().get(d).setSpaces(thePassages.get(passageIndex));
                                passageConnected = true;
                            }
                        }
                        temp = j;
                    }
                    temp += 1;
                    //add last target to chamber
                    targetsList = theChambers.get(i).getDoors().get(doorIndex).getTargets();
                    //now find door in target chamber that has chamber i as its target
                    for (int d = 0; d < targetsList.get(temp).getDoors().size(); d++) {
                        if (targetsList.get(temp).getDoors().get(d).getTargets().contains(theChambers.get(i))) {
                            //replace door for sec 2
                            thePassages.get(passageIndex).replacePassageSectionDoor(targetsList.get(temp).getDoors().get(d), temp + 1);
                            targetsList.get(temp).getDoors().get(d).setSpaces(thePassages.get(passageIndex));
                            passageConnected = true;
                        }
                    }
                }
                if (passageConnected) {
                    passageIndex++;
                    passageConnected = false;
                }
            } // door loop
        } //i loop
        removeUnusedPassages(passageIndex);
    }

    private void printDoorsPassages() {
        for (int i = 0; i < 5; i++) {
            System.out.println("CHAMBER " + i + "\n\n");
            for (int j = 0; j < theChambers.get(i).getDoors().size(); j++) {
                System.out.println("Door " + j);
                if (theChambers.get(i).getDoors().get(j).getSpaces().get(1) instanceof Passage) {
                    System.out.println(" connects to passage number " + theChambers.get(i).getDoors().get(j).getSpaces().get(1).getMyNum());
                    System.out.println(theChambers.get(i).getDoors().get(j).getSpaces().get(1).getDescription());
                } else {
                    System.out.println("CRAP ");
                }
            }
        }
    }

    private void removeUnusedPassages(int passageIndex) {
        for (int i = 24; i >= passageIndex; i--) {
            thePassages.remove(i);
        }

    }

    private void printHashMap() {
        ArrayList<Chamber> targets;
        //PRINT HASH MAP DEETS
        for (int i = 0; i < 5; i++) {
            System.out.println("\nCHAMBER NUMBER " + i);
            for (int p = 0; p < theChambers.get(i).getDoors().size(); p++) {
                System.out.println("DOOR NUMBER: " + p);
                targets =  doorsMap.get(theChambers.get(i).getDoors().get(p));
                for (int j = 0; j < targets.size(); j++) {
                    System.out.println("Target " + j + ": " + targets.get(j).getMyNum());
                }
            }
        }
    }

}
