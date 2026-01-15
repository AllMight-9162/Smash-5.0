package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.RampSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterCommand extends Command{

    private final ShooterSubsystem shooter;
    private final RampSubsystem ramp;

    public Boolean ShooterAtivo = false;

    public ShooterCommand(ShooterSubsystem shooter, RampSubsystem ramp) {
        this.shooter = shooter;
        this.ramp = ramp;
        addRequirements(shooter, ramp);
    
    }

    @Override
    public void initialize() {
        ShooterAtivo = !ShooterAtivo;
        
        if(ShooterAtivo){
            ramp.take();
            shooter.shoot();
        } else {
            ramp.stop();
            shooter.stop();
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
