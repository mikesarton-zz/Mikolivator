package esi.atlir5.mikolivator.model;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class Person {

    private int currentFloor;
    private int goalFloor;
    private Elevator elevator;
    private boolean isWaiting;
    private int position; //  la position de la personne dans l'étage
    private final int id;

    public Person(int id, int current_floor, int goal_floor) {
        this.id = id;
        position = 10;
        currentFloor = current_floor;
        goalFloor = goal_floor;
        elevator = null;
        isWaiting = false;
    }

    // getters
    public Elevator getElevator() {
        return elevator;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getGoalFloor() {
        return goalFloor;
    }
    
    public boolean isGoingUp(){
        return goalFloor - currentFloor > 0;
    }

    public int getID() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public boolean getIsWaiting(int floor) {
        return (currentFloor == floor) && isWaiting;
    }

    //  setters
    public void setElevator(Elevator el) {
        elevator = el;
    }

    public void setCurrentFloor(int current_floor) {
        currentFloor = current_floor;
    }

    public void setPosition(int pos) {
        position = pos;
    }
    
    public void setIsWaiting(boolean wait){
        isWaiting = wait;
    }

    public boolean isInElevator() {
        return elevator != null;
    }
}
