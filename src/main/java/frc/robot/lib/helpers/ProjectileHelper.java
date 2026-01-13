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
            Distance adjuctedTangentialDistance = Meters.of(tangentialVelocity * adjustedShotTime.in(Seconds));
            FieldLocation adjustedShooterLocation = new FieldLocation(
                shooterLocation.getX().plus(Meters.of(adjuctedTangentialDistance.in(Meters) * Math.cos(targetLocation.getIn(Meters).minus(shooterLocation.getIn(Meters)).getAngle().getRadians() + Math.PI / 2))),
                shooterLocation.getY().plus(Meters.of(adjuctedTangentialDistance.in(Meters) * Math.sin(targetLocation.getIn(Meters).minus(shooterLocation.getIn(Meters)).getAngle().getRadians() + Math.PI / 2)))
            );
            profile = profileFunction.apply(adjustedShooterLocation);
        }

        return profile;
    }













}
