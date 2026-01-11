package frc.robot.lib.subsystem;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.Command;

public class MotorSubsystem extends ImprovedSubsystemBase {

    protected final TalonFX motor;

    protected boolean isStopped = false;
    
    /**
     * Creates a new MotorSubsystem.
     * 
     * @param subsystemName subsystem name for logging purposes
     * @param motorID motor CAN ID for TalonFX
     * @param motorName motor name for logging purposes
     * @param config TalonFXConfiguration for motor
     */
    public MotorSubsystem(String subsystemName, int motorID, String motorName, TalonFXConfiguration config) {
        super(subsystemName);

        this.motor = new TalonFX(motorID);
        motor.getConfigurator().apply(config);
        addMotorLogging(motor, motorName);
    }

    /** Stops the motor */
    public void stopMotor() {
        isStopped = true;
    }

    /** Starts the motor */
    public void startMotor() {
        isStopped = false;
    }

    /**
     * Checks if the motor is stopped 
     * @return true if the motor is stopped
     */
    public boolean isMotorStopped() {
        return isStopped;
    }

    /**
     * Sets the stopped state of the motor
     * @param stopped true to stop the motor, false to start the motor
     */
    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    /** Gets the current position of the motor 
     * @return current position of the motor as an Angle
     */
    public Angle getCurrentPosition() {
        return motor.getPosition().getValue();
    }

    /** Command to stop the motor 
     * @return Command to stop the motor
     */

    public Command stopMotorCommand() {
        return this.runOnce(() -> stopMotor());
    }

    /** Command to start the motor 
     * @return Command to start the motor
     */
    public Command startMotorCommand() {
        return this.runOnce(() -> startMotor());
    }

    /** Sets the motor speed 
     * @param speed speed to set the motor to
     */
    protected void setMotorSpeed(double speed) {
        motor.set(speed);
    }

    @Override
    public void periodic() {
        super.periodic();
    }

}
