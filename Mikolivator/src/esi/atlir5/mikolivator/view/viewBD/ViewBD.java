package esi.atlir5.mikolivator.view.viewBD;

import javafx.scene.Parent;
import javafx.scene.layout.HBox;

/**
 * View Database with tableview and piechart
 * @author Mike Sarton & Olivier Cordier
 */
public final class ViewBD extends Parent {

    private MyPieChart myPieChart;
    private MyTableView myTableView;

    /**
     * Default constructor
     */
    public ViewBD() {

        HBox hbox = new HBox();
        myPieChart = new MyPieChart(1);
        myTableView = new MyTableView(myPieChart);

        hbox.getChildren().addAll(myTableView, myPieChart);
        getChildren().add(hbox);
    }

}
