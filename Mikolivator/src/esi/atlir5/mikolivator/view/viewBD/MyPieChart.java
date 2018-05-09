package esi.atlir5.mikolivator.view.viewBD;

import esi.atlir5.mikolivator.business.AdminFacade;
import esi.atlir5.mikolivator.dtoDB.EvacuationDTO;
import esi.atlir5.mikolivator.dtoDB.FloorDTO;
import esi.atlir5.mikolivator.exception.MikolivatorBusinessException;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Piechart class graphical view
 * 
 * @author Mike Sarton & Olivier Cordier
 */
public final class MyPieChart extends Parent {

    private final PieChart pieChart;
    private boolean isEvac;
    private int simId;
    private final Label idLabel;
    private final Label titleLabel;
    private ObservableList<FloorDTO> listFloorDTO;
    private ObservableList<EvacuationDTO> listEVacDTO;
    private boolean isConnected;
    private final Button floorInfoButton; 
    private final Button evacInfoButton;
    MyPieChart(int simdId) {
        isConnected = true;
        VBox vbox = new VBox();
        HBox tileButtonLeft = new HBox();
        tileButtonLeft.setAlignment(Pos.BOTTOM_RIGHT);
        tileButtonLeft.setPadding(new Insets(0, 35, 10, 0));
        HBox tileButtonRight = new HBox();
        tileButtonRight.setAlignment(Pos.BASELINE_LEFT);
        tileButtonRight.setPadding(new Insets(0, 0, 10, 35));

        vbox.setAlignment(Pos.CENTER);
        titleLabel = new Label();
        titleLabel.setPadding(new Insets(10, 0, 0, 0));
        titleLabel.getStyleClass().add("titleTableView");
        idLabel = new Label();
        idLabel.getStyleClass().add("title2TableView");
        isEvac = false;
        pieChart = new PieChart();
        pieChart.setLegendVisible(false);
        HBox hboxButton = new HBox();
        hboxButton.setAlignment(Pos.CENTER);

        floorInfoButton = new Button("Presence by Floor ");
        floorInfoButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if (isEvac) {
                    isEvac = false;
                    upDateViewPieChart();
                }

            }
        });
        evacInfoButton = new Button("Evacuation by Floor");
        evacInfoButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (!isEvac) {
                    isEvac = true;
                    upDateViewPieChart();
                }

            }
        });
        tileButtonLeft.getChildren().add(floorInfoButton);
        tileButtonRight.getChildren().add(evacInfoButton);
        hboxButton.getChildren().add(tileButtonLeft);
        hboxButton.getChildren().add(tileButtonRight);
        upDateView(simdId);
        vbox.getChildren().addAll(titleLabel, idLabel, pieChart, hboxButton);

        getChildren().add(vbox);
    }

    private void upDateViewPieChart() {
        pieChart.getData().clear();
        evacInfoButton.setVisible(true);
        floorInfoButton.setVisible(true);
        idLabel.setText("Simulation ID " + this.simId);
        idLabel.setTextFill(Color.web("#000000"));
        pieChart.setVisible(true);
        int counter = 0;
        if (isEvac) {
            titleLabel.setText("People who were evacuated");

            for (EvacuationDTO f : listEVacDTO) {
                PieChart.Data pieData = new PieChart.Data("Floor " + f.getNumFloor()
                        + " (" + f.getNbPeopleEvacuated() + " people)", f.getNbPeopleEvacuated());
                pieChart.getData().add(pieData);
                counter += f.getNbPeopleEvacuated();
            }
            if (counter == 0) {
                noDataFound("NO EVACUATION IN SIMULATION ");
            }
        } else {
            titleLabel.setText("People who were on the floors");
            for (FloorDTO f : listFloorDTO) {
                PieChart.Data pieData = new PieChart.Data("Floor " + f.getNumFloor()
                        + " (" + f.getNbPrsTotal() + " people)", f.getNbPrsTotal());
                pieChart.getData().add(pieData);
                counter += f.getNbPrsTotal();
            }
            if (counter == 0) {
                noDataFound("NOBODY WAS GENERATED IN SIMULATION ");
            }
        }
    }

    private void noDataFound(String str) {
        pieChart.setVisible(false);
        idLabel.setText(str + this.simId);
        idLabel.setTextFill(Color.web("#FF0000"));
    }

    private void updateDTOs(int simId) {
        try {
            listFloorDTO = AdminFacade.getFloorsInfos(simId);
            listEVacDTO = AdminFacade.getEvacuationsInfos(simId);
        } catch (MikolivatorBusinessException ex) {
            isConnected = false;
            System.err.println(ex.getMessage());
        }
    }

    void upDateView(int simId) {
        updateDTOs(simId);
        this.simId = simId;
        if(isConnected)upDateViewPieChart();
    }
    
    void noConnection(){
        idLabel.setVisible(false);
        evacInfoButton.setVisible(false);
        floorInfoButton.setVisible(false);
        titleLabel.setText("NO CONNECTION TO DATABASE");      
    }
}
