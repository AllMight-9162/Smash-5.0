package frc.Java_Is_AllMight.Codes;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.Java_Is_AllMight.Control.PIDConfig;
import frc.Java_Is_AllMight.Control.PIDController;
import frc.Java_Is_AllMight.Motors.SparkConfigurator;
import frc.Java_Is_AllMight.Sensors.LimelightHelpers;
import frc.robot.subsystems.SwerveSubsystem;

/*
 * TURRET SUBSYSTEM 
 * 
 * O QUE ISSO FAZ:
 * - Controla a rotação da turret para alinhar com o alvo automaticamente
 * - Prioriza a Limelight (visão) quando o alvo é visível
 * - Usa odometria do swerve como alternativa quando a visão some
 * - Mantem microajustes constantes (sem parar a turret)
 * 
 * RESUMÃO DE COMOFUNCIONA:
 * 1) Se a Limelight enxergar o alvo:
 *    -> usamos o tx (erro angular da câmera)
 *    -> o PID tenta zerar esse erro
 * 
 * 2) Se NÃO enxergar o alvo:
 *    -> calculamos o ângulo até o alvo usando a pose do robô (odometria)
 *    -> compensamos o heading do robô
 *    -> o PID alinha a turret para esse ângulo
 * 
 * 3) Existe um deadband pequeno pra evitar tremedeira quando o erro é mínimo
 * 
 * ====================== O QUE PRECISA SER AJUSTADO PRA TESTAAAR ======================
 * 
 * 1) ROTATIONS_PER_RADIANS
 *    - Esse valor converte rotações do motor -> radianos da turret
 *    - Depende da relação de engrenagens
 *    - Se estiver errado a
 *        -> turret não chega no ângulo certo
 *        -> ou gira demais / de menos
 * 
 *    COMO TESTAR:
 *    - Gira a turret manualmente ~90 graus (π/2 rad)
 *    - Ve quantas rotações o encoder marcou
 *    - ROTATIONS_PER_RADIANS = rotações / radianos
 * 
 * 2) LIMITES MECÂNICOS (MIN_TURRET_ROT / MAX_TURRET_ROT)
 *    - Evitam quebrar fio ou engrenagem
 *    - Ajustar conforme o limite físico real da turret
 * 
 * 3) PID DA TURRET (kP principalmente)
 *    - Começar só com kP
 *    - Se oscilar -> kP alto demais
 *    - Se for lento -> kP baixo demais
 * 
 * 4) DEADZONE (TURRET_DEADBAND_RAD)
 *    - Se a turret ficar tremendo, aumentar um pouco
 *    - Se ficar imprecisa, diminuir
 * 
 *  5) TARGET_TAG_ID 
 *    - ID da tag que a limelight vai seguri
 * 
 * IMPORTANTE:
 * - NÃO misturar tx da Limelight com odometria
 * - NÃO remover o deadband achando que "vai ficar mais preciso"
 * - Se mexer em uma coisa e quebrar tudo, VOLTAP PRO ULTIMO COMMIT Rafael ou Niord ou quem tiver lendo, pelo amor de deus
 */

public class TurretSubsytem {
  //Harware
  private final SparkMax shooterMotor;
  private final SparkMax turretMotor;

  //Swerve
  private final SwerveSubsystem swerve;

  //Controle
  private final PIDController shooterPID = new PIDController(new PIDConfig(0, 0, 0));
  private final PIDController turretPID = new PIDController(new PIDConfig(0, 0, 0));

  //positons
  private Translation2d goalPosition; //Posicao fixa do alvo no campo

  //constants
  //Quantas rotações do motor equivalem a 1 radiano da turret
  private static final double ROTATIONS_PER_RADIANS = 1.0;  //tem que ajusta

  // Limites da turret -em rotações do motor
  private static final double MIN_TURRET_ROT = -2.0;
  private static final double MAX_TURRET_ROT =  2.0;
  private static final String TURRET_LIMELIGHT = "turretlimelight";
  private static final double TURRET_DEADBAND_RAD = Math.toRadians(0.75);
  private static final int TARGET_TAG_ID = 21;

  //Limelight
  private boolean hasVisionTarget;
  private double visionYawRad;

  public TurretSubsytem(SwerveSubsystem swerve) {

    this.swerve = swerve;

    // Motor da turret  
    turretMotor = SparkConfigurator.createSparkMax(
      10,
      MotorType.kBrushless,
      new PIDConfig(0.5, 0.0, 0.0, 0.0, 0.0),
      IdleMode.kCoast,
      40
      );
    
    shooterMotor = SparkConfigurator.createSparkMax(
      9, 
      MotorType.kBrushless, 
      new PIDConfig(0.5, 0.0, 0.0, 0.0, 0.0), 
      IdleMode.kCoast, 
      40
      ); 
    // Posição fixa do alvo no campo (exemplo)
    goalPosition = new Translation2d(16.54, 5.55);
    }
  
  public void periodic() {

    // Verifica se a Limelight está vendo um alvo válido
    hasVisionTarget = LimelightHelpers.getTV(TURRET_LIMELIGHT);
    int seenTagId = (int) LimelightHelpers.getFiducialID(TURRET_LIMELIGHT);
    double errorRad;

    boolean validVision = hasVisionTarget && seenTagId == TARGET_TAG_ID;

    if (validVision) {
        //Contorle por visão usando tx
        errorRad = Math.toRadians(LimelightHelpers.getTX(TURRET_LIMELIGHT));
    } else {
        //Se não tiver a limelight usa a famosa odometria das roda
        // Calcula onde a turret DEVERIA estar mirando
        double setpoint = getTurretSetpoint();
        double current = getTurretAngleInRad();
        errorRad = PIDController.angleError(setpoint, current);
    }

    //Deadband(zona morta da turret pra nao ficar oscilando esssa bagaça)
    if (Math.abs(errorRad) < TURRET_DEADBAND_RAD) {
        errorRad = 0.0;
    }

    //PID 
    double output = turretPID.calculate(0.0, errorRad);
    output = MathUtil.clamp(output, -1.0, 1.0);

    double motorRot = turretMotor.getEncoder().getPosition();

    //Limite mecanico pra nao quebrar tudo
    if ((motorRot <= MIN_TURRET_ROT && output < 0) ||
        (motorRot >= MAX_TURRET_ROT && output > 0)) {
        output = 0;
    }

    turretMotor.set(output);
    SmartDashboard.putBoolean("Turret/Has Vision", hasVisionTarget);
    SmartDashboard.putNumber("Turret/Seen Tag ID", seenTagId);
    SmartDashboard.putBoolean("Turret/Valid Vision", validVision);
    SmartDashboard.putNumber("Turret/Error (rad)", errorRad);
    SmartDashboard.putNumber(
        "Turret/Angle (rad)",
        getTurretAngleInRad()
    );

}

  //Calcula o ângulo absoluto do robô até o alvo no campo
  private double getTargetAngle(){
    Pose2d robotPose = swerve.getPose();

    Translation2d robotPos = robotPose.getTranslation();
    Translation2d toTarget = goalPosition.minus(robotPos);

    return Math.atan2(toTarget.getY(), toTarget.getX());
  }

  //Converte o ^^angulo do campo em ângulo relativo à turret
  private double getTurretSetpoint(){
    double targetAngle = getTargetAngle();
    double robotYaw = swerve.getPose().getRotation().getRadians();

    return targetAngle - robotYaw;
  }

 //Converte rotações do motor em radianos da turret
  private double getTurretAngleInRad(){
    double motorRotations = turretMotor.getEncoder().getPosition();
    return motorRotations / ROTATIONS_PER_RADIANS;
  }

}
