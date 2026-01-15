package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.Java_Is_AllMight.Motors.SparkConfigurator;

public class ShooterSubsystem extends SubsystemBase {

  private final SparkMax shooterRightMotor;
  private final SparkMax shooterLeftMotor;
  private final SparkMax shooterTakeMotor;

  private final SwerveSubsystem swerve;

  private final Translation2d hubPosition = new Translation2d(4.6, 4.0);

  //Limites de potência por segurança
  private static final double MIN_POWER = 0.0;
  private static final double MAX_POWER = 1.0;

  //Ajuste distância -> potência
  private static final double DIST_TO_POWER_M = 0.08;
  private static final double DIST_TO_POWER_B = 0.2;

  private double shooterPower = 0.0;

  public ShooterSubsystem(SwerveSubsystem swerve) {
    this.swerve = swerve;

    shooterLeftMotor = SparkConfigurator.createSparkMax(
        12,
        MotorType.kBrushless,
        null,
        IdleMode.kCoast,
        40
    );

    shooterRightMotor = SparkConfigurator.createSparkMax(
        13,
        MotorType.kBrushless,
        null,
        IdleMode.kCoast,
        40
    );

    shooterTakeMotor = SparkConfigurator.createSparkMax(
        14,
        MotorType.kBrushless,
        null,
        IdleMode.kCoast,
        40
    );

  }

  public void periodic() {

    double distanceMeters;
    
    distanceMeters = getDistanceFromOdometria();

    shooterPower = distanceToPower(distanceMeters);
    shooterPower = MathUtil.clamp(shooterPower, MIN_POWER, MAX_POWER);

    SmartDashboard.putNumber("Shooter/Distance (m)", distanceMeters);
    SmartDashboard.putNumber("Shooter/Power", shooterPower);
  }

  //Converte distância até o alvo em potência do shooter
  private double distanceToPower(double distanceMeters) {
    return DIST_TO_POWER_M * distanceMeters + DIST_TO_POWER_B;
  }

  //Calcula distância usando a pose do robô no campo
  private double getDistanceFromOdometria() {
    Pose2d robotPose = swerve.getPose();
    Translation2d robotPos = robotPose.getTranslation();
    return robotPos.getDistance(hubPosition);
  }

  //Liga o shooter
  public void shoot() {
    shooterLeftMotor.set(shooterPower);
    shooterRightMotor.set(-shooterPower);
    shooterTakeMotor.set(0.5);
  }

  //Desligar o shooter
  public void stop() {
    shooterLeftMotor.set(0.0);
    shooterRightMotor.set(0.0);
    shooterTakeMotor.set(0.0);
  }
}

