package esi.atlir5.mikolivator.model;

import java.util.List;

/**
 * Rassemblement de fonctions utiles pour des générations aléatoires.
 * @author Mike Sarton & Olivier Cordier
 */

class Functions {
    
    static int generateRandomDestination(int min, int max, int exception, 
            List<Integer> floorsEvacuated) {
        int nb = min + (int) (Math.random() * (max - min + 1));
        if ((nb == exception || (floorsEvacuated.contains(nb)))) {
           return generateRandomDestination(min, max, exception, floorsEvacuated);
        }
        return nb;
    }
    
    static int generateRandomTimeOfHide() {
        return 1 + (int) (Math.random() * (10 - 1 + 1));
    }
    
    static int generateRandomSpeedOfWalk() {
        return 100 + (int) (Math.random() * (750 - 100 + 1));
    }
}
