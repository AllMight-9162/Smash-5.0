package frc.robot.subsystems;

import com.revrobotics.spark.SparkFlex;
import frc.Java_Is_AllMight.Motors.SparkConfigurator;
import frc.Java_Is_AllMight.Control.PIDConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class ClimbSubsystem {

    // Preparação do climb: deixa lentinho e sobe os ngc
    public enum ClimbState {
        IDLE,
        PREPARE,
        CLIMBING
    }

    private static final int MOTOR1_CLIMB_ID = 16;
    private static final int MOTOR2_CLIMB_ID = 17;
    private static final PIDConfig MOTOR_CLIMB_PID = new PIDConfig(0,0,0);
    private static final int MOTOR_CLIMB_CURRENT_LIMIT = 40;

    private ClimbState state = ClimbState.IDLE;

    private final SparkFlex climbMotor;
    private final SparkFlex climbMotor2;
    private final SwerveSubsystem swerve;

    public ClimbSubsystem(SwerveSubsystem swerveSubsystem){
        this.swerve = swerveSubsystem;

        climbMotor = SparkConfigurator.createSparkFlex(
            MOTOR1_CLIMB_ID, 
            MotorType.kBrushless, 
            MOTOR_CLIMB_PID, 
            null,
            MOTOR_CLIMB_CURRENT_LIMIT
        );

        climbMotor2 = SparkConfigurator.createSparkFlex(
            MOTOR2_CLIMB_ID, 
            MotorType.kBrushless, 
            MOTOR_CLIMB_PID, 
            null,
            MOTOR_CLIMB_CURRENT_LIMIT
        );

    }

    public void toggleClimb(){
        switch(state){
            case IDLE -> prepareClimb();
            case PREPARE -> startClimbing();
            case CLIMBING -> {}
        }
    }

    private void prepareClimb(){
        state = ClimbState.PREPARE;
        swerve.setSpeedMultiplier(0.2);
        moveArmToClimb();
    }

    private void startClimbing(){
        state = ClimbState.CLIMBING;
        climbMotor.set(1.0);
    }

    public void resetClimb(){
        state = ClimbState.IDLE;
        swerve.setSpeedMultiplier(1.0);
        climbMotor.set(0);
    }

    private void moveArmToClimb(){
        climbMotor.set(0.4);
        climbMotor2.set(0.4);
    }

    public ClimbState gState(){
        return state;
    }

    
}


