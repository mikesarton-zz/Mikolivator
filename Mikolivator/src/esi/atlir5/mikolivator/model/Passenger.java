package esi.atlir5.mikolivator.model;

import esi.atlir5.mikolivator.events.PassengerListener;
import esi.atlir5.mikolivator.events.PassengerToFloorListener;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public abstract class Passenger {

    private PassengerState state;
    private int destinationFloor;
    private final Position position;
    private final int timeOfHide;
    private final PassengerListener listener;
    private PassengerToFloorListener floorListener;

    Passenger(int destination, int timeOfHide, PassengerListener listener) {
        destinationFloor = destination;
        this.timeOfHide = timeOfHide;
        position = new Position();
        state = PassengerState.WALKINGTOELEVATOR;
        this.listener = listener;
        floorListener = null;
    }

    //  --------------- TOUS LES GETTERS        
    Position getPosition() {
        return position;
    }

    int getPlace() {
        return position.getPlace();
    }

    PassengerState getState() {
        return state;
    }

    //  ------------- TOUS LES SETTERS
    void setPlace(int place) {
        if ((position.getPlace() + place) < -1) {
            setState(PassengerState.HIDDEN);
            return;
        }
        position.setPlace(place);
    }

    private void setFloor(int floor) {
        position.setFloor(floor);
    }

    void setState(PassengerState state) {
        this.state = state;
        if (state != PassengerState.INELEVATOR){
            ptfChangedStateEvent();
        }
    }

    //  ----------------- METHODES D'ACTIONS
    @SuppressWarnings("empty-statement")
    void enterElevator() {
        listener.passengerLeftFloor(this, position.getFloor());
        setPlace(getPosition().getPlace() + 1);
        setState(PassengerState.INELEVATOR);
    }

    @SuppressWarnings("empty-statement")
    boolean leaveElevator() {
        setPlace(getPosition().getPlace() - 1);
        setFloor(destinationFloor);
        listener.passengerEnterFloor(this, position.getFloor());
        if (floorListener.isEvacuated() && (getCurrentFloor() != 0)) {
            listener.passengerLeftFloor(this, position.getFloor());
            setPlace(getPosition().getPlace() + 1);
            destinationFloor = 0;
            return false;
        } else {
            setState(PassengerState.WALKINGTOCORRIDOR);
            return true;
        }
    }

    void callElevator() {
        setState(PassengerState.WAITINGELEVATOR);
        needSomething(PassengerNeeds.NEEDELEVATOR);
    }

    void evacuate() {
        if ((position.getFloor() == 0) && 
                ((state == PassengerState.WALKINGTOELEVATOR) || 
                (state == PassengerState.WAITINGELEVATOR))) {
            setState(PassengerState.WALKINGTOCORRIDOR);
        } else if ((position.getFloor() == 0) && 
                ((state == PassengerState.HIDDEN) || 
                (state == PassengerState.WALKINGTOCORRIDOR))) {
        } else if ((state == PassengerState.WAITINGELEVATOR) && 
                (position.getFloor() != 0)) {
            destinationFloor = 0;
        } else {
            setState(PassengerState.WALKINGTOELEVATOR);
            destinationFloor = 0;
        }
    }

    abstract void makeUTurn();

    abstract void walk();

    void setDestinationFloor(int destination) {
        if (state != PassengerState.HIDDEN) {
            return;
        }

        if (destination == getCurrentFloor()) {
            return;
        }
        
        if (floorListener.isEvacuated()) {
            return;
        }

        try {
            destinationFloor = destination;
            Thread.sleep(timeOfHide * 1000L);
            setState(PassengerState.WALKINGTOELEVATOR);
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
    }

    int getCurrentFloor() {
        return position.getFloor();
    }

    void needSomething(PassengerNeeds pn) {
        switch (pn) {
            case NEEDDESTINATION:
                listener.passengerNeedsNewDestination(this);
                break;
            case NEEDELEVATOR:
                listener.passengerNeedsElevator(this, position.getPlace() + 1);
                break;
        }
    }

    int getDestinationFloor() {
        return destinationFloor;
    }

    //  ----------------- METHODES DE PASSENGER TO FLOOR
    void floorAddListener(PassengerToFloorListener l) {
        floorListener = l;
        ptfChangedStateEvent();
    }

    void floorDeleteListener() {
        floorListener = null;
    }

    private void ptfChangedStateEvent() {
        floorListener.passengerOnFloorHasChangedStated();
    }

    void ptfHasMoved() {
        if (floorListener != null) {
            floorListener.aPassengerHasMoved();
        }
    }
}
