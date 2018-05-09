
package esi.atlir5.mikolivator.db;

import esi.atlir5.mikolivator.dtoDB.SimulationDTO;
import esi.atlir5.mikolivator.exception.MikolivatorDBException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is used for every interaction with the Simulation's table in
 * the database
 * @author Mike Sarton & Olivier Cordier
 */
public class SimulationDB {

    /**
     * Add an entry in the database in the table Simulation
     * @param sim The Simulation DTO to add
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void addSimulation (SimulationDTO sim) 
            throws MikolivatorDBException {
        try {
            int num = SequenceDB.getNextNum("Simulation");
            
            Connection connection = DBManager.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO "
                    + "Simulation(simID, dateDebut, dateFin, nbPrsGenerated, "
                    + "nbPrsElevator) values(?, ?, ?, ?, ?)");
            stmt.setInt(1, num);
            stmt.setTimestamp(2, sim.getStartTimeDB());
            stmt.setTimestamp(3, sim.getEndTimeDB());
            stmt.setInt(4, sim.getNbPersGenerated());
            stmt.setInt(5, sim.getNbPersInElevator());
            
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new MikolivatorDBException("Erreur ajout d'une simulation: " 
                    + ex.getMessage());
        }
    }
    
    private static boolean simulationExist(int id) throws MikolivatorDBException {
        try {
            Connection connection = DBManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT simID FROM Simulation WHERE "
                    + "simID = " + id);
            return rs.next();
        } catch (SQLException ex) {
            throw new MikolivatorDBException(ex.getMessage());
        }
    }
    
    /**
     * Deletes a certain simulation
     * @param id The simulation's ID to delete
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void deleteSimulation(int id) throws MikolivatorDBException {
        try {
            if (!simulationExist(id)) {
                return;
            }
            Connection connection = DBManager.getConnection();
            Statement stmt = connection.createStatement();
            stmt.execute("DELETE FROM Simulation WHERE simID = " + id);
        } catch (SQLException ex) {
            throw new MikolivatorDBException(ex.getMessage());
        } catch (MikolivatorDBException ex) {
            throw new MikolivatorDBException("Simulation inexistante: " + ex.getMessage());
        }
    }
    
    /**
     * Returns all the simulations records from the database
     * @return A list with all the Simulation DTO from the database
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static ObservableList<SimulationDTO> getAllSimulations() 
            throws MikolivatorDBException {
        try {
            List<SimulationDTO> simulations = new ArrayList<>();
            Connection connection = DBManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Simulation");
            while (rs.next()) {
                SimulationDTO sim = new SimulationDTO(rs.getInt("SIMID"), 
                        rs.getInt("NBPRSGENERATED"), rs.getInt("NBPRSELEVATOR"), 
                        rs.getTimestamp("DATEDEBUT"), rs.getTimestamp("DATEFIN"));
                simulations.add(sim);
            }
            return FXCollections.observableArrayList(simulations);
        } catch (SQLException ex) {
            throw new MikolivatorDBException(ex.getMessage());
        }
    }
    
    /**
     * Methode used when we want to update the simulation record of the current
     * simulation (when the simulation is over)
     * @param nbPrsGenerated The number of people generated during the simulation
     * @param ts The timestamp related to the simulation's end
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void updateSimulation(int nbPrsGenerated, Timestamp ts) 
            throws MikolivatorDBException {
        try {
            Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement("UPDATE "
                    + "Simulation SET nbPrsGenerated = ? , dateFin = ? WHERE "
                    + "simID = ?");
            stmt.setInt(1, nbPrsGenerated);
            stmt.setTimestamp(2, ts);
            stmt.setInt(3, getLastSimulationID());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new MikolivatorDBException(ex.getMessage());
        }
    }

    /**
     * Returns the last ID of the simulations from the database
     * @return The last ID of the simulations from the database
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static int getLastSimulationID() throws MikolivatorDBException {
        try {
            Connection connection = DBManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT max(simID) as simID FROM Simulation");
            if (rs.next()){
                return rs.getInt("simID");
            } else {
                return -99;
            }
        } catch (SQLException ex) {
            throw new MikolivatorDBException(ex.getMessage());
        }
    }
}
