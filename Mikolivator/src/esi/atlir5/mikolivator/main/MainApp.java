package esi.atlir5.mikolivator.main;

import esi.atlir5.mikolivator.exception.MikolivatorBusinessException;
import esi.atlir5.mikolivator.view.MainViewController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class launching the Simulation
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws MikolivatorBusinessException {
        new MainViewController();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
    }

    @Override
    public void init() {
    }
}
