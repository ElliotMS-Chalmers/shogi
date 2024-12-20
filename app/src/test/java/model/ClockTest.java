package model;

import model.game.Clock;
import org.junit.jupiter.api.Test;

import util.Side;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ClockTest {
    @Test
    void testRunNormalDecrement() throws InterruptedException {
        AtomicBoolean gameRunning = new AtomicBoolean(true);
        Clock clock = new Clock(3, Side.SENTE, gameRunning);
        Thread clockThread = new Thread(clock);

        clockThread.start();

        // Allow the clock to decrement for 3 seconds
        Thread.sleep(3500);

        assertEquals(0, clock.getSeconds().get(), "Clock should reach 0 after 3 seconds");
        assertFalse(gameRunning.get(), "Game should stop when clock reaches 0");

        clockThread.join(); // Wait for the thread to finish
    }

    @Test
    void testRunPauseAndResume() throws InterruptedException {
        AtomicBoolean gameRunning = new AtomicBoolean(true);
        Clock clock = new Clock(5, Side.SENTE, gameRunning);
        Thread clockThread = new Thread(clock);

        clockThread.start();

        // Let the clock run for 2 seconds
        Thread.sleep(2000);

        // Pause the clock
        clock.pause();

        int timeRemaining = clock.getSeconds().get();

        // Ensure no decrement while paused
        Thread.sleep(2000);
        assertEquals(timeRemaining, clock.getSeconds().get(), "Clock should not decrement while paused");

        // Resume the clock
        clock.resume();

        // Allow the clock to run for 2 more seconds
        Thread.sleep(2000);

        assertEquals(timeRemaining - 2, clock.getSeconds().get(), 1, "Clock should decrement after resuming");

        clock.pause(); // Pause to stop the test from decrementing further
        clockThread.interrupt(); // Stop the thread
        clockThread.join(); // Wait for the thread to finish
    }

    @Test
    void testRunHandleInterruptionWhilePaused() throws InterruptedException {
        AtomicBoolean gameRunning = new AtomicBoolean(true);
        Clock clock = new Clock(10, Side.SENTE, gameRunning);
        Thread clockThread = new Thread(clock);

        clockThread.start();

        // Pause the clock
        clock.pause();

        // Interrupt while paused
        clockThread.interrupt();

        Thread.sleep(100); // Allow thread to process interruption

        assertTrue(clockThread.isInterrupted(), "Clock thread should be interrupted");
        assertEquals(10, clock.getSeconds().get(), "Clock should not decrement while interrupted and paused");

        clockThread.join(); // Wait for the thread to finish
    }

    @Test
    void testRunClockReachesZero() throws InterruptedException {
        AtomicBoolean gameRunning = new AtomicBoolean(true);
        Clock clock = new Clock(1, Side.GOTE, gameRunning);
        Thread clockThread = new Thread(clock);

        clockThread.start();

        // Allow the clock to decrement
        Thread.sleep(1500);

        assertEquals(0, clock.getSeconds().get(), "Clock should reach 0");
        assertFalse(gameRunning.get(), "Game should stop when clock reaches 0");

        clockThread.join(); // Wait for the thread to finish
    }

    @Test
    void testRunInterruptedDuringSleep() throws InterruptedException {
        AtomicBoolean gameRunning = new AtomicBoolean(true);
        Clock clock = new Clock(10, Side.SENTE, gameRunning);
        Thread clockThread = new Thread(clock);

        clockThread.start();

        // Allow the clock to decrement for 1 second
        Thread.sleep(1000);

        // Interrupt the thread during sleep
        clockThread.interrupt();

        Thread.sleep(100); // Allow thread to process interruption

        assertTrue(clockThread.isInterrupted(), "Clock thread should be interrupted");
        assertTrue(clock.getSeconds().get() < 10, "Clock should have decremented before interruption");

        clockThread.join(); // Wait for the thread to finish
    }

    @Test
    void testRunHandlesInterruptedException() throws InterruptedException {
        AtomicBoolean gameRunning = new AtomicBoolean(true);
        Clock clock = new Clock(5, Side.SENTE, gameRunning);
        Thread clockThread = new Thread(clock);

        clockThread.start();

        clock.pause();

        Thread.sleep(200);

        clockThread.interrupt();

        Thread.sleep(2000); // Give it a bit more time to process the interruption

        System.out.println("Game running after interruption: " + gameRunning.get());

        assertFalse(gameRunning.get(), "Game should be stopped due to the interruption");

        clockThread.join();  // Wait for the thread to finish
        assertFalse(clockThread.isAlive(), "Clock thread should exit gracefully after being interrupted");
    }

    @Test
    void testRunGameEndsWhilePaused() throws InterruptedException {
        AtomicBoolean gameRunning = new AtomicBoolean(true);
        Clock clock = new Clock(5, Side.SENTE, gameRunning);
        Thread clockThread = new Thread(clock);
    
        clockThread.start();
    
        clock.pause();
    
        gameRunning.set(false);
    
        clock.resume();
    
        clockThread.join();
    
        assertFalse(gameRunning.get(), "Game should no longer be running");
        assertFalse(clockThread.isAlive(), "Clock thread should exit gracefully after game ends");
    }
}
