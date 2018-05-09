
package esi.atlir5.mikolivator.events;

import esi.atlir5.mikolivator.model.Passenger;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public interface PassengerListener {

    /**
     * Method to give a new destination to someone.
     * @param p The person who needs a new destination.
     */
    void passengerNeedsNewDestination(Passenger p);

    /**
     * Method to indicate someone called an elevator.
     * @param p The person who needs an elevator.
     * @param positionElevator The elevator's position who's been called.
     */
    void passengerNeedsElevator(Passenger p, int positionElevator);

    /**
     * Method to indicate that someone left the floor.
     * @param p The person who left the floor.
     * @param floor The floor the person left.
     */
    void passengerLeftFloor(Passenger p, int floor);

    /**
     * Method to indicate that someone enter the floor.
     * @param p The person who enter the floor.
     * @param floor The floor the person enter.
     */
    void passengerEnterFloor(Passenger p, int floor);
}
