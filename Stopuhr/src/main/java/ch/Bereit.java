package ch;

public class Bereit extends StoppuhrState {

  public Bereit(StoppuhrController controller) {
    super(controller);
    controller.uhrReset();
    controller.zeigeUhr();
  }

  @Override
  public void startStop() {
    controller.setCurrentState(new Laeuft(controller));
  }
}