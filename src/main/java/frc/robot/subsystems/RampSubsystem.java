package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RampSubsystem extends SubsystemBase {
    private final Spark leftMotor;
    private final Spark rightMotor;

    private boolean active;

    public RampSubsystem(){
        leftMotor = new Spark(0);
        rightMotor = new Spark(1);
    }

    public void set(double set){
        //leftMotor.set(-set);
        rightMotor.set(set);
    }

    public void stop(){
        //leftMotor.set(0);
        rightMotor.set(0);
    }
}
