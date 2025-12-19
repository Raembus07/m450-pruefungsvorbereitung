package ch.example.stoppuhr;

public class StoppuhrController {
    private StoppuhrState currentState;
    public StoppuhrAnzeige anzeige;
    public boolean uhrLaeuft;

    public StoppuhrController() {
        currentState = new Bereit(this);
    }

    public StoppuhrState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(StoppuhrState state) {
        currentState = state;
    }

    public void startStop() {
        currentState.startStop();
    }

    public void resetZwischenzeit() {
        currentState.resetZwischenzeit();
    }

    public void zeigeUhr() {
        anzeige = StoppuhrAnzeige.UHR;
    }

    public void zeigeZwischenzeit() {
        anzeige = StoppuhrAnzeige.ZWISCHENSPEICHER;
    }

    public void uhrStart() {
        uhrLaeuft = true;
    }

    public void uhrStop() {
        uhrLaeuft = false;
    }

    public void uhrReset() {
    }
}
