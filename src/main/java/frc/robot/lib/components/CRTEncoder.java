package frc.robot.lib.components;

import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.Rotations;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.CANcoder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Pair;
import edu.wpi.first.units.measure.Angle;
import frc.robot.lib.helpers.MathHelpers;
/* 
 * Chinese Remainder Theory
 * 
 * The CRT helps finds a certain T that:
 * T ≡ rA (mod a)
 * T ≡ rB (mod b)
 * 
 * if gcd(a, b) = 1, AKA a & b are coprime, there is only one T that satisfies the system. 
 * 
 * In our application, a = Gear A's # of teeth, b = Gear B's # of teeth.
 * if aTeeth isn't coprime with bTeeth, this method will not work. In our case, we went with 19t and 21t.
 * 
 * The absolute position of Gear A and Gear B together act as a sort of 'fingerprint' for the bigger, 200T turret gear.
*/
public class CRTEncoder {

    private final CANcoder encoderA;
    private final CANcoder encoderB;
    private final int aTeeth;
    private final int bTeeth;
    private final int messuredGearTeeth;
    private final int invAToB;

    private Angle zeroPosition = Rotations.of(0);

    public CRTEncoder(int encoderAId, int encoderBId, int aTeeth, int bTeeth, int measuredGearTeeth) {
        this.aTeeth = aTeeth;
        this.bTeeth = bTeeth;
        this.messuredGearTeeth = measuredGearTeeth;

        encoderA = new CANcoder(encoderAId);
        encoderB = new CANcoder(encoderBId);

        invAToB = MathHelpers.modInverse(aTeeth, bTeeth);
    }

    public CRTEncoder(int encoderAId, int encoderBId, int aTeeth, int bTeeth, int messuredGearTeeth, Angle zeroPosition) {
        this(encoderAId, encoderBId, aTeeth, bTeeth, messuredGearTeeth);
        this.zeroPosition = zeroPosition;
    }

    public Angle getMeasuredGearAngle() {
        return zeroPosition.plus(getNonZeroMeasuredGearAngle());
    }

    public Pair<Angle, Angle> getEncoderABAngle() {
        StatusSignal<Angle> cancoderAPositionSignal = encoderA.getAbsolutePosition(false);
        StatusSignal<Angle> cancoderBPositionSignal = encoderB.getAbsolutePosition(false);

        BaseStatusSignal.waitForAll(0.020, cancoderAPositionSignal, cancoderBPositionSignal);
        return new Pair<Angle,Angle>(cancoderAPositionSignal.getValue(), cancoderBPositionSignal.getValue());
    }

    public Angle getNonZeroMeasuredGearAngle() {
        // get absolute positions of both encoders
        Angle aAngle = getEncoderABAngle().getFirst();
        Angle bAngle = getEncoderABAngle().getSecond();

        double ratioAtoB = (double) aTeeth / bTeeth;
        
        // predict the angle if there have been no rotations of encoder A
        Angle predictedAngleB = aAngle.times(ratioAtoB);
        Angle differenceInPredictedAngle = bAngle.minus(predictedAngleB);

        Angle differenceWrappedAngle = Radians.of(MathUtil.angleModulus(differenceInPredictedAngle.in(Radians)));

        Angle errorPerGearARotation = Rotations.of(ratioAtoB);

        long numGearARotations = Math.round(differenceWrappedAngle.in(Rotations) / (errorPerGearARotation.in(Rotations) -1.0));

        // find the angle of the measured gear
        Angle gearAAngle = aAngle.plus(Rotations.of(numGearARotations));
        Angle measuredGearAngle = gearAAngle.times((double) aTeeth / messuredGearTeeth);
        return measuredGearAngle;

    }

    public void setZeroPosition(Angle zeroPosition) {
        this.zeroPosition = zeroPosition;
    }

    public Angle getZeroPosition() {
        return zeroPosition;
    }

    public void reset() {
        setZeroPosition(getMeasuredGearAngle());
    }

    public void updateSimulationFromMotor(double turretPose) {
        
        double sensorARotations = turretPose * ((double) messuredGearTeeth / aTeeth);
        double sensorBRotations = turretPose * ((double) messuredGearTeeth / bTeeth);

        encoderA.getSimState().setRawPosition(sensorARotations % 1.0);
        encoderB.getSimState().setRawPosition(sensorBRotations % 1.0);
    }
}
