package frc.pidSources;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class WallMidpoint implements PIDSource {
    static double centerFOV;
    static final double[] empty = {};
    private NetworkTableEntry centerArray;

    public WallMidpoint(String axis, double centerFOV) {//('X' or 'Y'), (FOV: X = 80, Y = 60) 
      this.centerArray = NetworkTableInstance
      .getDefault()
      .getTable("GRIP/referenceContours")
      .getEntry("center" + axis);

      this.centerFOV = centerFOV;
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {			
	}
			
	@Override
    public double pidGet() {
        double[] centerVals = centerArray.getDoubleArray(empty);

        if(centerVals.length != 2) {
            return 0;
        }

        double minCenter = Math.min(centerVals[0], centerVals[1]);
        double maxCenter = Math.max(centerVals[0], centerVals[1]);
    
        return minCenter + (maxCenter - minCenter)/2 - centerFOV;		
	}
			
	@Override
    public PIDSourceType getPIDSourceType() {			
	    return PIDSourceType.kDisplacement;		
	}
}