package frc.robot.commands;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Commands;

import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class AutoCommands {

    private final SwerveSubsystem swerve;
    private final ShooterSubsystem shooter;
    private final IntakeSubsystem intake;

    public AutoCommands(SwerveSubsystem swerveSubsystem, ShooterSubsystem shooter, IntakeSubsystem intake){
        this.swerve = swerveSubsystem;
        this.shooter = shooter;
        this.intake = intake;
    }
}
