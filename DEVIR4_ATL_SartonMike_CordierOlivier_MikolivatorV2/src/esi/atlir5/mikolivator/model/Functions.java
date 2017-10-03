package esi.atlir5.mikolivator.model;

/**
 *
 * @author mike
 */
class Functions {
    
    public static void main(String[] args) {
        for (int i=0; i<10; ++i){
            System.out.println(randomNumber(0, 6));
        }
    }
    static int randomNumber (int min, int max){
        return min + (int)(Math.random() * (max-min+1));
    }
}
