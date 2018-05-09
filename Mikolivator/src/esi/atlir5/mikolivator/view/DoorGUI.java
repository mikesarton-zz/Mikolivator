package esi.atlir5.mikolivator.view;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * Graphic representation of an opened door
 *
 * @author Mike Sarton & Olivier Cordier
 */
class Door extends Parent {

    private Rectangle rectangle;

    Door(boolean b) {
        rectangle = new Rectangle(25, 90);
        Image img;
        if (b) {
            img = new Image("resources/door.png");
        } else {
            img = new Image("resources/bric.png");
        }
        rectangle.setFill(new ImagePattern(img));
        getChildren().add(rectangle);
    }

}
