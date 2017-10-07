package esi.atlir5.mikolivator.model;

import esi.atlir5.mikolivator.observers.Observable;
import esi.atlir5.mikolivator.observers.Observer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class Simulation extends Thread implements Observable {

//    private final Building building;
    private final ControllerElevator ctrlElevator;
    private final int minInterval;
    private final int maxInterval;
    private final List<Observer> observers;
    private List<Passenger> people;
    private List<Passenger> waitingPeople;
    private final List<Integer> elevatorsPositions;
    private final int nbPersonsMax;
    private boolean isRunning;

    public Simulation(int nb_persons_max_in_building, 
            int nb_persons_max_in_elevator, int number_of_floors, 
            int min_interval_generate_passengers,
            int max_interval_generate_passengers, List<Integer> elevators_positions) {
        
//        building = new Building(nb_persons_max_in_building);
        ctrlElevator = new ControllerElevator(nb_persons_max_in_elevator, 0, 
                number_of_floors, 0, 11);
        Thread thread = new Thread (ctrlElevator);  //  à retirer si comportement suspect.
        thread.start();
        minInterval = min_interval_generate_passengers;
        maxInterval = max_interval_generate_passengers;
        observers = new ArrayList<>();
        people = new ArrayList<>();
        waitingPeople = new ArrayList<>();
        nbPersonsMax = nb_persons_max_in_building;
        elevatorsPositions = elevators_positions;
        isRunning = true;
    }

    @Override
    public void run() {
        generatePassenger(minInterval*1000, maxInterval*1000);
        while (isRunning) {
            lookForWaitingPeople();
            enterPeople();            
        }
    }    

    private void generatePassenger(int min, int max) {
        int delay = min + (int) (Math.random() * (max - min + 1));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                generatePassenger(minInterval*1000, maxInterval*1000);
            }
        }, delay);
        addPerson(new Passenger(Functions.randomNumber(
                ctrlElevator.getLowestFloor()+1, ctrlElevator.getLastFloor()), elevatorsPositions));
    }
    
    private synchronized void addPerson (Passenger p) {
        if (people.size() == nbPersonsMax) return;
        people.add(p);
        Thread thread = new Thread (p);
        thread.start();
//        notifyObs();
    }
    
    private synchronized void lookForWaitingPeople() {
        people.stream().filter((p) -> (p.isWaiting() && !waitingPeople.contains(p))).forEachOrdered((p) -> {
            addPersonWaiting(p);
            System.out.println(waitingPeople.size() + " personnes attendent l'ascenseur.");
            ctrlElevator.addDestination(p.getPosition().getFloor());
//            notifyObs();
        });
    }
    
    private void enterPeople() {
        if (waitingPeople.isEmpty() || (ctrlElevator.getMovement() != MovementElevator.STANDBY)) return;
        
        int index = 0;
        int cpt = 0;
        
        while (index < waitingPeople.size()) {
            if (waitingPeople.get(index).getPosition().getFloor() == ctrlElevator.getCurrentFloor()) {
                if (!ctrlElevator.addPassenger(waitingPeople.get(index))) {
                    System.out.println("L'ascenseur est complet... départ");
                    ctrlElevator.move();
                    return;
                } else {
                    ++cpt;
                    waitingPeople.remove(index);
                    index = 0;                    
                }
            } else {
                ++index;                
            }
        }
        System.out.println(cpt + " personnes sont montées dans l'ascenseur.");
        ctrlElevator.move();
    }
    
    private void addPersonWaiting (Passenger p){
        waitingPeople.add(p);
    }
    
    public int getNumberOfPeople() {
        return people.size();
    }
    
    public int getNumberOfWaitingPeople() {
        return waitingPeople.size();
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
