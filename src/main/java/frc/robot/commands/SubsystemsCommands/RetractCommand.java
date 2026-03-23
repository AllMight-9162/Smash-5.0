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
        intake.retract(); // começa a retrair
    }

    @Override
    public void end(boolean interrupted) {
        intake.angleBack(); // volta o intake quando o comando acabar
    }

    @Override
    public boolean isFinished() {
        return false; // 🔥 mantém o comando vivo enquanto o botão estiver true
    }
}