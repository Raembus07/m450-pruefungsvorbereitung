package ch.example.mobilfunk;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

class MonatsRechnungTest {

    @Test
    void testRechnungBerechnung() {
        // Beispielhafte Testmethode für MonatsRechnung
        MonatsRechnung rechnung = new MonatsRechnung();
        double result = rechnung.berechneDatenkosten(2.0);
        assertEquals(2.0, result);
    }

    @Test
    void shouldThrowExceptionIfUsageIsUnder1GB() {
        MonatsRechnung monatsRechnung = new MonatsRechnung();
        assertThrows(IllegalArgumentException.class, () -> monatsRechnung.berechneDatenkosten(-1));
    }

    @Test
    void shouldCostNothingIfUsageIsUnder1GB() {
        MonatsRechnung monatsRechnung = new MonatsRechnung();
        double result = monatsRechnung.berechneDatenkosten(0.5);
        assertEquals(0, result);
    }

    @Test
    void shouldNothingIfUsageIsExactly1GB() {
        MonatsRechnung monatsRechnung = new MonatsRechnung();
        double result = monatsRechnung.berechneDatenkosten(1);
        assertEquals(0, result);
    }

    @Test
    void shouldCost10FrIfUsageIs6GB() {
        MonatsRechnung monatsRechnung = new MonatsRechnung();
        double result = monatsRechnung.berechneDatenkosten(6);
        assertEquals(10, result);
    }

    @Test
    void shouldNotCost10FrIfUsageIs5GB() {
        MonatsRechnung monatsRechnung = new MonatsRechnung();
        double result = monatsRechnung.berechneDatenkosten(5);
        assertNotEquals(10, result);
    }

    @Test
    void shouldNotCost10FrIfUsageIs7GB() {
        MonatsRechnung monatsRechnung = new MonatsRechnung();
        double result = monatsRechnung.berechneDatenkosten(7);
        assertNotEquals(10, result);
    }

    @Test
    void shouldCost20FrIfUsageIs11GB() {
        MonatsRechnung monatsRechnung = new MonatsRechnung();
        double result = monatsRechnung.berechneDatenkosten(11);
        assertEquals(20, result);
    }

    @Test
    void shouldCost20FrIfUsageIs12GB() {
        MonatsRechnung monatsRechnung = new MonatsRechnung();
        double result = monatsRechnung.berechneDatenkosten(12);
        assertEquals(20, result);
    }

    @Test
    void shouldAlwaysCost20IfItsOver20FrInTotal() {
        MonatsRechnung monatsRechnung = new MonatsRechnung();
        double result = monatsRechnung.berechneDatenkosten(15);
        assertEquals(20, result);

        double result2 = monatsRechnung.berechneDatenkosten(16);
        assertEquals(20, result2);

        double result3 = monatsRechnung.berechneDatenkosten(17);
        assertEquals(20, result3);

    }







    //tests if mockito is working properly
    //List geht nicht
//    @Test
//    void mockitoSmokeTest() {
//        List<String> list = mock(List.class);
//        when(list.size()).thenReturn(3);
//
//        assertEquals(3, list.size());
//    }
}




