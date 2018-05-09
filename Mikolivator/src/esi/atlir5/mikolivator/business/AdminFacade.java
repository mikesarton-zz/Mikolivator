package esi.atlir5.mikolivator.business;

import esi.atlir5.mikolivator.dtoDB.EvacuationDTO;
import esi.atlir5.mikolivator.dtoDB.FloorDTO;
import esi.atlir5.mikolivator.dtoDB.SimulationDTO;
import esi.atlir5.mikolivator.db.DBManager;
import esi.atlir5.mikolivator.db.EvacuationDB;
import esi.atlir5.mikolivator.db.FloorDB;
import esi.atlir5.mikolivator.db.SequenceDB;
import esi.atlir5.mikolivator.db.SimulationDB;
import esi.atlir5.mikolivator.exception.MikolivatorBusinessException;
import esi.atlir5.mikolivator.exception.MikolivatorDBException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

/**
 * This class is used as facade by the program to interact with the database.
 * @author Mike Sarton & Olivier Cordier
 */
public class AdminFacade {
    
    /**
     * Add an entry in the database.
     * @param type The table in which we want to add an entry
     * @param obj The object who contains the data we want to add
     * @throws MikolivatorBusinessException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void addEntry(TablesDB type, Object obj) 
            throws MikolivatorBusinessException{
        try {
            DBManager.startTransaction();
            switch (type) {
                case SIMULATION:
                    SimulationDB.addSimulation((SimulationDTO) obj);
                    break;
                case EVACUATION:
                    EvacuationDB.addEvacuation((EvacuationDTO) obj);
                    break;
                case FLOOR:
                    FloorDB.addFloor((FloorDTO) obj);
                    break;
            }
            DBManager.validateTransaction();
        } catch (MikolivatorDBException ex) {
            String errMsg = ex.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (MikolivatorDBException exx) {
                errMsg += " + " + exx.getMessage();
            } finally {
                throw new MikolivatorBusinessException("Impossible d'ajouter "
                        + "une entrée: " + errMsg);
            }
        }
    }
    
    /**
     * Delete an entry in the database
     * @param type The table in which we want to delete an entry
     * @param id The id of the record we want to delete
     * @throws MikolivatorBusinessException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void deleteEntry(TablesDB type, int id) 
            throws MikolivatorBusinessException {
        try {
            DBManager.startTransaction();
            switch (type) {
                case EVACUATION: EvacuationDB.deleteEvacuation(id);
                break;
                case FLOOR: deleteFloor(id);
                break;
                case SIMULATION: deleteSimulation(id);
                break;
                default: break;
            }
            DBManager.validateTransaction();
        } catch (MikolivatorDBException ex) {
            String errMsg = ex.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (MikolivatorDBException exx) {
                errMsg += " + " + exx.getMessage();
            } finally {
                throw new MikolivatorBusinessException("Impossible de supprimer "
                        + "une entrée: " + errMsg);
            }
        }
    }
    
    /**
     * Clean the 3 tables of the database: Simulation, Floor and Evacuation.
     * @throws MikolivatorBusinessException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void cleanDatabase() throws MikolivatorBusinessException {
        try {
            DBManager.startTransaction();
            for (int i=SimulationDB.getLastSimulationID(); i>=1; --i) {
                deleteEntry(TablesDB.SIMULATION, i);
            }
            DBManager.validateTransaction();
        } catch (MikolivatorDBException ex) {
            String errMsg = ex.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (MikolivatorDBException exx) {
                errMsg += " + " + exx.getMessage();
            } finally {
                throw new MikolivatorBusinessException("Impossible de supprimer "
                        + "une entrée: " + errMsg);
            }
        }
    }
    
    /**
     * Clean all the tables of the database including the table Sequence.
     * @throws MikolivatorBusinessException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void cleanTableSequence() throws MikolivatorBusinessException {
        try {
            DBManager.startTransaction();
            cleanDatabase();
            SequenceDB.cleanTable();
            DBManager.validateTransaction();
        } catch (MikolivatorDBException ex) {
            String errMsg = ex.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (MikolivatorDBException exx) {
                errMsg += " + " + exx.getMessage();
            } finally {
                throw new MikolivatorBusinessException("Impossible de récupérer "
                        + "les infos des évacuations: " + errMsg);
            }
        }
    }
    
    /**
     * Method used when the view needs all the informations in the database
     * about the evacuations related to a certain simulation.
     * @param simID The Simulation's ID 
     * @return A list of all the evacuations records via the Evacuation Data
     * Transfert Objects.
     * @throws MikolivatorBusinessException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static ObservableList<EvacuationDTO> getEvacuationsInfos(int simID) 
            throws MikolivatorBusinessException {
        try {
            ObservableList<EvacuationDTO> evacuations;
            DBManager.startTransaction();
            evacuations = EvacuationDB.getEvacuationsViewBySimID(simID);
            DBManager.validateTransaction();
            return evacuations;
        } catch (MikolivatorDBException ex) {
            String errMsg = ex.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (MikolivatorDBException exx) {
                errMsg += " + " + exx.getMessage();
            } finally {
                throw new MikolivatorBusinessException("Impossible de récupérer "
                        + "les infos des évacuations: " + errMsg);
            }
        }
    }

    /**
     * Method used when the view needs all the informations of the database
     * about the floors of a certain simulation.
     * @param simID The Simulation's ID
     * @return A list of all the floors records via the Floors Data Transfert
     * Objects
     * @throws MikolivatorBusinessException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static ObservableList<FloorDTO> getFloorsInfos(int simID) 
            throws MikolivatorBusinessException {
        try {
            ObservableList<FloorDTO> floors;
            DBManager.startTransaction();
            floors = FloorDB.getFloorsViewBySimID(simID);
            DBManager.validateTransaction();
            return floors;
        } catch (MikolivatorDBException ex) {
            String errMsg = ex.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (MikolivatorDBException exx) {
                errMsg += " + " + exx.getMessage();
            } finally {
                throw new MikolivatorBusinessException("Impossible de récupérer "
                        + "les infos des étages: " + errMsg);
            }
        }
    }
    
    /**
     * Method used when the view needs all the informations from the database
     * about all the previous Simulations.
     * @return A list with all the Simulations records via the Simulation Data Transfert
     * Objects.
     * @throws MikolivatorBusinessException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static ObservableList<SimulationDTO> getSimulationsInfos() 
            throws MikolivatorBusinessException {
        try {
            ObservableList<SimulationDTO> simulations;
            DBManager.startTransaction();
            simulations = SimulationDB.getAllSimulations();
            DBManager.validateTransaction();
            return simulations;
        } catch (MikolivatorDBException ex) {
            String errMsg = ex.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (MikolivatorDBException exx) {
                errMsg += " + " + exx.getMessage();
            } finally {
                throw new MikolivatorBusinessException("Impossible de récupérer "
                        + "les infos des simulations: " + errMsg);
            }
        }
    }
    
    /**
     * Method used when the Simulation is over and we want to update the informations
     * of the last simulation.
     * @param nbPrsGenerated The number of people generated during this simulation
     * @param ts The timestamp related to the Simulation's end
     * @throws MikolivatorBusinessException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void updateSimulation(int nbPrsGenerated, Timestamp ts) 
            throws MikolivatorBusinessException {
        try {
            DBManager.startTransaction();
            SimulationDB.updateSimulation(nbPrsGenerated, ts);
            DBManager.validateTransaction();
        } catch (MikolivatorDBException ex) {
            String errMsg = ex.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (MikolivatorDBException exx) {
                errMsg += " + " + exx.getMessage();
            } finally {
                throw new MikolivatorBusinessException("Impossible d'update la "
                        + "simulation: " + errMsg);
            }
        }
    }
    
    /**
     * Method used when the simulation is over and we want to update the 
     * informations of the last simulation's floors.
     * @param numFloor The floor's number
     * @param nbPrsTotal The number of people who walk on this floor during the 
     * simulation
     * @throws MikolivatorBusinessException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public static void updateFloor(int numFloor, int nbPrsTotal) 
            throws MikolivatorBusinessException {
        try {
            DBManager.startTransaction();
            FloorDB.updateFloor(numFloor, nbPrsTotal);
            DBManager.validateTransaction();
        } catch (MikolivatorDBException ex) {
            String errMsg = ex.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (MikolivatorDBException exx) {
                errMsg += " + " + exx.getMessage();
            } finally {
                throw new MikolivatorBusinessException("Impossible d'update "
                        + "l'étage: " + errMsg);
            }
        }
    }
    
    private static void deleteFloor(int id) throws MikolivatorBusinessException {
        try {
            List<Integer> evacID = EvacuationDB.getEvacuationsIDByFloorID(id);
            for (int i : evacID) {
                EvacuationDB.deleteEvacuation(i);
            }
            FloorDB.deleteFloor(id);
        } catch (MikolivatorDBException ex) {
            throw new MikolivatorBusinessException("Impossible de supprimer un "
                    + "floor: " + ex.getMessage());
        }
    }
    
    private static void deleteSimulation(int id) throws MikolivatorBusinessException {
        try {
            List<Integer> floorID = FloorDB.getFloorsIDBySimID(id);
            List<List<Integer>> evacID = new ArrayList<>();
            for (int i : floorID) {
                evacID.add(EvacuationDB.getEvacuationsIDByFloorID(i));
            }
            for (List<Integer> l : evacID) {
                for (int i : l) {
                    EvacuationDB.deleteEvacuation(i);
                }
            }
            for (int i : floorID) {
                FloorDB.deleteFloor(i);
            }
            SimulationDB.deleteSimulation(id);
        } catch (MikolivatorDBException ex) {
            throw new MikolivatorBusinessException("Impossible de supprimer une "
                    + "simulation : " + ex.getMessage());
        }
    }
}
