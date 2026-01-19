package frc.robot.commands.SubsystemsCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterCommand extends Command{

    private final ShooterSubsystem shooter;

    public Boolean ShooterAtivo = false;

    public ShooterCommand(ShooterSubsystem shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    
    }

    @Override
    public void initialize() {
        shooter.toggleShoot();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
