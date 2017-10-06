package esi.atlir5.mikolivator.model;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */
class ControllerElevator extends ElevatorBehavior {

    ControllerElevator(int nb_persons_max, int current_floor, int last_floor,
            int lowest_floor, int elevator_position) {
        super(new Elevator(nb_persons_max, current_floor, last_floor,
                lowest_floor), elevator_position);
    }

    //  déplace l'ascenseur (reste à définir l'algo qu'on veut)
    @Override
    public void move() {
        //  tant qu'il reste des destinations...
        while (!destinations.isEmpty()) {
            //  prendre la première qui a été entrée
            int destination = destinations.remove(0);

            //  monter ou descendre selon où l'ascenseur se situe
            if (destination > elevator.getCurrentFloor()) {
                goingUp(destination);
            } else {
                goingDown(destination);
            }

            //  afficher l'état
            System.out.println(passengers.size() + " passagers restants.");
            System.out.println(destinations.size() + " destinations restantes.");
        }
    }
    
    public static void main(String[] args) {

        //  petit main pour créer différents cas de figure (tester en gros)
//        Passenger p = new Passenger(4);
//        Passenger p2 = new Passenger(6);
//        Passenger p3 = new Passenger(2);
//        Passenger p4 = new Passenger(6);
//        Passenger p5 = new Passenger(5);
//        Passenger p6 = new Passenger(9);
//        Passenger p7 = new Passenger(4);
//        Passenger p8 = new Passenger(3);
//        Passenger p9 = new Passenger(5);
//        Passenger p10 = new Passenger(15);
//        Passenger p11 = new Passenger(8);
//
//        ControllerElevator ce = new ControllerElevator(15, 0, 15, 0);
//        ce.addPassenger(p);
//        ce.addPassenger(p2);
//        ce.addPassenger(p3);
//        ce.addPassenger(p4);
//        ce.addPassenger(p5);
//        ce.addPassenger(p6);
//        ce.addPassenger(p7);
//        ce.addPassenger(p8);
//        ce.addPassenger(p9);
//        ce.addPassenger(p10);
//        ce.addPassenger(p11);
//        ce.move();
    }

}
