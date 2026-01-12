// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.Rotations;

import org.littletonrobotics.junction.LoggedRobot;

import edu.wpi.first.hal.DriverStationJNI;
import edu.wpi.first.util.WPIUtilJNI;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.internal.DriverStationModeThread;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.lib.components.CRTEncoder;

/**
 * This class is run automatically. If you change the name of this class or the package after
 * creating this project, you must also update the Main.java file in the project.
 */
public class Robot extends LoggedRobot {
  public CRTEncoder turretEstimator;
  public Joystick joystick;

  private double motorSimPos = 0.0;
  private double trueTurretPos = 0.0;

  public Robot() {
    turretEstimator = new CRTEncoder(0, 1, 19, 21, 200);
    
    joystick = new Joystick(0);
  }

  public void disabled() {}

  public void autonomous() {}

  public void teleop() {}

  public void test() {}

  @Override
  public void simulationPeriodic() {
    double speed = joystick.getY();
    motorSimPos += speed * 0.01;
    trueTurretPos = motorSimPos * (20.0 / 200.0);
    turretEstimator.updateSimulationFromMotor(motorSimPos);

    double estimatedRotations = turretEstimator.getNonZeroMeasuredGearAngle().in(Rotations);
    double estimatedDegrees = estimatedRotations * 360.0;
    double trueDegrees = trueTurretPos * 360.0;

    System.out.printf(
      "Motor: %.4f rot | Truth: %.4f deg | CRT Solved: %.4f deg | Error: %.4f%n",
      motorSimPos, trueDegrees, estimatedDegrees, Math.abs(trueDegrees - estimatedDegrees)
    );
  }
}
