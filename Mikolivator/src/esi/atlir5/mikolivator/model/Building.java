package esi.atlir5.mikolivator.model;

import esi.atlir5.mikolivator.events.BuildingListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe représente le building. Elle joue le rôle d'intermédiaire entre
 * la classe Simulation et les différents étages.
 *
 * @author Mike Sarton & Olivier Cordier
 */
class Building {

    private final List<Floor> floors;
    private final BuildingListener listener;
    private int numberOfPersonsInBuilding;

    Building(int numberFloors, BuildingListener listener) {
        floors = new ArrayList<>();
        this.listener = listener;
        createFloors(numberFloors);
        numberOfPersonsInBuilding = 0;
    }

    private void createFloors(int numberOfFloors) {
        for (int i = 0; i <= numberOfFloors; ++i) {
            Floor f = new Floor(i);
            floors.add(f);
        }
    }

    void addPersonInBuilding(Passenger p) {
        floors.get(0).addPersonAtFloor(p);
        ++numberOfPersonsInBuilding;
        listener.updateNumberPeopleInBuilding(numberOfPersonsInBuilding);
    }
    
    List<Floor> getFloors() {
        return floors;
    }

    void addPersonAtFloor(Passenger p, int floor) {
        floors.get(floor).addPersonAtFloor(p);
    }

    void removePersonAtFloor(Passenger p, int floor) {
        floors.get(floor).removePersonAtFloor(p);
    }

    int getNumberOfFloors() {
        return floors.size();
    }

    int getNumberOfPersonsInBuilding() {
        return numberOfPersonsInBuilding;
    }
    
    List<Integer> getNbPeopleTotalFromFloors() {
        List<Integer> list = new ArrayList<>();
        for (Floor f : floors) {
            list.add(f.getNbPeopleTotal());
        }
        return list;
    }

    void evacuationFloor(int floor) {
        floors.get(floor).evacuationFloor();
    }

    void openFloor(int floor) {
        floors.get(floor).setEvacuated(false);
    }
}
