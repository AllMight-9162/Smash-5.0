package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;

import frc.Java_Is_AllMight.Control.PIDConfig;
import frc.Java_Is_AllMight.Motors.SparkConfigurator;

public class IntakeSubsystem {

    private final SparkMax IntakeMotor;
    private final SparkMax IntakeAngulateMotor;

    private static final int ID_MOTOR_INTAKE = 15;
    private static final int MOTOR_INTAKE_CURRENT_LIMIT = 40;
    private static final PIDConfig MOTOR_INTAKE_PID = new PIDConfig(0.0,0.0,0.0,0.0,0.0);
    public IntakeSubsystem(){
        
        IntakeMotor = SparkConfigurator.createSparkMax(
            ID_MOTOR_INTAKE,
            MotorType.kBrushless,
            null,
            IdleMode.kCoast,
            MOTOR_INTAKE_CURRENT_LIMIT);

        IntakeAngulateMotor = SparkConfigurator.createSparkMax(
            20, 
            MotorType.kBrushless, 
            MOTOR_INTAKE_PID, 
            IdleMode.kBrake, 
            MOTOR_INTAKE_CURRENT_LIMIT);
    }

    public void Intakeinit(){
        setPosition(IntakeAngulateMotor, 0);
    }

    public void take(){
        IntakeMotor.set(0.5);
    }

    public void stop(){
        IntakeMotor.set(0.0);
    }

    private void setPosition(SparkMax motor, double setPosition){
        motor.getClosedLoopController().setSetpoint(setPosition,ControlType.kPosition);
    }

}
