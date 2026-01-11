package frc.robot.lib.geometry;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.DistanceUnit;
import edu.wpi.first.units.measure.Distance;

public class FieldLocation {
    
    private Distance x;
    private Distance y;

    public FieldLocation(Distance x, Distance y) {
        this.x = x;
        this.y = y;
    }

    public FieldLocation(double x, double y, DistanceUnit unit) {
        this.x = unit.of(x);
        this.y = unit.of(y);
    }

    public FieldLocation(Translation2d translation, DistanceUnit unit) {
        this.x = unit.of(translation.getX());
        this.y = unit.of(translation.getY());
    }

    public Distance getX() {
        return x;
    }

    public Distance getY() {
        return y;
    }

    public double getX(DistanceUnit unit) {
        return x.in(unit);
    }

    public double getY(DistanceUnit unit) {
        return y.in(unit);
    }

    public void setX(Distance x) {
        this.x = x;
    }

    public void setY(Distance y) {
        this.y = y;
    }

    public void setX(double x, DistanceUnit unit) {
        this.x = unit.of(x);
    }

    public void setY(double y, DistanceUnit unit) {
        this.y = unit.of(y);
    }

    public Translation2d getIn(DistanceUnit unit) {
        return new Translation2d(getX(unit), getY(unit));
    }
}
