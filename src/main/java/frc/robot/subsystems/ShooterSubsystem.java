package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

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

import frc.Java_Is_AllMight.Control.PIDConfig;
import frc.Java_Is_AllMight.Motors.SparkConfigurator;

public class ShooterSubsystem extends SubsystemBase {

    private final SwerveSubsystem swerve;

    private final SparkMax  takerMotor;
    private final SparkFlex shooterMotor;

    private static final int TAKE_ID = 13;
    private static final int SHOOTER_ID = 14;

    private static final int CURRENT_LIMIT = 60;
    
    private static final PIDConfig MOTOR_PID = new PIDConfig(
        0.00021,  
        0.0,      
        0.0,      
        0.0026, 
        0.0);

    private static final double OUTPUT_MIN = -1.0;
    private static final double OUTPUT_MAX = 1.0;

    private static final double RAMP_RATE = 0.2;
    private static final double RPM_TOLERANCE = 100.0;
    private static final double GEAR_RATIO = 1.0;

    private double targetRPM = 0.0;
    private double distance = 0.0;

    private static final Translation2d BLUE_HUB = new Translation2d(4.6, 4.0);
    private static final Translation2d RED_HUB = new Translation2d(12.0, 4.0);

    private final InterpolatingDoubleTreeMap rpmTable =
        new InterpolatingDoubleTreeMap();

    public ShooterSubsystem(SwerveSubsystem swerve) {
        this.swerve = swerve;

        takerMotor = SparkConfigurator.createSparkMax(
            TAKE_ID,
            MotorType.kBrushless,
            null,
            IdleMode.kCoast,
            CURRENT_LIMIT,
            OUTPUT_MIN, OUTPUT_MAX,
            GEAR_RATIO,
            false
           );

        shooterMotor = SparkConfigurator.createSparkFlex(
            SHOOTER_ID,
            MotorType.kBrushless,
            MOTOR_PID,
            IdleMode.kCoast,
            CURRENT_LIMIT,
            OUTPUT_MIN, OUTPUT_MAX,
            GEAR_RATIO,
            true
        );
        
        SparkConfigurator.configureRampSparkFlex(
            shooterMotor, RAMP_RATE
        );     

        rpmTable.put(0.0, 0.0);
        rpmTable.put(1.0, 1000.0);
        rpmTable.put(2.0, 1500.0);
        rpmTable.put(3.0, 2000.0);
        rpmTable.put(4.0, 2500.0);
        rpmTable.put(5.0, 3000.0);
        rpmTable.put(6.0, 3500.0);
        rpmTable.put(7.0, 4000.0);
    }

    @Override
    public void periodic() {
        double rawDistance = getDistanceFromHub();
        distance = MathUtil.clamp(rawDistance, 0.0, 7.0);

        SmartDashboard.putNumber("Shooter/Distance RAW", rawDistance);
        SmartDashboard.putNumber("Shooter/Distance CLAMPED", distance);

        SmartDashboard.putNumber("Shooter/TargetRPM",
            targetRPM);
        SmartDashboard.putNumber("Shooter/ActualRPM",
            getRPM());
        SmartDashboard.putBoolean("At speed", 
            atSpeed());
    
    }

    public void shoot() {

        targetRPM = rpmTable.get(distance);

        shooterMotor.getClosedLoopController()
        .setSetpoint(targetRPM, ControlType.kVelocity);

        if (atSpeed()) {
         takerMotor.set(1.0);
        } 
    }

    public void stop() {
        targetRPM = 0.0;
        shooterMotor.set(0.0);
        takerMotor.set(0.0);
    }

    private boolean atSpeed() {
        return Math.abs(getRPM() - targetRPM) <= RPM_TOLERANCE;
    }

    private double getRPM() {
        return shooterMotor.getEncoder().getVelocity();
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