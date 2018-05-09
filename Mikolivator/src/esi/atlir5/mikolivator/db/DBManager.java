
package esi.atlir5.mikolivator.db;

import esi.atlir5.mikolivator.exception.MikolivatorDBException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is used in every interaction with the database. It starts the 
 * transactions, valide them and cancel them if necessary.
 * @author Mike Sarton & Olivier Cordier
 */
public class DBManager {
    private static Connection connection;
    
    /**
     * Method used before any interaction with the database to get the connection
     * with the database
     * @return The connection with the database
     * @throws MikolivatorDBException If something goes wrong, an exception is thrown
     * with the appropriate message
     */
    public static Connection getConnection() throws MikolivatorDBException {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:derby://localhost:1527/MikolivatorDB", "mikoli", "ilokim");
            } catch (SQLException ex) {
                throw new MikolivatorDBException(ex.getMessage());
            }
        }
        return connection;
    }
    
    /**
     * Method used to start a transaction before any interaction with the database
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void startTransaction() throws MikolivatorDBException {
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException ex) {
            throw new MikolivatorDBException("Erreur dans le démarrage d'une "
                    + "transaction: " + ex.getMessage());
        }
    }
    
    /**
     * Method used to start a transaction with a certain isolation level before
     * any interaction with the database
     * @param isolationLevel The isolation level
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void startTransaction(int isolationLevel) 
            throws MikolivatorDBException {
        try {
            getConnection().setAutoCommit(false);

            int isol = 0;
            switch (isolationLevel) {
                case 0:
                    isol = java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;
                    break;
                case 1:
                    isol = java.sql.Connection.TRANSACTION_READ_COMMITTED;
                    break;
                case 2:
                    isol = java.sql.Connection.TRANSACTION_REPEATABLE_READ;
                    break;
                case 3:
                    isol = java.sql.Connection.TRANSACTION_SERIALIZABLE;
                    break;
                default:
                    throw new MikolivatorDBException("Degré d'isolation "
                            + "inexistant!");
            }
            getConnection().setTransactionIsolation(isol);
        } catch (SQLException ex) {
            throw new MikolivatorDBException("Impossible de démarrer une "
                    + "transaction: "+ex.getMessage());
        }
    }
    
    /**
     * Method used to validate a transaction into the database
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void validateTransaction() throws MikolivatorDBException {
        try {
            getConnection().commit();
            getConnection().setAutoCommit(true);
        } catch (SQLException ex) {
            throw new MikolivatorDBException("Impossible de valider la "
                    + "transaction: "+ex.getMessage());
        }
    }
    
    /**
     * Method used to cancel a transaction if something went wrong during the
     * transaction
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void cancelTransaction() throws MikolivatorDBException {
        try {
            getConnection().rollback();
            getConnection().setAutoCommit(true);
        } catch (SQLException ex) {
            throw new MikolivatorDBException("Impossible d'annuler la "
                    + "transaction: "+ex.getMessage());
        }
    }
}
