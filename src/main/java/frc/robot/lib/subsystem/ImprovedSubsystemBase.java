// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.lib.subsystem;

import java.util.HashMap;
import java.util.Map;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ImprovedSubsystemBase extends SubsystemBase {

  /** The name of the subsystem for logging purposes */
  protected final String subsystemName;

  /** Map of TalonFX motors to be logged */
  private final Map<String, TalonFX> talonFXMotors = new HashMap<>();

  /** Creates a new ImprovedSubsystemBase. 
   * 
   * @param subsystemName The name of the subsystem for logging purposes
   */
  public ImprovedSubsystemBase(String subsystemName) {
    this.subsystemName = subsystemName;

  }

  /** Adds multiple TalonFX motors to be logged
   * 
   * @param motor The TalonFX motors to log
   * @param names The names to log the motors as
   */
  public void addMotorLogging(TalonFX[] motor, String[] names) {
    for (int i = 0; i < motor.length && i < names.length; i++) {
      addMotorLogging(motor[i], names[i]);
    }
  }

  /** Adds a TalonFX motor to be logged
   * 
   * @param motor The TalonFX motor to log
   * @param name  The name to log the motor as
   */
  public void addMotorLogging(TalonFX motor, String name) {
    talonFXMotors.put(name, motor);
  }

  @Override
  public void periodic() {
    logInfo();
  }

  /** Logs motor information through Logger */
  private void logInfo() {
    for (String key : talonFXMotors.keySet()) {
      TalonFX motor = talonFXMotors.get(key);
      Logger.recordOutput(subsystemName + "/" + key + "/Position", motor.getPosition().getValue());
      Logger.recordOutput(subsystemName + "/" + key + "/Velocity", motor.getVelocity().getValue());
      Logger.recordOutput(subsystemName + "/" + key + "/Voltage", motor.getMotorVoltage().getValue());
      Logger.recordOutput(subsystemName + "/" + key + "/Current", motor.getStatorCurrent().getValue());
      Logger.recordOutput(subsystemName + "/" + key + "/Is Connected", motor.isConnected());
      Logger.recordOutput(subsystemName + "/" + key + "/Is Alive", motor.isAlive());
      Logger.recordOutput(subsystemName + "/" + key + "/Speed", motor.get());
      Logger.recordOutput(subsystemName + "/" + key + "/ID", motor.getDeviceID());
    }
  }
}
