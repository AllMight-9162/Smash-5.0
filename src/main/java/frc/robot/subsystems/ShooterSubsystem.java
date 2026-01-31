package frc.robot.subsystems;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.Java_Is_AllMight.Control.PIDConfig;
import frc.Java_Is_AllMight.Motors.SparkConfigurator;

public class ShooterSubsystem extends SubsystemBase {

    private final SwerveSubsystem swerve;

    private final SparkFlex shooterLeaderMotor;
    private final SparkMax  takeMotor;

    private static final int TAKE_ID = 13;
    private static final int SHOOTER_ID = 14;

    private static final int CURRENT_LIMIT = 40;
    
    private static final PIDConfig MOTOR_PID = new PIDConfig(
        0.00025,  
        0.0,      
        0.0,      
        0.003, 
        0.0);

    private static final double OUTPUT_MIN = -1.0;
    private static final double OUTPUT_MAX = 1.0;

    private static final double RAMP_RATE = 0.3;
    private static final double RPM_TOLERANCE = 120.0;
    private static final double GEAR_RATIO = 1.0;

    private double targetRPM = 0.0;

    private static final Translation2d BLUE_HUB = new Translation2d(4.6, 4.0);
    private static final Translation2d RED_HUB = new Translation2d(12.0, 4.0);


    private final InterpolatingDoubleTreeMap rpmTable =
        new InterpolatingDoubleTreeMap();

    public ShooterSubsystem(SwerveSubsystem swerve) {
        this.swerve = swerve;

        takeMotor = SparkConfigurator.createSparkMax(
            TAKE_ID,
            MotorType.kBrushed,
            null,
            IdleMode.kCoast,
            CURRENT_LIMIT,
            OUTPUT_MIN, OUTPUT_MAX,
            GEAR_RATIO
            );

        shooterLeaderMotor = SparkConfigurator.createSparkFlex(
            SHOOTER_ID,
            MotorType.kBrushless,
            MOTOR_PID,
            IdleMode.kCoast,
            CURRENT_LIMIT,
            OUTPUT_MIN, OUTPUT_MAX,
            GEAR_RATIO
        );

        SparkConfigurator.configureRampSparkFlex(
            shooterLeaderMotor, RAMP_RATE
       );     

        rpmTable.put(0.0, 0.0);
        rpmTable.put(1.0, 2000.0);
        rpmTable.put(2.0, 3000.0);
        rpmTable.put(3.0, 4000.0);
        rpmTable.put(4.0, 5000.0);
        rpmTable.put(5.0, 6000.0);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Shooter/TargetRPM",
            targetRPM);
        SmartDashboard.putNumber("Shooter/ActualRPM",
            getRPM());
    
    }

    public void shoot() {
        double distance = MathUtil.clamp(getDistanceFromHub(), 1.0, 5.0);

        targetRPM = rpmTable.get(distance);

        shooterLeaderMotor.getClosedLoopController()
            .setSetpoint(targetRPM, ControlType.kVelocity);
        
         take();
    }

    public void stopShooter() {
        shooterLeaderMotor.set(0.0);
        stopTake();
        targetRPM = 0.0;
    }

    private void take() {
       takeMotor.set(1.0);
    }

    private void stopTake() {
       takeMotor.set(0.0);
    }

    private boolean atSpeed() {
        return Math.abs(getRPM() - targetRPM) <= RPM_TOLERANCE;
    }

    private double getRPM() {
        return shooterLeaderMotor.getEncoder().getVelocity();
    }

    private double getDistanceFromHub() {
        Pose2d pose = swerve.getPose();
        return pose.getTranslation()
            .getDistance(getHubPosition());
    }

    private Translation2d getHubPosition() {
        return DriverStation.getAlliance()
            .filter(a -> a == Alliance.Red)
            .map(a -> RED_HUB)
            .orElse(BLUE_HUB);
    }
}