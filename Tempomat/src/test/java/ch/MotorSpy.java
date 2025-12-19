package ch;

public class MotorSpy implements IMotor {

  private double value;

  public double getValue() {
    return value;
  }

  @Override
  public void setThrottle(double throttle) {
    this.value = throttle;
  }
}