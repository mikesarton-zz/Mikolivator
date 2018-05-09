
package esi.atlir5.mikolivator.business;

/**
 * This enumeration is used for specify which table is modified.
 * @author Mike Sarton & Olivier Cordier
 */
public enum TablesDB {

    /**
     *
     */
    SIMULATION ("Simulation"),

    /**
     *
     */
    FLOOR ("Floor"),

    /**
     *
     */
    EVACUATION ("Evacuation");
    
    private String name;
    
    TablesDB (String name) {
        this.name = name;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
