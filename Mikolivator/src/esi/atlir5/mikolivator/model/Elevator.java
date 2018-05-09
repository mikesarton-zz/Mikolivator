package esi.atlir5.mikolivator.model;

import esi.atlir5.mikolivator.events.ElevatorListener;
import esi.atlir5.mikolivator.events.ElevatorToControllerElevatorListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public abstract class Elevator {

    private final int nbPersonsMax;
    private int currentFloor;  
    private final double speed;
    private MovementElevator movement;  
    private List<Integer> destinations; 
    private List<Passenger> passengers; 
    private final int elevatorPosition; 
    private ElevatorListener listener;   
    private ElevatorToControllerElevatorListener controller;  
    
    Elevator(int nb_persons_max, int current_floor, int position, double speed) {
        nbPersonsMax = nb_persons_max;
        this.speed = speed;
        currentFloor = current_floor;
        movement = MovementElevator.STANDBY;
        elevatorPosition = position;
        destinations = new ArrayList<>();
        passengers = new ArrayList<>();
        listener = null;
        controller = null;
    }

    //  ------------- TOUS LES GETTERS
    int getCurrentFloor() {
        return currentFloor;
    }

    List<Integer> getDestinations() {
        return destinations;
    }

    int getElevatorPosition() {
        return elevatorPosition;
    }

    //  ----------------- TOUS LES SETTERS
    void setCurrentFloor(int floor) {
        currentFloor = floor;
    }

    void setMovement(MovementElevator mv) {
        movement = mv;
        updateElevatorState();
    }

    void addDestination(int floor) {
        if (destinations.contains(floor)) { 
            return;
        }
        destinations.add(floor);
    }

    //  ----------------- AUTRES
    boolean isFreePlace() {
        return passengers.size() < nbPersonsMax;
    }

    void endMove(int sleepTime) {
        try { 
            Thread.sleep((long) (sleepTime * speed * 1000L));
            setMovement(MovementElevator.STANDBY); 
            releasePassenger();  
            updateElevatorState();
        } catch (InterruptedException ex) {}
    }

    //  ------------------ TOUTES LES METHODES D'ACTION
    abstract void move();

    abstract void goingUp(int destination);

    abstract void goingDown(int destination);

    abstract int pickDestination();

    boolean addPassenger(Passenger p) {
        if (passengers.contains(p) || !isFreePlace()) {
            return false;
        }
        addDestination(p.getDestinationFloor());
        p.enterElevator();
        passengers.add(p);
        updateElevatorState();
        updateElevatorInfos();
        return true;
    }

    private void releasePassenger() {
        int index = 0;

        while (index < passengers.size()) {
            if (passengers.get(index).getDestinationFloor() == currentFloor) {
                if (passengers.get(index).leaveElevator()) {
                    passengers.remove(index);
                    index = 0;
                    updateElevatorInfos();                    
                } else {
                    addDestination(0);
                }
            } else {
                ++index;
            }
        }
    }

    //  ----------------- METHODES D'EVENEMENTS
    void addListener(ElevatorListener l) {
        listener = l;
    }

    void deleteListener(ElevatorListener l) {
        listener = null;
    }
    
    void addControllerListener(ElevatorToControllerElevatorListener l) {
        controller = l;
    }
    
    void deleteControllerListener(ElevatorToControllerElevatorListener l) {
        controller = null;
    }

    void needToEmbark() {
        controller.embark(currentFloor, this);
    }

    void updateElevatorInfos() {
        listener.updateElevatorInfos(elevatorPosition, currentFloor, passengers.size());
    }
    
    private void updateElevatorState() {
        listener.updateElevatorState(currentFloor, movement, passengers.size());
    }
}
