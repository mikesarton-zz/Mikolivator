package esi.atlir5.mikolivator.view;

import esi.atlir5.mikolivator.model.Simulation;
import esi.atlir5.mikolivator.observers.Observer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class PrintState implements Observer {
    
    private final Simulation simulation;
    
    public PrintState() {
        List<Integer> elevatorsPositions = new ArrayList<>();
        elevatorsPositions.add(11);
        simulation = new Simulation(1, 5, 10, 3, 5, elevatorsPositions);
        simulation.addObserver(this);
        simulation.start();
    }

    @Override
    public void update() {
        System.out.println("----- Etat du bâtiment -----");
        System.out.println(simulation.getNumberOfPeople() + " personnes dans le "
                        + "bâtiment.");
        System.out.println(simulation.getNumberOfWaitingPeople() + " personnes attendent un ascenseur.");
        System.out.println("------- FIN -------");
    }
    
}
