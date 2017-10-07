package esi.atlir5.mikolivator.model;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
class Elevator {
    
    private final int nbPersonsMax; //  nombre de personnes maximum dans l'ascenseur
    private int nbCurrentPersons;   //  nombre actuel de personnes dans l'ascenseur
    private final int lastFloor;    //  dernier étage possible
    private final int lowestFloor;  //  étage le plus bas possible
    private MovementElevator movement;  //  mouvement de l'ascenseur
    private int currentFloor;   //  étage courant de l'ascenseur
    
    //  constructeur
    Elevator (int nb_persons_max, int current_floor, int last_floor, 
            int lowest_floor){
        
        nbPersonsMax = nb_persons_max;
        currentFloor = current_floor;
        lastFloor = last_floor;
        lowestFloor = lowest_floor;
        movement = MovementElevator.STANDBY;
        nbCurrentPersons = 0;
    }
    
    //  ------------- TOUS LES GETTERS
    //  retourner l'étage courant de l'ascenseur
    int getCurrentFloor(){
        return currentFloor;
    }
    
    //  retourner le mouvement de l'ascenseur
    MovementElevator getMovement(){
        return movement;
    }

    //  retourner le dernier étage possible
    int getLastFloor() {
        return lastFloor;
    }

    //  retourner l'étage le plus bas possible
    int getLowestFloor() {
        return lowestFloor;
    }
    
    //  ----------------- TOUS LES SETTERS
    //  changer l'étage courant de l'ascenseur
    void setCurrentFloor (int floor){
        currentFloor = floor;
    }
    
    //  changer le mouvement de l'ascenseur
    void setMovement (MovementElevator mv) {
        movement = mv;
    }
    
    //  vérifier qu'il reste de la place dans l'ascenseur
    boolean isFreePlace(){
        return nbCurrentPersons < nbPersonsMax;
    }
    
    //  incrémenter le nombre de passagers dans l'ascenseur
    void addOnePerson(){
        ++nbCurrentPersons;
    }
    
    //  décrémenter le nombre de passagers dans l'ascenseur
    void releaseOnePerson(){
        --nbCurrentPersons;
    }
}
