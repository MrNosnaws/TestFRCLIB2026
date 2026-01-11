package frc.robot.lib.util;

/**
 * An iterative boolean latch that delays the transition from false to true.
 */
public class DelayedBoolean {
	private boolean lastValue;
	private double transitionTimestamp;
	private final double mDelay;

	public DelayedBoolean(double timestamp, double delay) {
		transitionTimestamp = timestamp;
		lastValue = false;
		mDelay = delay;
	}

	public boolean update(double timestamp, boolean value) {
		boolean result = false;

		if (value && !lastValue) {
			transitionTimestamp = timestamp;
		}

		// If we are still true and we have transitioned.
		if (value && (timestamp - transitionTimestamp > mDelay)) {
			result = true;
		}

		lastValue = value;
		return result;
	}
}