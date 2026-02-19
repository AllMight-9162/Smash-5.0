package frc.robot;

import frc.robot.Constants.Controle;
import frc.robot.commands.AutoCommands;
import frc.robot.commands.SubsystemsCommands.*;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveSubsystem;

import java.io.File;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
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

  private final AutoCommands autoCommands = new AutoCommands(swerve, shooter, intake, climb);

  private CommandXboxController controleXbox = new CommandXboxController(Controle.xboxControle);
 
  public RobotContainer() {
    swerve.setDefaultCommand(swerve.driveCommandAlinharComJoystick(
      () -> MathUtil.applyDeadband(controleXbox.getLeftY(), Constants.Controle.DEADBAND),
      () -> MathUtil.applyDeadband(controleXbox.getLeftX(), Constants.Controle.DEADBAND),
      () -> controleXbox.getRightX(),
      () -> controleXbox.getRightY(),
      () -> controleXbox.rightBumper().getAsBoolean(),
      () -> controleXbox.leftBumper().getAsBoolean()));

      new AutoCommands(swerve, shooter, intake, climb);

    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);

    configureBindings();
  }
  
  private void configureBindings() {
    if(!Robot.isReal()){
      controleXbox.start().onTrue(Commands.runOnce(() -> swerve.resetOdometry(new Pose2d(3, 3, new Rotation2d()))));
    }

    controleXbox.a().whileTrue(swerve.driveAlign0(
    () -> MathUtil.applyDeadband(-controleXbox.getLeftY(), Constants.Controle.DEADBAND),
    () -> MathUtil.applyDeadband(-controleXbox.getLeftX(), Constants.Controle.DEADBAND)));

    controleXbox.b().whileTrue(swerve.driveAlign180(
    () -> MathUtil.applyDeadband(-controleXbox.getLeftY(), Constants.Controle.DEADBAND),
    () -> MathUtil.applyDeadband(-controleXbox.getLeftX(), Constants.Controle.DEADBAND)));

    new JoystickButton(pilotoSub, XboxController.Button.kA.value)
    .whileTrue(new IntakeCommand(intake));

    new JoystickButton(pilotoSub, XboxController.Button.kLeftBumper.value)
    .whileTrue(new RetractCommand(intake));

    new JoystickButton(pilotoSub, XboxController.Button.kX.value)
    .whileTrue(new ShooterInField(shooter));

    new JoystickButton(pilotoSub, XboxController.Button.kY.value)
    .whileTrue(new ShooterInHub(shooter));

   new JoystickButton(pilotoSub, XboxController.Button.kY.value)
    .whileTrue(swerve.driveAlignToHub(
    () -> MathUtil.applyDeadband(controleXbox.getLeftY(), Constants.Controle.DEADBAND),
    () -> MathUtil.applyDeadband(controleXbox.getLeftX(), Constants.Controle.DEADBAND)));

    //new JoystickButton(pilotoSub, XboxController.Button.kRightBumper.value)
    //.whileTrue(new ClimbCommand(climb));
  }

  public void init() {
    
  }

  public void periodic() {
    double matchtime = DriverStation.getMatchTime();
    SmartDashboard.putString("Match Time", String.format("%.0f", matchtime));
  }

  public void autoInit() {
    intake.Init();
    climb.Init();
  }

  public void teleOpinit() {
    //intake.Init();
    //climb.Init();
  }

  public void teleOP() {
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

}
