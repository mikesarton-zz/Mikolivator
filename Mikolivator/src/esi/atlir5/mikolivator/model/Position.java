package esi.atlir5.mikolivator.model;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
class Position {
    private int place;  //  position au niveau de l'étage
    private int floor;  //  étage courant d'une personne
    
    Position () {
        this(-1,0);
    }
    
    Position (int place, int floor) {
        this.place = place;
        this.floor = floor;
    }
    
    Position (Position p) {
        this.place = p.getPlace();
        this.floor = p.getFloor();
    }

    int getPlace() {
        return place;
    }

    void setPlace(int place) {
        this.place = place;
    }

    int getFloor() {
        return floor;
    }

    void setFloor(int floor) {
        this.floor = floor;
    }
    
    
}
