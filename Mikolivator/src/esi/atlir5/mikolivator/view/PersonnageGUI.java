package esi.atlir5.mikolivator.view;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * Graphic representation of a character
 *
 * @author Mike Sarton & Olivier Cordier
 */
class PersonnageGUI extends Parent {

    private Rectangle rectangle;

    PersonnageGUI(PersonnageState ps) {
        rectangle = new Rectangle(25, 67);
        rectangle.setFill(new ImagePattern(setImageView(ps)));
        getChildren().add(rectangle);
    }

    private Image setImageView(PersonnageState ps) {
        switch (ps) {
            case LEFTSTOP:
                return new Image("resources/olivierArretGauche.png");
            case LEFTWALKING:
                return new Image("resources/olivierMarcheGauche.png");
            case RIGHTSTOP:
                return new Image("resources/olivierArretDroite.png");
            case RIGHTWALKING:
                return new Image("resources/olivierMarcheDroite.png");
            default:
                return null;
        }
    }

    void setImage(PersonnageState ps) {
        rectangle.setFill(new ImagePattern(setImageView(ps)));
    }
}
