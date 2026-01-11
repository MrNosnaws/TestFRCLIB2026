package frc.robot.lib.subsystem;

import com.ctre.phoenix6.configs.TalonFXConfiguration;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.lib.util.PIDConfiguration;

public class RollerSubsystem extends MotorSubsystem {

    private final ProfiledPIDController pid;
    private final double defaultRunSpeed;

    private double targetSpeed = 0;

    /**
     * Creates a new RollerSubsystem.
     * @param subsystemName subsystem name for logging purposes
     * @param pidConfig configuration for the Profiled PID controller
     * @param servoID motor CAN ID for TalonFX
     * @param config TalonFXConfiguration for motor
     * @param defaultRunSpeed default speed to run the roller at
     */
    public RollerSubsystem(String subsystemName, PIDConfiguration pidConfig, int servoID, TalonFXConfiguration config, double defaultRunSpeed) {
        super(subsystemName, servoID, "RollerMotor", config);

        this.pid = new ProfiledPIDController(pidConfig.kP, pidConfig.kI, pidConfig.kD, 
            new Constraints(pidConfig.maxVel, pidConfig.maxAcc));

        this.defaultRunSpeed = defaultRunSpeed;
    }

    /**
     * Sets the target speed for the roller
     * @param speed target speed to set
     */
    public void setTargetSpeed(double speed) {
        this.targetSpeed = speed;
    }

    /**
     * Gets the target speed for the roller
     * @return target speed
     */
    public double getTargetSpeed() {
        return targetSpeed;
    }

    /**
     * Gets the current speed of the roller
     * @return current speed
     */
    public double getCurrentSpeed() {
        return motor.get();
    }

    /** Runs the roller at the default run speed */
    public void runRoller() {
        setTargetSpeed(defaultRunSpeed);
        isStopped = false;
    }

    /** Runs the roller in reverse at the default run speed */
    public void runRollerReverse() {
        setTargetSpeed(-defaultRunSpeed);
        isStopped = false;
    }

    /** Command to run the roller at the default run speed
     * @return Command to run the roller
     */
    public Command runRollerCommand() {
        return this.runOnce(() -> runRoller());
    }

    /** Command to run the roller in reverse at the default run speed
     * @return Command to run the roller in reverse
     */
    public Command runRollerReverseCommand() {
        return this.runOnce(() -> runRollerReverse());
    }

    @Override
    public void periodic() {
        super.periodic();

        if (!isStopped) {
            double output = pid.calculate(motor.get(), targetSpeed);
            setMotorSpeed(output);
        } else {
            motor.stopMotor();
        }
    }
    
}
