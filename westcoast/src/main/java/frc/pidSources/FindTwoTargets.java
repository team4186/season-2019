package frc.pidSources;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class FindTwoTargets implements PIDSource {

    private NetworkTableEntry centerArray;
    static final double[] empty = {};

    public FindTwoTargets() {
      this.centerArray = NetworkTableInstance
      .getDefault()
      .getTable("GRIP/referenceContours")
      .getEntry("centerX");
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {			
	}
			
	@Override
    public double pidGet() {
        double[] centerVals = centerArray.getDoubleArray(empty);
    
        return centerVals.length;
	}
			
	@Override
    public PIDSourceType getPIDSourceType() {			
	    return PIDSourceType.kDisplacement;		
	}
}