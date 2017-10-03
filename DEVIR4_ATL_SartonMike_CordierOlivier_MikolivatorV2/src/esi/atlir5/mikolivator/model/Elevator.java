package esi.atlir5.mikolivator.model;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class Elevator {
    
    private final int nbPersonsMax;
    private int nbCurrentPersons;
    private final int lastFloor;
    private final int lowestFloor;
    private MovementElevator movement;
    private int currentFloor;
    
    public Elevator (int nb_persons_max, int current_floor, int last_floor, 
            int lowest_floor){
        nbPersonsMax = nb_persons_max;
        currentFloor = current_floor;
        lastFloor = last_floor;
        lowestFloor = lowest_floor;
        movement = MovementElevator.STANDBY;
        nbCurrentPersons = 0;
    }
    
    //  getters
    public int getCurrentFloor(){
        return currentFloor;
    }
    
    public MovementElevator getMovement(){
        return movement;
    }

    int getLastFloor() {
        return lastFloor;
    }

    int getLowestFloor() {
        return lowestFloor;
    }
    
    //  setters
    public void setCurrentFloor (int floor){
        currentFloor = floor;
    }
    
    void setMovement (MovementElevator mv) {
        movement = mv;
    }
    
    public boolean isFreePlace(){
        return nbCurrentPersons < nbPersonsMax;
    }
    
    void addOnePerson(){
        ++nbCurrentPersons;
    }
    
    void releaseOnePerson(){
        --nbCurrentPersons;
    }
}
