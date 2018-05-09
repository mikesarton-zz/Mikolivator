package esi.atlir5.mikolivator.model;

import esi.atlir5.mikolivator.events.PassengerListener;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
class NormalPerson extends Passenger implements Runnable {

    private final List<Integer> elevatorsPositions;
    private final int speedOfWalk;

    NormalPerson(int destination, int timeOfHide, int speed,
            List<Integer> elevators_positions, PassengerListener listener) {
        super(destination, timeOfHide, listener);
        elevatorsPositions = elevators_positions;
        speedOfWalk = speed;
    }

    //  faire demi-tour... à implémenter.
    @Override
    void makeUTurn() {

    }
    
    @Override
    void walk() {
        setPlace(getPosition().getPlace() + getState().getValue());
        if (getPosition().getPlace() == -1) { 
            setState(PassengerState.HIDDEN); 
        }
        if (getPosition().getPlace() == (elevatorsPositions.get(0) - 1)) { 
            callElevator(); 
        }
        ptfHasMoved();
    }

    @Override
    public void run() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                switch (getState()) {
                    case WALKINGTOELEVATOR:
                    case WALKINGTOCORRIDOR:
                        walk();
                        break;
                    case HIDDEN: 
                        needSomething(PassengerNeeds.NEEDDESTINATION);
                        break;
                    default:
                }
            }
        }, new Date(), speedOfWalk);
    }
}
