package frc.pidSources;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class CenterDisplacement implements PIDSource {
    static final double centerXFOV = 80;
    static final double[] empty = {};
    private NetworkTableEntry centerXArray;

    public CenterDisplacement() {
      centerXArray = NetworkTableInstance
      .getDefault()
      .getTable("GRIP/referenceContours")
      .getEntry("centerX");
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {			
	}
			
	@Override
    public double pidGet() {
        double[] centerXVals = centerXArray.getDoubleArray(empty);
        return centerXVals.length != 0 ? centerXVals[0] - centerXFOV : 0;			
	}
			
	@Override
    public PIDSourceType getPIDSourceType() {			
	    return PIDSourceType.kDisplacement;		
	}
}