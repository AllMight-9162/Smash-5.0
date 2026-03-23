package frc.robot.commands;

import com.fasterxml.jackson.databind.util.Named;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj2.command.Commands;

import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.commands.SubsystemsCommands.RampCommand;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.RampSubsystem;

public class AutoCommands {

    private final SwerveSubsystem swerve;
    private final ShooterSubsystem shooter;
    private final IntakeSubsystem intake;
    private final ClimbSubsystem climb;
    private final RampSubsystem ramp;

    public AutoCommands(SwerveSubsystem swerveSubsystem, ShooterSubsystem shooter, IntakeSubsystem intake, ClimbSubsystem climb, RampSubsystem ramp){
        this.swerve = swerveSubsystem;
        this.shooter = shooter;
        this.intake = intake;
        this.climb = climb;
        this.ramp = ramp;
        
        NamedCommands.registerCommand(
            "shoot", Commands.runEnd(
            () -> shooter.shootInHub(),
            () -> shooter.stop()).
            withTimeout(7.0));

        NamedCommands.registerCommand
        ("ramp", Commands.runEnd(
            () -> ramp.set(0.70),
            () -> ramp.stop()).
            withTimeout(7.0));

        NamedCommands.registerCommand(
            "intake", Commands.startEnd(
            () -> intake.take(),
            () -> intake.stop()).
            withTimeout(1.5)); 
        NamedCommands.registerCommand(
            "intakeRetract", Commands.runOnce(
            () -> intake.retract()));

        NamedCommands.registerCommand(
            "climbUp", Commands.runOnce(
            () -> climb.climbUp()));

        NamedCommands.registerCommand(
            "climbDown", Commands.runOnce(
            () -> climb.climbDown()));
    }
}
