package esi.atlir5.mikolivator.view.viewBD;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import esi.atlir5.mikolivator.business.AdminFacade;
import esi.atlir5.mikolivator.dtoDB.SimulationDTO;
import esi.atlir5.mikolivator.exception.MikolivatorBusinessException;
import esi.atlir5.mikolivator.exception.MikolivatorDBException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

/**
 * Table view with Database's informations
 * 
 * @author Mike Sarton & Olivier Cordier
 */
public class MyTableView extends Parent {

    private TableView table;
    private ObservableList<SimulationDTO> data;
    private MyPieChart myPieChart;
    private VBox vbox;
    private final Label titleLabel;
    private boolean bdAccess;

    //MyTableView Constructor
    MyTableView(MyPieChart pieChart) {
        bdAccess = true;
        vbox = new VBox();
        myPieChart = pieChart;
        try {
            data = AdminFacade.getSimulationsInfos();
        } catch (MikolivatorBusinessException ex) {
            bdAccess = false;
            System.err.println(ex.getMessage());
        }
        titleLabel = new Label("Simulation information");
        titleLabel.setPadding(new Insets(0, 0, 10, 0));
        titleLabel.getStyleClass().add("titleTableView");
        if(bdAccess){
            initTable();
            initVBox();
        }
        else{
            myPieChart.noConnection();
        }

    }

    private void initTable() {
        table = new TableView();
        table.setId("my-table");
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setClickAction();
        //make the TableView's column not rearrangeable
        table.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            final TableHeaderRow header = (TableHeaderRow) lookup("TableHeaderRow");
            header.reorderingProperty().addListener((o, oldVal, newVal) -> header.setReordering(false));
        });

        TableColumn col1 = createColumnInt("simID", "Id sim", 60);
        TableColumn col2 = createColumnTime("startTime", "Start Time", 180);
        TableColumn col3 = createColumnTime("endTime", "Duration", 180);
        TableColumn col4 = createColumnInt("nbPersGenerated", "Generated", 105);
        TableColumn col5 = createColumnInt("nbPersInElevator", "Lift Max", 105);

        table.setItems(data);
        table.getColumns().addAll(col1, col2, col3, col4, col5);
    }

    //initialization of the vbox that contains the Title and the TableView
    private void initVBox() {
        vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(titleLabel, table);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        getChildren().add(vbox);
    }

    //to create a TableView's row with an Integer as value
    private TableColumn createColumnInt(String att, String column, int width) {
        TableColumn col = new TableColumn(column);
        col.setMinWidth(50);
        col.setMaxWidth(width);
        col.setResizable(false);
        col.setCellValueFactory(
                new PropertyValueFactory<SimulationDTO, Integer>(att));
        return col;
    }

    //to create a TableView's row with Timestamp as value
    private TableColumn createColumnTime(String att, String column, int width) {
        TableColumn col = new TableColumn(column);
        col.setMinWidth(width);
        col.setResizable(false);
        col.setCellValueFactory(
                new PropertyValueFactory<SimulationDTO, String>(att));
        return col;
    }

    //Actionclick Configuration of a TableView's row
    private void setClickAction() {
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                if (table.getSelectionModel().getSelectedItem() != null) {
                    try {
                        SimulationDTO selectedItems = (SimulationDTO) table.getSelectionModel().getSelectedItems().get(0);
                        int value = selectedItems.getSimID();
                        myPieChart.upDateView(value);
                    } catch (MikolivatorDBException ex) {
                        System.err.println(ex.getMessage());
                    }
                }
            }

        });
    }
}
