package esi.atlir5.mikolivator.view;

import esi.atlir5.mikolivator.events.EvacuationButtonListener;
import esi.atlir5.mikolivator.view.otherView.MyGraph;
import esi.atlir5.mikolivator.view.otherView.NewViewBuilding;
import esi.atlir5.mikolivator.exception.MikolivatorException;
import esi.atlir5.mikolivator.model.Simulation;
import esi.atlir5.mikolivator.model.MovementElevator;
import esi.atlir5.mikolivator.model.PassengerState;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import esi.atlir5.mikolivator.observers.ObserverDisplayBuilding;
import javafx.scene.layout.VBox;

/**
 * Building GUI class
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class BuildingGUI extends Parent implements ObserverDisplayBuilding,
        EvacuationButtonListener {

    private GridPane gp;
    private int nbFloor;
    private FloorGUI[] floors;
    private Simulation simulation;

    /**
     * Default constructor
     */
    public BuildingGUI() {
    }

    /**
     * Buildind GUI constructor with parameters
     *
     * @param nbFloor the number of floors in the building
     * @param nbPeople number of people that will be created in the building
     * @param nbPeopleLift the maximum number of people allowed to be in an
     * elevator at the same time
     */
    public BuildingGUI(int nbFloor, int nbPeople, int nbPeopleLift) {
        floors = new FloorGUI[nbFloor + 1];
        this.nbFloor = nbFloor;
        gp = new GridPane();
        gp.getStyleClass().add("floor-grid");

        for (int i = 0; i <= nbFloor; i++) {
            EvacuateButton button = new EvacuateButton(nbFloor - i);
            button.addListener(this);
            FloorGUI floor = new FloorGUI();
            floors[nbFloor - i] = floor;
            gp.add(button, 0, i);
            gp.add(floors[nbFloor - i], 1, i);
        }

        floors[0].getElevator().setState(ElevatorState.OPENEMPTY);

        VBox vbox = new VBox();
        vbox.getChildren().add(gp);

        getChildren().add(vbox);

        List<Integer> elevatorsPositions = new ArrayList<>();
        elevatorsPositions.add(25);
        try {
            simulation = new Simulation(nbPeople, nbPeopleLift, nbFloor, elevatorsPositions);
            simulation.addObserver(this);

        } catch (MikolivatorException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     *
     * @param nbFloor number of floor
     * @param nbPeople number of people
     * @param nbPeopleLift max people accepted in the lift
     * @param nvb the second view
     * @param myGraph the statistics graph
     */
    public BuildingGUI(int nbFloor, int nbPeople, int nbPeopleLift, NewViewBuilding nvb, MyGraph myGraph) {
        this(nbFloor, nbPeople, nbPeopleLift);
        simulation.addObserver(nvb);
        simulation.addObserver(myGraph);
    }

    @Override
    public void updateAFloorPositions(int floor, List<Integer> pos,
            List<PassengerState> states) {
        floors[floor].setPositionPassenger(pos, states);
    }

    /**
     * The method that update the elevator's view
     *
     * @param floor the elevator's floor
     * @param me the elevator's movement (the direction)
     * @param nbPeople number of people inside the elevator
     */
    @Override
    public void updateElevatorState(int floor, MovementElevator me, int nbPeople) {
        if (me != MovementElevator.STANDBY) {
            for (int i = 0; i <= nbFloor; ++i) {
                floors[i].getElevator().setState(ElevatorState.CLOSED);
            }
        } else {
            if (nbPeople > 0) {
                floors[floor].getElevator().setState(ElevatorState.OPENPEOPLE);
            } else {
                floors[floor].getElevator().setState(ElevatorState.OPENEMPTY);
            }
        }
    }

    /**
     * Method used to evacuate a floor after a user's click
     *
     * @param floor the floor that have to be evacuate
     */
    @Override
    public void evacuationFloor(int floor) {
        simulation.evacuationFloor(floor);
    }

    /**
     * Method used to reopen a floor after a user's click
     *
     * @param floor the floor to be reopened
     */
    @Override
    public void openFloor(int floor) {
        simulation.openFloor(floor);
    }

    void saveDataBase() {
        simulation.saveDataInDatabase();
    }
}
