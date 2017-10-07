package esi.atlir5.mikolivator.model;

import esi.atlir5.mikolivator.observers.Observable;
import esi.atlir5.mikolivator.observers.Observer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
abstract class ElevatorBehavior implements Observable {

    final Elevator elevator;
    List<Integer> destinations;
    List<Passenger> passengers;
    final List<Observer> observers;
    final int elevatorPosition;

    ElevatorBehavior(Elevator elevator, int elevator_position) {
        this.elevator = elevator;
        destinations = new ArrayList<>();
        passengers = new ArrayList<>();
        elevatorPosition = elevator_position;
        observers = new ArrayList<>();
    }

    //  ajoute une destination en vérifiant qu'elle n'est pas déjà présente
    void addDestination(int floor) {
        if (destinations.contains(floor) || elevator.getCurrentFloor() == floor) {
            return;
        }
        destinations.add(floor);
    }

    //  retourne le dernier étage du bâtiment
    int getLastFloor() {
        return elevator.getLastFloor();
    }

    //  retourne l'étage le plus bas du bâtiment
    int getLowestFloor() {
        return elevator.getLowestFloor();
    }
    
    int getCurrentFloor() {
        return elevator.getCurrentFloor();
    }
    
    MovementElevator getMovement() {
        return elevator.getMovement();
    }

    //  monter l'ascenseur jusqu'à la destination
    void goingUp(int destination) {
        //  définir le nombre de secondes que cela va durer
        int sleepTime = destination - elevator.getCurrentFloor();

        elevator.setMovement(MovementElevator.UP);

        //  instancier le timer permettant le mouvement de l'ascenseur
        Timer t = new Timer();

        //  faire monter l'ascenseur
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (elevator.getCurrentFloor() + 1 == destination) {
                    cancel();
                }
                elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
//                System.out.println("Etage courant: " + getCurrentFloor());
                notifyObs(101);
            }
        }, new Date(), 1000);

        //  bloquer le thread courant le temps que l'ascenseur monte
        try {
            Thread.sleep(sleepTime * 1000);
            elevator.setMovement(MovementElevator.STANDBY);
            int nb = releasePassenger(elevator.getCurrentFloor());  //  faire sortir les personnes de l'ascenseur
        } catch (InterruptedException ex) {
            System.out.println("Erreur - ControllerElevator - goingUp(): "
                    + ex.getMessage());
        }
    }

    //  descendre l'ascenseur jusqu'à la destination
    void goingDown(int destination) {
        //  définir le temps que l'ascenseur va descendre
        int sleepTime = Math.abs(destination - elevator.getCurrentFloor());

        elevator.setMovement(MovementElevator.DOWN);

        //  instancier le timer permettant à l'ascenseur de monter
        Timer t = new Timer();

        //  faire monter l'ascenseur
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (elevator.getCurrentFloor() - 1 == destination) {
                    cancel();
                }
                elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
//                System.out.println("Etage courant: " + getCurrentFloor());
                notifyObs(101);
            }
        }, new Date(), 1000);

        //  bloquer le thread courant le temps que l'ascenseur descend
        try {
            Thread.sleep(sleepTime * 1000);
            elevator.setMovement(MovementElevator.STANDBY);
            int nb = releasePassenger(elevator.getCurrentFloor());  //  faire sortir les personnes de l'ascenseur
        } catch (InterruptedException ex) {
            System.out.println("Erreur - ControllerElevator - goingDown(): "
                    + ex.getMessage());
        }
    }

    //  ajoute des passagers à l'ascenseur si c'est possible
    boolean addPassenger(Passenger p) {
        if (!elevator.isFreePlace()) { //  vérifie s'il reste de la place
            return false;
        }
//        try {
            addDestination(p.getDestinationFloor());    //  ajoute la destinationr
            p.enterElevator(elevatorPosition);    //  faire entrer le passager dans l'ascenseur et le positionner dans l'ascenseur
            passengers.add(p);  //  ajoute la personne à la liste des passagers
            elevator.addOnePerson();    //  augmente le nombre de personnes dans l'ascenseur
//        } catch (MikolivatorException ex) {
//            System.out.println(ex.getMessage());
//            return false;
//        }
        notifyObs(101);
        return true;    //  retourne vrai si la personne a bien été ajoutée.
    }

    //  retire un passager de l'ascenseur
    int releasePassenger(int floor) {
        int cpt = 0;
        int index = 0;

        while (index < passengers.size()) { //  parcourir tous les passagers
            if (passengers.get(index).getDestinationFloor() == floor) { //  si il descend à cet étage
                passengers.get(index).leaveElevator(elevatorPosition - 1);   //  le positionner hors de l'ascenseur
                passengers.remove(index);   //  le supprimer de la liste des passagers
                elevator.releaseOnePerson();    //  diminue le nombre de personnes dans l'ascenseur
                index = 0;    //  recommencer au début de la liste de passager
                ++cpt;  //  incrémenter le nombre de passagers libérés
            } else {
                ++index;   //   passer à la personne suivante
            }
        }

//        System.out.println(cpt + " personnes sont sorties de l'ascenseur.");
        notifyObs(101);
        notifyObs(floor);
        return cpt;
    }
    
    int getNumberOfPassengersInElevator() {
        return passengers.size();
    }

    abstract void move();
}
