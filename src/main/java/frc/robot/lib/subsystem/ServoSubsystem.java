package frc.robot.lib.subsystem;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Rotations;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.configs.TalonFXConfiguration;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.lib.util.PIDConfiguration;

public class ServoSubsystem extends MotorSubsystem {

    protected final ProfiledPIDController pid;
    private Angle targetPosition;
    
    /**
     * Creates a new ServoSubsystem.
     * 
     * @param subsystemName subsystem name for logging purposes
     * @param pidConfig configuration for the Profiled PID controller
     * @param servoID motor CAN ID for TalonFX
     * @param config TalonFXConfiguration for motor
     * @param initialPosition initial position of the servo
     */
    public ServoSubsystem(String subsystemName, PIDConfiguration pidConfig, int servoID, TalonFXConfiguration config, Angle initialPosition) {
        super(subsystemName, servoID, "ServoMotor", config);

        pid = new ProfiledPIDController(pidConfig.kP, pidConfig.kI, pidConfig.kD,
                new Constraints(pidConfig.maxVel, pidConfig.maxAcc));

        targetPosition = initialPosition;
    }

    /**
     * Sets the target position for the servo
     * @param target target position to set
     */
    public void setTargetPosition(Angle target) {
        targetPosition = target;
    }

    /**
     * Gets the target position for the servo
     * @return target position
     */
    public Angle getTargetPosition() {
        return targetPosition;
    }

    /**
     * Checks if the servo is at the target position within a given tolerance
     * @param tolerance tolerance to check within
     * @return true if at target position within tolerance, false otherwise
     */
    public boolean atTargetPosition(Angle tolerance) {
        return Math.abs(getCurrentPosition().in(Rotations) - targetPosition.in(Rotations)) <= tolerance.in(Rotations);
    }

    /**
     * Creates a command to set the target position for the servo
     * @param target target position to set
     * @return command to set target position
     */
    public Command setPositionCommand(Angle target) {
        return this.runOnce(() -> setTargetPosition(target));
    }

    @Override
    public void periodic() {
        super.periodic();

        if (!isStopped) {
            Angle currentPosition = getCurrentPosition();
            double output = pid.calculate(currentPosition.in(Rotations), targetPosition.in(Rotations));
            motor.set(output);
        } else {
            motor.stopMotor();
        }       

        Logger.recordOutput(subsystemName + "/PID/target", targetPosition.in(Degrees));
    }

    
}
