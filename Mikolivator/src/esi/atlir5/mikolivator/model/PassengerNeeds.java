package esi.atlir5.mikolivator.model;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */

//  Enumeration des différents besoins d'une personne
enum PassengerNeeds {
    NEEDDESTINATION ("NeedDestination"),
    NEEDELEVATOR ("NeedElevator");

    private final String name;
    
    private PassengerNeeds(String name) {
        this.name = name;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
