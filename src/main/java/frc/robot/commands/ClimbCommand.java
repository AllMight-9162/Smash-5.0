package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimbSubsystem;

public class ClimbCommand extends Command{

    private final ClimbSubsystem climb;
    public Boolean ClimbAtivo = false;

    public ClimbCommand(ClimbSubsystem climb) {
        this.climb = climb;
        addRequirements(climb);
    
    }

    @Override
    public void initialize() {

        if(ClimbAtivo == false){
            climb.prepareClimb();
            ClimbAtivo = true;
        } else if (ClimbAtivo == true) {
            climb.startClimbing();
        } else {
            climb.resetClimb();
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
