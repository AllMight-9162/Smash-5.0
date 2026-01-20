package frc.robot;

import frc.robot.Constants.Controle;
import frc.robot.commands.SubsystemsCommands.ClimbCommand;
import frc.robot.commands.SubsystemsCommands.IntakeCommand;
import frc.robot.commands.SubsystemsCommands.ShooterCommand;
import edu.wpi.first.wpilibj.XboxController;

import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.ClimbSubsystem;

import java.io.File;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class RobotContainer {
  private SwerveSubsystem swerve = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve"));
  private final SendableChooser<Command> autoChooser;

  private final XboxController pilotoSub = new XboxController(1);

  private final IntakeSubsystem intake = new IntakeSubsystem();
  private final ShooterSubsystem shooter = new ShooterSubsystem(swerve);
  private final ClimbSubsystem climb = new ClimbSubsystem();
 

  private CommandXboxController controleXbox = new CommandXboxController(Controle.xboxControle);
 
  public RobotContainer() {
    swerve.setDefaultCommand(swerve.driveCommandAlinharComJoystick(
      () -> MathUtil.applyDeadband(controleXbox.getLeftY(), Constants.Controle.DEADBAND),
      () -> MathUtil.applyDeadband(controleXbox.getLeftX(), Constants.Controle.DEADBAND),
      () -> controleXbox.getRightX(),
      () -> controleXbox.getRightY(),
      () -> controleXbox.rightBumper().getAsBoolean()));

    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);

    configureBindings();
  }
  
  private void configureBindings() {
    if(!Robot.isReal()){
      controleXbox.start().onTrue(Commands.runOnce(() -> swerve.resetOdometry(new Pose2d(3, 3, new Rotation2d()))));
    }

    controleXbox.a().whileTrue(swerve.driveAlign45(
    () -> MathUtil.applyDeadband(controleXbox.getLeftY(), Constants.Controle.DEADBAND),
    () -> MathUtil.applyDeadband(controleXbox.getLeftX(), Constants.Controle.DEADBAND)));

    controleXbox.b().whileTrue(swerve.driveAlignToGoal(
    () -> MathUtil.applyDeadband(controleXbox.getLeftY(), Constants.Controle.DEADBAND),
    () -> MathUtil.applyDeadband(controleXbox.getLeftX(), Constants.Controle.DEADBAND),
    () -> controleXbox.rightBumper().getAsBoolean()));

    new JoystickButton(pilotoSub, XboxController.Button.kA.value)
        .onTrue(new IntakeCommand(intake));

    new JoystickButton(pilotoSub, XboxController.Button.kB.value)
        .whileTrue(new ShooterCommand(shooter, swerve));

    new JoystickButton(pilotoSub, XboxController.Button.kY.value)
        .onTrue(new ClimbCommand(climb));
  }

  public void init() {
    
  }

  public void periodic() {

  }

  public void autoInit() {
    setMotorBrake(true);
  }

  public void teleOpinit() {
    
  }

  public void teleOP() {
    
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

  public void setMotorBrake(boolean brake) {
    swerve.setMotorBrake(brake);
  }
}
