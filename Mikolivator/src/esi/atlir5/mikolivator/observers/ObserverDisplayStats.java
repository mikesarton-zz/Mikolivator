
package esi.atlir5.mikolivator.observers;

/**
 * This interface is used when the Simulation updates stats's view
 * @author Mike Sarton & Olivier Cordier
 */
public interface ObserverDisplayStats {
    
    /**
     * This method will get the important informations of the observable
     * and update the graphical interface with these informations.
     * @param nbOfPeopleInBuilding
     */
    public void updateBuilding(int nbOfPeopleInBuilding);

    /**
     * This method updates the number of persons waiting an elevator
     * @param nbOfPeopleWaitingAnElevator Number of persons waiting an elevator
     */
    public void updateControllerElevator (int nbOfPeopleWaitingAnElevator);

    /**
     * This method updates the elevator's stats
     * @param positionElevator The elevator's position
     * @param currentFloor The elevator's current position
     * @param nbPassengers The number of persons in the elevator
     */
    public void updateElevatorInfos(int positionElevator, int currentFloor, 
            int nbPassengers);

    /**
     * This method updates the informations of a floor
     * @param floor The floor's number
     * @param nbPassengersHidden Number of persons hidden on this floor
     * @param nbPassengersWalking Number of persons walking on this floor
     * @param nbPassengersWaiting Number of persons waiting on this floor
     */
    public void updateAFloor(int floor, int nbPassengersHidden, 
            int nbPassengersWalking, int nbPassengersWaiting);
}
