package model;

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

        assertEquals(timeRemaining - 2, clock.getSeconds().get(), "Clock should decrement after resuming");

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

        // Pause the clock so that it enters the waiting state
        clock.pause();

        Thread.sleep(200);

        // Interrupt the thread while it's waiting
        clockThread.interrupt();

        // Allow some time for the interruption to be processed
        Thread.sleep(2000); // Give it a bit more time to process the interruption

        // Debugging: Check the gameRunning status and output
        System.out.println("Game running after interruption: " + gameRunning.get());

        // Ensure that gameRunning is set to false due to the interruption
        assertFalse(gameRunning.get(), "Game should be stopped due to the interruption");

        // Ensure the clock thread has stopped
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
    // @Test
    // void testInitialState() {
    //     AtomicBoolean gameRunning = new AtomicBoolean(true);
    //     Clock clock = new Clock(60, Side.SENTE, gameRunning);

    //     assertEquals(60, clock.getSeconds().get(), "Initial seconds should be set correctly");
    //     assertTrue(gameRunning.get(), "Game should initially be running");
    // }

    // @Test
    // void testPauseAndResume() throws InterruptedException {
    //     AtomicBoolean gameRunning = new AtomicBoolean(true);
    //     Clock clock = new Clock(10, Side.GOTE, gameRunning);
    //     Thread clockThread = new Thread(clock);

    //     clockThread.start();

    //     // Let the clock run for 3 seconds
    //     Thread.sleep(3000);

    //     clock.pause();

    //     int timeRemaining = clock.getSeconds().get();

    //     // Wait while the clock is paused
    //     Thread.sleep(2000);

    //     assertEquals(timeRemaining, clock.getSeconds().get(), "Clock should not decrement while paused");

    //     clock.resume();

    //     // Let the clock run for another 2 seconds
    //     Thread.sleep(2000);

    //     assertEquals(timeRemaining - 2, clock.getSeconds().get(), "Clock should decrement after resuming");

    //     clockThread.interrupt();
    //     clockThread.join();
    // }

    // @Test
    // void testClockReachesZero() throws InterruptedException {
    //     AtomicBoolean gameRunning = new AtomicBoolean(true);
    //     Clock clock = new Clock(3, Side.SENTE, gameRunning);
    //     Thread clockThread = new Thread(clock);

    //     clockThread.start();

    //     // Let the clock run until it reaches zero
    //     Thread.sleep(4000);

    //     assertEquals(0, clock.getSeconds().get(), "Clock should reach zero");
    //     assertFalse(gameRunning.get(), "Game should stop when clock reaches zero");

    //     clockThread.interrupt();
    //     clockThread.join();
    // }

    // @Test
    // void testClockInterruptHandling() throws InterruptedException {
    //     AtomicBoolean gameRunning = new AtomicBoolean(true);
    //     Clock clock = new Clock(10, Side.GOTE, gameRunning);
    //     Thread clockThread = new Thread(clock);

    //     clockThread.start();

    //     clockThread.interrupt();

    //     // Wait briefly to ensure thread processes the interrupt
    //     Thread.sleep(100);

    //     assertTrue(clockThread.isInterrupted(), "Clock thread should be interrupted");
    // }
}
