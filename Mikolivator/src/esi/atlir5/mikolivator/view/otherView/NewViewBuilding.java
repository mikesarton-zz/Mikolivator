package esi.atlir5.mikolivator.view.otherView;

import esi.atlir5.mikolivator.observers.ObserverDisplayStats;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class NewViewBuilding extends Parent implements ObserverDisplayStats {

    private int buildinLength;
    private final int buildingWeidth;
    private final GridPane building;
    private final Label[][] label;

    /**
     * Constructor with parameter
     * @param nbFloors number of floor in the building
     */
    public NewViewBuilding(int nbFloors) {
        buildinLength = nbFloors + 1;
        buildingWeidth = 5;
        building = new GridPane();
        building.getStyleClass().add("border");
        label = new Label[25][25];
        setMatrix();
        setBorder();
        nameFloors();
        getChildren().add(building);

    }

    //Private Methods 
    private void setMatrix() {
        for (int i = 0; i < buildingWeidth; i++) {
            for (int j = 0; j < buildinLength; j++) {
                label[i][j] = new Label(" ");
                label[i][j].getStyleClass().add("labelGrid");
                HBox box = new HBox();
                box.setPrefSize(setWIdthGrid(i), 30);
                box.getChildren().add(label[i][j]);
                box.setAlignment(Pos.CENTER);
                building.add(box, i, j);
            }
        }
    }

    private void initFloor(int floor, int nbHiddenPassenger,
            int nbWalkingPassenger, int nbWaittingPassengers) {

        putInfos(floor, 0, nbHiddenPassenger);
        putInfos(floor, 1, nbWalkingPassenger);
        putInfos(floor, 2, nbWaittingPassengers);
    }

    private void putInfos(int floor, int pos, int info) {
        label[pos][(buildinLength - 1) - floor].setText(noDisplayZero(info));

    }

    private void putInfosLift(int floor, int pos, int info) {

        label[pos][(buildinLength - 1) - floor].setText(String.valueOf(info));

    }

    private void putInfosLiftEmpty(int floor, int pos, int info) {

        label[pos][(buildinLength - 1) - floor].setText("");

    }

    private void nameFloors() {
        for (int i = 0; i < buildinLength; i++) {
            String name = "Floor " + i;
            label[4][(buildinLength - 1) - i].setText(name);
        }
    }

    private void initLift(int floor, int nbPassenger) {
        for (int i = 0; i < buildinLength; i++) {
            if (i == floor) {
                putInfosLift(floor, 3, nbPassenger);
            } else {
                putInfosLiftEmpty(i, 3, 0);
            }

        }
    }

    private String noDisplayZero(int info) {
        if (info == 0) {
            return " ";
        } else {
            return String.valueOf(info);
        }
    }

    private void setBorder() {
        for (int i = 0; i < buildingWeidth; i++) {
            for (int j = 0; j < buildinLength; j++) {
                Pane pane = new Pane();
                pane.setStyle("-fx-border-color: black");
                building.add(pane, i, j);
            }
        }
    }

    private int setWIdthGrid(int place) {
        switch (place) {
            case 0:
            case 2:
                return 25;
            case 1:
                return 100;
            case 3:
                return 50;
            case 4:
                return 90;

            default:
                return 50;
        }
    }

    //ObserverDisplayStats Methods
    @Override
    public void updateBuilding(int nbOfPeopleInBuilding) {
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
        initLift(currentFloor, nbPassengers);
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

        initFloor(floor, nbPassengersHidden, nbPassengersWalking, nbPassengersWaiting);
    }
}
