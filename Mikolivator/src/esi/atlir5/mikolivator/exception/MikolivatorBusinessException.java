package esi.atlir5.mikolivator.exception;

/**
 * Personnalized exception to indicate errors in the interactions with the database.
 * @author Mike Sarton & Olivier Cordier
 */

public class MikolivatorBusinessException extends Exception {

    /**
     * Personnalized error's constructor.
     * @param msg The error message.
     */
    public MikolivatorBusinessException (String msg) {
        super (msg);
    }
}
