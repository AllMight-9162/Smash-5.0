package frc.robot.subsystems;

// Dependências necessárias para o funcionamento do subsistema
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkBase.ControlType;

import frc.Java_Is_AllMight.Control.PIDConfig;
import frc.Java_Is_AllMight.Motors.SparkConfigurator;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    private static final PIDConfig ANGULATE_PID  = new PIDConfig(0.8, 0.0003, 0.0001, 0.0, 0.3);

    // Limite de corrente dos motores
    private static final int CURRENT_LIMIT = 40;

    // Limite de potencia dos motores
    private static final double INTAKE_OUT_MIN = -1.0;
    private static final double INTAKE_OUT_MAX = 1.0;
    private static final double ANGULATE_OUT_MIN = -1;
    private static final double ANGULATE_OUT_MAX = 0.60;

    // Redução dos motores
    private static final double INTAKE_GEAR_RATIO = 15;
    private static final double ANGULATE_GEAR_RATIO = 27;

    // Variavel de controle do estado do intake
    private boolean active = false;

    // Construtor do subsistema do intake
    public IntakeSubsystem() {

        leftIntakeMotor = SparkConfigurator.createSparkMax(
            LEFT_INTAKE_MOTOR_ID,
            MotorType.kBrushless,
            null,
            IdleMode.kCoast,
            CURRENT_LIMIT,
            INTAKE_OUT_MIN,INTAKE_OUT_MAX,
            INTAKE_GEAR_RATIO,
            true
        );

        rightIntakeMotor = SparkConfigurator.createSparkMax(
            RIGHT_INTAKE_MOTOR_ID,
            MotorType.kBrushless,
            null,
            IdleMode.kCoast,
            CURRENT_LIMIT,
            INTAKE_OUT_MIN,INTAKE_OUT_MAX,
            INTAKE_GEAR_RATIO,
            true
        );

        leftAngulateMotor = SparkConfigurator.createSparkMax(
            LEFT_ANGULATE_MOTOR_ID,
            MotorType.kBrushless,
            ANGULATE_PID ,
            IdleMode.kBrake,
            CURRENT_LIMIT,
            ANGULATE_OUT_MIN, ANGULATE_OUT_MAX,
            ANGULATE_GEAR_RATIO,
            true
        );

        rightAngulateMotor = SparkConfigurator.createSparkMaxFollower(
            RIGHT_ANGULATE_MOTOR_ID,
            MotorType.kBrushless,
            IdleMode.kBrake,
            CURRENT_LIMIT,
            LEFT_ANGULATE_MOTOR_ID, false,
            ANGULATE_GEAR_RATIO
        );
    }

    @Override
    public void periodic(){
        double encoder = leftAngulateMotor.getEncoder().getPosition();
        SmartDashboard.putNumber("Intake position", encoder);

        SmartDashboard.putBoolean("intake ativo", active);
    }

    public void toggleIntake(){
        active = !active;

        if(active){
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

    public void angleBack(){
        angulate(0.3205);
    }
    
    public void Init(){
        leftAngulateMotor.getEncoder().setPosition(0.0);
        stop();
    }

    // Posiciona o intake e inicia a coleta
    public void take() {
        angulate(0.33);
        leftIntakeMotor.set(1.0);
        rightIntakeMotor.set(0.4);
    }

    // Para o motor de intake
    public void stop() {
        leftAngulateMotor.set(0.0);
        rightIntakeMotor.set(0.0);
        leftIntakeMotor.set(0.0);
    }

    // Define a posição alvo em graus do motor de angulação usando controle em malha fechada (PID)
    private void angulate(double set) {
        leftAngulateMotor.getClosedLoopController()
        .setSetpoint(set, ControlType.kPosition);
        
    }
}

