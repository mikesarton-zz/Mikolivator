
package esi.atlir5.mikolivator.events;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public interface PassengerToFloorListener {

    /**
     * Indicate someone has changed his state.
     */
    void passengerOnFloorHasChangedStated();

    /**
     * Indicate someone has moved.
     */
    void aPassengerHasMoved();

    /**
     * Indicate if the floor is evacuated or not.
     * @return True if the floor is evacuated, false otherwise.
     */
    boolean isEvacuated();
}
