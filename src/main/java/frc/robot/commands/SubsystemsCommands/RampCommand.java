package frc.robot.commands.SubsystemsCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.RampSubsystem;

public class RampCommand extends Command {

    private final RampSubsystem ramp;

    public RampCommand(RampSubsystem ramp) {
        this.ramp = ramp;
        addRequirements();
    }

    @Override
    public void initialize() {
        ramp.set(1.0);
    }

    @Override
    public void end(boolean interrupted) {
        ramp.stop();
    }

    @Override
    public boolean isFinished() {
        return false; 
    }
}