package esi.atlir5.mikolivator.model;

/**
 * Enumération des différents états qu'une personne peut prendre
 * @author Mike Sarton & Olivier Cordier
 */

public enum PassengerState {

    /**
     * Etat lorsque la personne marche en direction de l'ascenseur.
     */
    WALKINGTOELEVATOR (1, "WalkingToElevator"),

    /**
     * Etat lorsque la personne marche en direction du couloir.
     */
    WALKINGTOCORRIDOR (-1, "WalkingToCorridor"),

    /**
     * Etat lorsque la personne attend un ascenseur.
     */
    WAITINGELEVATOR (0, "WaitingElevator"),

    /**
     * Etat lorsque la personne est cachée au fond du couloir.
     */
    HIDDEN (0, "Hidden"),

    /**
     * Etat lorsque la personne est dans un ascenseur.
     */
    INELEVATOR (0, "InElevator");
    
    private final int value;
    private final String name;

    private PassengerState(int nb, String name) {
        value = nb;
        this.name = name;
    }
    
    int getValue() {
        return value;
    }
    
    /**
     * Retourne une représentation textuelle des différents états qu'une personne
     * peut prendre.
     * @return
     */
    @Override
    public String toString(){
        return name;
    }
}
