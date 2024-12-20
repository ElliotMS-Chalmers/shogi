package model.game;

import util.Side;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a game clock for tracking time for a specific player or side during a game.
 */
public class Clock implements Runnable {
    /**
     * The number of seconds remaining on the clock.
     */
    private IntegerProperty seconds;

    /**
     * The side (e.g., SENTE or GOTE) associated with this clock.
     */
    private Side side;

    /**
     * Indicates whether the game is currently running.
     */
    private AtomicBoolean gameRunning;

    /**
     * Indicates whether the clock is currently paused.
     */
    private AtomicBoolean paused;

    /**
     * Constructs a new Clock with the specified initial time, side, and game state.
     *
     * @param seconds      the initial number of seconds on the clock
     * @param side         the side associated with this clock
     * @param gameRunning  an AtomicBoolean indicating whether the game is running
     */
    public Clock(int seconds, Side side, AtomicBoolean gameRunning) {
        this.seconds = new SimpleIntegerProperty(seconds);
        this.side = side;
        this.gameRunning = gameRunning;
        this.paused = new AtomicBoolean(false);
    }

    /**
     * Gets the property representing the remaining seconds on the clock.
     *
     * @return the IntegerProperty representing the remaining seconds
     */
    public IntegerProperty getSeconds() {
        return seconds;
    }

    /**
     * Gets the value of AtomicBoolean paused
     * 
     * @return the boolean value for AtomicBoolean paused
     */
    public boolean isPaused() {
        return paused.get();
    }

    /**
     * Pauses the clock, preventing it from decrementing.
     */
    public void pause() {
        paused.set(true);
    }

    /**
     * Resumes the clock, allowing it to continue decrementing.
     */
    public void resume() {
        paused.set(false);
        synchronized (this) {
            this.notify();
        }
    }

    /**
     * Runs the clock in a separate thread, decrementing the time every second until it reaches zero
     * or the game is no longer running. If paused, the clock will wait until resumed.
     */
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
                        gameRunning.set(false);
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

            seconds.set(seconds.get() - 1);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Handle interruption during sleep
                Thread.currentThread().interrupt(); // Restore interrupt status
                gameRunning.set(false); //Stop the game if interrupted during sleep
                break; // Exit the loop
            }
        }
    }
}
