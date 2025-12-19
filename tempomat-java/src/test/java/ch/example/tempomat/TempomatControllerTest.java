package ch.example.tempomat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TempomatControllerTest {

    private IMotor motor;
    private TempomatController tempomatController;

    @BeforeEach
    void setUp() {
        motor = Mockito.mock(IMotor.class);
        tempomatController = new TempomatController(motor);
    }

    @Test
    void isTempomatActive() {
        assertFalse(tempomatController.isTempomatActive());

        tempomatController.setDesiredSpeed(20);
        assertTrue(tempomatController.isTempomatActive());

        tempomatController.updateCurrentSpeed(30);
        assertTrue(tempomatController.isTempomatActive());

        tempomatController.breakPedal(50);
        assertFalse(tempomatController.isTempomatActive());
    }


    @Test
    void isTempomatNotActive() {
        TempomatController spy = spy(tempomatController);

        assertFalse(tempomatController.isTempomatActive());

        tempomatController.setDesiredSpeed(20);
        assertTrue(tempomatController.isTempomatActive());

        tempomatController.updateCurrentSpeed(30);
        assertTrue(tempomatController.isTempomatActive());

        tempomatController.breakPedal(50);
        assertFalse(tempomatController.isTempomatActive());


        verify(spy, times(4)).isTempomatActive();
    }


    @Test
    void throttle() {
        TempomatController spy = spy(tempomatController);

        assertFalse(tempomatController.isTempomatActive());

        tempomatController.setDesiredSpeed(20);
        assertTrue(tempomatController.isTempomatActive());

        tempomatController.updateCurrentSpeed(30);
        assertTrue(tempomatController.isTempomatActive());

        tempomatController.breakPedal(50);
        assertFalse(tempomatController.isTempomatActive());

        tempomatController.setDesiredSpeed(20);

        verify(spy, times(4)).setDesiredSpeed(30);
    }


}
