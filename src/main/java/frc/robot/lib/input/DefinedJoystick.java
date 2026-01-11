package frc.robot.lib.input;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.lib.helpers.MathHelpers;

public class DefinedJoystick extends Joystick {

    public DefinedJoystick(int port) {
        super(port);
    }

    public double getAxisWithDeadband(int axis, double deadband) {
        double value = getRawAxis(axis);
        return MathHelpers.deadband(value, deadband);
    }

    public double getYAxisWithDeadband(double deadband) {
        double value = getY();
        return MathHelpers.deadband(value, deadband);
    }

    public double getXAxisWithDeadband(double deadband) {
        double value = getX();
        return MathHelpers.deadband(value, deadband);
    }

    public double getZAxisWithDeadband(double deadband) {
        double value = getZ();
        return MathHelpers.deadband(value, deadband);
    }

    public double getTwistWithDeadband(double deadband) {
        double value = getTwist();
        return MathHelpers.deadband(value, deadband);
    }

    public double getThrottleWithDeadband(double deadband) {
        double value = getThrottle();
        return MathHelpers.deadband(value, deadband);
    }

    public JoystickButton getButton(int buttonNumber) {
        return new JoystickButton(this, buttonNumber);
    }

    public JoystickButton getTriggerButton() {
        return new JoystickButton(this, JoystickConstants.TRIGGER_BUTTON);
    }

    public JoystickButton getTopLeftButton() {
        return new JoystickButton(this, JoystickConstants.TOP_LEFT_BUTTON);
    }

    public JoystickButton getTopRightButton() {
        return new JoystickButton(this, JoystickConstants.TOP_RIGHT_BUTTON);
    }

    public JoystickButton getTopMiddleButton() {
        return new JoystickButton(this, JoystickConstants.TOP_MIDDLE_BUTTON);
    }

    public JoystickButton getLeftNumPadTopLeftButton() {
        return new JoystickButton(this, JoystickConstants.LEFT_NUM_PAD_TOP_LEFT_BUTTON);
    }

    public JoystickButton getLeftNumPadTopMiddleButton() {
        return new JoystickButton(this, JoystickConstants.LEFT_NUM_PAD_TOP_MIDDLE_BUTTON);
    }

    public JoystickButton getLeftNumPadTopRightButton() {
        return new JoystickButton(this, JoystickConstants.LEFT_NUM_PAD_TOP_RIGHT_BUTTON);
    }

    public JoystickButton getLeftNumPadBottomLeftButton() {
        return new JoystickButton(this, JoystickConstants.LEFT_NUM_PAD_BOTTOM_LEFT_BUTTON);
    }

    public JoystickButton getLeftNumPadBottomMiddleButton() {
        return new JoystickButton(this, JoystickConstants.LEFT_NUM_PAD_BOTTOM_MIDDLE_BUTTON);
    }

    public JoystickButton getLeftNumPadBottomRightButton() {
        return new JoystickButton(this, JoystickConstants.LEFT_NUM_PAD_BOTTOM_RIGHT_BUTTON);
    }

    public JoystickButton getRightNumPadTopLeftButton() {
        return new JoystickButton(this, JoystickConstants.RIGHT_NUM_PAD_TOP_LEFT_BUTTON);
    }

    public JoystickButton getRightNumPadTopMiddleButton() {
        return new JoystickButton(this, JoystickConstants.RIGHT_NUM_PAD_TOP_MIDDLE_BUTTON);
    }

    public JoystickButton getRightNumPadTopRightButton() {
        return new JoystickButton(this, JoystickConstants.RIGHT_NUM_PAD_TOP_RIGHT_BUTTON);
    }

    public JoystickButton getRightNumPadBottomLeftButton() {
        return new JoystickButton(this, JoystickConstants.RIGHT_NUM_PAD_BOTTOM_LEFT_BUTTON);
    }

    public JoystickButton getRightNumPadBottomMiddleButton() {
        return new JoystickButton(this, JoystickConstants.RIGHT_NUM_PAD_BOTTOM_MIDDLE_BUTTON);
    }

    public JoystickButton getRightNumPadBottomRightButton() {
        return new JoystickButton(this, JoystickConstants.RIGHT_NUM_PAD_BOTTOM_RIGHT_BUTTON);
    }

    public JoystickButton getPOVUpButton() {
        return this.getPOVUpButton();
    }

    public JoystickButton getPOVDownButton() {
        return this.getPOVDownButton();
    }

    public JoystickButton getPOVLeftButton() {
        return this.getPOVLeftButton();
    }

    public JoystickButton getPOVRightButton() {
        return this.getPOVRightButton();
    }

    public JoystickButton getPOVUpRightButton() {
        return this.getPOVUpRightButton();
    }

    public JoystickButton getPOVUpLeftButton() {
        return this.getPOVUpLeftButton();
    }

    public JoystickButton getPOVDownRightButton() {
        return this.getPOVDownRightButton();
    }

    public JoystickButton getPOVDownLeftButton() {
        return this.getPOVDownLeftButton();
    }

}
