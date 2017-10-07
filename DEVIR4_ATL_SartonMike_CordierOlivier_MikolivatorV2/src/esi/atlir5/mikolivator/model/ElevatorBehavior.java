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

    final Elevator elevator;    //  l'ascenseur sur lequel porte les actions
    List<Integer> destinations; //  liste des destinations de l'ascenseur
    List<Passenger> passengers; //  liste des passagers de l'ascenseur
    final List<Observer> observers; 
    final int elevatorPosition; //  position de l'ascenseur dans le bâtiment

    //  constructeur
    ElevatorBehavior(Elevator elevator, int elevator_position) {
        this.elevator = elevator;
        elevatorPosition = elevator_position;
        destinations = new ArrayList<>();
        passengers = new ArrayList<>();
        observers = new ArrayList<>();
    }

    //  ajouter une destination à l'ascenseur
    void addDestination(int floor) {
        if (destinations.contains(floor) || elevator.getCurrentFloor() == floor) {  //  vérifie qu'elle n'est pas déjà présente OU étage courant de l'ascenseur
            return;
        }
        destinations.add(floor);
    }

    //  retourner le dernier étage du bâtiment
    int getLastFloor() {
        return elevator.getLastFloor();
    }

    //  retourner l'étage le plus bas du bâtiment
    int getLowestFloor() {
        return elevator.getLowestFloor();
    }
    
    //  retourner l'étage courant de l'ascenseur
    int getCurrentFloor() {
        return elevator.getCurrentFloor();
    }
    
    //  retourner le mouvement de l'ascenseur
    MovementElevator getMovement() {
        return elevator.getMovement();
    }

    //  monter l'ascenseur jusqu'à la destination
    void goingUp(int destination) {        
        int sleepTime = destination - elevator.getCurrentFloor();   //  définir le nombre de secondes que cela va durer
        elevator.setMovement(MovementElevator.UP);  //  changer le mouvement de l'ascenseur       
        
        Timer t = new Timer();  //  instancier le timer permettant le mouvement de l'ascenseur        
        t.schedule(new TimerTask() {    //  faire monter l'ascenseur
            @Override
            public void run() {
                if (elevator.getCurrentFloor() + 1 == destination) {    //  vérifier qu'il n'a pas atteint la destination
                    cancel();
                }
                elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);   //  modifier l'étage courant de l'ascenseur
                notifyObs(101); //  notifier un changement au niveau de l'ascenseur 1
            }
        }, new Date(), 1000);
        
        try { //  bloquer le thread courant le temps que l'ascenseur monte
            Thread.sleep(sleepTime * 1000);
            elevator.setMovement(MovementElevator.STANDBY); //  changer le mouvement de l'ascenseur (il est à l'arrêt)
            releasePassenger(elevator.getCurrentFloor());  //  faire sortir les personnes de l'ascenseur
        } catch (InterruptedException ex) {
            System.out.println("Erreur - ControllerElevator - goingUp(): "
                    + ex.getMessage());
        }
    }

    //  descendre l'ascenseur jusqu'à la destination
    void goingDown(int destination) {        
        int sleepTime = Math.abs(destination - elevator.getCurrentFloor()); //  définir le temps que l'ascenseur va descendre
        elevator.setMovement(MovementElevator.DOWN);    //  changer le mouvement de l'ascenseur
        
        Timer t = new Timer();  //  instancier le timer permettant à l'ascenseur de descendre
        
        t.schedule(new TimerTask() {    //  faire descendre l'ascenseur
            @Override
            public void run() {
                if (elevator.getCurrentFloor() - 1 == destination) {    //  vérifier que l'ascenseur n'a pas atteint sa destination
                    cancel();
                }
                elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);   //  modifier l'étage courant de l'ascenseur
                notifyObs(101); //  notifier un changement au niveau de l'ascenseur 1
            }
        }, new Date(), 1000);
        
        try {   //  bloquer le thread courant le temps que l'ascenseur descend
            Thread.sleep(sleepTime * 1000);
            elevator.setMovement(MovementElevator.STANDBY); //  modifier le mouvement de l'ascenseur
            releasePassenger(elevator.getCurrentFloor());  //  faire sortir les personnes de l'ascenseur
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
        addDestination(p.getDestinationFloor());    //  ajouter la destination du passager à l'ascenseur
        p.enterElevator(elevatorPosition);    //  faire entrer le passager dans l'ascenseur et le positionner dans l'ascenseur
        passengers.add(p);  //  ajouter la personne à la liste des passagers
        elevator.addOnePerson();    //  augmenter le nombre de personnes dans l'ascenseur

        notifyObs(101); //  notifier un changement au niveau de l'ascenseur 1
        return true;    //  retourne vrai si la personne a bien été ajoutée.
    }

    //  retire un passager de l'ascenseur
    void releasePassenger(int floor) {
        int index = 0;  //  positionnement dans la liste des passagers de l'ascenseur

        while (index < passengers.size()) { //  parcourir tous les passagers
            if (passengers.get(index).getDestinationFloor() == floor) { //  si il descend à cet étage
                passengers.get(index).leaveElevator(elevatorPosition - 1);   //  le sortir de l'ascenseur et le positionner hors de l'ascenseur
                passengers.remove(index);   //  le supprimer de la liste des passagers
                elevator.releaseOnePerson();    //  diminuer le nombre de personnes dans l'ascenseur
                index = 0;    //  recommencer au début de la liste des passagers
            } else {
                ++index;   //   passer à la personne suivante
            }
        }
        
        notifyObs(101); //  notifier un changement au niveau de l'ascenseur 1
        notifyObs(floor);   //  notifier un changement au niveau de l'étage "floor"
    }
    
    //  retourner le nombre de passagers dans l'ascenseur
    int getNumberOfPassengersInElevator() {
        return passengers.size();
    }

    abstract void move();
}
