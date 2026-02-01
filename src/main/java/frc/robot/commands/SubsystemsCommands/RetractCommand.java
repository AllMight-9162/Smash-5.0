package frc.robot.commands.SubsystemsCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

public class RetractCommand extends Command {

    private final IntakeSubsystem intake;

    public RetractCommand(IntakeSubsystem intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.retract();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
