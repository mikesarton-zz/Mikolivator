package esi.atlir5.mikolivator.model;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public interface ElevatorBehavior {
    public void move();
    public void goingUp();
    public void goingDown();
    public boolean addPassenger(Passenger p);
    public void releasePassenger(Passenger p);
}
