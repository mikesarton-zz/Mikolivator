package esi.atlir5.mikolivator.exception;

/**
 * Personnalized error to indicate an error in the simulation.
 * @author Mike Sarton & Olivier Cordier
 */

public class MikolivatorException extends Exception {

    /**
     * Personnalized error's constructor.
     * @param msg The error message.
     */
    public MikolivatorException (String msg) {
        super (msg);
    }
}
