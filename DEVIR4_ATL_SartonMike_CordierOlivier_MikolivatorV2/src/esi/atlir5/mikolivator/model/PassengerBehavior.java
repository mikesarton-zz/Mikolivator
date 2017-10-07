package esi.atlir5.mikolivator.model;

import esi.atlir5.mikolivator.observers.Observable;
import esi.atlir5.mikolivator.observers.Observer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
abstract class PassengerBehavior implements Observable {
    
    private int destinationFloor;   //  étage de destination de la personne
    Position position;  //  position de la personne (étage, place)
    boolean isInElevator;   //  indique si la personne est dans l'ascenseur
    boolean isWaiting;  //  indique si la personne attend l'ascenseur
    boolean isHidden;   //  indique si la personne est cachée (fond du couloir)
    MovementPassenger movement; //  le sens de la personne
    private final List<Integer> elevatorsPositions; //  liste des positions des ascenseurs
    final List<Observer> observers; //  liste des observateurs

    //  constructeur
    PassengerBehavior(int destination, List<Integer> elevators_positions) {
        destinationFloor = destination;
        position = new Position();
        isInElevator = false;
        isWaiting = false;
        isHidden = false;
        elevatorsPositions = elevators_positions;
        movement = MovementPassenger.TOELEVATOR;
        observers = new ArrayList<>();
    }        
    
    //  --------------- TOUS LES GETTERS
    
    //  retourne la destination de la personne
    int getDestinationFloor() {
        return destinationFloor;
    }


    //  retourne le sens de marche de la personne (vérifié si utilisé + tard)
    MovementPassenger getMovement() {
        return movement;
    }

    //  retourner la position de la personne
    Position getPosition() {
        return position;
    }

    //  retourner si la personne est caché ou non (au fond du couloir)
    boolean isHidden() {
        return isHidden;
    }
    
    //  retourner si la personne attend un ascenseur ou non
    boolean isWaiting() {
        return isWaiting;
    }
    
    //  ------------- TOUS LES SETTERS

    //  modifie la destination de la personne
    void setDestinationFloor(int destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    //  modifie le sens de marche de la personne (vérifié si utilisé + tard)
    void setMovement(MovementPassenger movement) {
        this.movement = movement;
    }
    
    //  ------------------ TOUTES LES ACTIONS

    //  appeler l'ascenseur en vérifiant si on est bien placé pour pouvoir le faire
    void callElevator() {
        boolean wellPlaced = false;
        int index = 0;
        while (index<elevatorsPositions.size() && !wellPlaced) {
            wellPlaced = position.getPlace() == (elevatorsPositions.get(index) - 1);    //  vérifie qu'on est bien placé pour pouvoir appeler l'ascenseur
            ++index;
        }
        if (!wellPlaced) {
            return;
        }
        
        isWaiting = true;   //  si la personne est bien placée, elle est maintenant en attente d'un ascenseur
    }

    //  faire entrer la personne dans l'ascenseur
    void enterElevator(int placeElevator) {
        position.setPlace(placeElevator);   //  positionner la personne à l'emplacement de l'ascenseur
        isInElevator = true;    //  indiquer que la personne est dans l'ascenseur
        isWaiting = false;  //  indiquer que la personne n'attend plus un ascenseur
    }

    //  faire sortir la personne de l'ascenseur
    void leaveElevator(int placeElevator) {
        position.setFloor(destinationFloor);    //  positionner la personne sur l'étage de destination
        position.setPlace(placeElevator);   //  positionner la personne hors de l'ascenseur
        isInElevator = false;   //  indiquer que la personne n'est plus dans l'ascenseur
        movement = MovementPassenger.TOCORRIDOR;    //  modifier le sens de marche de la personne
    }

    //  faire demi-tour... à implémenter.
    void makeUTurn() {
        
    }
    
    abstract void walk();
}
