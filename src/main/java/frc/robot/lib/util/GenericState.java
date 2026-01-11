package frc.robot.lib.util;

import edu.wpi.first.wpilibj.DriverStation;

public class GenericState {
    
    public static boolean isRedAlliance() {
        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent()) {
            return (alliance.get() == DriverStation.Alliance.Red) ? true : false;
        } else {
            return false;
        }
    }

    public static double getMatchTime() {
        Double time = DriverStation.getMatchTime();
        if (time.isNaN()) {
            return 0.0;
        } else {
            return time;
        }
    }

    public static boolean isTeleop() {
        return DriverStation.isTeleop();
    }

    public static boolean isAutonomous() {
        return DriverStation.isAutonomous();
    }

    public static boolean isTest() {
        return DriverStation.isTest();
    }

    public static boolean isDisabled() {
        return DriverStation.isDisabled();
    }

    public static boolean isEnabled() {
        return DriverStation.isEnabled();
    }
    
}
