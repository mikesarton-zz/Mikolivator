package esi.atlir5.mikolivator.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class Building {

    private int numberFloors;
    private Elevator elevator;
    private List<Person> people;
    private List<Person> waitingPeople;

    public Building(int number_floors, Elevator elevator, List<Person> people_list) {
        numberFloors = number_floors;
        this.elevator = elevator;
        people = people_list;
        waitingPeople = new ArrayList<>();
    }

    //  getters
    public Elevator getElevator() {
        return elevator;
    }
    
    public List<Person> getPeople(){
        return people;
    }
    
    public int getNumberOfFloors(){
        return numberFloors;
    }
    
    public List<Person> getPeopleWaitingAtFloor (int floor){
        List<Person> list = new ArrayList<>();
        for (Person p : people){
            if (p.getIsWaiting(floor)) list.add(p);
        }
        return list;
    }
    
    public int getNumberPersonWaitingAtFloor (int floor){
        return getPeopleWaitingAtFloor(floor).size();
    }
    
    public int getNumberPersonWaitingInBuilding(){
        return waitingPeople.size();
    }
    
//    public Person removeFirstPersonWaiting(int floor){
//        List<Person> list = getPeopleWaitingAtFloor(floor);
//        if (!list.isEmpty()) return list.remove(0);
//        return null;
//    }
    
    public Person removeFirstPersonWaitingGoingUp(int floor){
        for (int i=0; i<waitingPeople.size(); ++i){
            if ((waitingPeople.get(i).getCurrentFloor() == floor) 
                    && waitingPeople.get(i).isGoingUp())
                return waitingPeople.remove(i);
        }
        return null;
    }
    
    public Person removeFirstPersonWaitingGoingDown(int floor){
        for (int i=0; i<waitingPeople.size(); ++i){
            if ((waitingPeople.get(i).getCurrentFloor() == floor) 
                    && !waitingPeople.get(i).isGoingUp())
                return waitingPeople.remove(i);
        }
        return null;
    }
    
    //  setters
    public void setElevator(Elevator el){
        elevator = el;
    }
    
    public void addPerson (Person p) {
        people.add(p);
    }
    
    public Elevator findElevatorByFloor (int floor){
        if (elevator.getCurrentFloor() == floor) return elevator;
        return null;
    }
    
    public void addPersonWaiting (Person p){
        waitingPeople.add(p);
    }
    
    
}
