package esi.atlir5.mikolivator.model;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
class SimpleElevator extends Elevator implements Runnable {
    private final double speed;

    SimpleElevator(int nb_persons_max, int current_floor, int position, double speed) {
        super(nb_persons_max, current_floor, position, speed);        
        this.speed = speed;
    }

    private void tryToEmbark(){
        if (!isFreePlace()) return;
        needToEmbark();
    }
    
    //  ------------------ TOUTES LES METHODES D'ACTION

    @Override
    void goingUp(int destination) {
        int sleepTime = destination - getCurrentFloor();  
        setMovement(MovementElevator.UP);  

        Timer t = new Timer();  
        t.schedule(new TimerTask() {    
            @Override
            public void run() {
                if (getCurrentFloor() + 1 == destination) {   
                    cancel();
                }
                setCurrentFloor(getCurrentFloor() + 1);  
                updateElevatorInfos();  
            }
        }, new Date(), (long) (speed*1000));
        endMove(sleepTime); 
    }

    @Override
    void goingDown(int destination) {
        int sleepTime = Math.abs(destination - getCurrentFloor());
        setMovement(MovementElevator.DOWN);

        Timer t = new Timer(); 

        t.schedule(new TimerTask() { 
            @Override
            public void run() {
                if (getCurrentFloor() - 1 == destination) {   
                    cancel();
                }
                setCurrentFloor(getCurrentFloor() - 1); 
                updateElevatorInfos(); 
            }
        }, new Date(), (long) (speed*1000));
        endMove(sleepTime); 
    }

    @Override
    int pickDestination(){
        return getDestinations().remove(0);
    }
    
    @Override
    void move() {
        while (!getDestinations().isEmpty()) {  
            int destination = pickDestination();  

            if (destination > getCurrentFloor()) {
                goingUp(destination);   
            } else if (destination < getCurrentFloor()) {
                goingDown(destination); 
            }
            tryToEmbark(); 
            try {
                Thread.sleep((long) (1000));
                tryToEmbark();
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    @Override
    public void run() {
        while(true) {
            move();
            tryToEmbark();
        }
    }
}
