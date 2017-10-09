package esi.atlir5.mikolivator.model;

/**
 *
 * @author Mike Sarton & Olivier Cordier
 */

//  Rassemblement de fonctions utiles...
class Functions {

    public static void main(String[] args) {
//        for (int i = 0; i < 10; ++i) {
//            System.out.println(randomNumber(0, 6));
//        }
    }

    static int randomNumber(int min, int max, int exception) {
        int nb = min + (int) (Math.random() * (max - min + 1));
        return (nb == exception) ? randomNumber(min, max, exception) : nb;
    }
}
