package frc.robot.commands.SubsystemsCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimbSubsystem;

public class ClimbCommand extends Command{

    private final ClimbSubsystem climb;

    public ClimbCommand(ClimbSubsystem climb) {
        this.climb = climb;
        addRequirements(climb);
    
    }

    @Override
    public void initialize() {
        climb.toggleClimb();
       
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
