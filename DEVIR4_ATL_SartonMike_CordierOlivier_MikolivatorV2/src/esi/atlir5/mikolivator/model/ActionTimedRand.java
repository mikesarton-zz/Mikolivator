import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Liongeek
 */
public class ActionTimedRand {
 static Timer timer = new Timer();

    private static class MyActionTimer extends TimerTask {
        @Override
        public void run() {
            doSomething(3000,15000);        
        }
    }
    
    private static void doSomething(int min,int max){
        int delay = min + (int)(Math.random()*(max-min+1));
            timer.schedule(new MyActionTimer(), delay);
            System.out.println(new Date());
    }

    public static void main(String[] args) {
        
          new MyActionTimer().run();
    }
}
