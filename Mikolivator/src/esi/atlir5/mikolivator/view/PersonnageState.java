package esi.atlir5.mikolivator.view;

/**
 * Charactter's state enumeration
 *
 * @author Mike Sarton & Olivier Cordier
 */
enum PersonnageState {
    LEFTSTOP("Left & Stop"),
    LEFTWALKING("Left & Walking"),
    RIGHTSTOP("Right & Stop"),
    RIGHTWALKING("Right & Walking");

    private String stateName;

    PersonnageState(String state) {
        stateName = state;
    }

    String getStateName() {
        return stateName;
    }
}
