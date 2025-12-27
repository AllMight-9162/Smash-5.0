package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Filesystem;

import java.io.File;

import com.revrobotics.spark.SparkFlex;
import frc.Java_Is_AllMight.Control.PIDConfig;
import frc.Java_Is_AllMight.Motors.SparkConfigurator;
import frc.robot.subsystems.SwerveSubsystem;

public class TurretSubsytem {
    public SparkFlex shooterMotor;
    public SparkFlex turretMotor;
    private SwerveSubsystem swerve;

    public TurretSubsytem() {
        PIDConfig motorPID = new PIDConfig(0.5, 0.0, 0.0, 0.0, 0.0);
        shooterMotor = SparkConfigurator.createSparkFlex(1, MotorType.kBrushless, motorPID, IdleMode.kCoast, 40); 
        turretMotor = SparkConfigurator.createSparkFlex(2, MotorType.kBrushless, motorPID, IdleMode.kBrake, 40);
        swerve = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve"));
    }

    void shoot(){

    }

    void alignTurret(Pose2d pose){
        pose = swerve.getPose();

        //alinhar
        //alinhar
        //alinhar
        //alinhar
    }


}
