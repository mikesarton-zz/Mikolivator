package esi.atlir5.mikolivator.view;

import esi.atlir5.mikolivator.model.MikolivatorException;
import esi.atlir5.mikolivator.model.Simulation;
import esi.atlir5.mikolivator.observers.Observer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class PrintState implements Observer {

    private Simulation simulation = null;

    public PrintState() {
        List<Integer> elevatorsPositions = new ArrayList<>();
        elevatorsPositions.add(11);
        try {
            simulation = new Simulation(20, 5, 10, 3, 5, elevatorsPositions, this);
            simulation.addObserver(this);
        } catch (MikolivatorException e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    @Override
    public void update(int view) {
        switch (view) {
            case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                printFloor(view);
                break;
            case 101:
                printElevator();
                break;
            case 102:
                System.out.println("Update ascenseur 2.");
                break;
            case 103:
                System.out.println("Update ascenseur 3.");
                break;
            case 104:
                printBuilding();
                break;
        }
    }

    public void printBuilding() {
        System.out.println("------- Building -------");
        System.out.println(simulation.getNumberOfPeople() + " personnes dans le bâtiment.");
        System.out.println(simulation.getNumberOfWaitingPeople() + " attendent un ascenseur.");
    }
    
    public void printElevator() {
        System.out.println("------- Elevator -------");
        System.out.println("Etage de l'ascenseur: " + simulation.getCurrentFloorElevator());
        System.out.println("Nb personnes dans l'ascenseur: " + simulation.getNumberPassengersInElevator());
    }
    
    public void printFloor(int view) {
        System.out.println("------- Floor " + view + " -------");
        System.out.println(simulation.getNumberPassengerWaitingAtFloor(view) + " attendent un ascenseur.");
        System.out.println(simulation.getNumberPassengerWalkingAtFloor(view) + " marchent à cet étage.");
        System.out.println(simulation.getNumberPassengerHiddenAtFloor(view) + " sont cachés à cet étage.");
    }

}
