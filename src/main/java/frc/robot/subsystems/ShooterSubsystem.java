package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.Java_Is_AllMight.Motors.SparkConfigurator;
import frc.Java_Is_AllMight.Sensors.LimelightHelpers;

/*
 * SHOOTER SUBSYSTEM
 *
 * O QUE ISSO FAZ:
 * - Controla APENAS a potência do motor do shooter
 * - Calcula essa potência com base na distância até o alvo
 * - Usa a Limelight quando a visão está confiável
 * - Usa a odometria do swerve como alternativa quando a visão some
 *
 * RESUMÃO DE COMO FUNCIONA:
 * 1) Se a Limelight estiver vendo a TAG correta:
 *    -> pegamos a distância direto da câmera (em metros)
 *
 * 2) Se NÃO estiver vendo:
 *    -> calculamos a distância até o alvo usando a pose do robô no campo
 *
 * 3) Essa distância vira uma potência de shooter usando uma fórmula linear
 *
 * COMO A POTÊNCIA É CALCULADA:
 * 
 *   shooterPower = DIST_TO_POWER_M * distanceMeters + DIST_TO_POWER_B
 *
 * Onde:
 * - distanceMeters = distância até o alvo (em metros)
 * - DIST_TO_POWER_M = o quanto a potência cresce a cada metro
 * - DIST_TO_POWER_B = potência mínima base (offset)
 *
 * EXEMPLO:
 * - Distância: 3 metros
 * - Cálculo: (0.08 * 3) + 0.2 = 0.44
 * - Shooter gira com +/- 44% da potência
 *
 * POR QUE ISSO FUNCIONA:
 * - Quanto mais longe o alvo, mais forte o shooter precisa girar
 * - A relação é aproximada, simples e fácil de ajustar no campo
 *
 * O QUE PRECISA SER AJUSTADO NO TESTE:
 *
 * 1) DIST_TO_POWER_M
 *    - Se bolas estão caindo antes -> aumentar
 *    - Se estão passando do alvo -> diminuir
 *
 * 2) DIST_TO_POWER_B
 *    - Potência mínima pra bola sair com consistência
 *
 * 3) TARGET_TAG_ID
 *    - ID correto da AprilTag do alvo
 *
 * SE LIGA ALLMIGHTERS DE PLANTAO, ANTES DE MEXER:
 * - Confere primeiro se a distância está correta
 * - Depois mexe na potência
 * - NÃO sai mexendo em tudo ao mesmo tempo
 * - Se mexer em uma coisa e quebrar tudo, VOLTA PRO ULTIMO COMMIT Rafael ou Niord ou quem tiver lendo, pelo amor de deus
 */

public class ShooterSubsystem {

  //Hardware
  private final SparkMax shooterMotor;

  //Swerve
  private final SwerveSubsystem swerve;

  //Limelight
  private static final int TARGET_TAG_ID = 21;

  //Posição fixa do alvo no campo ajustarrrrrrrrr
  private final Translation2d goalPosition = new Translation2d(16.54, 5.55);

  //Limites de potência por segurança
  private static final double MIN_POWER = 0.0;
  private static final double MAX_POWER = 1.0;

  //Ajuste distância -> potência
  private static final double DIST_TO_POWER_M = 0.08;
  private static final double DIST_TO_POWER_B = 0.2;

  private static final String LIMELIGHT_NAME = "limelightshooter";

  private double shooterPower = 0.0;

  public ShooterSubsystem(SwerveSubsystem swerve) {

    this.swerve = swerve;

    shooterMotor = SparkConfigurator.createSparkMax(
        9,
        MotorType.kBrushless,
        null,
        IdleMode.kCoast,
        40
    );
  }

  public void periodic() {

    double distanceMeters;

    boolean hasVision = LimelightHelpers.getTV(LIMELIGHT_NAME);
    int seenTagId = (int) LimelightHelpers.getFiducialID(LIMELIGHT_NAME);

    boolean validVision = hasVision && seenTagId == TARGET_TAG_ID;

    if (validVision) {
      //Distância vinda diretamente da Limelight
      distanceMeters = LimelightHelpers.getTargetPose3d_RobotSpace(LIMELIGHT_NAME)
          .getTranslation()
          .getNorm(); //a distância da origem até a translation.
    } else {
      // Alternativa: distância via odometria das roda
      distanceMeters = getDistanceFromOdometria();
    }

    shooterPower = distanceToPower(distanceMeters);
    shooterPower = MathUtil.clamp(shooterPower, MIN_POWER, MAX_POWER);

    SmartDashboard.putBoolean("Shooter/Has Vision", hasVision);
    SmartDashboard.putBoolean("Shooter/Valid Vision", validVision);
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
    return robotPos.getDistance(goalPosition);
  }

  //Liga o shooter
  public void shoot() {
    shooterMotor.set(shooterPower);
  }

  //Desligar o shooter
  public void stop() {
    shooterMotor.set(0.0);
  }
}

