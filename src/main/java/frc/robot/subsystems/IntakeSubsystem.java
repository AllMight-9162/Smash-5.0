/*
 * Subsistema responsável pelo mecanismo de intake (coleta de peças)
 */
package frc.robot.subsystems;

// Dependências necessárias para o funcionamento do subsistema
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkBase.ControlType;

import frc.Java_Is_AllMight.Control.PIDConfig;
import frc.Java_Is_AllMight.Motors.SparkConfigurator;

@SuppressWarnings("unused")
// Subsistema do mecanismo do intake
public class IntakeSubsystem extends SubsystemBase {

    // Motores do sistema de intake
    private final SparkMax leftIntakeMotor;
    private final SparkMax rightIntakeMotor;
    private final SparkMax leftAngulateMotor;
    private final SparkMax rightAngulateMotor;

    private static final int LEFT_ANGULATE_MOTOR_ID = 9;
    private static final int RIGHT_ANGULATE_MOTOR_ID = 10;
    private static final int LEFT_INTAKE_MOTOR_ID = 11; 
    private static final int RIGHT_INTAKE_MOTOR_ID = 12;
    
    // Configuração de PID do motor de angulação
    private static final PIDConfig ANGULATE_PID  = new PIDConfig(0.3, 0.0, 0.0);

    // Limite de corrente dos motores
    private static final int CURRENT_LIMIT = 120;

    // Limite de potencia dos motores
    private static final double INTAKE_OUT_MIN = -1.0;
    private static final double INTAKE_OUT_MAX = 1.0;
    private static final double ANGULATE_OUT_MIN = -1.0;
    private static final double ANGULATE_OUT_MAX = 1.0;

    // Redução dos motores
    private static final double INTAKE_GEAR_RATIO = 15;
    private static final double ANGULATE_GEAR_RATIO = 27;

    // Variavel de controle do estado do intake
    private boolean intakeativo = false;

    // Construtor do subsistema do intake
    public IntakeSubsystem() {

        leftIntakeMotor = SparkConfigurator.createSparkMax(
            LEFT_INTAKE_MOTOR_ID,
            MotorType.kBrushless,
            null,
            IdleMode.kCoast,
            CURRENT_LIMIT,
            INTAKE_OUT_MIN,INTAKE_OUT_MAX,
            INTAKE_GEAR_RATIO
        );

        rightIntakeMotor = SparkConfigurator.createSparkMaxFollower(
            RIGHT_INTAKE_MOTOR_ID,
            MotorType.kBrushless,
            IdleMode.kCoast,
            CURRENT_LIMIT,
            LEFT_INTAKE_MOTOR_ID, true,
            INTAKE_GEAR_RATIO
        );

        leftAngulateMotor = SparkConfigurator.createSparkMax(
            LEFT_ANGULATE_MOTOR_ID,
            MotorType.kBrushless,
            ANGULATE_PID ,
            IdleMode.kBrake,
            CURRENT_LIMIT,
            ANGULATE_OUT_MIN, ANGULATE_OUT_MAX,
            ANGULATE_GEAR_RATIO
        );

        rightAngulateMotor = SparkConfigurator.createSparkMaxFollower(
            RIGHT_ANGULATE_MOTOR_ID,
            MotorType.kBrushless,
            IdleMode.kBrake,
            CURRENT_LIMIT,
            LEFT_ANGULATE_MOTOR_ID, true,
            ANGULATE_GEAR_RATIO
        );
    }

    public void toggleIntake(){
        intakeativo = !intakeativo;

        if(intakeativo){
            take();
        } else {
            stop();
        }
    }

    // Recolhe o intake para a posição inicial
    public void retract() {
        stop();
        angulate(0.0);
    }
    
    public void Init(){
        leftAngulateMotor.getEncoder().setPosition(0.0);
        stop();
        angulate(0.2);
    }

    // Posiciona o intake e inicia a coleta
    private void take() {
        leftIntakeMotor.set(0.7);
    }

    // Para o motor de intake
    private void stop() {
        leftIntakeMotor.set(0.0);
    }

    // Define a posição alvo em graus do motor de angulação usando controle em malha fechada (PID)
    private void angulate(double set) {
        leftAngulateMotor.getClosedLoopController()
        .setSetpoint(set, ControlType.kPosition);
    }
}

