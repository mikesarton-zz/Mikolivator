package esi.atlir5.mikolivator.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class ControllerElevator implements ElevatorBehavior {

    private Elevator elevator;
    private List<Integer> destinations;

    public ControllerElevator(int nb_persons_max, int current_floor, int last_floor,
            int lowest_floor) {
        elevator = new Elevator(nb_persons_max, current_floor, last_floor,
                lowest_floor);
        destinations = new ArrayList<>();
    }

    private void addDestination(int floor) throws MikolivatorException {
        if (floor > elevator.getLastFloor() || floor < elevator.getLowestFloor())
        {
            throw new MikolivatorException("Etage inexistant");
        }
        destinations.add(floor);
    }
    
    private void eraseDestination (int floor) {
        if (!destinations.contains(floor)) return;
        
        for (int i=0; i<destinations.size(); ++i) {
            if (destinations.get(i) == floor) destinations.remove(i);
        }
    }

    private int getNextDestination(MovementElevator move) throws MikolivatorException {
        for (int i = 0; i < destinations.size(); ++i) {
            switch (move) {
                case UP:
                    if (destinations.get(i) > elevator.getCurrentFloor()) {
                        return destinations.remove(i);
                    }
                case DOWN:
                    if (destinations.get(i) < elevator.getCurrentFloor()) {
                        return destinations.remove(i);
                    }
            }
        }
        throw new MikolivatorException("Aucun étage correspondant");
    }

    int getLastFloor() {
        return elevator.getLastFloor();
    }

    int getLowestFloor() {
        return elevator.getLowestFloor();
    }

    @Override
    public void move() {        
        while (!destinations.isEmpty()) {
            if (destinations.get(0) > elevator.getCurrentFloor()) {
                goingUp();
            } else {
                goingDown();
            }
        }
    }

    @Override
    public void goingUp() {
        //  regarder si il existe des destinations
        if (destinations.isEmpty()) return;
        
        int destination;
        
        //  récupérer la prochaine destination montante
        try {
            destination = getNextDestination(MovementElevator.UP);
        } catch (MikolivatorException e) {
            System.out.println(e.getMessage());
            return;
        }

        //  définir le nombre de temps que cela va durer
        int sleepTime = destination - elevator.getCurrentFloor();
        
        elevator.setMovement(MovementElevator.UP);
        System.out.println("--- L'ascenseur va monter. ---");
        
        //  instancier le timer permettant le mouvement de l'ascenseur
        Timer t = new Timer();
        
        //  faire monter l'ascenseur
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (elevator.getCurrentFloor() + 1 == destination) cancel();
                elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
                System.out.println("Etage elevator: " + elevator.getCurrentFloor());
            }
        }, new Date(), 1000);
        
        //  bloquer le thread courant le temps que l'ascenseur monte
        try {
            Thread.sleep(sleepTime * 1000);
            System.out.println("Ascenseur bloqué.");
        } catch (InterruptedException ex) {
            System.out.println("Erreur - ControllerElevator - goingUp(): " 
                    + ex.getMessage());
        }
    }

    @Override
    public void goingDown() {
        //  regarder si il existe des destinations
        if (destinations.isEmpty()) return;
        
        int destination;
        
        //  récupérer la prochaine destination montante
        try {
            destination = getNextDestination(MovementElevator.UP);
        } catch (MikolivatorException e) {
            System.out.println(e.getMessage());
            return;
        }
        
        //  définir le temps que l'ascenseur va monter
        int sleepTime = Math.abs(destination - elevator.getCurrentFloor());
        
        elevator.setMovement(MovementElevator.DOWN);
        System.out.println("--- L'ascenseur va descendre. ---");
        
        //  instancier le timer permettant à l'ascenseur de monter
        Timer t = new Timer();
        
        //  faire monter l'ascenseur
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (elevator.getCurrentFloor() - 1 == destination) cancel();
                elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
                System.out.println("Etage elevator: " + elevator.getCurrentFloor());
            }
        }, new Date(), 1000);
        
        //  bloquer le thread courant le temps que l'ascenseur monte
        try {
            Thread.sleep(sleepTime * 1000);
            System.out.println("Ascenseur bloqué.");
        } catch (InterruptedException ex) {
            System.out.println("Erreur - ControllerElevator - goingDown(): " 
                    + ex.getMessage());
        }        
    }

    @Override
    public boolean addPassenger(Passenger p) {
        if (!elevator.isFreePlace()) return false;        
        try {
            addDestination(p.getDestinationFloor());
        } catch (MikolivatorException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        p.setInElevator(true);
        p.getPosition().setPlace(11);
        elevator.addOnePerson();
        return true;
    }

    @Override
    public void releasePassenger(Passenger p) {
        p.setInElevator(false);
        p.getPosition().setPlace(10);
        elevator.releaseOnePerson();
    }

    public static void main(String[] args) {
        Passenger p = new Passenger(3);
        Passenger p2 = new Passenger (8);
        Passenger p3 = new Passenger (1);
        
        ControllerElevator ce = new ControllerElevator(5, 0, 10, 0);
        ce.addPassenger(p);
        ce.addPassenger(p2);
        ce.addPassenger(p3);
        ce.move();
    }
}
