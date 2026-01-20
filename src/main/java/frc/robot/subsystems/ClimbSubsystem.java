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
    private final SparkFlex leftMotor;
    private final SparkFlex rightMotor;

    private static final int LEFT_MOTOR_ID = 17;
    private static final int RIGHT_MOTOR_ID = 18;

    // Configurações de PID dos motores
    private static final PIDConfig CLIMB_PID = new PIDConfig(0, 0, 0);

    // Limite de corrente dos motores de climb
    private static final int CURRENT_LIMIT = 40;

    // Limite de potencia dos motores
    private static final double OUT_MIN = -1.0;
    private static final double OUT_MAX = 1.0;

    //Redução dos motores
    private static final double CLIMB_GEAR_RATIO = 60.0;

    // Construtor do subsistema de climb
    public ClimbSubsystem() {

        leftMotor = SparkConfigurator.createSparkFlex(
            LEFT_MOTOR_ID,
            MotorType.kBrushless,
            CLIMB_PID,
            IdleMode.kBrake,
            CURRENT_LIMIT,
            OUT_MIN,OUT_MAX,
            CLIMB_GEAR_RATIO
        );

        rightMotor = SparkConfigurator.createSparkFlexFollower(
            RIGHT_MOTOR_ID,
            MotorType.kBrushless,
            IdleMode.kBrake,
            CURRENT_LIMIT,
            LEFT_MOTOR_ID, true, 
            CLIMB_GEAR_RATIO
        );
    }

    // Prepara o robô para iniciar o climb
    public void climbUp() {
        setPosition(0.3);
        
    }

    // Reseta o sistema de climb para a posição inicial
    public void climbDown() {
        setPosition(0.0);

    }

    // Define a posição alvo do motor usando controle em malha fechada (PID)
    private void setPosition(double position) {
        leftMotor.getClosedLoopController().
        setSetpoint(position, ControlType.kPosition);
    }
}



