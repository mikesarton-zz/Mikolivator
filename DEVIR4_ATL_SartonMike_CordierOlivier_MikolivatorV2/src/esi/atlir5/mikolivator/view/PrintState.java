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
        simulation = new Simulation(20, 5, 10, 3, 5, elevatorsPositions);
        simulation.addObserver(this);
        simulation.start();
    }

    @Override
    public void update(int view) {
        switch (view) {
            case 0:
                System.out.println("Update étage 0.");
                break;
            case 1:
                System.out.println("Update étage 1.");
                break;
            case 2:
                System.out.println("Update étage 2.");
                break;
            case 3:
                System.out.println("Update étage 3.");
                break;
            case 4:
                System.out.println("Update étage 4.");
                break;
            case 5:
                System.out.println("Update étage 5.");
                break;
            case 6:
                System.out.println("Update étage 6.");
                break;
            case 7:
                System.out.println("Update étage 7.");
                break;
            case 8:
                System.out.println("Update étage 8.");
                break;
            case 9:
                System.out.println("Update étage 9.");
                break;
            case 10:
                System.out.println("Update étage 10.");
                break;
            case 101:
                System.out.println("Update ascenseur 1.");
                break;
            case 102:
                System.out.println("Update ascenseur 2.");
                break;
            case 103:
                System.out.println("Update ascenseur 3.");
                break;
            case 104:
                System.out.println("Update building.");
                break;
        }
    }

}
