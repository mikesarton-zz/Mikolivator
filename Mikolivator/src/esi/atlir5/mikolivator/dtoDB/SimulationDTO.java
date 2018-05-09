package esi.atlir5.mikolivator.dtoDB;

import esi.atlir5.mikolivator.exception.MikolivatorDBException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * This class is used before or after any interaction with the table Simulation
 * in the database
 * @author Mike Sarton & Olivier Cordier
 */
public class SimulationDTO {

    private Integer simID;
    private Integer nbPersGenerated;
    private Integer nbPersInElevator;
    private Timestamp startTime;
    private Timestamp endTime;

    /**
     * SimulationDTO's constructor
     * @param simID Simulation's ID
     * @param nbPersGenerated The number of people generated
     * @param nbPersInElevator The number of people maximum in an elevator
     * @param startTime The start time
     * @param endTime The end time
     */
    public SimulationDTO(Integer simID, Integer nbPersGenerated,
            Integer nbPersInElevator, Timestamp startTime, Timestamp endTime) {
        this.simID = simID;
        this.nbPersGenerated = nbPersGenerated;
        this.nbPersInElevator = nbPersInElevator;
        this.startTime = new Timestamp(startTime.getTime());
        this.endTime = new Timestamp(endTime.getTime());
    }

    /**
     * SimulationDTO's constructor 2
     * @param nbPersInElevator The number of people maximum in an elevator
     */
    public SimulationDTO(Integer nbPersInElevator) {
        this(-99, 0, nbPersInElevator, new Timestamp(new Date().getTime()), 
                new Timestamp(new Date().getTime()));
    }

    /**
     * Getter of simID
     * @return simID
     * @throws MikolivatorDBException If something goes wrong, an exception
     * is thrown with the appropriate message
     */
    public Integer getSimID() throws MikolivatorDBException {
        if (simID == -99) {
            throw new MikolivatorDBException("simID is null");
        }
        return simID;
    }

    /**
     * Getter of nbPrsGenerated
     * @return nbPrsGenerated
     */
    public Integer getNbPersGenerated() {
        return nbPersGenerated;
    }

    /**
     * Getter of nbPersInElevator
     * @return nbPrsInElevator
     */
    public Integer getNbPersInElevator() {
        return nbPersInElevator;
    }

    /**
     * Getter of startTime 
     * @return startTime
     */
    public Timestamp getStartTimeDB() {
        return new Timestamp(startTime.getTime());
    }

    /**
     * Getter of startTime as a String for the view
     * @return startTime as a String
     */
    public String getStartTime() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(startTime);
    }

    /**
     * Getter of endTime
     * @return endTime
     */
    public Timestamp getEndTimeDB() {
        return new Timestamp(endTime.getTime());
    }

    /**
     * Getter of endTime as a String for the view
     * @return endTime as a String
     */
    public String getEndTime() {
        LocalDateTime localStart
                = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime.getTime()), 
                        ZoneId.systemDefault());
        LocalDateTime localEnd
                = LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime.getTime()), 
                        ZoneId.systemDefault());
        long min = ChronoUnit.SECONDS.between(localStart, localEnd);

        int hours = (int) (min / 3600);
        int minutes = (int) ((min % 3600) / 60);
        int seconds = (int) (min % 60);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
