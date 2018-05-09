
package esi.atlir5.mikolivator.events;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public interface ControllerElevatorListener {

    /**
     * Update the number of people waiting an elevator in the building.
     * @param nbOfPeopleWaiting
     */
    void updatePassengersWaitingElevator(int nbOfPeopleWaiting);
}
