package frc.robot.lib.components;

import static edu.wpi.first.units.Units.Rotations;

import com.ctre.phoenix6.hardware.CANcoder;
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

public Angle getNonZeroMeasuredGearAngle() {
    double valA = encoderA.getAbsolutePosition().getValue().in(Rotations);
    double valB = encoderB.getAbsolutePosition().getValue().in(Rotations);

    valA = valA % 1.0; 
    valB = valB % 1.0;
    if (valA < 0) valA += 1.0;
    if (valB < 0) valB += 1.0;

    double remainderA = valA * aTeeth;
    double remainderB = valB * bTeeth;

    double diff = remainderB - remainderA;
    double kRaw = (diff * invAToB) % bTeeth;
    if (kRaw < 0) kRaw += bTeeth;

    long kInt = Math.round(kRaw);
    if (kInt == (long)bTeeth) kInt = 0;

    double totalTeeth = remainderA + (aTeeth * kInt);

    double predictedRB = totalTeeth % bTeeth;
    double error = Math.abs(predictedRB - remainderB);

    if (error > bTeeth / 2.0) {
        error = bTeeth - error;
    }

    if (error > 0.1) System.out.println("CRT Error too high: " + error);

    if (error > 0.5) {
        return Rotations.of(-1);
    }

    return Rotations.of(totalTeeth / messuredGearTeeth);
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

    public void updateSimulationFromMotor(double motorRotations) {
        double turretRotations = motorRotations * (20.0 / 200.0);

        double sensorARotations = turretRotations * (200.0 / 19.0);
        double sensorBRotations = turretRotations * (200.0 / 21.0);

        encoderA.getSimState().setRawPosition(sensorARotations);
        encoderB.getSimState().setRawPosition(sensorBRotations);
    }
}
