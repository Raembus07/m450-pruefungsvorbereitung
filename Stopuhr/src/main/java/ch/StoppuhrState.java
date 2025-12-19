package ch;

public abstract class StoppuhrState {

  protected final StoppuhrController controller;

  protected StoppuhrState(StoppuhrController controller) {
    this.controller = controller;
  }

  public void startStop() { }

  public void resetZwischenzeit() { }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}