package frc.robot.commands.SubsystemsCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveSubsystem;

public class ShooterCommand extends Command {

    private final SwerveSubsystem swerve;
    private final ShooterSubsystem shooter;

    public ShooterCommand(ShooterSubsystem shooter, SwerveSubsystem swerve) {
        this.shooter = shooter;
        this.swerve = swerve;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        shooter.updateTargetRPMFromPose();

        if (shooter.atSpeed()) {
            shooter.take();
        } else {
            shooter.stopTake();
        }
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stopShooter();
    }

    @Override
    public boolean isFinished() {
        return false; 
    }
}
