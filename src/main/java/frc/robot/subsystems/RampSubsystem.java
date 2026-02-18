package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class RampSubsystem {
    private final Spark leftMotor;
    private final Spark rightMotor;

    public RampSubsystem(){
        leftMotor = new Spark(0);
        rightMotor = new Spark(1);
    }

    public void set(double set){
        leftMotor.set(-set);
        rightMotor.set(set);
    }

    public void stop(){
        leftMotor.set(0);
        rightMotor.set(0);
    }
}
