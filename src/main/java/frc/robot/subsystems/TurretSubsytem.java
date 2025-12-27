package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.Java_Is_AllMight.Control.PIDConfig;
import frc.Java_Is_AllMight.Motors.SparkConfigurator;
import frc.Java_Is_AllMight.Sensors.LimelightHelpers;

public class TurretSubsytem {
    public SparkMax shooterMotor;
    public SparkMax turretMotor;

    private SwerveSubsystem swerve;

    private Pose2d robotPose;
    private Translation2d goal;

    private PIDConfig motorPID;
    private PIDController turretPID;

    boolean hasTarget;

    public TurretSubsytem(SwerveSubsystem swerve) {
        motorPID = new PIDConfig(0.5, 0.0, 0.0, 0.0, 0.0);
        turretPID = new PIDController(0.5, 0.0, 0.0);
        turretPID.enableContinuousInput(-Math.PI, Math.PI); //Valor do Encoder em Radianos

        shooterMotor = SparkConfigurator.createSparkMax(9, MotorType.kBrushless, motorPID, IdleMode.kCoast, 40); 
        turretMotor = SparkConfigurator.createSparkMax(10, MotorType.kBrushless, motorPID, IdleMode.kCoast, 40);

        this.swerve = swerve;

        goal = new Translation2d(10, 10); //Exemplo
        hasTarget = LimelightHelpers.getTV("turretlimelight");
    }

    void shoot(){

    }

    public void alignTurret(){
        robotPose = swerve.getPose();

        double dx = goal.getX() - robotPose.getX();
        double dy = goal.getY() - robotPose.getY();

        double angle = Math.atan2(dy, dx);
        double robotHeading = robotPose.getRotation().getRadians();

        double turretTargetAngle = MathUtil.angleModulus(angle - robotHeading);
        double currentTurretAngle = turretMotor.getEncoder().getPosition();

        double output = turretPID.calculate(currentTurretAngle, turretTargetAngle);
        output = MathUtil.clamp(output, 0.0, 1.0);

        turretMotor.set(output);
    } 
}
