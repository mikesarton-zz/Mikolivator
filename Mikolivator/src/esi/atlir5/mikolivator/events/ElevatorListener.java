
package esi.atlir5.mikolivator.events;

import esi.atlir5.mikolivator.model.MovementElevator;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public interface ElevatorListener {

    /**
     * Update the stats of an elevator.
     * @param positionElevator The elevator's position in the building.
     * @param currentFloor The elevator's current floor.
     * @param nbPassengers The number of people inside the elevator.
     */
    void updateElevatorInfos(int positionElevator, int currentFloor, int nbPassengers);

    /**
     * Update the elevator's state.
     * @param floor The elevator's current floor.
     * @param me The elevator's direction.
     * @param nbPeople The number of people inside the elevator.
     */
    void updateElevatorState(int floor, MovementElevator me, int nbPeople);
}
