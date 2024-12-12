package model;

import util.Side;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.concurrent.atomic.AtomicBoolean;



public class Clock implements Runnable{
    private IntegerProperty seconds;
    private Side side;
    private AtomicBoolean gameRunning;
    private AtomicBoolean paused;

    public Clock (int seconds, Side side, AtomicBoolean gameRunning) {
        this.seconds = new SimpleIntegerProperty(seconds);
        this.side = side;
        this.gameRunning = gameRunning;
        this.paused = new AtomicBoolean(false);
    }

    public IntegerProperty getSeconds() {
        return seconds;
    }

    public void pause() {
        paused.set(true);
    }

    public void resume() {
        paused.set(false);
        synchronized(this) {
            this.notify();
        }
    }

    @Override
    public void run() {
        while (gameRunning.get() && !Thread.currentThread().isInterrupted()) {
            synchronized (this) {
                while (paused.get()) {
                    try {
                        this.wait(); // Pause the clock thread
                    } catch (InterruptedException e) {
                        // Handle the interruption
                        Thread.currentThread().interrupt(); // Restore interrupt status
                        return; // Exit the thread when interrupted
                    }
                }
            }
    
            if (seconds.get() == 0) {
                synchronized (gameRunning) {
                    if (gameRunning.get()) {
                        switch (this.side) {
                            case GOTE -> System.out.println("GOTE LOST BECAUSE OF TIME");
                            case SENTE -> System.out.println("SENTE LOST BECAUSE OF TIME");
                        }
                        gameRunning.set(false);
                    }
                }
                break; // Exit loop
            }
    
            System.out.println(this.side + " - Seconds left: " + this.seconds);
            seconds.set(seconds.get() - 1);
    
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Handle interruption during sleep
                Thread.currentThread().interrupt(); // Restore interrupt status
                break; // Exit the loop
            }
        }
    }
}