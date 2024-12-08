package model;

import util.Side;

import java.util.concurrent.atomic.AtomicBoolean;



public class Clock implements Runnable{
    private int seconds;
    private Side side;
    private AtomicBoolean gameRunning;

    public Clock (int seconds, Side side, AtomicBoolean gameRunning) {
        this.seconds = seconds;
        this.side = side;
        this.gameRunning = gameRunning;
    }

    @Override
    public void run() {
        while(gameRunning.get()){

            if (seconds == 0) {
                synchronized (gameRunning) {
                    if (gameRunning.get()) {
                        switch (this.side) {
                            case GOTE -> System.out.println("GOTE LOST BECAUSE OF TIME");
                            case SENTE -> System.out.println("SENTE LOST BECAUSE OF TIME");
                        }
                        gameRunning.set(false);
                    }
                }
                break;
            }

            System.out.println(this.seconds);
            this.seconds -= 1;

            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}