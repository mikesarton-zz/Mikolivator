package esi.atlir5.mikolivator.model;

import java.util.List;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
abstract class PassengerBehavior {
    
    private int destinationFloor;
    Position position;
    boolean isInElevator;
    boolean isWaiting;
    boolean isHidden;
    MovementPassenger movement;
    private final List<Integer> elevatorsPositions;

    PassengerBehavior(int destination, List<Integer> elevators_positions) {
        destinationFloor = destination;
        position = new Position();
        isInElevator = false;
        isWaiting = false;
        elevatorsPositions = elevators_positions;
        movement = MovementPassenger.TOELEVATOR;
    }        
    
    int getDestinationFloor() {
        return destinationFloor;
    }

    void setDestinationFloor(int destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    public MovementPassenger getMovement() {
        return movement;
    }

    public void setMovement(MovementPassenger movement) {
        this.movement = movement;
    }        

    Position getPosition() {
        return position;
    }

    void setPosition(Position position) {
        this.position = position;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setIsHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }
            
    boolean isGoingUp(){
        return destinationFloor > position.getFloor();
    }
    
    boolean isWaiting() {
        return isWaiting;
    }
    
    boolean isInElevator() {
        return isInElevator;
    }    

    void callElevator() throws MikolivatorException {
        boolean wellPlaced = false;
        int index = 0;
        while (index<elevatorsPositions.size() && !wellPlaced) {
            wellPlaced = position.getPlace() == (elevatorsPositions.get(index) - 1);
            ++index;
        }
        if (!wellPlaced) {
            throw new MikolivatorException("Impossible d'appeler l'ascenseur "
                + "si on est pas devant.");
        }
        
        isWaiting = true;
    }

    void enterElevator(int placeElevator) {
        position.setPlace(placeElevator);
        isInElevator = true;
        isWaiting = false;
    }

    void leaveElevator(int placeElevator) {
        position.setFloor(destinationFloor);
        position.setPlace(placeElevator);
        isInElevator = false;
        movement = MovementPassenger.TOCORRIDOR;
    }

    void makeUTurn() {
        
    }
    
    abstract void walk();
}
