package esi.atlir5.mikolivator.model;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class Passenger extends Person {
    private int destinationFloor;
    private Position position;
    private boolean isInElevator;
    
    public Passenger (int destination) {
        destinationFloor = destination;
        position = new Position();
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public void setDestinationFloor(int destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    
    boolean isGoingUp(){
        return destinationFloor > position.getFloor();
    }
    
    boolean isInElevator() {
        return isInElevator;
    }
    
    void setInElevator(boolean b) {
        isInElevator = b;
    }
}
