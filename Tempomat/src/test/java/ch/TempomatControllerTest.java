package ch;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TempomatControllerTest {

  @Test
  void isTempomatActiveFalseAfterCreation() {
    // Arrange
    IMotor motorStub = throttle -> { };
    TempomatController controller =
        new TempomatController(motorStub);

    // Assert
    assertFalse(controller.isTempomatActive());
  }

  @Test
  void isTempomatActiveTrueAfterSetDesiredSpeed() {
    // Arrange
    IMotor motorStub = throttle -> { };
    TempomatController controller =
        new TempomatController(motorStub);

    // Act
    controller.setDesiredSpeed(100);

    // Assert
    assertTrue(controller.isTempomatActive());
  }

  @Test
  void actualSpeedIsHigherThanDesiredSpeed() {
    // Arrange
    MotorSpy motorSpy = new MotorSpy();
    TempomatController controller =
        new TempomatController(motorSpy);

    // Act
    controller.setDesiredSpeed(50.0);
    controller.updateCurrentSpeed(55.0);

    // Assert
    assertEquals(0.0, motorSpy.getValue());
  }

  @Test
  void actualSpeedIsLowerThanDesiredSpeed() {
    // Arrange
    MotorSpy motorSpy = new MotorSpy();
    TempomatController controller =
        new TempomatController(motorSpy);

    // Act
    controller.setDesiredSpeed(50.0);
    controller.updateCurrentSpeed(45.0);

    // Assert
    assertTrue(motorSpy.getValue() > 0.0);
  }
}