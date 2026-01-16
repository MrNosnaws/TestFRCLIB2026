package frc.robot.lib.helpers;

import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.Seconds;

import java.util.function.Function;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.Time;
import frc.robot.lib.geometry.FieldLocation;
import frc.robot.lib.geometry.Vector2d;

public class ProjectileHelper {
    
    /**
     * Calculates the offset vector that the robot should aim at to hit a target given the distance to the target, the robot's rotation, the time it takes for the projectile to reach the target, and the field relative velocity of the target.
     * @param distanceToTarget The distance to the target
     * @param robotTargetRotation The rotation of the robot relative to the target
     * @param shotTimeFunction A function that takes a distance and returns the time it takes for the projectile to reach that distance
     * @param fieldRelitiveVelocityMetersPerSecond The velocity of the target relative to the field in meters per second
     * @return The offset vector that the robot should aim at
     */
    public static Vector2d getShotOffsetVector(Distance distanceToTarget, Rotation2d robotTargetRotation, Function<Distance, Time> shotTimeFunction, Vector2d fieldRelitiveVelocityMetersPerSecond) {
        // assume robot is at (0, 0) and target is at (distanceToTarget, 0)
        Time shotTime = shotTimeFunction.apply(distanceToTarget);

        // translate field relitive velocity to robot target relitive velocity
        Vector2d robotTargetRelitiveVelocity = fieldRelitiveVelocityMetersPerSecond.rotated(robotTargetRotation.unaryMinus());
        double towardsTargetVelocity = robotTargetRelitiveVelocity.getX();
        Time intialGuess = shotTime.minus(Seconds.of(distanceToTarget.in(Meters) / towardsTargetVelocity));

        FieldLocation intialLocation = new FieldLocation(0, 0, Meters);
        FieldLocation targetLocation = new FieldLocation(distanceToTarget.in(Meters), 0, Meters);

        FieldLocation adjustedLocation = adjustShotLocationForVelocity(
            intialLocation, 
            targetLocation, 
            robotTargetRelitiveVelocity, 
            intialGuess, 
            shotTimeFunction, 
            Seconds.of(0.01), 
            10,
            0.8
        );

        Vector2d shotOffsetVector = new Vector2d(
            adjustedLocation.getIn(Meters).getX(),
            adjustedLocation.getIn(Meters).getY()
        ).rotated(robotTargetRotation);

        return shotOffsetVector;
    }

    private static FieldLocation adjustShotLocationForVelocity(
        FieldLocation initalLocation, 
        FieldLocation targetLocation,
        Vector2d velocityMetersPerSecond, 
        Time intialGuess, 
        Function<Distance, Time> shotTimeFunction, 
        Time allowableError, 
        int maxIterations,
        double adjustmentScalar
    ) {
        FieldLocation adjuctedLocation = initalLocation;
        for (int i = 0; i < maxIterations; i++) {
            // Calculate the adjusted location
            adjuctedLocation = new FieldLocation(
                velocityMetersPerSecond.scaled(intialGuess.in(Seconds)).applyVector(initalLocation.getIn(Meters)),
                Meters
            );

            // calculate the shot time for the adjusted location
            Time shotTime = shotTimeFunction.apply(Meters.of(adjuctedLocation.getIn(Meters).minus(targetLocation.getIn(Meters)).getNorm()));
            Time timeError = shotTime.minus(intialGuess);

            // If the time error is within the allowable error, return the adjusted location
            if (timeError.in(Seconds) < allowableError.in(Seconds)) {
                return adjuctedLocation;
            }

            // adjusts it by a scaler to try and reduce overshooting and oscillation around the target time
            Time shotTimeWithAdjustmentScaler = shotTime.plus(Seconds.of((1 - adjustmentScalar) * (intialGuess.in(Seconds) - shotTime.in(Seconds))));
            intialGuess = shotTimeWithAdjustmentScaler;
        }


        System.out.println("Max iterations reached in adjustShotLocationForVelocity, returning last adjusted location");
        return adjuctedLocation;
    }











}
