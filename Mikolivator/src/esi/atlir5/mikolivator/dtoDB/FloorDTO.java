
package esi.atlir5.mikolivator.dtoDB;

import esi.atlir5.mikolivator.exception.MikolivatorDBException;

/**
 * This class is used before or after any interaction with the table Floor
 * of the database
 * @author Mike Sarton & Olivier Cordier
 */
public class FloorDTO {
    private int floorID;
    private int numFloor;
    private int nbPrsTotal;
    private int simID;
    
    /**
     * FloorDTO's constructor
     * @param floorID The floor's ID
     * @param numFloor The floor's number
     * @param nbPrsTotal The number of people who walk on this floor
     * @param simID The simulation's ID
     */
    public FloorDTO (int floorID, int numFloor, int nbPrsTotal, int simID) {
        this.floorID = floorID;
        this.numFloor = numFloor;
        this.nbPrsTotal = nbPrsTotal;
        this.simID = simID;
    }
    
    /**
     * FloorDTO's constructor 2
     * @param numFloor The floor's number
     */
    public FloorDTO(int numFloor) {
        this(-99, numFloor, 0, -99);
    }
    
    /**
     * Getter of floorID
     * @return floorID
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public int getFloorID() throws MikolivatorDBException {
        if (floorID == -99) throw new MikolivatorDBException("floorID is null");
        return floorID;
    }

    /**
     * Getter of numFloor
     * @return numFloor
     */
    public int getNumFloor() {
        return numFloor;
    }

    /**
     * Getter of nbPrsTotal
     * @return nbPrsTotal
     */
    public int getNbPrsTotal() {
        return nbPrsTotal;
    }

    /**
     * Getter of simID
     * @return simID
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public int getSimID() throws MikolivatorDBException {
        if (simID == -99) throw new MikolivatorDBException("simID is null");
        return simID;
    }
    
    
}
