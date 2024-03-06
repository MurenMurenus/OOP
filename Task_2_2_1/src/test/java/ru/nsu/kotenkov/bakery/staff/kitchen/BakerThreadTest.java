package ru.nsu.kotenkov.bakery.staff.kitchen;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nsu.kotenkov.bakery.staff.Order;
import ru.nsu.kotenkov.bakery.staff.management.CourierThread;
import ru.nsu.kotenkov.bakery.staff.management.Storage;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class BakerThreadTest {
    OutputStream error = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setErr(new PrintStream(error));
    }

    @AfterAll
    public static void tearDown() {
        System.setErr(System.err);
    }

    @Test
    @DisplayName("Baker OK")
    public void checkOkThread() {
        Order testOrder = new Order();
        testOrder.setTimeToDeliver(10);
        Storage storage = new Storage(2);

        BakerThread testBaker = new BakerThread(0, 1, storage);

        testBaker.setOrder(testOrder);
        testBaker.setMyself(new Thread(testBaker));

        testBaker.getMyself().start();
        try {
            Thread.sleep(12 * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertFalse(testBaker.getMyself().isAlive());
    }

    @Test
    @DisplayName("Baker interruption")
    public void checkThreadInterruption() {
        Order testOrder = new Order();
        testOrder.setTimeToCook(10);
        Storage storage = new Storage(2);

        BakerThread testBaker = new BakerThread(0, 1, storage);

        testBaker.setOrder(testOrder);
        testBaker.setMyself(new Thread(testBaker));

        testBaker.getMyself().start();
        try {
            Thread.sleep(5 * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        testBaker.getMyself().interrupt();
        assertTrue(testBaker.getMyself().isInterrupted());
    }
}
