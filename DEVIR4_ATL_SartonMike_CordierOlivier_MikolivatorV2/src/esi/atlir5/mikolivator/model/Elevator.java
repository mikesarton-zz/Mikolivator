package esi.atlir5.mikolivator.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class Elevator {
    
    private int nbPersonsMax;
    private boolean isGoingUp;
    private boolean isMoving;
    private int currentFloor;
    private List<Integer> destinations;
    private List<Person> passengers;
    private Building building;
    
    public Elevator (int nb_persons_max, int current_floor, Building b){
        nbPersonsMax = nb_persons_max;
        currentFloor = current_floor;
        isGoingUp = false;
        isMoving = false;
        destinations = new ArrayList<>();
        passengers = new ArrayList<>();
        building = b;
    }
    
    //  getters
    public int getCurrentFloor(){
        return currentFloor;
    }
    
    public boolean isGoingUp(){
        return isGoingUp;
    }
    
    public boolean isMoving(){
        return isMoving;
    }
    
    //  setters
    public void setCurrentFloor (int floor){
        currentFloor = floor;
    }
    
    public boolean isFreePlace(){
        return passengers.size() < nbPersonsMax;
    }
    
    public void addDestination (int floor){
        destinations.add(floor);
    }
    
    public void addPassenger (){
        while (isFreePlace() && 
                (building.getNumberPersonWaitingAtFloor(currentFloor) != 0)){
            if (isGoingUp) 
                passengers.add(building.removeFirstPersonWaitingGoingUp(currentFloor));
            else
                passengers.add(building.removeFirstPersonWaitingGoingDown(currentFloor));
        }
    }
    
    public void releasePeople(){
        for (int i=0; i<passengers.size(); ++i){
            if (passengers.get(i).getGoalFloor() == currentFloor){
                passengers.get(i).setCurrentFloor(currentFloor);
                passengers.remove(i);
            }
        }
    }
    
    public boolean isElevatorAtTheTop(){
        return currentFloor == building.getNumberOfFloors();
    }
    
    public boolean isElevatorAtTheBottom(){
        return currentFloor == 0;
    }
}
