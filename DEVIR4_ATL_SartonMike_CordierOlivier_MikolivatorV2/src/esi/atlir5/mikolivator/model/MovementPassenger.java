package esi.atlir5.mikolivator.model;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
enum MovementPassenger {
    TOELEVATOR ("toElevator"),
    TOCORRIDOR ("toCorridor");
    
    private String name;

    private MovementPassenger(String name) {
        this.name = name;
    }
        
    String getName() {
        return name;
    }
}
