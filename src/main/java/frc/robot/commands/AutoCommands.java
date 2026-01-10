package frc.robot.commands;

import java.io.File;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.Constants;

public class AutoCommands {
    
private SwerveSubsystem swerve = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve"));
private CommandXboxController controleXbox;

    public AutoCommands(File directory){

        NamedCommands.registerCommand("Align 45", Commands.runOnce(() -> swerve.setDefaultCommand(swerve.driveAlign45(
            () -> MathUtil.applyDeadband(controleXbox.getLeftY(), Constants.Controle.DEADBAND),
            () -> MathUtil.applyDeadband(controleXbox.getLeftX(), Constants.Controle.DEADBAND)))));

        NamedCommands.registerCommand("DriveAlignToReef", Commands.runOnce(() -> swerve.setDefaultCommand(swerve.driveReefAlign(
            () -> MathUtil.applyDeadband(controleXbox.getLeftY(), Constants.Controle.DEADBAND),
            () -> MathUtil.applyDeadband(controleXbox.getLeftX(), Constants.Controle.DEADBAND),
            () -> controleXbox.rightBumper().getAsBoolean()))));
    }
}
