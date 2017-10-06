package esi.atlir5.mikolivator.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
class Building {

    private List<Passenger> people;
    private List<Passenger> waitingPeople;
    private final int nbPersonsMax;

    Building(int nb_persons_max_in_building) {
        people = new ArrayList<>();
        waitingPeople = new ArrayList<>();
        nbPersonsMax = nb_persons_max_in_building;
    }

    //  getters    
    List<Passenger> getPeople(){
        return people;
    }
    
    List<Passenger> getPeopleWaitingAtFloor (int floor){
        List<Passenger> list = new ArrayList<>();
        for (int i=0; i<waitingPeople.size(); ++i) {
            if (waitingPeople.get(i).getPosition().getFloor() == floor)
                list.add(waitingPeople.get(i));
        }
        return list;
    }
    
    int getNumberPersonWaitingAtFloor (int floor){
        return getPeopleWaitingAtFloor(floor).size();
    }
    
    int getNumberPersonWaitingInBuilding(){
        return waitingPeople.size();
    }    
    
    Passenger removeFirstPersonWaitingGoingUp(int floor){
        List <Passenger> list = getPeopleWaitingAtFloor(floor);
        for (int i=0; i<list.size(); ++i){
            if (list.get(i).isGoingUp()) return list.remove(i);
        }
        return null;
    }
    
    Passenger removeFirstPersonWaitingGoingDown(int floor){
        List <Passenger> list = getPeopleWaitingAtFloor(floor);
        for (int i=0; i<list.size(); ++i){
            if (!list.get(i).isGoingUp()) return list.remove(i);
        }
        return null;
    }
    
    //  setters
    void addPerson (Passenger p) {
        if (people.size() == nbPersonsMax) return;
        people.add(p);
    }
    
    void addPersonWaiting (Passenger p){
        waitingPeople.add(p);
    }
}