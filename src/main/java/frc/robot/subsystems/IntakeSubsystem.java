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

// Subsistema do mecanismo do intake
public class IntakeSubsystem extends SubsystemBase {

    // Motores do sistema de intake
    private final SparkMax IntakeMotor;
    private final SparkMax IntakeAngulateMotor;

    private static final int ID_MOTOR_INTAKE = 15; // TEMPORARIO PRA TESTES, LEMBRAR DE MUDARRRRRR
    private static final int ID_MOTOR_INTAKE_ANGULATE = 16;

    // Configuração de PID do motor de angulação
    private static final PIDConfig MOTOR_INTAKE_PID = new PIDConfig(0.0, 0.0, 0.0);

    // Limite de corrente dos motores do intake
    private static final int MOTOR_INTAKE_CURRENT_LIMIT = 40;

    // Construtor do subsistema do intake
    public IntakeSubsystem() {

        IntakeMotor = SparkConfigurator.createSparkMax(
            ID_MOTOR_INTAKE,
            MotorType.kBrushless,
            null,
            IdleMode.kCoast,
            MOTOR_INTAKE_CURRENT_LIMIT
        );

        IntakeAngulateMotor = SparkConfigurator.createSparkMax(
            ID_MOTOR_INTAKE_ANGULATE,
            MotorType.kBrushless,
            MOTOR_INTAKE_PID,
            IdleMode.kBrake,
            MOTOR_INTAKE_CURRENT_LIMIT
        );
    }

    // Posiciona o intake e inicia a coleta
    public void take() {
        //angulate(30);
        IntakeMotor.set(0.55);
    }

    // Para o motor de intake
    public void stop() {
        IntakeMotor.set(0.0);
    }

    // Recolhe o intake para a posição inicial
    public void retract() {
        stop();
        angulate(0.0);
    }

    // Define a posição alvo do motor de angulação usando controle em malha fechada (PID)
    private void angulate(double setPosition) {
       IntakeAngulateMotor
           .getClosedLoopController()
           .setSetpoint(setPosition, ControlType.kPosition);
    }
}

