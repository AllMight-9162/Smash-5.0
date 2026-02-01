package frc.Java_Is_AllMight.Motors;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.Java_Is_AllMight.Control.PIDConfig;

public class SparkConfigurator {

  // "API" publica
  public static SparkMax createSparkMax(
    int id, 
    MotorType motorType, 
    PIDConfig pidConfig, 
    SparkBaseConfig.IdleMode idleMode, 
    int currentLimit,
    double outMin,
    double outMax,
    double gearRatio,
    boolean isInverted
    
    ) {
    SparkMax motor = new SparkMax(id, motorType);
    SparkMaxConfig config = new SparkMaxConfig();
    
    applyCommonConfig(config, pidConfig, idleMode, currentLimit, outMin, outMax, gearRatio, isInverted);
    motor.configure(
      config, 
      ResetMode.kResetSafeParameters, 
      PersistMode.kPersistParameters
      );
  
    return motor;
  }

  public static SparkMax createSparkMaxFollower(
    int id, 
    MotorType motorType,
    SparkBaseConfig.IdleMode idleMode,
    int currentLimit, 
    int idFollow,
    boolean isInverted,
    double gearRatio
  ) {
    SparkMax motor = new SparkMax(id, motorType);
    SparkMaxConfig config = new SparkMaxConfig();

    applyFollowerConfig(config, idleMode, currentLimit, idFollow, isInverted, gearRatio);

    motor.configure(
      config,
      ResetMode.kResetSafeParameters, 
      PersistMode.kPersistParameters
    );
    return motor;
  };

  public static void configureRampSparkMax(SparkMax motor, double rampRateSeconds) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.closedLoopRampRate(rampRateSeconds);
     config.openLoopRampRate(rampRateSeconds);
    motor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
  }

  public static SparkFlex createSparkFlex(
    int id, 
    MotorType motorType, 
    PIDConfig pidConfig, 
    SparkBaseConfig.IdleMode idleMode, 
    int currentLimit,
    double outMin,
    double outMax,
    double gearRatio,
    boolean isInverted
    ) {
    SparkFlex motor = new SparkFlex(id, motorType);
    SparkFlexConfig config = new SparkFlexConfig();
  
    applyCommonConfig(config, pidConfig, idleMode, currentLimit, outMin, outMax, gearRatio, isInverted);
    
    motor.configure(
      config,
      ResetMode.kNoResetSafeParameters,
      PersistMode.kPersistParameters 
    );

    return motor;
  }

  public static SparkFlex createSparkFlexFollower(
    int id, 
    MotorType motorType,
    SparkBaseConfig.IdleMode idleMode,
    int currentLimit, 
    int idFollow,
    boolean isInverted,
    double gearRatio
  ) {
    SparkFlex motor = new SparkFlex(id, motorType);
    SparkFlexConfig config = new SparkFlexConfig();
    
    applyFollowerConfig(config, idleMode, currentLimit, idFollow, isInverted, gearRatio);

    motor.configure(
      config,
      ResetMode.kResetSafeParameters, 
      PersistMode.kPersistParameters
    );
    return motor;
  };

  public static void configureRampSparkFlex(SparkFlex motor, double rampRateSeconds) {
    SparkFlexConfig config = new SparkFlexConfig();
    config.closedLoopRampRate(rampRateSeconds);
    config.openLoopRampRate(rampRateSeconds);
    motor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
  }

  //Implementação interna 
  private static void applyCommonConfig(
    SparkBaseConfig config, 
    PIDConfig pidConfig, 
    SparkBaseConfig.IdleMode idleMode, 
    int currentLimit,
    double outMin,
    double outMax,
    double gearRatio,
   boolean isInverted
  ) {
    
    if(pidConfig != null){
      config.closedLoop.pidf(
        pidConfig.kP, 
        pidConfig.kI, 
        pidConfig.kD, 
        pidConfig.kF
      );
      config.closedLoop.iZone(pidConfig.iZone);
      config.closedLoop.outputRange(outMin, outMax);   
    }

    config.idleMode(idleMode);
    config.smartCurrentLimit(currentLimit);

    config.inverted(isInverted);

    config.encoder.positionConversionFactor(1.0 / gearRatio);
    config.encoder.velocityConversionFactor(1.0 / gearRatio);
  }

  private static void applyFollowerConfig(
    SparkBaseConfig config,
    SparkBaseConfig.IdleMode idleMode,
    int currentLimit, 
    int idFollow,
    boolean isInverted,
    double gearRatio
  ) {

    applyCommonConfig(config, null, idleMode, currentLimit, 0, 0, gearRatio, false);
    config.follow(idFollow, isInverted);
  }

}
