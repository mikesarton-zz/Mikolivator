package esi.atlir5.mikolivator.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * Graphic representation of an elevator
 *
 * @author Mike Sarton & Olivier Cordier
 */
class ElevatorGUI extends Parent {

    private final Rectangle rectangle;
    private final ObjectProperty<ElevatorState> state;

    ElevatorGUI() {
        state = new ObjectPropertyBase<ElevatorState>() {
            @Override
            public Object getBean() {
                return this;
            }

            @Override
            public String getName() {
                return "Elevator's state";
            }
        };

        rectangle = new Rectangle(72, 90);
        rectangle.setStroke(Color.BLACK);
        Image img = new Image("resources/liftClosed.jpg");
        rectangle.setFill(new ImagePattern(img));
        getChildren().add(rectangle);
    }

    /**
     * To modify the elevator's drawing
     *
     * @param state the elevator's state
     */
    void setState(ElevatorState state) {
        this.state.set(state);
        switch (state) {
            case CLOSED:
                Image openEmpty = new Image("resources/liftClosed.jpg");
                rectangle.setFill(new ImagePattern(openEmpty));
                break;
            case OPENEMPTY:
                Image closed = new Image("resources/liftOpen.jpg");
                rectangle.setFill(new ImagePattern(closed));
                break;

            case OPENPEOPLE:
                Image openPeople = new Image("resources/liftOpenPeopleAnonym2.jpg");
                rectangle.setFill(new ImagePattern(openPeople));
                break;
            default:
                System.err.println("cas par défaut");

        }
    }
}
