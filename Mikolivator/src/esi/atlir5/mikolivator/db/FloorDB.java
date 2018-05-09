
package esi.atlir5.mikolivator.db;

import esi.atlir5.mikolivator.dtoDB.FloorDTO;
import esi.atlir5.mikolivator.exception.MikolivatorDBException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is used for every interaction with the table Floor in the dabatase
 * @author Mike Sarton & Olivier Cordier
 */
public class FloorDB {

    /**
     * Add an entry in the floor table in the database
     * @param floor The floor DTO to add.
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void addFloor (FloorDTO floor) throws MikolivatorDBException {
        try {
            int num = SequenceDB.getNextNum("Floor");
            
            Connection connection = DBManager.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO "
                    + "Floor(floorID, numFloor, nbPrsTotal, simulation) "
                    + "values(?, ?, ?, ?)");
            stmt.setInt(1, num);
            stmt.setInt(2, floor.getNumFloor());
            stmt.setInt(3, floor.getNbPrsTotal());
            stmt.setInt(4, SimulationDB.getLastSimulationID());
            
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new MikolivatorDBException("Erreur ajout d'une simulation: " 
                    + ex.getMessage());
        }
    }
    
    /**
     * Check if an floor exists in the database.
     * @param id The floor's ID
     * @return True if the record exists, false otherwise
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static boolean floorExist(int id) throws MikolivatorDBException {
        try {
            Connection connection = DBManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT floorID FROM Floor WHERE "
                    + "floorID = " + id);
            return rs.next();
        } catch (SQLException ex) {
            throw new MikolivatorDBException(ex.getMessage());
        }
    }
    
    /**
     * Delete a floor in the floor's table from the database
     * @param id The floor's ID
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void deleteFloor(int id) throws MikolivatorDBException {
        try {
            if (!floorExist(id)) {
                return;
            }
            Connection connection = DBManager.getConnection();
            Statement stmt = connection.createStatement();
            stmt.execute("DELETE FROM Floor WHERE floorID = " + id);
        } catch (SQLException ex) {
            throw new MikolivatorDBException(ex.getMessage());
        } catch (MikolivatorDBException ex) {
            throw new MikolivatorDBException("Floor inexistant: " + ex.getMessage());
        }
    }
    
    /**
     * Return all the ID of each floor related to a certain Simulation's ID
     * @param id The Simulation's ID
     * @return A list with all the ID of each floor related to the simulation's ID
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static List<Integer> getFloorsIDBySimID(int id) 
            throws MikolivatorDBException {
        try {
            List<Integer> listFloorID = new ArrayList<>();
            Connection connection = DBManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT floorID FROM Floor "
                    + "WHERE simulation = " + id);
            while (rs.next()) {
                listFloorID.add(rs.getInt("floorID"));
            }
            return listFloorID;
        } catch (SQLException ex) {
            throw new MikolivatorDBException(ex.getMessage());
        }
    }        
        
    /**
     * Return a list of FloorDTO when the view needs it according to a certain
     * Simulation's ID
     * @param simID The Simulation's ID
     * @return A list with all the FloorDTO related to a certain simulation's ID
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static ObservableList<FloorDTO> getFloorsViewBySimID(int simID) 
            throws MikolivatorDBException {
        try {
            List<FloorDTO> floors = new ArrayList<>();
            Connection connection = DBManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT floorID, nbPrsTotal, numFloor "
                    + "FROM Floor WHERE simulation = " + simID);
            while (rs.next()) {
                FloorDTO floor = new FloorDTO(rs.getInt("floorID"), 
                        rs.getInt("numFloor"), rs.getInt("nbPrsTotal"), simID);
                floors.add(floor);
            }
            return FXCollections.observableArrayList(floors);
        } catch (SQLException ex) {
            throw new MikolivatorDBException(ex.getMessage());
        }
    }
    
    /**
     * Method used when the simulation is over and we need to update the stats
     * of a floor
     * @param numFloor The floor's number
     * @param nbPrsTotal The number of people who walk on this floor
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void updateFloor(int numFloor, int nbPrsTotal) 
            throws MikolivatorDBException {
        try {
            Connection connection = DBManager.getConnection();
            Statement stmt = connection.createStatement();
            stmt.execute("UPDATE Floor SET nbPrsTotal = " + nbPrsTotal 
                    + " WHERE numFloor = " + numFloor + " AND simulation = " 
                    + SimulationDB.getLastSimulationID());
        } catch (SQLException ex) {
            throw new MikolivatorDBException(ex.getMessage());
        }
    }
    
    /**
     * Return the floor's ID of a certain floor of the current simulation
     * @param numFloor The floor's number we need the floor's ID
     * @return The floor's ID
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static int getFloorIDOfFloor(int numFloor) throws MikolivatorDBException {
        try {
            Connection connection = DBManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT floorID FROM Floor "
                    + "WHERE numFloor = " + numFloor 
                    + " AND simulation = " + SimulationDB.getLastSimulationID());
            if (rs.next()) {
                return rs.getInt("floorID");
            } else {
                return -99;
            }
        } catch (SQLException ex) {
            throw new MikolivatorDBException(ex.getMessage());
        }
    }
}
