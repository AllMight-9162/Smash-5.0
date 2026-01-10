package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import frc.Java_Is_AllMight.Motors.SparkConfigurator;
import edu.wpi.first.wpilibj.Servo;

public class IntakeSubsystem {

    private final SparkMax IntakeMotor;

    private final Servo servo1;
    private final Servo servo2;

    private static final int ID_MOTOR_INTAKE = 15;
    private static final int MOTOR_INTAKE_CURRENT_LIMIT = 40;
    private static final int ServoPos = 90;
    
    public IntakeSubsystem(){
        servo1 = new Servo(0);
        servo2 = new Servo(1);

        IntakeMotor = SparkConfigurator.createSparkMax(
            ID_MOTOR_INTAKE,
            MotorType.kBrushless,
            null,
            IdleMode.kCoast,
            MOTOR_INTAKE_CURRENT_LIMIT);
    }

    public void Intakeinit(){
        servo1.setAngle(ServoPos);
        servo2.setAngle(ServoPos);
    }

    public void take(){
        IntakeMotor.set(0.5);
    }

    public void stop(){
        IntakeMotor.set(0.0);
    }

}
