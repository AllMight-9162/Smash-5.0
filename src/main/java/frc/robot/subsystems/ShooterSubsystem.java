package frc.robot.subsystems;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkBase.ControlType;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

import frc.Java_Is_AllMight.Motors.SparkConfigurator;

public class ShooterSubsystem extends SubsystemBase {

    private final SwerveSubsystem swerve;

    private final SparkFlex leftShooterMotor;
    private final SparkFlex rightShooterMotor;
    private final SparkMax shooterTakeMotor;

    private static final int LEFT_MOTOR_ID = 14; 
    private static final int RIGHT_MOTOR_ID = 15; 
    private static final int SHOOTERTAKE_MOTOR_ID = 16;

    private static final int CURRENT_LIMIT = 40;

    private static final double SHOOTER_OUT_MIN = -1.0;
    private static final double SHOOTER_OUT_MAX = 1.0;

    private static final double SHOOTER_GEAR_RATIO = 1.0;

    private final Translation2d hubPosition = new Translation2d(4.6, 4.0);

    private final InterpolatingDoubleTreeMap rpmTable = new InterpolatingDoubleTreeMap();

    private static final double RPM_TOLERANCE = 120.0;
    private static final double RAMP_RATE = 0.3;

    private double targetRPM = 0.0;
    private boolean shooterAtivo = false;

    public ShooterSubsystem(SwerveSubsystem swerve) {
        this.swerve = swerve;

        leftShooterMotor = SparkConfigurator.createSparkFlex(
            LEFT_MOTOR_ID,
            MotorType.kBrushless,
            null,
            IdleMode.kCoast,
            CURRENT_LIMIT,
            SHOOTER_OUT_MIN, SHOOTER_OUT_MAX,
            SHOOTER_GEAR_RATIO
        ); 
        SparkConfigurator.configureRampSparkFlex(leftShooterMotor, RAMP_RATE);

        rightShooterMotor = SparkConfigurator.createSparkFlexFollower(
            RIGHT_MOTOR_ID,
            MotorType.kBrushless,
            IdleMode.kCoast,
            CURRENT_LIMIT,
            LEFT_MOTOR_ID, true, 
            SHOOTER_GEAR_RATIO
        );

        shooterTakeMotor = SparkConfigurator.createSparkMax(
            SHOOTERTAKE_MOTOR_ID,
            MotorType.kBrushed,
            null,
            IdleMode.kCoast,
            CURRENT_LIMIT,
            -1.0, 1.0,
            1.0
        );

        rpmTable.put(1.0, 1800.0);
        rpmTable.put(2.0, 2280.0);
        rpmTable.put(3.0, 2760.0);
        rpmTable.put(4.0, 3480.0);
        rpmTable.put(5.0, 3900.0);
    }

    @Override
    public void periodic() {
        double distanceMeters = getDistanceFromHub();

        // Calcula RPM alvo interpolando na tabela
        targetRPM = rpmTable.get(distanceMeters);

        SmartDashboard.putNumber("Shooter/Distance (m)", distanceMeters);
        SmartDashboard.putNumber("Shooter/Target RPM", targetRPM);
        SmartDashboard.putNumber("Shooter/Actual RPM", getShooterRPM());

        if (shooterAtivo) {
            shoot();
        }

    }

    public void toggleShoot() {
        shooterAtivo = !shooterAtivo;

        if(!shooterAtivo){
            stop();
        }
    }

    // Liga o shooter e o motor do intake quando atingir RPM
    private void shoot() {
        leftShooterMotor.getClosedLoopController()
            .setSetpoint(targetRPM, ControlType.kVelocity);

        if (isShooterAtSpeed()) {
            shooterTakeMotor.set(0.8);
        } else {
            shooterTakeMotor.set(0.0);
        }
    }

    // Desliga todos os motores do shooter
    private void stop() {
        leftShooterMotor.set(0.0);
        shooterTakeMotor.set(0.0);
    }

    // Retorna a velocidade atual do shooter em RPM
    private double getShooterRPM() {
        return leftShooterMotor.getEncoder().getVelocity();
    }

    // Verifica se o shooter já está na velocidade alvo
    private boolean isShooterAtSpeed() {
        return Math.abs(getShooterRPM() - targetRPM) <= RPM_TOLERANCE;
    }

    // Calcula a distância até o hub usando a pose do robô
    private double getDistanceFromHub() {
        Pose2d robotPose = swerve.getPose();
        Translation2d robotPos = robotPose.getTranslation();
        return robotPos.getDistance(hubPosition);
    }
}


