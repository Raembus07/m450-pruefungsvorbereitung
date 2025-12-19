package ch.example.stoppuhr;

public class Zwischenzeit extends StoppuhrState {
    public Zwischenzeit(StoppuhrController controller) {
        super(controller);
        controller.zeigeZwischenzeit();
    }

    @Override
    public void resetZwischenzeit() {
        controller.setCurrentState(new Laeuft(controller));
    }

    @Override
    public void startStop() {
        controller.setCurrentState(new Stopp(controller));
    }
}
