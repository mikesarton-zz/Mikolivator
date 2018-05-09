package esi.atlir5.mikolivator.view.options;

import esi.atlir5.mikolivator.business.AdminFacade;
import esi.atlir5.mikolivator.exception.MikolivatorBusinessException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * FXML Menu parameters controller
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class StartViewController implements Initializable {

    private Spinner spinnerFloors;
    private Spinner spinnerPeople;
    private Spinner spinnerPeopleLift;

    @FXML
    private HBox hboxSpinFloors = new HBox();

    @FXML
    private HBox hboxSpinPeople = new HBox();

    @FXML
    private HBox hboxSpinPeopleLift = new HBox();

    @FXML
    private Button startButton = new Button();

    @FXML
    private Button cancelButton = new Button();

    @FXML
    private Button confirmButton = new Button();

    @FXML
    private Button emptyDatabaseButton = new Button();

    @FXML
    private Label messageDB = new Label();

    /**
     * Default constructor
     */
    public StartViewController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String OS = System.getProperty("os.name").toLowerCase();

        if (OS.contains("mac")) {
            spinnerFloors = createSpinner(1, 5, 150);
        } else {
            spinnerFloors = createSpinner(1, 7, 150);
        }
        spinnerFloors.getValueFactory().setValue(5);
        spinnerPeople = createSpinner(1, 100, 150);
        spinnerPeople.getValueFactory().setValue(10);
        spinnerPeopleLift = createSpinner(1, 10, 150);
        spinnerPeopleLift.getValueFactory().setValue(5);

        hboxSpinFloors.getChildren().add(spinnerFloors);
        hboxSpinPeople.getChildren().add(spinnerPeople);
        hboxSpinPeopleLift.getChildren().add(spinnerPeopleLift);

        messageDB.setVisible(false);
        visibleButtons(false);
        startButton.setOnAction((ActionEvent event) -> {
            startButton.getScene().getWindow().hide();
        });
        cancelButton.setOnAction((ActionEvent event) -> {
            visibleButtons(false);
        });
        confirmButton.setOnAction((ActionEvent event) -> {
            visibleButtons(false);
            try {
                AdminFacade.cleanTableSequence();
                messageDB.setText("DB is now empty !!!");
                delMessageDBTimer();
            } catch (MikolivatorBusinessException ex) {
                messageDB.setText("DB cannot be erased !");
                delMessageDBTimer();
            }
            messageDB.setVisible(true);
        });
        emptyDatabaseButton.setOnAction((ActionEvent event) -> {
            visibleButtons(true);
        });
    }

    private Spinner createSpinner(int min, int max, int weidth) {
        SpinnerValueFactory svf
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
        Spinner sp = new Spinner();
        sp.setValueFactory(svf);
        sp.setEditable(false);
        sp.setPrefWidth(weidth);

        return sp;
    }

    /**
     * To get the number of floor from a spin box
     *
     * @return the number of floor choosed by the user from the spin box
     */
    public int getNbFloorsFromSpinner() {
        return (Integer) spinnerFloors.getValue();
    }

    /**
     * To get the number of characters to be generated, choosed by user
     *
     * @return number of characters to be generated
     */
    public int getNbPeopleFromSpinner() {
        return (Integer) spinnerPeople.getValue();
    }

    /**
     * To get the max number of characted allowed to be int the elevator
     *
     * @return max number of characters in the elevator
     */
    public int getNbPeopleLiftFromSpinner() {
        return (Integer) spinnerPeopleLift.getValue();
    }

    private void visibleButtons(boolean b) {
        cancelButton.setVisible(b);
        confirmButton.setVisible(b);
    }

    private void delMessageDBTimer() {
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(event -> messageDB.setText(""));
        delay.play();
    }
}
