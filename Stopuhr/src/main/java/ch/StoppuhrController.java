package ch;

public class StoppuhrController {

  private StoppuhrState currentState;
  StoppuhrAnzeige anzeige;
  boolean uhrLaeuft;

  public StoppuhrController() {
    currentState = new Bereit(this);
  }

  public StoppuhrState getCurrentState() {
    return currentState;
  }

  public StoppuhrAnzeige getAnzeige() {
    return anzeige;
  }

  public boolean isUhrLaeuft() {
    return uhrLaeuft;
  }

  public void setCurrentState(StoppuhrState state) {
    currentState = state;
  }

  public void startStop() {
    System.out.println("StartStop");
    currentState.startStop();
  }

  public void resetZwischenzeit() {
    System.out.println("ResetZwischenzeit");
    currentState.resetZwischenzeit();
  }

  public void zeigeUhr() {
    anzeige = StoppuhrAnzeige.UHR;
    System.out.println("-> Zeige Uhr");
  }

  public void zeigeZwischenzeit() {
    anzeige = StoppuhrAnzeige.ZWISCHENSPEICHER;
    System.out.println("-> Zeige Zwischenzeit");
  }

  public void uhrStart() {
    uhrLaeuft = true;
    System.out.println("-> Starte Uhr");
  }

  public void uhrStop() {
    uhrLaeuft = false;
    System.out.println("-> Stoppe Uhr");
  }

  public void uhrReset() {
    System.out.println("-> Loesche Uhr");
  }
}