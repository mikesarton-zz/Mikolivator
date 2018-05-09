package esi.atlir5.mikolivator.model;

import esi.atlir5.mikolivator.events.FloorListener;
import esi.atlir5.mikolivator.events.PassengerToFloorListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe représente un étage du building.
 *
 * @author Mike Sarton & Olivier Cordier
 */
class Floor implements PassengerToFloorListener {

    private List<Passenger> peopleAtFloor;
    private FloorListener listener;
    private final int floorNumber;
    private boolean isEvacuated;
    private int nbPeopleTotal;

    Floor(int number) {
        isEvacuated = false;
        floorNumber = number;
        peopleAtFloor = new ArrayList<>();
        listener = null;
        nbPeopleTotal = 0;
    }

    synchronized void addPersonAtFloor(Passenger p) {
        p.floorAddListener(this);
        peopleAtFloor.add(p);
        ++nbPeopleTotal;
        updateFloorPositionsPassenger();
        updateFloorStats();
    }

    synchronized void removePersonAtFloor(Passenger p) {
        p.floorDeleteListener();
        peopleAtFloor.remove(p);
        updateFloorPositionsPassenger();
        updateFloorStats();
    }

    void evacuationFloor() {
        setEvacuated(true);
        listener.updateNewEvacuationRecord(peopleAtFloor.size(), floorNumber);
        peopleAtFloor.forEach((p) -> {
            p.evacuate();
        });
    }

    void setEvacuated(boolean evacuated) {
        isEvacuated = evacuated;
    }
    
    synchronized int getNbPeopleTotal() {
        return nbPeopleTotal;
    }

    //  ---------- METHODES D'UN FLOOR A LA SIMULATION (LISTENER)
    void addListener(FloorListener l) {
        listener = l;
    }
    
    private synchronized void updateFloorStats() {
        int nbHidden = 0;
        int nbWalking = 0;
        int nbWaiting = 0;

        for (Passenger p : peopleAtFloor) {
            switch (p.getState()) {
                case WAITINGELEVATOR:
                    ++nbWaiting;
                    break;
                case WALKINGTOCORRIDOR:
                case WALKINGTOELEVATOR:
                    ++nbWalking;
                    break;
                case HIDDEN:
                    ++nbHidden;
                    break;
                default:
            }
        }

        listener.updateFloorStats(floorNumber, nbHidden, nbWalking,
                nbWaiting);
    }

    private synchronized void updateFloorPositionsPassenger() {
        List<Integer> positions = new ArrayList<>();
        List<PassengerState> states = new ArrayList<>();
        for (Passenger p : peopleAtFloor) {
            if ((p.getState() != PassengerState.HIDDEN) && (p.getState() 
                    != PassengerState.INELEVATOR)) {
                if (p.getPlace() >= 0 && p.getPlace() < 25) {
                    positions.add(p.getPlace());
                    states.add(p.getState());
                }
            }
        }

        listener.updateFloorPositionsPassengers(floorNumber, positions, states);
    }

    //  ---------- METHODES D'UN PASSENGER AU FLOOR
    @Override
    public void passengerOnFloorHasChangedStated() {
        updateFloorStats();
    }

    @Override
    public void aPassengerHasMoved() {
        updateFloorPositionsPassenger();
    }

    @Override
    public boolean isEvacuated() {
        return isEvacuated;
    }

}
