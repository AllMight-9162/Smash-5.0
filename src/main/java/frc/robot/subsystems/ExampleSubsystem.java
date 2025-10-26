package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.Java_Is_AllMight.Control.PIDConfig;
import frc.Java_Is_AllMight.Motors.SparkMAXMotor;

public class ExampleSubsystem {

    private final int IdAngulateMotor = 1;
    private final int IdLeftMotor = 2;
    private final int IdRightMotor = 3;

    private final SparkMax angulateMotor;
    private final SparkMax leftMotor;
    private final SparkMax rightMotor;

    private final RelativeEncoder encoder;
    private final SparkClosedLoopController pidController;

    public ExampleSubsystem() {
        PIDConfig motorPID = new PIDConfig(0.5, 0.000001, 2.0, 0.0, 0.0);

        angulateMotor = SparkMAXMotor.create(IdAngulateMotor, MotorType.kBrushless, motorPID, IdleMode.kBrake, 40);
        leftMotor = SparkMAXMotor.create(IdLeftMotor, MotorType.kBrushless, motorPID, IdleMode.kBrake, 40);
        rightMotor = SparkMAXMotor.create(IdRightMotor, MotorType.kBrushless, motorPID, IdleMode.kBrake, 40);

        encoder = angulateMotor.getEncoder();
        encoder.setPosition(0.0);
        pidController = angulateMotor.getClosedLoopController();
    }

    public void angulate(double position) {
        pidController.setReference(position, ControlType.kPosition);
    }

    public void take() {
        rightMotor.set(-1.0);
        leftMotor.set(-1.0);
    }

    public void drop(double speed) {
        rightMotor.set(speed);
        leftMotor.set(speed);
    }

    public void up(double speed) {
        angulateMotor.set(speed);
    }

    public void down(double speed) {
        angulateMotor.set(speed);
    }

    public void reset() {
        angulate(0.0);
    }

    public void stop() {
        rightMotor.set(0.0);
        leftMotor.set(0.0);
    }

    public double getPosition() {
        return encoder.getPosition();
    }
}
