package frc.Java_Is_AllMight.Motors;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.Java_Is_AllMight.Control.PIDConfig;

public class SparkConfigurator {
  public static SparkMax createSparkMax(int id, MotorType motorType, PIDConfig pidConfig, SparkBaseConfig.IdleMode idleMode, int currentLimit){
    SparkMax motor = new SparkMax(id, motorType);
    SparkMaxConfig config = new SparkMaxConfig();
    applyCommonConfig(config, pidConfig, idleMode, currentLimit);
    return motor;
  }

  public static SparkFlex createSparkFlex(int id, MotorType motorType, PIDConfig pidConfig, SparkBaseConfig.IdleMode idleMode, int currentLimit){
    SparkFlex motor = new SparkFlex(id, motorType);
    SparkFlexConfig config = new SparkFlexConfig();
    applyCommonConfig(config, pidConfig, idleMode, currentLimit);
    return motor;
  }

  private static void applyCommonConfig(SparkBaseConfig config, PIDConfig pidConfig, SparkBaseConfig.IdleMode idleMode, int currentLimit){
    config.encoder.positionConversionFactor(1);
    config.encoder.velocityConversionFactor(1);
    if(pidConfig != null){
      config.closedLoop.pidf(pidConfig.kP, pidConfig.kI, pidConfig.kD, pidConfig.kF);
      config.closedLoop.iZone(pidConfig.iZone);
      config.closedLoop.outputRange(-1.0, 1.0);    
    }
    config.smartCurrentLimit(currentLimit);
    config.idleMode(idleMode);
  }
}
