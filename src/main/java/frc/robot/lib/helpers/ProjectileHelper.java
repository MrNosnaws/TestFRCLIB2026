package frc.robot.lib.helpers;

import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.Seconds;

import java.util.function.Function;

import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.Time;
import frc.robot.lib.geometry.FieldLocation;
import frc.robot.lib.geometry.Vector2d;
import frc.robot.lib.util.ShotProfile;

public class ProjectileHelper {
    
    public static ShotProfile getShotProfile(FieldLocation targetLocation, FieldLocation shooterLocation, Function<FieldLocation, ShotProfile> profileFunction, Function<ShotProfile, Time> shotTimeFunction, Vector2d velocityMetersPerSecond, boolean includeShootWhileMoving) {
        ShotProfile profile = profileFunction.apply(targetLocation);
        Time shotTime = shotTimeFunction.apply(profile);

        Distance distanceToTarget = Meters.of(targetLocation.getIn(Meters).minus(shooterLocation.getIn(Meters)).getNorm());

        // velocity towords the target
        double radialVelocity = velocityMetersPerSecond.getVectorAsTranslation().getNorm() 
                                    * Math.cos(
                                        velocityMetersPerSecond.getVectorAsTranslation().getAngle().getRadians()
                                        - targetLocation.getIn(Meters).minus(shooterLocation.getIn(Meters)).getAngle().getRadians()
                                    );

        // velocity perpendicular to the target
        double tangentialVelocity = velocityMetersPerSecond.getVectorAsTranslation().getNorm() 
                                    * Math.sin(
                                        velocityMetersPerSecond.getVectorAsTranslation().getAngle().getRadians()
                                        - targetLocation.getIn(Meters).minus(shooterLocation.getIn(Meters)).getAngle().getRadians()
                                    );

        if (includeShootWhileMoving) {
            Time adjustedShotTime = shotTime.plus(Seconds.of(distanceToTarget.in(Meters) / radialVelocity));
            FieldLocation approximateLocation = adjustShotLocationForVelocity(
                shooterLocation, 
                velocityMetersPerSecond, 
                adjustedShotTime, 
                shotTimeFunction, 
                profileFunction, 
                Seconds.of(0.05), 
                3, 
                0.8);
            profile = profileFunction.apply(approximateLocation);
        }

        return profile;
    }

    public static FieldLocation adjustShotLocationForVelocity(
        FieldLocation initalLocation, 
        Vector2d velocityMetersPerSecond, 
        Time intialGuess, 
        Function<ShotProfile, Time> shotTimeFunction, 
        Function<FieldLocation, ShotProfile> profileFunction,
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
            ShotProfile profile = profileFunction.apply(adjuctedLocation);
            Time shotTime = shotTimeFunction.apply(profile);
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
