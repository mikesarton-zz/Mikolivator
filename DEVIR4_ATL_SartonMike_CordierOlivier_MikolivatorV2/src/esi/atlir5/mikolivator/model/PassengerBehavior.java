package esi.atlir5.mikolivator.model;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
interface PassengerBehavior {

    void walk();

    void callElevator();

    void enterElevator();

    void leaveElevator();

    void makeUTurn();
}
