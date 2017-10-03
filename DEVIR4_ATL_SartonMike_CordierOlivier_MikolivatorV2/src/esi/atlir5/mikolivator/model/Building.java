package esi.atlir5.mikolivator.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class Building {

    private List<Passenger> people;
    private List<Passenger> waitingPeople;

    public Building() {
        people = new ArrayList<>();
        waitingPeople = new ArrayList<>();
    }

    //  getters    
    public List<Passenger> getPeople(){
        return people;
    }
    
    public List<Passenger> getPeopleWaitingAtFloor (int floor){
        List<Passenger> list = new ArrayList<>();
        for (int i=0; i<waitingPeople.size(); ++i) {
            if (waitingPeople.get(i).getPosition().getFloor() == floor)
                list.add(waitingPeople.get(i));
        }
        return list;
    }
    
    public int getNumberPersonWaitingAtFloor (int floor){
        return getPeopleWaitingAtFloor(floor).size();
    }
    
    public int getNumberPersonWaitingInBuilding(){
        return waitingPeople.size();
    }    
    
    public Passenger removeFirstPersonWaitingGoingUp(int floor){
        List <Passenger> list = getPeopleWaitingAtFloor(floor);
        for (int i=0; i<list.size(); ++i){
            if (list.get(i).isGoingUp()) return list.remove(i);
        }
        return null;
    }
    
    public Person removeFirstPersonWaitingGoingDown(int floor){
        List <Passenger> list = getPeopleWaitingAtFloor(floor);
        for (int i=0; i<list.size(); ++i){
            if (!list.get(i).isGoingUp()) return list.remove(i);
        }
        return null;
    }
    
    //  setters
    public void addPerson (Passenger p) {
        people.add(p);
    }
    
    public void addPersonWaiting (Passenger p){
        waitingPeople.add(p);
    }
    
    
}
