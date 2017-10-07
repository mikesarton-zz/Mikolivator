package esi.atlir5.mikolivator.model;

import esi.atlir5.mikolivator.observers.Observable;
import esi.atlir5.mikolivator.observers.Observer;
import esi.atlir5.mikolivator.view.PrintState;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class Simulation extends Thread implements Observable {

    private final ControllerElevator ctrlElevator;
    private final int minInterval;
    private final int maxInterval;
    private final List<Observer> observers;
    private List<Passenger> people;
    private List<Passenger> waitingPeople;
    private final List<Integer> elevatorsPositions;
    private final int nbPersonsMax;

    public Simulation(int nb_persons_max_in_building, 
            int nb_persons_max_in_elevator, int number_of_floors, 
            int min_interval_generate_passengers,
            int max_interval_generate_passengers, List<Integer> elevators_positions, PrintState ps) 
            throws MikolivatorException {
        
        if (number_of_floors > 10) throw new MikolivatorException("10 étages maximum.");
        
        ctrlElevator = new ControllerElevator(nb_persons_max_in_elevator, 0, 
                number_of_floors, 0, 11);
        ctrlElevator.addObserver(ps);
        Thread thread = new Thread (ctrlElevator);  //  à retirer si comportement suspect.
        thread.start();
        minInterval = min_interval_generate_passengers;
        maxInterval = max_interval_generate_passengers;
        nbPersonsMax = nb_persons_max_in_building;
        elevatorsPositions = elevators_positions;
        observers = new ArrayList<>();
        people = new ArrayList<>();
        waitingPeople = new ArrayList<>();
        start();
    }

    @Override
    public void run() {
        generatePassenger(minInterval*1000, maxInterval*1000);
        while (true) {
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
        notifyObs(104);
        notifyObs(p.getPosition().getFloor());
    }
    
    private synchronized void lookForWaitingPeople() {
        people.stream().filter((p) -> (p.isWaiting() && !waitingPeople.contains(p))).forEachOrdered((p) -> {
            addPersonWaiting(p);
//            System.out.println(waitingPeople.size() + " personnes attendent l'ascenseur.");
            ctrlElevator.addDestination(p.getPosition().getFloor());
            notifyObs(p.getPosition().getFloor());
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
//                    System.out.println("L'ascenseur est complet... départ");
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
//        System.out.println(cpt + " personnes sont montées dans l'ascenseur.");
        notifyObs(104);
        ctrlElevator.move();
    }
    
    private void addPersonWaiting (Passenger p){
        waitingPeople.add(p);
        notifyObs(104);
        notifyObs(p.getPosition().getFloor());
    }
    
    public int getNumberOfPeople() {
        return people.size();
    }
    
    public int getNumberOfWaitingPeople() {
        return waitingPeople.size();
    }
    
    public int getCurrentFloorElevator() {
        return ctrlElevator.getCurrentFloor();
    }
    
    public int getNumberPassengersInElevator() {
        return ctrlElevator.getNumberOfPassengersInElevator();
    }
    
    public int getNumberPassengerWaitingAtFloor(int floor) {
        int cpt = 0;
        for (Passenger p : waitingPeople) {
            if (p.getPosition().getFloor() == floor) ++cpt;
        }
        return cpt;
    }
    
    public int getNumberPassengerWalkingAtFloor (int floor) {
        int cpt = 0;
        for (Passenger p : people) {
            if ((p.getPosition().getFloor() == floor) && (!p.isHidden()) && (!p.isWaiting())) ++cpt;
        }
        return cpt;
    }
    
    public int getNumberPassengerHiddenAtFloor (int floor) {
        int cpt = 0;
        for (Passenger p : people) {
            if ((p.getPosition().getFloor() == floor) && (p.isHidden())) ++cpt;
        }
        return cpt;
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
