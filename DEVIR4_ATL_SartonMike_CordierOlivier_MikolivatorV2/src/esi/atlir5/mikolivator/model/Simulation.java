package esi.atlir5.mikolivator.model;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class Simulation extends Thread {
    private final Building building;
    private ControllerElevator ctrlElevator;
    
    public Simulation (int nb_persons_max, int number_of_floors){
        building = new Building ();
        ctrlElevator = new ControllerElevator(nb_persons_max, 0, number_of_floors, 0);
    }
    
    private Passenger createPassenger(){
        return new Passenger(Functions.randomNumber(
                ctrlElevator.getLowestFloor(), ctrlElevator.getLastFloor()));
    }

    @Override
    public void run() {
        
    }
}
