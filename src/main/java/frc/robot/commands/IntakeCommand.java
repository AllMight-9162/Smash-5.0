package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.RampSubsystem;

public class IntakeCommand extends Command{

    private final IntakeSubsystem intake;
    private final RampSubsystem ramp;

    public Boolean intakeativo = false;

    public IntakeCommand(IntakeSubsystem intake, RampSubsystem ramp) {
        this.intake = intake;
        this.ramp = ramp;
        addRequirements(intake, ramp);
    
    }

    @Override
    public void initialize() {
        intakeativo = !intakeativo;

        if(intakeativo){
            intake.take();
            ramp.take();
        } else {
            intake.stop();
            ramp.stop();
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}

