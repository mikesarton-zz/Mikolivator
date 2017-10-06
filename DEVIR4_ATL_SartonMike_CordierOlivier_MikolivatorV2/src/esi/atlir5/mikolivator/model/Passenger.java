package esi.atlir5.mikolivator.model;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
class Passenger extends PassengerBehavior implements Runnable{
    
    
    Passenger (int destination, List<Integer> elevators_positions) {
        super (destination, elevators_positions);
    }

    @Override
    void walk() {
        switch (movement) {
            case TOELEVATOR : position.setPlace(position.getPlace() + 1);
                break;
            case TOCORRIDOR : position.setPlace(position.getPlace() - 1);
                break;
        }
        
        System.out.println("Etage: " + position.getFloor() + " Position: " + position.getPlace() + " Destination: " + getDestinationFloor());
        
        try {
            callElevator();
        } catch (MikolivatorException e) {
//            System.out.println(e.getMessage());
        }
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

    
}
