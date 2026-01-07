package frc.Java_Is_AllMight.Control;

public class PIDController {

  private final PIDConfig config;
  private double integral;
  private double previousError;

  public PIDController(PIDConfig pidConfig){
    this.config = pidConfig;
  }

  public double calculate(double setpoint, double measurement) {
    double error = angleError(setpoint, measurement);

    if (config.iZone == 0 || Math.abs(error) < config.iZone) {
        integral += error;
    } else {
        integral = 0;
    }

    double derivative = error - previousError;
    previousError = error;

    return (config.kP * error)
         + (config.kI * integral)
         + (config.kD * derivative)
         + config.kF;
}

  public void reset() {
      integral = 0;
      previousError = 0;
  }

  public static double angleError(double target, double current) {
    double error = target - current;

    while (error > Math.PI) {
        error -= 2 * Math.PI;
    }
    while (error < -Math.PI) {
        error += 2 * Math.PI;
    }

    return error;
}

}
