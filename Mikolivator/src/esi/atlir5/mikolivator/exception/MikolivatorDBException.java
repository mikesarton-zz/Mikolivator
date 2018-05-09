package esi.atlir5.mikolivator.exception;

/**
 * Personnalized exception to indicate error with the interactions with the database.
 * @author Mike Sarton & Olivier Cordier
 */

public class MikolivatorDBException extends Exception {

    /**
     * Personnalized error's constructor.
     * @param msg The error message.
     */
    public MikolivatorDBException (String msg) {
        super (msg);
    }
}
