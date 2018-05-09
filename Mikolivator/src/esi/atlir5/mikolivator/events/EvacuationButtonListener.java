
package esi.atlir5.mikolivator.events;

/**
 * This interface is used when the user click on the "evacuation button" in the view.
 * @author Mike Sarton & Olivier Cordier
 */
public interface EvacuationButtonListener {

    /**
     * Evacuate a floor
     * @param floor The floor's number
     */
    void evacuationFloor (int floor);

    /**
     * Open a floor
     * @param floor The floor's number
     */
    void openFloor (int floor);
}
