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

    private final ControllerElevator ctrlElevator;  //  contrôleur de l'ascenseur
    private final int minInterval;  //  interval min. pour génération des personnes
    private final int maxInterval;  //  interval max. pour génération des personnes
    private final List<Observer> observers;
    private List<Passenger> people; //  liste des gens dans le bâtiment
    private List<Passenger> waitingPeople;  //  liste des gens dans le bâtiment qui attendent un ascenseur
    private final List<Integer> elevatorsPositions; //  liste des positions des ascenseurs
    private final int nbPersonsMax; //  nombre de personnes max. dans le bâtiment

    //  constructeur de la simulation
    public Simulation(int nb_persons_max_in_building, 
            int nb_persons_max_in_elevator, int number_of_floors, 
            int min_interval_generate_passengers,
            int max_interval_generate_passengers, List<Integer> elevators_positions, PrintState ps) 
            throws MikolivatorException {
        
        //  vérification sur le nombre d'étage
        if (number_of_floors > 10) throw new MikolivatorException("10 étages maximum.");
        
        //  instanciation du contrôleur d'ascenseur + ajout de l'observateur + lancement du thread
        ctrlElevator = new ControllerElevator(nb_persons_max_in_elevator, 0, 
                number_of_floors, 0, 11);
        ctrlElevator.addObserver(ps);
        Thread thread = new Thread (ctrlElevator);  //  à retirer si comportement suspect.
        thread.start();
        
        //  instanciation des différents attributs
        minInterval = min_interval_generate_passengers;
        maxInterval = max_interval_generate_passengers;
        nbPersonsMax = nb_persons_max_in_building;
        elevatorsPositions = elevators_positions;
        observers = new ArrayList<>();
        people = new ArrayList<>();
        waitingPeople = new ArrayList<>();
        start();    //  lancement du thread de la simulation
    }

    //  contenu du thread de la simulation:
    //  1) génération des personnes
    //  2) vérifier si des gens attendent un ascenseur
    //  3) faire entrer les gens dans l'ascenseur si possible
    @Override
    public void run() {
        generatePassenger(minInterval*1000, maxInterval*1000);  //  générer des personnes
        while (true) {
            lookForWaitingPeople(); //  vérifier si des gens attendent un ascenseur
            enterPeople();  //  faire entrer les gens dans l'ascenseur si possible
        }
    }    

    //  génération des personnes
    private void generatePassenger(int min, int max) {
        int delay = min + (int) (Math.random() * (max - min + 1));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                generatePassenger(minInterval*1000, maxInterval*1000);
            }
        }, delay);
        
        //  création d'une personne
        addPerson(new Passenger(Functions.randomNumber(
                ctrlElevator.getLowestFloor()+1, ctrlElevator.getLastFloor()), elevatorsPositions));
    }
    
    //  ajouter une personne au bâtiment
    private synchronized void addPerson (Passenger p) {
        if (people.size() == nbPersonsMax) return;  //  vérifier qu'on a pas déjà atteint le nombre maximum de personnes autorisées
        people.add(p);
        Thread thread = new Thread (p);
        thread.start(); //  lancement du thread de la personne
        notifyObs(104); //  notifier un changement au niveau du bâtiment
        notifyObs(p.getPosition().getFloor());  //  notifier un changement au niveau de l'étage (ici 0)
    }
    
    //  vérifier si des gens attendent un ascenseur, si oui, les ajouter à la liste correspondante et prévenir l'ascenseur
    private synchronized void lookForWaitingPeople() {
//        people.stream().filter((p) -> (p.isWaiting() && !waitingPeople.contains(p))).forEachOrdered((p) -> {
//            addPersonWaiting(p);    //  ajouter la personne à la liste des personnes qui attendent
//            ctrlElevator.addDestination(p.getPosition().getFloor());    //  prévenir l'ascenseur d'aller chercher la personne
//            notifyObs(p.getPosition().getFloor());  //  notifier un changement au niveau de l'étage
//        });
        people.forEach((p) -> {
            if (p.isWaiting() && !waitingPeople.contains(p)) {
                addPersonWaiting(p);    //  ajouter la personne à la liste des personnes qui attendent
                ctrlElevator.addDestination(p.getPosition().getFloor());    //  prévenir l'ascenseur d'aller chercher la personne
                notifyObs(p.getPosition().getFloor());  //  notifier un changement au niveau de l'étage
            } else if (p.isWaiting()) {
                if (!ctrlElevator.getDestinations().contains(p.getPosition().getFloor())) {
                    ctrlElevator.addDestination(p.getPosition().getFloor());
                }
            }
        });
    }

    //  faire entrer dans l'ascenseur les gens qui attendent et se trouvant à l'étage courant de l'ascenseur
    private void enterPeople() {
        if (waitingPeople.isEmpty() || (ctrlElevator.getMovement() != MovementElevator.STANDBY)) return;
        
        int index = 0;  //  position dans waitingPeople
        int cpt = 0;    //  nombre de gens embarqués dans l'ascenseur
        
        while (index < waitingPeople.size()) {
            if (waitingPeople.get(index).getPosition().getFloor() == ctrlElevator.getCurrentFloor()) {
                if (!ctrlElevator.addPassenger(waitingPeople.get(index))) { //  si l'ascenseur est complet, le faire partir
                    ctrlElevator.move();    //  faire partir l'ascenseur
                    return;
                } else {    //  s'il reste de la place dans l'ascenseur, faire monter la personne
                    ++cpt;  //  incrémenter le nombre de gens embarqués dans l'ascenseur
                    waitingPeople.remove(index);    //  retirer la personne de la liste des gens qui attendent
                    index = 0;  //  revenir au début de la liste des personnes qui attendent un ascenseur
                }
            } else {
                ++index;    //  passer à la personne suivante (signifie que la personne courante n'est pas à l'étage courant de l'ascenseur)
            }
        }
        notifyObs(104); //  notifier un changement au niveau du bâtiment
        ctrlElevator.move();    //  faire partir l'ascenseur
    }
    
    //  ajouter une personne à la liste des gens qui attendent un ascenseur
    private void addPersonWaiting (Passenger p){
        waitingPeople.add(p);
        notifyObs(104); //  notifier un changement au niveau du bâtiment
        notifyObs(p.getPosition().getFloor());  //  notifier un changement au niveau de l'étage
    }
    
    //  --------------------------- TOUS LES GETTERS
    
    //  retourner le nombre de personnes présentes dans le bâtiment
    public int getNumberOfPeople() {
        return people.size();
    }
    
    //  retourner le nombre de personnes qui attendent un ascenseur dans le bâtiment
    public int getNumberOfWaitingPeople() {
        return waitingPeople.size();
    }
    
    //  retourner l'étage courant de l'ascenseur
    public int getCurrentFloorElevator() {
        return ctrlElevator.getCurrentFloor();
    }
    
    //  retourner le nombre de passagers dans l'ascenseur
    public int getNumberPassengersInElevator() {
        return ctrlElevator.getNumberOfPassengersInElevator();
    }
    
    //  retourner le nombre de personnes qui attendent un ascenseur à l'étage "floor"
    public int getNumberPassengerWaitingAtFloor(int floor) {
        int cpt = 0;
        for (Passenger p : waitingPeople) {
            if (p.getPosition().getFloor() == floor) ++cpt;
        }
        return cpt;
    }
    
    //  retourner le nombre de personnes qui marchent à l'étage "floor"
    public int getNumberPassengerWalkingAtFloor (int floor) {
        int cpt = 0;
        for (Passenger p : people) {
            if ((p.getPosition().getFloor() == floor) && (!p.isHidden()) && (!p.isWaiting())) ++cpt;
        }
        return cpt;
    }
    
    //  retourner le nombre de personnes qui sont cachés à l'étage "floor"
    public int getNumberPassengerHiddenAtFloor (int floor) {
        int cpt = 0;
        for (Passenger p : people) {
            if ((p.getPosition().getFloor() == floor) && (p.isHidden())) ++cpt;
        }
        return cpt;
    }
    
    //  temporaire, pour du Debug
    public int getNumberPassengerWhoReachedTheirGoal() {
        int cpt = 0;
        for (Passenger p : people) {
            if (p.getPosition().getFloor() == p.getDestinationFloor()) ++cpt;
        }
        return cpt;
    }

    //  ------------------ IMPLEMENTATION DES METHODES POUR LES OBSERVEURS
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
