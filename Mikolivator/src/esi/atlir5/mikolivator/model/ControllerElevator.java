package esi.atlir5.mikolivator.model;

import esi.atlir5.mikolivator.events.ControllerElevatorListener;
import esi.atlir5.mikolivator.events.ElevatorToControllerElevatorListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
class ControllerElevator implements ElevatorToControllerElevatorListener {

    private final List<Elevator> elevators;
    private List<Passenger> passengersWaitingAnElevator;
    private ControllerElevatorListener listener;
    private int nbPersonsMaxInElevator;

    ControllerElevator(int nb_persons_max, List<Integer> positions_elevators) {
        nbPersonsMaxInElevator = nb_persons_max;
        elevators = new ArrayList<>();
        passengersWaitingAnElevator = new ArrayList<>();
        listener = null;
        createElevators(nb_persons_max, positions_elevators);
    }

    private void createElevators(int nb_persons_max, List<Integer> positions_elevators) {
        for (int i = 0; i < positions_elevators.size(); ++i) {
            SimpleElevator se = new SimpleElevator(nb_persons_max, 0, 
                    positions_elevators.get(i), 0.5);
            se.addControllerListener(this);
            Thread t = new Thread(se);
            elevators.add(se);
            t.start();
        }
    }

    List<Elevator> getElevators() {
        return elevators;
    }

    int getNbPersonsMaxInElevator() {
        return nbPersonsMaxInElevator;
    }        

    void someoneCalledAnElevator(Passenger p, int positionElevator) {
        for (int i = 0; i < elevators.size(); ++i) {
            if (elevators.get(i).getElevatorPosition() == positionElevator) {
                elevators.get(i).addDestination(p.getCurrentFloor());
                passengersWaitingAnElevator.add(p);
                updatePassengersWaitingElevator();
            }
        }
    }

    private void updatePassengersWaitingElevator() {
        listener.updatePassengersWaitingElevator(passengersWaitingAnElevator.size());
    }

    //  ---------- METHODES POUR AJOUTER LA SIMULATION COMME LISTENER
    void addListener(ControllerElevatorListener l) {
        listener = l;
    }

    //  ---------- METHODES D'UN ELEVATOR TO CONTROLLER ELEVATOR LISTENER
    @Override
    public synchronized void embark(int floor, Elevator e) {
        for (int i = 0; i < passengersWaitingAnElevator.size(); ++i) {
            if (passengersWaitingAnElevator.get(i).getCurrentFloor() == floor) {
                if (e.addPassenger(passengersWaitingAnElevator.get(i))) {
                    passengersWaitingAnElevator.remove(i);
                    updatePassengersWaitingElevator();
                }
            }
        }
    }
}
