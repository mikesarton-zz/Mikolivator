package esi.atlir5.mikolivator.model;

import esi.atlir5.mikolivator.observers.Observer;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
class Passenger extends PassengerBehavior implements Runnable {

    //  constructeur, reçoit:
    //  1)  la destination à atteindre
    //  2)  la liste des positions des différents ascenseurs (s'il y en a plusieurs)
    Passenger(int destination, List<Integer> elevators_positions) {
        super(destination, elevators_positions);    //  envoyer ces paramètres au constructeur du parent
    }

    //  faire marcher la personne en fonction de son sens
    //  si la personne marche jusqu'à l'ascenseur, elle l'appellera lorsqu'elle sera bien positionnée
    //  si la personne marche vers le couloir, elle ira se cacher au fond
    @Override
    void walk() {
        switch (movement) {
            case TOELEVATOR:    //  marcher vers l'ascenseur
                position.setPlace(position.getPlace() + 1); //  modifier la position de la personne
                callElevator(); //  tenter d'appeler l'ascenseur (n'aura aucune incidence si la personne n'est pas devant)
                notifyObs(position.getFloor()); //  notifier un changement au niveau de l'étage
                break;
            case TOCORRIDOR:    //  marcher vers le couloir
                position.setPlace(position.getPlace() - 1); //  modifier la position de la personne
                if(position.getPlace() == -1) { //  si on a atteint la position "cachée"
                    isHidden = true;    //  indiquer que la personne est cachée                    
                }
                notifyObs(position.getFloor()); //  notifier un changement au niveau de l'étage
                break;
        }
    }

    //  contenu du thread de la personne (marcher si possible)
    @Override
    public void run() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isInElevator && !isWaiting && !isHidden) {  //  vérifier si la personne n'est pas dans l'ascenseur ou en train d'attendre un ascenseur ou n'est pas caché
                    walk();
//                    System.out.println("Etage: " + position.getFloor() + ", place: " + position.getPlace() + ", destination: " + getDestinationFloor());
                }
            }
        }, new Date(), 1000);
    }

    //  ----------------- METHODES CONCERNANT LES OBSERVATEURS
    
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
