package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import frc.Java_Is_AllMight.Motors.SparkConfigurator;
import frc.Java_Is_AllMight.Control.PIDConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class EarsUpSubsystem{

    
    private static final int MOTOR1_EARS_ID = 18;
    private static final int MOTOR2_EARS_ID = 19;
    private static final PIDConfig MOTOR_EARS_PID = new PIDConfig(0.0,0.0,0.0,0.0,0.0);
    private static final int MOTOR_EARS_CURRENT_LIMIT = 40;

    private final SparkMax earsMotor;
    private final SparkMax earsMotor2;

    private static final double EARS_UP_POSITION = 50.0;
    private static final double EARS_DOWN_POSITION = 0.0;


    public EarsUpSubsystem(){

        earsMotor = SparkConfigurator.createSparkMax(
            MOTOR1_EARS_ID, 
            MotorType.kBrushless, 
            MOTOR_EARS_PID, 
            IdleMode.kBrake,
            MOTOR_EARS_CURRENT_LIMIT
        );

        earsMotor2 = SparkConfigurator.createSparkMax(
            MOTOR2_EARS_ID, 
            MotorType.kBrushless, 
            MOTOR_EARS_PID, 
            IdleMode.kBrake,
            MOTOR_EARS_CURRENT_LIMIT
        );
    }

    public void up(){
        setPosition(earsMotor, EARS_UP_POSITION);
        setPosition(earsMotor2, EARS_UP_POSITION);
    }

    public void down(){
        setPosition(earsMotor, EARS_DOWN_POSITION);
        setPosition(earsMotor2, EARS_DOWN_POSITION);
    }

    public double getPosition(){
        return earsMotor.getEncoder().getPosition();
    }

    public boolean isUp(){
        return Math.abs(getPosition() - EARS_UP_POSITION) < 1.0;
    }

    private void setPosition(SparkMax motor, double setPosition){
        motor.getClosedLoopController()
            .setReference(setPosition,
            com.revrobotics.spark.SparkBase.ControlType.kPosition);
    }
}