
package esi.atlir5.mikolivator.db;

import esi.atlir5.mikolivator.exception.MikolivatorDBException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is used for every interaction with the table Sequence of the database
 * @author Mike Sarton & Olivier Cordier
 */
public class SequenceDB {

    static synchronized int getNextNum(String sequence) throws MikolivatorDBException {
        try {
            java.sql.Connection connexion = DBManager.getConnection();
            String query = "Update Sequence set " + sequence + " = " + sequence + "+1";
            java.sql.PreparedStatement update = connexion.prepareStatement(query);
            update.execute();
            java.sql.Statement stmt = connexion.createStatement();
            query = "Select "+ sequence + " FROM Sequence";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            int nvId;
            if (rs.next()) {
                nvId = rs.getInt(sequence);
                return nvId;
            } else {
                throw new MikolivatorDBException("Nouveau n° de séquence "
                        + "inaccessible!");
            }
        } catch (java.sql.SQLException eSQL) {
            throw new MikolivatorDBException("Nouveau n° de séquence "
                    + "inaccessible!\n" + eSQL.getMessage());
        }
    }
    
    /**
     * Method used to clean the Sequence's table
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void cleanTable() throws MikolivatorDBException {
        try {
            Connection connection = DBManager.getConnection();
            Statement stmt = connection.createStatement();
            stmt.execute("DELETE FROM Sequence WHERE Simulation != 0");
            stmt.execute("INSERT INTO Sequence (Simulation, Floor, Evacuation) "
                    + "VALUES (0,0,0)");
        } catch (SQLException ex) {
            throw new MikolivatorDBException("Impossible de clean la table "
                    + "Sequence: " + ex.getMessage());
        }
    }

}
