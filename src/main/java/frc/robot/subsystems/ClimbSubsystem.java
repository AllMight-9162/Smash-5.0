package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkBase.ControlType;

import frc.Java_Is_AllMight.Control.PIDConfig;
import frc.Java_Is_AllMight.Motors.SparkConfigurator;

// Subsistema do mecanismo de climb
public class ClimbSubsystem extends SubsystemBase {

    // Motores do sistema de climb
    private final SparkMax Motor;

    private static final int MOTOR_ID = 15;

    // Configurações de PID dos motores
    private static final PIDConfig CLIMB_PID = new PIDConfig(1.5, 0, 0);

    // Limite de corrente dos motores de climb
    private static final int CURRENT_LIMIT = 60;

    // Limite de potencia dos motores
    private static final double OUT_MIN = -1.0;
    private static final double OUT_MAX = 1.0;

    //Redução dos motores
    private static final double CLIMB_GEAR_RATIO = 60.0;

    private boolean active = false;

    // Construtor do subsistema de climb
    public ClimbSubsystem() {

        Motor = SparkConfigurator.createSparkMax(
            MOTOR_ID,
            MotorType.kBrushless,
            CLIMB_PID,
            IdleMode.kBrake,
            CURRENT_LIMIT,
            OUT_MIN,OUT_MAX,
            CLIMB_GEAR_RATIO,
            true
        );

        SparkConfigurator.configureRampSparkMax(Motor, 0.1);
    }

    public void toggleClimb(){
        active = !active;

        if(active){
            climbUp();
        }else{
            climbDown();
        }
    }

    // Prepara o robô para iniciar o climb
    public void climbUp() {
        setPosition(2.4);
        
    }

    // Reseta o sistema de climb para a posição inicial
    public void climbDown() {
        setPosition(0.0);
    }

    public void Init(){
        Motor.getEncoder().setPosition(0.0);
    }
    // Define a posição alvo do motor usando controle em malha fechada (PID)
    private void setPosition(double position) {
        Motor.getClosedLoopController().
        setSetpoint(position, ControlType.kPosition);
    }
}



