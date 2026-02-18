package frc.robot.commands;

import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj2.command.Commands;

import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class AutoCommands {

    private final SwerveSubsystem swerve;
    private final ShooterSubsystem shooter;
    private final IntakeSubsystem intake;
    private final ClimbSubsystem climb;

    public AutoCommands(SwerveSubsystem swerveSubsystem, ShooterSubsystem shooter, IntakeSubsystem intake, ClimbSubsystem climb){
        this.swerve = swerveSubsystem;
        this.shooter = shooter;
        this.intake = intake;
        this.climb = climb;

        NamedCommands.registerCommand(
            "shoot", Commands.runEnd(
            () -> shooter.shootInHub(),
            () -> shooter.stop()).
            withTimeout(10.0));

        NamedCommands.registerCommand("intake", Commands.startEnd(
            () -> intake.take(),
            () -> intake.stop()).
            withTimeout(0.5)); 

        NamedCommands.registerCommand("climbUp", Commands.runOnce(
            () -> climb.climbUp()));

        NamedCommands.registerCommand("climbDown", Commands.runOnce(
            () -> climb.climbDown()));
       
    }
}
