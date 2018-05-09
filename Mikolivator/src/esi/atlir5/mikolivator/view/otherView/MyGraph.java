package esi.atlir5.mikolivator.view.otherView;

import esi.atlir5.mikolivator.observers.ObserverDisplayStats;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class MyGraph extends Parent implements ObserverDisplayStats {

    private CategoryAxis xAxis = new CategoryAxis();
    private NumberAxis yAxis = new NumberAxis();
    private StackedBarChart<String, Number> sbc
            = new StackedBarChart<>(xAxis, yAxis);
    private XYChart.Series<String, Number> hidden;
    private XYChart.Series<String, Number> corridor
            = new XYChart.Series<>();
    private XYChart.Series<String, Number> waiting
            = new XYChart.Series<>();

    /**
     * Default constructor without parameters
     */
    public MyGraph() {
        this.hidden = new XYChart.Series<>();

    }

    /**
     * Constructor with parameter
     * @param floors number of floor in the building
     */
    public MyGraph(int floors) {
        ArrayList myArray = setArray(floors);
        hidden = new XYChart.Series<>();
        sbc.setTitle("Floor Occupation: ");
        xAxis.setLabel("Floors");
        xAxis.setCategories(FXCollections.<String>observableArrayList(myArray));
        yAxis.setLabel("Characters");

        initHidden(floors);
        initCorridor(floors);
        initWaiting(floors);

        sbc.getData().addAll(hidden, corridor, waiting);
        getChildren().add(sbc);

    }

    private String floorName(int i) {
        return "Floor " + i;
    }

    private void initHidden(int floors) {
        hidden.setName("Hidden");
        for (int i = 0; i <= floors; i++) {
            hidden.getData().add(new XYChart.Data<>(floorName(i), 0));
        }
    }

    private void initCorridor(int floors) {
        corridor.setName("Corridor");
        for (int i = 0; i <= floors; i++) {
            corridor.getData().add(new XYChart.Data<>(floorName(i), 0));
        }
    }

    private void initWaiting(int floors) {
        waiting.setName("Waiting");
        for (int i = 0; i <= floors; i++) {
            waiting.getData().add(new XYChart.Data<>(floorName(i), 0));
        }
    }

    private void setHidden(int floor, int value) {
        hidden.getData().get(floor).setYValue(value);
    }

    private void setCorridor(int floor, int value) {
        corridor.getData().get(floor).setYValue(value);
    }

    private void setWaiting(int floor, int value) {
        waiting.getData().get(floor).setYValue(value);
    }

    private ArrayList setArray(int floors) {
        ArrayList myArray = new ArrayList();
        for (int i = 0; i <= floors; i++) {
            myArray.add("Floor " + i);
        }
        return myArray;
    }

    @Override
    public void updateBuilding(int nbOfPeopleInBuilding) {
        sbc.setTitle("Floor Occupation: " + nbOfPeopleInBuilding + " people inside");
    }

    /**
     * Update the elevator's controller 
     * @param nbOfPeopleWaitingAnElevator the number of characters waiting for the elevator
     */
    @Override
    public void updateControllerElevator(int nbOfPeopleWaitingAnElevator) {
    }

    /**
     * Update elevator's informations
     * 
     * @param positionElevator the elevator's position
     * @param currentFloor the stage where the elevator is 
     * @param nbPassengers number of characters in the lift
     */
    @Override
    public void updateElevatorInfos(int positionElevator, int currentFloor, int nbPassengers) {
    }

    /**
     * Update of the floor view
     * @param floor the floor
     * @param nbPassengersHidden number of characters hidden behind the door
     * @param nbPassengersWalking number of characters walking in the corridor
     * @param nbPassengersWaiting number of characters waiting for the elevator
     */
    @Override
    public void updateAFloor(int floor, int nbPassengersHidden, int nbPassengersWalking, int nbPassengersWaiting) {
        setHidden(floor, nbPassengersHidden);
        setCorridor(floor, nbPassengersWalking);
        setWaiting(floor, nbPassengersWaiting);
    }

}
