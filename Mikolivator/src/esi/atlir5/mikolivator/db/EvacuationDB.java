
package esi.atlir5.mikolivator.db;

import esi.atlir5.mikolivator.dtoDB.EvacuationDTO;
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
 * This class is used for every interaction with the table Evacuation in the
 * database.
 * @author Mike Sarton & Olivier Cordier
 */
public class EvacuationDB {

    /**
     * Add an entry into the Evacuation table in the database
     * @param evac The Evacuation Data Transfert Object containing the informations
     * to add in the database
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void addEvacuation (EvacuationDTO evac) throws MikolivatorDBException {
        try {
            int num = SequenceDB.getNextNum("Evacuation");
            
            Connection connection = DBManager.getConnection();
           
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO "
                    + "Evacuation(evacID, floorID, nbPeopleEvacuated) values(?, ?, ?)");
            stmt.setInt(1, num);
            stmt.setInt(2, FloorDB.getFloorIDOfFloor(evac.getNumFloor()));
            stmt.setInt(3, evac.getNbPeopleEvacuated());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new MikolivatorDBException("Erreur ajout d'une simulation: " 
                    + ex.getMessage());
        }
    }
    
    /**
     * Check if an evacuation exist in the database
     * @param id The Evacuation's ID
     * @return True if it exists, false otherwise
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static boolean evacuationExist(int id) throws MikolivatorDBException {
        try {
            Connection connection = DBManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT evacID FROM Evacuation "
                    + "WHERE evacID = " + id);
            return rs.next();
        } catch (SQLException ex) {
            throw new MikolivatorDBException(ex.getMessage());
        }
    }
    
    /**
     * Delete an evacuation from the database
     * @param id The Evacuation's ID
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void deleteEvacuation (int id) throws MikolivatorDBException {
        try {
            if (!evacuationExist(id)) return;
            Connection connection = DBManager.getConnection();
            Statement stmt = connection.createStatement();
            stmt.execute("DELETE FROM Evacuation WHERE evacID = " + id);
        } catch (SQLException ex) {
            throw new MikolivatorDBException(ex.getMessage());
        }
    }
    
    /**
     * Return all the evacuations's ID related to a certain Floor.
     * @param id The Floor's ID
     * @return A list with all the ID of each evacuation related to the floor's ID
     * passed in parameter
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static List<Integer> getEvacuationsIDByFloorID(int id) 
            throws MikolivatorDBException {
        try {
            List<Integer> listEvacID = new ArrayList<>();
            Connection connection = DBManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT evacID FROM Evacuation "
                    + "WHERE floorID = " + id);
            while (rs.next()) {
                listEvacID.add(rs.getInt("evacID"));
            }
            return listEvacID;
        } catch (SQLException ex) {
            throw new MikolivatorDBException(ex.getMessage());
        }
    }
    
    /**
     * Return all the evacuations records from the database via the Evacuation
     * Data Transfert Object when the view needs them.
     * @param simID The Simulation's ID
     * @return A list with all the Evacuation DTO related to this simulation
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static ObservableList<EvacuationDTO> getEvacuationsViewBySimID 
        (int simID) throws MikolivatorDBException {
        try {
            List<EvacuationDTO> evacuations = new ArrayList<>();
            Connection connection = DBManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT evacID, e.floorID, "
                    + "nbPeopleEvacuated, numFloor "
                    + "FROM Evacuation e JOIN Floor f ON e.floorID = f.floorID "
                    + "WHERE simulation = " + simID);
            while (rs.next()) {
                EvacuationDTO evac = new EvacuationDTO(rs.getInt("evacID"), 
                        rs.getInt("floorID"), rs.getInt("nbPeopleEvacuated"), 
                        rs.getInt("numFloor"));
                evacuations.add(evac);
            }
            return FXCollections.observableArrayList(evacuations);
        } catch (SQLException ex) {
            throw new MikolivatorDBException(ex.getMessage());
        }
    }
}
