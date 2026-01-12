package frc.robot.lib.util;

import static edu.wpi.first.units.Units.Seconds;

import edu.wpi.first.units.measure.Time;
import edu.wpi.first.wpilibj.DriverStation;

public class GenericState {

    public static final Time TRANSITION_SHIFT_END_TIME = Seconds.of(2*60+10);
    public static final Time SHIFT_ONE_END_TIME = TRANSITION_SHIFT_END_TIME.minus(Seconds.of(25));
    public static final Time SHIFT_TWO_END_TIME = SHIFT_ONE_END_TIME.minus(Seconds.of(25));
    public static final Time SHIFT_THREE_END_TIME = SHIFT_TWO_END_TIME.minus(Seconds.of(25));
    public static final Time SHIFT_FOUR_END_TIME = SHIFT_THREE_END_TIME.minus(Seconds.of(25));
    
    public static boolean isRedAlliance() {
        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent()) {
            return (alliance.get() == DriverStation.Alliance.Red) ? true : false;
        } else {
            return false;
        }
    }

    public static Time getMatchTime() {
        Double time = DriverStation.getMatchTime();
        if (time.isNaN()) {
            return Seconds.of(0.0);
        } else {
            return Seconds.of(time);
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
    

    public static boolean isHubActive() {
        Shift currentShift = getCurrentShift();
        if (currentShift == Shift.AUTO || currentShift == Shift.TRANSITION_SHIFT || currentShift == Shift.ENDGAME) return true;

        String gameData;
        gameData = DriverStation.getGameSpecificMessage();

        if (gameData.length() > 0) {
            switch (gameData.charAt(0)) {
                case 'R': // red hub inactive first, will be active for shift 2 and shift 4
                    if (currentShift == Shift.SHIFT_ONE || currentShift == Shift.SHIFT_THREE) {
                        return !isRedAlliance();
                    } else {
                        return isRedAlliance();
                    }
                case 'B': // blue hub inactive first, will be active for shift 2 and shift 4
                    if (currentShift == Shift.SHIFT_ONE || currentShift == Shift.SHIFT_THREE) {
                        return isRedAlliance();
                    } else {
                        return !isRedAlliance();
                    }
                default:
                    System.out.println("Unexpected game data: " + gameData);
                    return false;
            }
        } else {
            return false;
        }
    }

    public static Shift getCurrentShift() {
        Time time = getMatchTime();
        if (isAutonomous()) {
            return Shift.AUTO;
        } else if (isTeleop()) {
            if (time.in(Seconds) >= TRANSITION_SHIFT_END_TIME.in(Seconds)) {
                return Shift.TRANSITION_SHIFT;
            } else if (time.in(Seconds) >= SHIFT_ONE_END_TIME.in(Seconds)) {
                return Shift.SHIFT_ONE;
            } else if (time.in(Seconds) >= SHIFT_TWO_END_TIME.in(Seconds)) {
                return Shift.SHIFT_TWO;
            } else if (time.in(Seconds) >= SHIFT_THREE_END_TIME.in(Seconds)) {
                return Shift.SHIFT_THREE;
            } else if (time.in(Seconds) >= SHIFT_FOUR_END_TIME.in(Seconds)) {
                return Shift.SHIFT_FOUR;
            } else {
                return Shift.ENDGAME;
            }
        } else {
            return Shift.NOT_DEFINED;
        }
    }

    public static Time getTimeToShiftChange() {
        Time time = getMatchTime();
        if (isAutonomous()) {
            return time;
        } else if (isTeleop()) {
            if (time.in(Seconds) >= TRANSITION_SHIFT_END_TIME.in(Seconds)) {
                return Seconds.of(150.0 - time.in(Seconds));
            } else if (time.in(Seconds) >= SHIFT_ONE_END_TIME.in(Seconds)) {
                return Seconds.of(TRANSITION_SHIFT_END_TIME.in(Seconds) - time.in(Seconds));
            } else if (time.in(Seconds) >= SHIFT_TWO_END_TIME.in(Seconds)) {
                return Seconds.of(SHIFT_ONE_END_TIME.in(Seconds) - time.in(Seconds));
            } else if (time.in(Seconds) >= SHIFT_THREE_END_TIME.in(Seconds)) {
                return Seconds.of(SHIFT_TWO_END_TIME.in(Seconds) - time.in(Seconds));
            } else if (time.in(Seconds) >= SHIFT_FOUR_END_TIME.in(Seconds)) {
                return Seconds.of(SHIFT_THREE_END_TIME.in(Seconds) - time.in(Seconds));
            } else {
                return time;
            }
        } else {
            return Seconds.of(0.0);
        }
    }

    public enum Shift {
        AUTO,
        TRANSITION_SHIFT,
        SHIFT_ONE,
        SHIFT_TWO,
        SHIFT_THREE,
        SHIFT_FOUR,
        ENDGAME,
        NOT_DEFINED
    }
}
