package esi.atlir5.mikolivator.model;

import esi.atlir5.mikolivator.dtoDB.EvacuationDTO;
import esi.atlir5.mikolivator.dtoDB.FloorDTO;
import esi.atlir5.mikolivator.dtoDB.SimulationDTO;
import esi.atlir5.mikolivator.business.AdminFacade;
import esi.atlir5.mikolivator.business.TablesDB;
import esi.atlir5.mikolivator.exception.MikolivatorException;
import esi.atlir5.mikolivator.events.BuildingListener;
import esi.atlir5.mikolivator.events.ControllerElevatorListener;
import esi.atlir5.mikolivator.events.ElevatorListener;
import esi.atlir5.mikolivator.events.FloorListener;
import esi.atlir5.mikolivator.events.PassengerListener;
import esi.atlir5.mikolivator.exception.MikolivatorBusinessException;
import esi.atlir5.mikolivator.observers.Observable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import esi.atlir5.mikolivator.observers.ObserverDisplayBuilding;
import esi.atlir5.mikolivator.observers.ObserverDisplayStats;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * The class Simulation is the facade of our simulator. It's the intermediate
 * for the interactions between the model and the view.
 * @author Mike Sarton & Olivier Cordier
 */
public class Simulation implements Observable, PassengerListener,
        BuildingListener, ControllerElevatorListener, FloorListener, ElevatorListener {

    private final ControllerElevator controllerElevator;
    private final List<ObserverDisplayBuilding> observersDisplayBuilding;
    private final List<ObserverDisplayStats> observerDisplayStats;
    private List<Integer> floorEvacuated;
    private final int nbPersonsMax;
    private final Building building;
    private final List<Integer> elevatorPositions;

    /**
     * Simulation's constructor
     *
     * @param nb_persons_max_in_building Number of persons max in the building.
     * @param nb_persons_max_in_elevator Number of persons max in an elevator.
     * @param number_of_floors Number of floors in the building.
     * @param elevators_positions List of elevator's positions.
     * @throws MikolivatorException Exception thrown if the number of floor
     * is incorrect, or the number of person in the building is incorrect, or
     * the number of persons in the elevator is incorrect.
     */
    public Simulation(int nb_persons_max_in_building,
            int nb_persons_max_in_elevator, int number_of_floors,
            List<Integer> elevators_positions) throws MikolivatorException {

        if (number_of_floors > 7) {
            throw new MikolivatorException("7 étages maximum.");
        } else if (number_of_floors < 1) {
            throw new MikolivatorException("Il faut au minimum 1 étage.");
        } else if (nb_persons_max_in_building < 1) {
            throw new MikolivatorException("Il faut au minimum 1 personne.");
        } else if (nb_persons_max_in_building > 100) {
            throw new MikolivatorException("100 personnes maximum sont acceptés "
                    + "dans le bâtiment.");
        } else if (nb_persons_max_in_elevator < 1) {
            throw new MikolivatorException("Il faut autoriser minimum 1 personne "
                    + "dans l'ascenseur.");
        } else if (nb_persons_max_in_elevator > 10) {
            throw new MikolivatorException("10 personnes maximum sont autorisés "
                    + "dans l'ascenseur.");
        }

        controllerElevator = new ControllerElevator(nb_persons_max_in_elevator, 
                elevators_positions);
        controllerElevator.addListener(this);
        controllerElevator.getElevators().forEach((e) -> {
            e.addListener(this);
        });
        nbPersonsMax = nb_persons_max_in_building;
        observerDisplayStats = new ArrayList<>();
        observersDisplayBuilding = new ArrayList<>();
        floorEvacuated = new ArrayList<>();
        elevatorPositions = elevators_positions;
        building = new Building(number_of_floors, this);
        building.getFloors().forEach((f) -> {
            f.addListener(this);
        });
        createEntryInDatabase();
        generatePassenger();
    }

    private void generatePassenger() {
        int delay = 1000 + (int) (Math.random() * (15000 - 1000 + 1));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                generatePassenger();
            }
        }, delay);

        if (building.getNumberOfPersonsInBuilding() == nbPersonsMax) {
            return;
        }

        if ((floorEvacuated.size() == building.getNumberOfFloors()) || 
                (building.getFloors().get(0).isEvacuated())) {
            return;
        }

        NormalPerson p = new NormalPerson(generateDestinationFloor(0), 
                Functions.generateRandomTimeOfHide(), 
                Functions.generateRandomSpeedOfWalk(), elevatorPositions, this);
        building.addPersonInBuilding(p);
        Thread t = new Thread(p);
        t.start();
    }

    private int generateDestinationFloor(int exception) {
        if (floorEvacuated.size() == building.getNumberOfFloors()) {
            return 0;
        } else if ((floorEvacuated.size() == (building.getNumberOfFloors() - 1)) 
                && (!floorEvacuated.contains(exception))) {
            return 0;
        } else {
            return Functions.generateRandomDestination(0, 
                    building.getNumberOfFloors() - 1, exception, floorEvacuated);
        }
    }

    /**
     * This method evacuates one floor of the building. Once called, the people
     * at this floor will walk toward the elevator to leave the floor. The people
     * having this floor as destination wont get out of the elevator. Finally, 
     * this floor won't be available anymore as possible destination.
     * @param floor Floor's number to evacuate.
     */
    public void evacuationFloor(int floor) {
        floorEvacuated.add(floor);
        building.evacuationFloor(floor);
    }

    /**
     * This method reopen a floor after it's evacuation. One called, the floor is
     * then available again in the possibles destinations.
     * @param floor Floor's number to reopen.
     */
    public void openFloor(int floor) {
        building.openFloor(floor);
        for (int i = 0; i < floorEvacuated.size(); ++i) {
            if (floorEvacuated.get(i) == floor) {
                floorEvacuated.remove(i);
                return;
            }
        }
    }
    
    private void createEntryInDatabase() {
        try {
            AdminFacade.addEntry(TablesDB.SIMULATION, 
                    new SimulationDTO(controllerElevator.getNbPersonsMaxInElevator()));
            for (int i=0; i<building.getNumberOfFloors(); ++i) {
                AdminFacade.addEntry(TablesDB.FLOOR, new FloorDTO(i));
            }
        } catch (MikolivatorBusinessException ex) {
            System.err.println("Impossible de créer une entrée dans la database"
                    + " pour la simulation: " + ex.getMessage());
        }
    }
    
    /**
     *
     */
    public void saveDataInDatabase() {
        try {
            AdminFacade.updateSimulation(building.getNumberOfPersonsInBuilding(), 
                    new Timestamp(new Date().getTime()));
            List<Floor> floors = building.getFloors();
            for (int i=0; i<building.getNumberOfFloors(); ++i) {
                AdminFacade.updateFloor(i, floors.get(i).getNbPeopleTotal());
            }
        } catch (MikolivatorBusinessException ex) {
            System.err.println("Impossible de mettre à jour la database avec "
                    + "les données du modèle en fin de simulation: " 
                    + ex.getMessage());
        }
    }

    //  ---------- METHODES POUR METTRE A JOUR LA VUE
    @Override
    public void addObserver(ObserverDisplayBuilding obs) {
        if (!observersDisplayBuilding.contains(obs)) {
            observersDisplayBuilding.add(obs);
        }
    }

    @Override
    public void deleteObserver(ObserverDisplayBuilding obs) {
        if (observersDisplayBuilding.contains(obs)) {
            observersDisplayBuilding.remove(obs);
        }
    }

    @Override
    public void addObserver(ObserverDisplayStats obs) {
        if (!observerDisplayStats.contains(obs)) {
            observerDisplayStats.add(obs);
        }
    }

    @Override
    public void deleteObserver(ObserverDisplayStats obs) {
        if (observerDisplayStats.contains(obs)) {
            observerDisplayStats.remove(obs);
        }
    }

    @Override
    public void notifyBuilding(int nbOfPeopleInBuilding) {
        observerDisplayStats.forEach((obs) -> {
            obs.updateBuilding(nbOfPeopleInBuilding);
        });
    }

    @Override
    public void notifyControllerElevator(int nbOfPeopleWaitingAnElevator) {
        observerDisplayStats.forEach((obs) -> {
            obs.updateControllerElevator(nbOfPeopleWaitingAnElevator);
        });
    }

    @Override
    public void notifyElevatorInfos(int positionElevator, int currentFloor,
            int nbPassengers) {
        observerDisplayStats.forEach((obs) -> {
            obs.updateElevatorInfos(positionElevator, currentFloor, nbPassengers);
        });
    }

    @Override
    public void notifyFloor(int floor, int nbPassengersHidden,
            int nbPassengersWalking, int nbPassengersWaiting) {
        observerDisplayStats.forEach((obs) -> {
            obs.updateAFloor(floor, nbPassengersHidden, nbPassengersWalking, 
                    nbPassengersWaiting);
        });
    }

    @Override
    public void notifyFloorPositions(int floor, List<Integer> pos, 
            List<PassengerState> states) {
        observersDisplayBuilding.forEach((obs) -> {
            obs.updateAFloorPositions(floor, pos, states);
        });
    }

    @Override
    public void notifyElevatorState(int floor, MovementElevator me, int nbPeople) {
        observersDisplayBuilding.forEach((obs) -> {
            obs.updateElevatorState(floor, me, nbPeople);
        });
    }

    //  ---------- METHODES D'EVENEMENT
    //  ----- EVENEMENTS DES PASSAGERS
    @Override
    public void passengerNeedsNewDestination(Passenger p) {
        p.setDestinationFloor(generateDestinationFloor(p.getCurrentFloor()));
    }

    @Override
    public void passengerNeedsElevator(Passenger p, int positionElevator) {
        controllerElevator.someoneCalledAnElevator(p, positionElevator);
    }

    @Override
    public void passengerLeftFloor(Passenger p, int floor) {
        building.removePersonAtFloor(p, floor);
    }

    @Override
    public void passengerEnterFloor(Passenger p, int floor) {
        building.addPersonAtFloor(p, floor);
    }

    //  ---------- METHODES DE DIFFERENTES CLASSES RECUPERANT DES INFOS POUR LA VUE
    //  ----- EVENEMENTS DU BUILDING
    @Override
    public void updateNumberPeopleInBuilding(int nbOfPeople) {
        Platform.runLater(() -> {
            notifyBuilding(nbOfPeople);
        });
    }
    
    //  ----- EVENEMENTS D'UN ETAGE
    @Override
    public void updateFloorStats(int nbFloor, int nbPassengersHidden, 
            int nbPassengersWalking, int nbPassengersWaiting) {
        Platform.runLater(() -> {
            notifyFloor(nbFloor, nbPassengersHidden, nbPassengersWalking, 
                    nbPassengersWaiting);
        });
    }

    @Override
    public void updateFloorPositionsPassengers(int floor, List<Integer> pos, 
            List<PassengerState> states) {
        Platform.runLater(() -> {
            notifyFloorPositions(floor, pos, states);
        });
    }
    
    @Override
    public void updateNewEvacuationRecord(int nbPeopleEvacuated, int floorNumber) {
        try {
            AdminFacade.addEntry(TablesDB.EVACUATION, 
                    new EvacuationDTO(floorNumber, nbPeopleEvacuated));
        } catch (MikolivatorBusinessException ex) {
            System.err.println("Impossible d'enregistrer une évacuation dans "
                    + "la database: " + ex.getMessage());
        }
    }

    //  ----- EVENEMENTS DU CONTROLLEUR D'ASCENSEUR
    @Override
    public void updatePassengersWaitingElevator(int nbOfPeopleWaiting) {
        Platform.runLater(() -> {
            notifyControllerElevator(nbOfPeopleWaiting);
        });
    }


    //  ----- EVENEMENTS D'UN ASCENSEUR
    @Override
    public void updateElevatorInfos(int positionElevator, int currentFloor, 
            int nbPassengers) {
        Platform.runLater(() -> {
            notifyElevatorInfos(positionElevator, currentFloor, nbPassengers);
        });
    }

    @Override
    public void updateElevatorState(int floor, MovementElevator me, int nbPeople) {
        Platform.runLater(() -> {
            notifyElevatorState(floor, me, nbPeople);
        });
    }
}
