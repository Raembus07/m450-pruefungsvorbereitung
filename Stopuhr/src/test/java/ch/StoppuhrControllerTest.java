package ch;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StoppuhrControllerTest {

  @Test
  void uhrIstBereitNachAnschalten() {
    StoppuhrController controller = new StoppuhrController();

    assertInstanceOf(Bereit.class, controller.getCurrentState());
    assertFalse(controller.isUhrLaeuft());
    assertEquals(StoppuhrAnzeige.UHR, controller.getAnzeige());
  }

  @Test
  void bereitStartDannLaeuftUhr() {
    StoppuhrController controller = new StoppuhrController();

    controller.startStop();

    assertInstanceOf(Laeuft.class, controller.getCurrentState());
    assertEquals(StoppuhrAnzeige.UHR, controller.getAnzeige());
    assertTrue(controller.isUhrLaeuft());
  }

  @Test
  void kannResetVonLaeuft() {
    StoppuhrController controller = new StoppuhrController();
    controller.setCurrentState(new Laeuft(controller));

    controller.resetZwischenzeit();

    assertInstanceOf(Zwischenzeit.class, controller.getCurrentState());
    assertEquals(StoppuhrAnzeige.ZWISCHENSPEICHER, controller.getAnzeige());
    assertTrue(controller.isUhrLaeuft());
  }

  @Test
  void kannNichtResetZwischenzeitVonBereit() {
    StoppuhrController controller = new StoppuhrController();

    controller.resetZwischenzeit();

    assertInstanceOf(Bereit.class, controller.getCurrentState());
    assertFalse(controller.isUhrLaeuft());
    assertEquals(StoppuhrAnzeige.UHR, controller.getAnzeige());
  }

  @Test
  void bereitInLaeuftInStoppInBereit() {
    StoppuhrController controller = new StoppuhrController();

    controller.startStop();
    controller.startStop();
    controller.resetZwischenzeit();

    assertInstanceOf(Bereit.class, controller.getCurrentState());
    assertFalse(controller.isUhrLaeuft());
    assertEquals(StoppuhrAnzeige.UHR, controller.getAnzeige());
  }

  @Test
  void bereitInLaeuftInZwischenzeitInLaeuft() {
    StoppuhrController controller = new StoppuhrController();

    controller.startStop();
    controller.resetZwischenzeit();
    controller.resetZwischenzeit();

    assertInstanceOf(Laeuft.class, controller.getCurrentState());
    assertEquals(StoppuhrAnzeige.UHR, controller.getAnzeige());
    assertTrue(controller.isUhrLaeuft());
  }

  @Test
  void bereitInLaeuftInStoppInLaeuft() {
    StoppuhrController controller = new StoppuhrController();

    controller.startStop();
    controller.startStop();
    controller.startStop();

    assertInstanceOf(Laeuft.class, controller.getCurrentState());
    assertEquals(StoppuhrAnzeige.UHR, controller.getAnzeige());
    assertTrue(controller.isUhrLaeuft());
  }
}