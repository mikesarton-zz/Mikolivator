
package esi.atlir5.mikolivator.dtoDB;

import esi.atlir5.mikolivator.exception.MikolivatorDBException;

/**
 * This class is used to save the informations before or after an interaction
 * with the database
 * @author Mike Sarton & Olivier Cordier
 */
public class EvacuationDTO {
    private int evacID;
    private int floorID;
    private int numFloor;
    private int nbPeopleEvacuated;    
    
    /**
     * EvacuationDTO's constructor
     * @param evacID The evacuation's ID
     * @param floorID The floor's ID
     * @param nbPeopleEvacuated The number of people evacuated
     * @param numFloor The floor's number
     */
    public EvacuationDTO (int evacID, int floorID, int nbPeopleEvacuated, 
            int numFloor) {
        this.evacID = evacID;
        this.floorID = floorID;
        this.nbPeopleEvacuated = nbPeopleEvacuated;
        this.numFloor = numFloor;
    }
    
    /**
     * EvacuationDTO's constructor 2
     * @param numFloor The floor's number
     * @param nbPeopleEvacuated The number of people generated
     */
    public EvacuationDTO(int numFloor, int nbPeopleEvacuated) {
        this(-99, -99, nbPeopleEvacuated, numFloor);
    }

    /**
     * Getter of evacID
     * @return evacID
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public int getEvacID() throws MikolivatorDBException {
        if (evacID == -99) throw new MikolivatorDBException("evacID is null");
        return evacID;
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
     * Getter of nbPeopleEvacuated
     * @return nbPeopleEvacuated
     */
    public int getNbPeopleEvacuated() {
        return nbPeopleEvacuated;
    }

    /**
     * Getter of numFloor
     * @return numFloor
     */
    public int getNumFloor() {        
        return numFloor;
    }   
}
