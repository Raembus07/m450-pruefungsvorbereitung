package ch.example.stoppuhr;

public class Laeuft extends StoppuhrState {
    public Laeuft(StoppuhrController controller) {
        super(controller);
        controller.uhrStart();
        controller.zeigeUhr();
    }

    @Override
    public void resetZwischenzeit() {
        controller.setCurrentState(new Zwischenzeit(controller));
    }

    @Override
    public void startStop() {
        controller.setCurrentState(new Stopp(controller));
    }
}
