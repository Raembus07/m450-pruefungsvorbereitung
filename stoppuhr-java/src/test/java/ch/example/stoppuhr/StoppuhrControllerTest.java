package ch.example.stoppuhr;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StoppuhrControllerTest {

    private StoppuhrController stoppuhrController;

    @BeforeEach
    void setup() {
        stoppuhrController = new StoppuhrController();
    }

    @Test
    void shouldClockRun() {
        stoppuhrController.uhrStart();
        assertTrue(stoppuhrController.uhrLaeuft);

        assertEquals(StoppuhrAnzeige.UHR, stoppuhrController.anzeige);

    }

    @Test
    void shouldClockShow() {
        stoppuhrController.uhrStart();
        assertTrue(stoppuhrController.uhrLaeuft);

        stoppuhrController.zeigeZwischenzeit();
        assertEquals(StoppuhrAnzeige.ZWISCHENSPEICHER, stoppuhrController.anzeige);
    }

    @Test
    void shouldShowZwischenspeicher() {
        stoppuhrController.uhrStart();
        assertTrue(stoppuhrController.uhrLaeuft);

        stoppuhrController.zeigeZwischenzeit();
        assertEquals(StoppuhrAnzeige.ZWISCHENSPEICHER, stoppuhrController.anzeige);
    }

    @Test
    void shouldStop() {
        stoppuhrController.uhrStart();
        assertTrue(stoppuhrController.uhrLaeuft);

        stoppuhrController.uhrStop();
        assertFalse(stoppuhrController.uhrLaeuft);
    }

    @Test
    void stateShouldNotBeStopped() {
        stoppuhrController.startStop();
        assertTrue(stoppuhrController.uhrLaeuft);

        stoppuhrController.startStop();
        assertFalse(stoppuhrController.uhrLaeuft);
    }

    @Test
    void checkState() {
        StoppuhrState currentState = stoppuhrController.getCurrentState();
        assertEquals("Bereit", currentState);

        stoppuhrController.startStop();
        assertTrue(stoppuhrController.uhrLaeuft);
        assertEquals("Läuft", stoppuhrController.getCurrentState());
    }

    @Test
    void checkState2() {
        StoppuhrState currentState = stoppuhrController.getCurrentState();
        assertEquals("Bereit", currentState);

        stoppuhrController.startStop();
        assertTrue(stoppuhrController.uhrLaeuft);
        assertEquals("Läuft", stoppuhrController.getCurrentState());

        stoppuhrController.startStop();
        assertEquals("Stop", stoppuhrController.getCurrentState());
    }

    @Test
    void checkState3() {
        StoppuhrState currentState = stoppuhrController.getCurrentState();
        assertEquals("Bereit", currentState);

        stoppuhrController.startStop();
        assertTrue(stoppuhrController.uhrLaeuft);
        assertEquals("Läuft", stoppuhrController.getCurrentState());

        stoppuhrController.resetZwischenzeit();
        assertEquals(StoppuhrAnzeige.ZWISCHENSPEICHER, stoppuhrController.anzeige);
        assertEquals("Läuft", stoppuhrController.getCurrentState());

        stoppuhrController.startStop();
        assertEquals("Stop", stoppuhrController.getCurrentState());
    }






    @Test
    void stateShouldNotBeZwischenspeicher() {
        stoppuhrController.startStop();
        assertNotEquals(StoppuhrAnzeige.ZWISCHENSPEICHER, stoppuhrController.anzeige);
    }

}
