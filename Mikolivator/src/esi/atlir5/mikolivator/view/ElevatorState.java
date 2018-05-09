package esi.atlir5.mikolivator.view;

/**
 * Elevator's status enumerations
 *
 * @author Mike Sarton & Olivier Cordier
 */
enum ElevatorState {
    OPENEMPTY("Open & empty"),
    OPENPEOPLE("Open & people"),
    CLOSED("Closed");

    private String name;

    ElevatorState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
