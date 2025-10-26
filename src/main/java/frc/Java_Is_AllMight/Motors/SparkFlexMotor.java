package frc.Java_Is_AllMight.Motors;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.Java_Is_AllMight.Control.PIDConfig;

public class SparkFlexMotor {

    public static SparkFlex create(int id, MotorType motorType, PIDConfig pidConfig, IdleMode idleMode, int currentLimit) {

        SparkFlex motor = new SparkFlex(id, motorType);
        SparkFlexConfig config = new SparkFlexConfig();

        config.encoder.positionConversionFactor(1);
        config.encoder.velocityConversionFactor(1);

        if (pidConfig != null) {
            config.closedLoop.pidf(pidConfig.kP, pidConfig.kI, pidConfig.kD, pidConfig.kF);
            config.closedLoop.iZone(pidConfig.iZone);
            config.closedLoop.outputRange(-1.0, 1.0);
        }

        config.smartCurrentLimit(currentLimit);
        config.idleMode(idleMode);

        motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

        return motor;
    }

}