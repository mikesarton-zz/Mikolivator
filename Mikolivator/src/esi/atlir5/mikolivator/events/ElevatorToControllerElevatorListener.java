
package esi.atlir5.mikolivator.events;

import esi.atlir5.mikolivator.model.Elevator;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public interface ElevatorToControllerElevatorListener {

    /**
     * Method to embark passengers waiting on the elevator's current floor.
     * @param floor
     * @param e
     */
    void embark(int floor, Elevator e);
}
