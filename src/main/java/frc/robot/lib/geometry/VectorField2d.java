package frc.robot.lib.geometry;

@FunctionalInterface
public interface VectorField2d {

	/**
	 * Computes a vector at a given x and y coordinate.
	 *
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @return the computed vector as a Vector2d
	 */
	Vector2d computeVector(double x, double y);

    public static VectorField2d getGradient(VectorField2d field) {
        return (x, y) -> {
            final double delta = 1e-4;

            Vector2d vectorAtPoint = field.computeVector(x, y);
            Vector2d vectorAtPointXPlusDelta = field.computeVector(x + delta, y);
            Vector2d vectorAtPointYPlusDelta = field.computeVector(x, y + delta);

            double dVx_dx = (vectorAtPointXPlusDelta.getX() - vectorAtPoint.getX()) / delta;
            double dVy_dy = (vectorAtPointYPlusDelta.getY() - vectorAtPoint.getY()) / delta;

            return new Vector2d(dVx_dx, dVy_dy);
        };
    }

    default VectorField2d getGradient() {
        return getGradient(this);
    }

    public static VectorField2d getDivergence(VectorField2d field) {
        return (x, y) -> {
            final double delta = 1e-4;

            Vector2d vectorAtPoint = field.computeVector(x, y);
            Vector2d vectorAtPointXPlusDelta = field.computeVector(x + delta, y);
            Vector2d vectorAtPointYPlusDelta = field.computeVector(x, y + delta);

            double dVx_dx = (vectorAtPointXPlusDelta.getX() - vectorAtPoint.getX()) / delta;
            double dVy_dy = (vectorAtPointYPlusDelta.getY() - vectorAtPoint.getY()) / delta;

            return new Vector2d(dVx_dx + dVy_dy, 0.0);
        };
    }

    default VectorField2d getDivergence() {
        return getDivergence(this);
    }

    public static VectorField2d getCurl(VectorField2d field) {
        return (x, y) -> {
            final double delta = 1e-4;

            Vector2d vectorAtPoint = field.computeVector(x, y);
            Vector2d vectorAtPointXPlusDelta = field.computeVector(x + delta, y);
            Vector2d vectorAtPointYPlusDelta = field.computeVector(x, y + delta);

            double dVy_dx = (vectorAtPointXPlusDelta.getY() - vectorAtPoint.getY()) / delta;
            double dVx_dy = (vectorAtPointYPlusDelta.getX() - vectorAtPoint.getX()) / delta;

            return new Vector2d(0.0, dVy_dx - dVx_dy);
        };
    }

    default VectorField2d getCurl() {
        return getCurl(this);
    }

    public static VectorField2d getZeroField() {
        return (x, y) -> new Vector2d(0.0, 0.0);
    }

    public static VectorField2d getConstantField(Vector2d constantVector) {
        return (x, y) -> constantVector;
    }

    public static VectorField2d getFieldToPoint(double targetX, double targetY) {
        return (x, y) -> new Vector2d(targetX - x, targetY - y);
    }

    public static VectorField2d addFields(VectorField2d field1, VectorField2d field2) {
        return (x, y) -> {
            Vector2d vector1 = field1.computeVector(x, y);
            Vector2d vector2 = field2.computeVector(x, y);
            vector1.add(vector2);
            return vector1;
        };
    }

    public static VectorField2d scaleField(VectorField2d field, double scalar) {
        return (x, y) -> {
            Vector2d vector = field.computeVector(x, y);
            vector.scale(scalar);
            return vector;
        };
    }

    public static VectorField2d invertField(VectorField2d field) {
        return (x, y) -> {
            Vector2d vector = field.computeVector(x, y);
            vector.invert();
            return vector;
        };
    }

} 