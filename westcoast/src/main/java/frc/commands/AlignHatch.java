package frc.commands;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;
import frc.pidSources.WallMidpoint;

public class AlignHatch extends Command {	
    
    private PIDController pid;

    public AlignHatch() {
		pid = new PIDController(0.00001, 0.00000005, 0.00001, new WallMidpoint(), new PIDOutput() {
			
			@Override
			public void pidWrite(double output) {
				System.out.println(output);				

			}
		});

	}
	
	@Override
	protected void initialize() {
		pid.setAbsoluteTolerance(1.0);
		pid.setOutputRange(-0.3, 0.3);
		pid.setContinuous(false);
		pid.setSetpoint(0);
		pid.enable();		
	}
	
	@Override
	protected void execute() {
        
    }
	
	@Override
	protected boolean isFinished() {
		return pid.onTarget();
	}
	
	@Override
	protected void end() {
		
		pid.reset();
		System.out.println("ded");
		
	}
		
}
