package frc.robot.commands.SubsystemsCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterInHub extends Command{

    private final ShooterSubsystem shooter;

    public Boolean ShooterAtivo = false;

    public ShooterInHub(ShooterSubsystem shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    
    }

    @Override
    public void execute() {
        shooter.shootInHub();
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
