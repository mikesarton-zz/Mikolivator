
package esi.atlir5.mikolivator.events;

import esi.atlir5.mikolivator.model.PassengerState;
import java.util.List;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public interface FloorListener {

    /**
     * Update the floor's stats.
     * @param nbFloor The floor's number.
     * @param nbPassengersHidden The number of hidden people at this floor.
     * @param nbPassengersWalking The number of walking people at this floor.
     * @param nbPassengersWaiting The number of people waiting an elevator at 
     * this floor.
     */
    void updateFloorStats(int nbFloor, int nbPassengersHidden, 
            int nbPassengersWalking, int nbPassengersWaiting);

    /**
     * Update the positions of people at this floor.
     * @param floor The floor's number.
     * @param pos The list of positions taken by people at this floor.
     * @param states The direction of people walking at this floor.
     */
    void updateFloorPositionsPassengers(int floor, List<Integer> pos, 
            List<PassengerState> states);
    
    /**
    * Method called when a floor is evacuated.
    * @param nbPeopleEvacuated The number of people evacuated.
    * @param floorNumber The floor's number.
    */
    void updateNewEvacuationRecord(int nbPeopleEvacuated, int floorNumber);    
}
