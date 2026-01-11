package frc.robot.lib.components;

import static edu.wpi.first.units.Units.Rotations;

import com.ctre.phoenix6.hardware.CANcoder;

import edu.wpi.first.units.measure.Angle;

public class CRTEncoder {

    private final CANcoder encoderA;
    private final CANcoder encoderB;
    private final int aTeeth;
    private final int bTeeth;
    private final int messuredGearTeeth;

    private Angle zeroPosition = Rotations.of(0);

    public CRTEncoder(int encoderAId, int encoderBId, int aTeeth, int bTeeth, int messuredGearTeeth) {
        this.aTeeth = aTeeth;
        this.bTeeth = bTeeth;
        this.messuredGearTeeth = messuredGearTeeth;

        encoderA = new CANcoder(encoderAId);
        encoderB = new CANcoder(encoderBId);
    }

    public CRTEncoder(int encoderAId, int encoderBId, int aTeeth, int bTeeth, int messuredGearTeeth, Angle zeroPosition) {
        this(encoderAId, encoderBId, aTeeth, bTeeth, messuredGearTeeth);
        this.zeroPosition = zeroPosition;
    }

    public Angle getMeasuredGearAngle() {
        return zeroPosition.plus(getNonZeroMeasuredGearAngle());
    }

    public Angle getNonZeroMeasuredGearAngle () {
        double remainderA = encoderA.getAbsolutePosition().getValue().in(Rotations) * aTeeth;
        double remainderB = encoderB.getAbsolutePosition().getValue().in(Rotations) * bTeeth;

        for (int i = 0; i <= aTeeth; i++) {
            double hypothosisTeeth = aTeeth*i + remainderA;
            double remainderBCheck = hypothosisTeeth % bTeeth;
            if (Math.abs(remainderBCheck - remainderB) < 0.01) {
                double totalTeeth = hypothosisTeeth + (remainderB - remainderBCheck);
                return Rotations.of(totalTeeth / messuredGearTeeth);
            }
        }
        return Rotations.of(-1); // Error case, should never happen
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
}
