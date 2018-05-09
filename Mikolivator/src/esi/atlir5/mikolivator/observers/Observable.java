
package esi.atlir5.mikolivator.observers;

import esi.atlir5.mikolivator.model.MovementElevator;
import esi.atlir5.mikolivator.model.PassengerState;
import java.util.List;

/**
 * This interface allows the Simulation's class to have one or several views
 * automatically updated according to the model.
 * @author Mike Sarton & Olivier Cordier
 */
public interface Observable {
    
    /**
     * This method add an observer to the list of observers
     * @param obs The observer to add.
     */
    public void addObserver(ObserverDisplayBuilding obs);

    /**
     * This method add an observer to the list of observers
     * @param obs The observer to add.
     */
    public void addObserver(ObserverDisplayStats obs);   
    
    /**
     * This method remove an observer from the list of observers
     * @param obs The observer to remove.
     */
    public void deleteObserver(ObserverDisplayBuilding obs);

    /**
     * This method remove an observer from the list of observers
     * @param obs The observer to remove.
     */
    public void deleteObserver(ObserverDisplayStats obs);
    
    /**
     * Notify a change of the number of people in the building.
     * @param nbOfPeopleInBuilding The number of persons in the building
     */
    public void notifyBuilding(int nbOfPeopleInBuilding);

    /**
     * Notify a change of the number of people waiting an elevator
     * @param nbOfPeopleWaitingAnElevator The number of persons waiting an elevator
     */
    public void notifyControllerElevator(int nbOfPeopleWaitingAnElevator);

    /**
     * Notify a change of the informations of an elevator
     * @param positionElevator The elevator's position on a floor
     * @param currentFloor The elevator's current floor
     * @param nbPassengers The number of passengers inside the elevator
     */
    public void notifyElevatorInfos(int positionElevator, int currentFloor, 
            int nbPassengers);

    /**
     * Notify a change of the informations of a floor
     * @param floor The floor number
     * @param nbPassengersHidden The number of hidden people at this floor
     * @param nbPassengersWalking The number of walking people at this floor
     * @param nbPassengersWaiting The number of waiting people at this floor
     */
    public void notifyFloor(int floor, int nbPassengersHidden, 
            int nbPassengersWalking, int nbPassengersWaiting);

    /**
     * Notify a change of the people's positions of a floor
     * @param floor The floor number
     * @param pos The list of people's positions
     * @param states The list of people's states
     */
    public void notifyFloorPositions(int floor, List<Integer> pos, 
            List<PassengerState> states);

    /**
     * Notify a change of the elevator's state
     * @param floor The elevator's floor
     * @param me The elevator's state
     * @param nbPeople The number of people inside the elevator
     */
    public void notifyElevatorState(int floor, MovementElevator me, int nbPeople);    
}
