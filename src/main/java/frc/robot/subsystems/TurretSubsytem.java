package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkFlex;
import frc.Java_Is_AllMight.Control.PIDConfig;
import frc.Java_Is_AllMight.Motors.SparkConfigurator;

public class TurretSubsytem {
    public SparkFlex shooterMotor;
    public SparkFlex turretMotor;

    public TurretSubsytem() {
        PIDConfig motorPID = new PIDConfig(0.5, 0.0, 0.0, 0.0, 0.0);
        shooterMotor = SparkConfigurator.createSparkFlex(1, MotorType.kBrushless, motorPID, IdleMode.kCoast, 40); 
        turretMotor = SparkConfigurator.createSparkFlex(2, MotorType.kBrushless, motorPID, IdleMode.kBrake, 40);
    }

    void shoot(){

    }

    void alignTurret(){

    }

    void getRobotPose(){

    }

}
