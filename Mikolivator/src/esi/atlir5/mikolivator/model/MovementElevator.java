package esi.atlir5.mikolivator.model;

/**
 * Enumération des différents mouvements qu'un ascenseur peut prendre.
 * @author Mike Sarton & Olivier Cordier
 */

public enum MovementElevator {

    /**
     * Etat lorsque l'ascenseur est en train de monter.
     */
    UP ("up"),

    /**
     * Etat lorsque l'ascenseur est à l'arrêt.
     */
    STANDBY ("standby"),

    /**
     * Etat lorsque l'ascenseur est en train de descendre.
     */
    DOWN ("down");
    
    private final String name;
    
    MovementElevator (String name){
        this.name = name;
    }
    
    String getName (){
        return name;
    }
    
    /**
     * Retourner une représentation textuelle du mouvement de l'ascenseur.
     * @return Le nom du mouvement
     */
    @Override
    public String toString(){
        return name;
    }
}
