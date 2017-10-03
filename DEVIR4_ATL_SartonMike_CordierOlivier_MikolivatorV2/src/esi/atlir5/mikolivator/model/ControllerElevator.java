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

    public void addDestination(int floor) {
        destinations.add(floor);
    }

    int getNextDestination(MovementElevator move) {
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
        return 0;
    }

    int getLastFloor() {
        return elevator.getLastFloor();
    }

    int getLowestFloor() {
        return elevator.getLowestFloor();
    }

    @Override
    public void move() {
        System.out.println("L'ascenseur va descendre.");
        goingUp();
//        goingDown();
        System.out.println("L'ascenseur est arrivé. -- move()");
        //  la méthode bug car aucune destination n'est ajoutée dans "destinations".
        //  il faut maintenant le faire proprement en ajoutant un passager avant de faire
        //  bouger l'ascenseur.
    }

    @Override
    public void goingUp() {
        int destination = getNextDestination(MovementElevator.UP);
//        int destination = 10;
        int sleepTime = destination - elevator.getCurrentFloor();
        elevator.setMovement(MovementElevator.UP);
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (elevator.getCurrentFloor() + 1 == destination) cancel();
                elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
                System.out.println("Etage elevator: " + elevator.getCurrentFloor());
            }
        }, new Date(), 1000);
        try {
            Thread.sleep(sleepTime * 1000);
        } catch (InterruptedException ex) {
            System.out.println("Erreur - ControllerElevator - goingUp(): " + ex.getMessage());
        }
    }

    @Override
    public void goingDown() {
        int destination = getNextDestination(MovementElevator.DOWN);
        int sleepTime = Math.abs(destination - elevator.getCurrentFloor());
        elevator.setMovement(MovementElevator.DOWN);
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (elevator.getCurrentFloor() - 1 == destination) cancel();
                elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
                System.out.println("Etage elevator: " + elevator.getCurrentFloor());
            }
        }, new Date(), 1000);
        try {
            Thread.sleep(sleepTime * 1000);
        } catch (InterruptedException ex) {
            System.out.println("Erreur - ControllerElevator - goingDown(): " + ex.getMessage());
        }
    }

    @Override
    public boolean addPassenger(Passenger p) {
        if (!elevator.isFreePlace()) return false;
        p.setInElevator(true);
        p.getPosition().setPlace(11);
        addDestination(p.getDestinationFloor());
        return true;
    }

    @Override
    public void releasePassenger() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void main(String[] args) {
        ControllerElevator ce = new ControllerElevator(5, 0, 6, 0);
        ce.move();
    }
}
