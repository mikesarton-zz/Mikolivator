package esi.atlir5.mikolivator.view;

import esi.atlir5.mikolivator.view.otherView.MyGraph;
import esi.atlir5.mikolivator.view.otherView.NewViewBuilding;
import esi.atlir5.mikolivator.view.options.StartViewController;
import esi.atlir5.mikolivator.view.viewBD.ViewBD;
import esi.atlir5.mikolivator.exception.MikolivatorBusinessException;
import esi.atlir5.mikolivator.exception.MikolivatorDBException;
import esi.atlir5.mikolivator.main.MainApp;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public class MainViewController extends Parent {

    private Parent root = null;
    private Stage primaryStage;
    private static String OS;
    private StartViewController svc;
    private NewViewBuilding nvb;
    private MenuBar menu;
    private MyGraph myGraph;
    private BuildingGUI building;

    /**
     *
     */
    public MainViewController() {
        OS = System.getProperty("os.name").toLowerCase();
        menu = creationMenuBar();
        intro();
        initRoot();
        prepSimulation();
    }

    private void createGame() {
        int nbFloors = svc.getNbFloorsFromSpinner();
        int nbPeople = svc.getNbPeopleFromSpinner();
        int nbPeopleLift = svc.getNbPeopleLiftFromSpinner();
        int height = getHeightView(nbFloors);
        int width = getWidthView();

        building = new BuildingGUI(nbFloors, nbPeople, nbPeopleLift, nvb, myGraph);
        Pane paneView = new Pane();
        VBox vbox = new VBox();
        vbox.getChildren().addAll(menu, building);
        paneView.getStyleClass().add("main");
        paneView.getChildren().add(vbox);

        Scene scene = new Scene(paneView,width, height);
        scene.getStylesheets().add("resources/styles/style.css");

        primaryStage = new Stage();
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            building.saveDataBase();
            System.exit(0);
        });
        primaryStage.setTitle("Mikolivator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    //Option Window before the simulation
    private void initRoot() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/esi/atlir5/mikolivator/view/options/StartView.fxml"));
            root = (Parent) loader.load();
            svc = loader.getController();

            Scene viewScene = new Scene(root);
            viewScene.getStylesheets().add("resources/styles/style.css");
            root.getStyleClass().add("v");
            Stage secondaryStage = new Stage();
            secondaryStage.setOnCloseRequest((WindowEvent we) -> {
                System.exit(0);
            });
            secondaryStage.setTitle("Mikolivator View 2");
            secondaryStage.setScene(viewScene);
            secondaryStage.setResizable(false);
            secondaryStage.showAndWait();
        } catch (IOException e) {
        }
    }

    private void intro() {
        Pane rootPane = new Pane();
        rootPane.getStyleClass().add("intro");
        Scene scene = new Scene(rootPane, 600, 400);
        scene.getStylesheets().add("resources/styles/style.css");

        Stage stage = new Stage();
        stage.setOnCloseRequest((WindowEvent we) -> {
            System.exit(0);
        });
        stage.setTitle("Mikolivator intro");
        stage.setScene(scene);
        stage.setResizable(false);
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> stage.hide());
        delay.play();
        stage.showAndWait();
    }

    private MenuBar creationMenuBar() {

        MenuBar menuBar = new MenuBar();
        Menu menuGame = new Menu("Menu");
        Menu menuBD = new Menu("Database");
        Menu menuView = new Menu("View");
        MenuItem view = new MenuItem("Grid View");
        MenuItem graphicView = new MenuItem("Graphic View");
        MenuItem tableView = new MenuItem("DB View");
        MenuItem itemExit = new MenuItem("Quitter");

        //MenuItems Actions
        view.setOnAction((ActionEvent t) -> {
            createSecondView();
        });

        graphicView.setOnAction((ActionEvent t) -> {
            createGraphView();
        });

        tableView.setOnAction((ActionEvent t) -> {
            try {
                createTableView();
            } catch (IOException | MikolivatorBusinessException | MikolivatorDBException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        itemExit.setOnAction((ActionEvent t) -> {
            building.saveDataBase();
            System.exit(0);
        });

        menuGame.getItems().addAll(itemExit);
        menuView.getItems().addAll(view, graphicView);
        menuBD.getItems().add(tableView);
        menuBar.getMenus().addAll(menuGame, menuView, menuBD);

        return menuBar;
    }

    //BD View TableView and PieChart
    private void createTableView() throws IOException, MikolivatorBusinessException, MikolivatorDBException {
        menu.getMenus().get(2).getItems().get(0).setDisable(true);
        Stage stage = new Stage();
        HBox hbox = new HBox();
        hbox.getStyleClass().add("tableView");
        ViewBD viewBD = new ViewBD();
        hbox.getChildren().add(viewBD);
        stage.setOnCloseRequest((WindowEvent we) -> {
            menu.getMenus().get(2).getItems().get(0).setDisable(false);
        });
        Scene viewScene = new Scene(hbox);
        stage.setTitle("BD Simulation Information");
        viewScene.getStylesheets().add("resources/styles/style.css");
        stage.setScene(viewScene);
        stage.setResizable(false);
        stage.show();
    }

    //Grid View Building
    private void createSecondView() {
        menu.getMenus().get(1).getItems().get(0).setDisable(true);
        Pane window = new Pane();
        window.getChildren().add(nvb);
        Scene viewScene = new Scene(window);
        viewScene.getStylesheets().add("resources/styles/style.css");
        Stage secondaryStage = new Stage();
        secondaryStage.setOnCloseRequest((WindowEvent we) -> {
            menu.getMenus().get(1).getItems().get(0).setDisable(false);
        });
        secondaryStage.setTitle("Grid Number View");
        secondaryStage.setScene(viewScene);
        secondaryStage.setResizable(false);
        secondaryStage.show();
    }

    /**
     * Method use yo know if the Simulation is launched on a iOS operating
     * system
     *
     * @return if the simulation is runnong on a Mac computer
     */
    public static boolean isMac() {
        return (OS.contains("mac"));
    }

    private static int getHeightView(int floors) {
        if (isMac()) {
            return 100 * (floors + 1) + 25;
        } else {
            return 100 * (floors + 1) + 4;
        }
    }
    private static int getWidthView(){
              if (isMac()) {
            return 790;
        } else {
            return 780;
        } 
    }

    //View Graphic simulation
    private void createGraphView() {
        menu.getMenus().get(1).getItems().get(1).setDisable(true);
        Pane window = new Pane();
        window.getChildren().add(myGraph);
        Scene viewScene = new Scene(window);
        viewScene.getStylesheets().add("resources/styles/style.css");
        Stage secondaryStage = new Stage();
        secondaryStage.setOnCloseRequest((WindowEvent we) -> {
            menu.getMenus().get(1).getItems().get(1).setDisable(false);
        });
        secondaryStage.setTitle("Graphic Floors");
        secondaryStage.setScene(viewScene);
        secondaryStage.setResizable(false);
        secondaryStage.show();
    }

    //To initialize the differents view and create the simulation
    //Used to prepare the simulations, the first time and for every new simulation
    private void prepSimulation() {
        myGraph = new MyGraph(svc.getNbFloorsFromSpinner());
        nvb = new NewViewBuilding(svc.getNbFloorsFromSpinner());
        createGame();
    }

}
