package esi.atlir5.mikolivator.view;

import esi.atlir5.mikolivator.events.EvacuationButtonListener;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * Evacuation Button
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class EvacuateButton extends Parent {

    private boolean state;
    private StackPane pane;
    private Image img;
    private Rectangle rectangle;
    private int floor;
    private EvacuationButtonListener listeners;

    /**
     * Button constructor with parameter
     *
     * @param floor the floor to which the button must be attached
     */
    public EvacuateButton(int floor) {
        this.floor = floor;
        state = false;
        pane = new StackPane();
        img = new Image("resources/evacuate.png");
        rectangle = new Rectangle(0, 0, 90, 90);
        rectangle.setFill(new ImagePattern(img));

        pane.getChildren().add(rectangle);

        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                state = !state;
                changeButton();
            }
        });

        getChildren().add(pane);
    }

    private void changeButton() {
        if (state) {
            img = new Image("resources/open.png");
            rectangle.setFill(new ImagePattern(img));
            listeners.evacuationFloor(floor);
        } else {
            img = new Image("resources/evacuate.png");
            rectangle.setFill(new ImagePattern(img));
            listeners.openFloor(floor);
        }
    }

    void addListener(EvacuationButtonListener l) {
        listeners = l;
    }

    void removeListener() {
        listeners = null;
    }

}
