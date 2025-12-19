package ch.example.tempomat;

public class TempomatController {
    private static final double THROTTLE_FACTOR = 10.0;

    private final IMotor motor;
    private double desiredSpeed = 0.0;
    private boolean tempomatActive = false;

    public TempomatController(IMotor motor) {
        this.motor = motor;
    }

    public boolean isTempomatActive() {
        return tempomatActive;
    }

    public void breakPedal(double pedalPercent) {
        tempomatActive = false;
    }

    public void setDesiredSpeed(double desiredSpeed) {
        tempomatActive = true;
        this.desiredSpeed = desiredSpeed;
    }

    public void updateCurrentSpeed(double currentSpeed) {
        if (currentSpeed >= desiredSpeed) {
            motor.setThrottle(0.0);
        } else {
            double throttle = Math.min((desiredSpeed - currentSpeed) * THROTTLE_FACTOR, 100.0);
            motor.setThrottle(throttle);
        }
    }



}
