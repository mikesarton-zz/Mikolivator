package esi.atlir5.mikolivator.model;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */

//  Enumération des différents mouvement qu'un ascenseur peut prendre
public enum MovementElevator {
    UP ("up"),
    STANDBY ("standby"),
    DOWN ("down");
    
    private String name;
    
    MovementElevator (String name){
        this.name = name;
    }
    
    public String getName (){
        return name;
    }
}
