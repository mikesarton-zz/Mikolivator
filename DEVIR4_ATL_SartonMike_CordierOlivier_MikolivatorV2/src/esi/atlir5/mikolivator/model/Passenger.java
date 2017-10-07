package esi.atlir5.mikolivator.model;

import esi.atlir5.mikolivator.observers.Observer;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
class Passenger extends PassengerBehavior implements Runnable {

    Passenger(int destination, List<Integer> elevators_positions) {
        super(destination, elevators_positions);
    }

    @Override
    void walk() {
        switch (movement) {
            case TOELEVATOR:
                position.setPlace(position.getPlace() + 1);
                try {
                    callElevator();
                } catch (MikolivatorException e) {
//            System.out.println(e.getMessage());
                }
                break;
            case TOCORRIDOR:
                if (isHidden) return;
                position.setPlace(position.getPlace() - 1);
                if(position.getPlace() == -1) {
                    isHidden = true;
                    notifyObs(position.getFloor());
                }
                break;
        }

//        System.out.println("Etage: " + position.getFloor() + " Position: " + position.getPlace() + " Destination: " + getDestinationFloor());
    }

    @Override
    public void run() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isInElevator && !isWaiting) {
                    walk();
                }
            }
        }, new Date(), 1000);
    }

    @Override
    public void addObserver(Observer obs) {
        if (!observers.contains(obs)) {
            observers.add(obs);
        }
    }

    @Override
    public void deleteObserver(Observer obs) {
        if (observers.contains(obs)) {
            observers.remove(obs);
        }
    }

    @Override
    public void notifyObs(int view) {
        observers.forEach((obs) -> {
            obs.update(view);
        });
    }

}
