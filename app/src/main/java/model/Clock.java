package model;


public class Clock implements Runnable{
    private int seconds;

    public Clock (int seconds) {
        this.seconds = seconds;
    }

    @Override
    public void run() {
        boolean stop = false;
        while(!stop){
            System.out.println(seconds);
            this.seconds -= 1;

            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (seconds == 0){
                System.out.println("CLOCK HITS ZERO");
                stop = true;
            }
        }

    }
}