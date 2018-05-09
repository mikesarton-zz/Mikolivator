package esi.atlir5.mikolivator.view;

import java.util.List;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import esi.atlir5.mikolivator.model.PassengerState;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Graphic representation of a floor
 *
 * @author Mike Sarton & Olivier Cordier
 */
class FloorGUI extends Parent {

    private final GridPane gridPane;
    private final ElevatorGUI elevator;
    private final List<Pane> listPanePerso;
    private final int nbSteps;

    FloorGUI() {
        nbSteps = 25;
        listPanePerso = new ArrayList<>();
        gridPane = new GridPane();
        elevator = new ElevatorGUI();
        gridPane.getStyleClass().add("floor");
        drawFloor();
        createPanes(nbSteps);
        getChildren().add(gridPane);
    }

    private void drawFloor() {
        addDecor();
        gridPane.add(elevator, nbSteps, 0);
    }

    void setPositionPassenger(List<Integer> listPosition, List<PassengerState> listState) {

        clearPanes();
        for (int i = 0; i < listPosition.size(); i++) {
            PersonnageGUI perso = new PersonnageGUI(passengerState(listPosition.get(i), listState.get(i)));
            VBox vbox = new VBox();
            VBox vboxBis = new VBox();
            boolean before = false;
            if (null == listPosition.get(i)) {
                vbox.getChildren().add(perso);
            } else {
                switch (listPosition.get(i)) {
                    case 0: {
                        Door door = new Door(true);
                        vbox.getChildren().add(door);
                        break;
                    }
                    case 1: {
                        vboxBis = new VBox();
                        Door door = new Door(true);
                        vboxBis.getChildren().add(door);
                        vbox.getChildren().add(perso);
                        before = true;
                        break;
                    }
                    default:
                        vbox.getChildren().add(perso);
                        break;
                }
            }
            vbox.setAlignment(Pos.CENTER);
            listPanePerso.get(listPosition.get(i)).getChildren().clear();
            if (before) {
                listPanePerso.get(0).getChildren().clear();
                listPanePerso.get(0).getChildren().add(vboxBis);
            }
            listPanePerso.get(listPosition.get(i)).getChildren().add(vbox);
        }
    }

    PersonnageState passengerState(int pos, PassengerState state) {
        switch (state) {
            case WAITINGELEVATOR:
                return PersonnageState.RIGHTSTOP;

            case WALKINGTOELEVATOR:
                if (Math.abs(pos) % 2 == 1) {
                    return PersonnageState.RIGHTWALKING;
                } else {
                    return PersonnageState.RIGHTSTOP;
                }

            case WALKINGTOCORRIDOR:
                if (Math.abs(pos) % 2 == 1) {
                    return PersonnageState.LEFTWALKING;
                } else {
                    return PersonnageState.LEFTSTOP;
                }
            default:
                return PersonnageState.RIGHTSTOP;
        }
    }

    GridPane getGridPane() {
        return gridPane;
    }

    ElevatorGUI getElevator() {
        return elevator;
    }

    private void createPanes(int nbPanes) {
        for (int i = 0; i < nbPanes; i++) {
            VBox pane = new VBox();
            listPanePerso.add(pane);
            gridPane.add(pane, i, 0);
            pane.setAlignment(Pos.BOTTOM_CENTER);
        }
    }

    private void clearPanes() {
        for (Pane p : listPanePerso) {
            p.getChildren().clear();
        }
    }

    private void addDecor() {
        generateObject("windows", nbSteps);
        generateObject("coca", 1);
        generateObject("couch", 1);
        generateObject("pelican", 1);
    }

    private void generateObject(String name, int nb) {
        int objectPlace = 0;
        Rectangle rectangle;
        for (int i = 0; i < nb; i++) {
            int nbObject = (int) (1 + (Math.random() * 2));
            rectangle = new Rectangle(0, 0, 25, 90);
            Image img;
            switch (name) {
                case "coca":
                    img = probabilityObject(nbObject, "coca");
                    objectPlace = (int) (4 + (Math.random() * 25));
                    break;
                case "windows":
                    img = probabilityWindows(nbObject, i);
                    objectPlace = i;
                    break;
                case "couch":
                    img = probabilityObject(nbObject, "couch");
                    objectPlace = (int) (1 + (Math.random() * 25));
                    break;
                case "pelican":
                    img = probabilityObject(nbObject, "pelican");
                    objectPlace = 2;
                    break;
                default:
                    img = new Image("resources/bric.png");
            }
            rectangle.setFill(new ImagePattern(img));
            gridPane.add(rectangle, objectPlace, 0);
        }
    }

    private Image probabilityObject(int nbObject, String str) {
        if (nbObject % 2 == 0) {
            switch (str) {
                case "coca":
                    return new Image("resources/coca.png");
                case "couch":
                    return new Image("resources/couch.png");
                case "pelican":
                    return new Image("resources/pelican.png");
            }
        }
        return new Image("resources/bric.png");

    }

    private Image probabilityWindows(int nbObject, int i) {
        if (nbObject % 2 == 0 && i % 5 == 0 && i != 0) {
            return new Image("resources/windows.png");
        } else {
            return new Image("resources/bric.png");
        }
    }
}
