
package esi.atlir5.mikolivator.events;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public interface BuildingListener {

    /**
     * Update the number of people in the building.
     * @param nbOfPeople
     */
    void updateNumberPeopleInBuilding(int nbOfPeople);
}
