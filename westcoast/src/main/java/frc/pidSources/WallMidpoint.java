package frc.pidSources;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class WallMidpoint implements PIDSource{

    static final double centerXFOV = 80;
    static final double[] empty = {};
    private NetworkTableEntry centerXArray;

    public WallMidpoint(){

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
    if(centerXVals.length != 2){
        return 0;
    }
    double minCenter = Math.min(centerXVals[0], centerXVals[1]);
    double maxCenter = Math.max(centerXVals[0], centerXVals[1]);
    
    return minCenter + (maxCenter - minCenter)/2 - centerXFOV;
				
	}
			
	@Override
	public PIDSourceType getPIDSourceType() {
				
	    return PIDSourceType.kDisplacement;
			
	}

}