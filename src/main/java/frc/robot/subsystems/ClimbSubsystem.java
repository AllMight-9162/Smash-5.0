/*
 * Subsistema responsável pelo mecanismo de climb (subida do robô).
 */
package frc.robot.subsystems;

// Dependências necessárias para o funcionamento do subsistema
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkBase.ControlType;
import frc.Java_Is_AllMight.Control.PIDConfig;
import frc.Java_Is_AllMight.Motors.SparkConfigurator;

// Subsistema do mecanismo de climb
public class ClimbSubsystem extends SubsystemBase {

    // Motores do sistema de climb
    private final SparkFlex climbAngulateMotor;
    private final SparkFlex climbLeftMotor;
    private final SparkFlex climbRightMotor;
    private final SwerveSubsystem swerve;

    private static final int LEFT_MOTOR_CLIMB_ID = 17;
    private static final int RIGHT_MOTOR_CLIMB_ID = 18;
    private static final int ANGULATE_MOTOR_CLIMB_ID = 19;

    // Configurações de PID dos motores
    private static final PIDConfig MOTOR_CLIMB_PID = new PIDConfig(0, 0, 0);
    private static final PIDConfig ANGULATE_MOTOR_CLIMB_PID = new PIDConfig(0, 0, 0);

    // Limite de corrente dos motores de climb
    private static final int MOTOR_CLIMB_CURRENT_LIMIT = 40;

    // Construtor do subsistema de climb
    public ClimbSubsystem(SwerveSubsystem swerveSubsystem) {
        this.swerve = swerveSubsystem;

        climbLeftMotor = SparkConfigurator.createSparkFlex(
            LEFT_MOTOR_CLIMB_ID,
            MotorType.kBrushless,
            MOTOR_CLIMB_PID,
            IdleMode.kBrake,
            MOTOR_CLIMB_CURRENT_LIMIT
        );

        climbRightMotor = SparkConfigurator.createSparkFlex(
            RIGHT_MOTOR_CLIMB_ID,
            MotorType.kBrushless,
            MOTOR_CLIMB_PID,
            IdleMode.kBrake,
            MOTOR_CLIMB_CURRENT_LIMIT
        );

        climbAngulateMotor = SparkConfigurator.createSparkFlex(
            ANGULATE_MOTOR_CLIMB_ID,
            MotorType.kBrushless,
            ANGULATE_MOTOR_CLIMB_PID,
            IdleMode.kBrake,
            MOTOR_CLIMB_CURRENT_LIMIT
        );
    }

    // Prepara o robô para iniciar o climb
    public void prepareClimb() {
        swerve.setSpeedMultiplier(0.2);
        setPosition(climbAngulateMotor, 0.3);
    }

    // Inicia o processo de climb do robô
    public void startClimbing() {
        setPosition(climbLeftMotor, 0.2);
        setPosition(climbRightMotor, 0.2);
    }

    // Reseta o sistema de climb para a posição inicial
    public void resetClimb() {
        swerve.setSpeedMultiplier(1.0);
        setPosition(climbAngulateMotor, 0);
        setPosition(climbLeftMotor, 0);
        setPosition(climbRightMotor, 0);
    }

    // Define a posição alvo do motor usando controle em malha fechada (PID)
    private void setPosition(SparkFlex motor, double setPosition) {
        motor.getClosedLoopController().
        setSetpoint(setPosition, ControlType.kPosition);
    }
}



