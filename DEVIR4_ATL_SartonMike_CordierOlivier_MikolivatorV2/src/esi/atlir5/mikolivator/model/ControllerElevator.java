package esi.atlir5.mikolivator.model;

import esi.atlir5.mikolivator.observers.Observer;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
class ControllerElevator extends ElevatorBehavior implements Runnable {    

    //  constructeur, reçoit:
    //  1) le nombre de personne maximum dans l'ascenseur
    //  2) l'étage courant de l'ascenseur (où l'initialiser)
    //  3) le numéro d'étage le plus haut
    //  4) le numéro d'étage le plus bas
    //  5) la position de l'ascenseur (place)
    //  crée un ascenseur et l'envoit au constructeur parent
    ControllerElevator(int nb_persons_max, int current_floor, int last_floor,
            int lowest_floor, int elevator_position) {
        
        super(new Elevator(nb_persons_max, current_floor, last_floor,
                lowest_floor), elevator_position);
    }

    //  contenu du thread de l'ascenseur
    @Override
    public void run() {
        while (true) {
            move();            
        }
    }
    
    //  déplacer l'ascenseur (reste à définir l'algo qu'on veut)
    @Override
    public void move() {        
        while (!destinations.isEmpty()) {   //  tant qu'il reste des destinations...
            int destination = destinations.remove(0);   //  prendre la première destination qui a été entrée
            
            if (destination > elevator.getCurrentFloor()) { //  monter ou descendre selon où l'ascenseur se situe
                goingUp(destination);   //  monter l'ascenseur jusqu'à la destination "destination"
                notifyObs(104); //  notifier un changement au niveau du bâtiment
            } else if (destination < elevator.getCurrentFloor()){
                goingDown(destination); //  descendre l'ascenseur jusqu'à la destination "destination"
                notifyObs(104); //  notifier un changement au niveau du bâtiment
            }
        }
    }

    //  ----------------------- METHODES POUR LES OBSERVEURS
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
