
package esi.atlir5.mikolivator.observers;

import esi.atlir5.mikolivator.model.MovementElevator;
import esi.atlir5.mikolivator.model.PassengerState;
import java.util.List;

/**
 * This interface is used when the Simulation notify the building's view
 * @author Mike Sarton & Olivier Cordier
 */
public interface ObserverDisplayBuilding {
    
    /**
     * This method updates the stats of a floor
     * @param floor The floor's number
     * @param pos The list with the positions of each person on this floor
     * @param states Thie list with the direction of each person on this floor
     */
    public void updateAFloorPositions(int floor, List<Integer> pos, 
            List<PassengerState> states);

    /**
     * This method updates the elevator's state
     * @param floor The floor's number
     * @param me The elevator's movement
     * @param nbPeople The number of people in the elevator
     */
    public void updateElevatorState (int floor, MovementElevator me, int nbPeople);
}
