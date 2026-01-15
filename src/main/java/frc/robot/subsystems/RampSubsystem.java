package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.Java_Is_AllMight.Control.PIDConfig;
import frc.Java_Is_AllMight.Motors.SparkConfigurator;

public class RampSubsystem extends SubsystemBase {

    private final SparkMax RampMotor1;
    private final SparkMax RampMotor2;

    private static final int ID_MOTOR_RAMP = 15;
    private static final int ID_MOTOR_RAMP2 = 16;

    // Configuração de PID do motor de angulação
    private static final PIDConfig MOTOR_RAMP_PID = new PIDConfig(0.0, 0.0, 0.0);

    // Limite de corrente dos motores do intake
    private static final int MOTOR_RAMP_CURRENT_LIMIT = 40;

    // Construtor do subsistema do intake
    public RampSubsystem() {

        RampMotor1 = SparkConfigurator.createSparkMax(
            ID_MOTOR_RAMP,
            MotorType.kBrushless,
            null,
            IdleMode.kCoast,
            MOTOR_RAMP_CURRENT_LIMIT
        );

        RampMotor2 = SparkConfigurator.createSparkMax(
            ID_MOTOR_RAMP2,
            MotorType.kBrushless,
            null,
            IdleMode.kBrake,
            MOTOR_RAMP_CURRENT_LIMIT
        );
    }

    // Posiciona o intake e inicia a coleta
    public void take() {
        RampMotor1.set(0.6);
        RampMotor2.set(0.6);
    }

    // Para o motor de intake
    public void stop() {
        RampMotor1.set(0.0);
        RampMotor2.set(0.0);
    }
}
