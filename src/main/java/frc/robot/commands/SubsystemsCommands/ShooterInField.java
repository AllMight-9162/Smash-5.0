package frc.robot.commands.SubsystemsCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterInField extends Command{

    private final ShooterSubsystem shooter;

    public Boolean ShooterAtivo = false;

    public ShooterInField(ShooterSubsystem shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        shooter.shootInField();
    }

    @Override
    public void end(boolean interrupted){
        shooter.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}