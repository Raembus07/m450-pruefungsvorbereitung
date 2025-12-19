package ch;

public class Stopp extends StoppuhrState {

  public Stopp(StoppuhrController controller) {
    super(controller);
    controller.uhrStop();
    controller.zeigeUhr();
  }

  @Override
  public void resetZwischenzeit() {
    controller.setCurrentState(new Bereit(controller));
  }

  @Override
  public void startStop() {
    controller.setCurrentState(new Laeuft(controller));
  }
}