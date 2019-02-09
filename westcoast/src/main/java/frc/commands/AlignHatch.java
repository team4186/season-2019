package frc.commands;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;
import frc.pidSources.WallMidpoint;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class AlignHatch extends Command {	
    
	private PIDController pid;
	private EncoderArcade encoderArcade;
	
	double power;

    public AlignHatch(EncoderArcade encoderArcade) {

			this.encoderArcade = encoderArcade;

		pid = new PIDController(0.001, 0.0, 0.0, new WallMidpoint("X", 80.0), new PIDOutput() {
			
			@Override
			public void pidWrite(double output) {

				power = output;			

			}
		});

	}
	
	@Override
	protected void initialize() {

		pid.setAbsoluteTolerance(1.0);
		pid.setOutputRange(-0.3, 0.3);
		pid.setContinuous(false);
		pid.setSetpoint(0.0);
		pid.enable();

		encoderArcade.start();

	}
	
	@Override
	protected void execute() {
		
		//System.out.println(power);
		SmartDashboard.putNumber("Output", power);
		SmartDashboard.putBoolean("IsActive", pid.onTarget());

		encoderArcade.setDrive(power*15000, power*15000);

	}
	
	@Override
	protected boolean isFinished() {
		return pid.onTarget();
	}
	
	@Override
	protected void end() {
		
		pid.reset();
		encoderArcade.cancel();
		System.out.println("ded");
		
	}
		
}
