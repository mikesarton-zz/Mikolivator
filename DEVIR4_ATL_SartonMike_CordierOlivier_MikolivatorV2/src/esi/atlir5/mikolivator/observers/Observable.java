
package esi.atlir5.mikolivator.observers;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
public interface Observable {
    
    /**
     * This method add an observer to the observable's list.
     * @param obs The observer to add.
     */
    public void addObserver(Observer obs);
    
    /**
     * This method delete an observer to the observable's list.
     * @param obs The observer to delete.
     */
    public void deleteObserver(Observer obs);
    
    /**
     * This method calls the udpate() method of each observer of the
     * observable's list.
     */
    public void notifyObs(int i);
}
