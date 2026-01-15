package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeCommand extends Command{

    private final IntakeSubsystem intake;
    public Boolean intakeativo = false;

    public IntakeCommand(IntakeSubsystem intake) {
        this.intake = intake;
        addRequirements(intake);
    
    }

    @Override
    public void initialize() {
        intakeativo = !intakeativo;

        if(intakeativo){
            intake.take();
        } else {
            intake.stop();
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}

