package frc.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Command;
import frc.pidSources.WallMidpoint;

public class AlignHatchServo extends Command {	
    
	private PIDController pid;
	private Servo servo;
	
	double target;

    public AlignHatchServo(Servo servo) {

			this.servo = servo;

		pid = new PIDController(0.0001, 0.0, 0.0, new WallMidpoint("X", 80.0), new PIDOutput() {
			
			@Override
			public void pidWrite(double output) {

				target = output;			

			}
		});

	}
	
	@Override
	protected void initialize() {
		pid.setAbsoluteTolerance(1.0);
		pid.setOutputRange(0.0, 1.0);
		pid.setContinuous(false);
		pid.setSetpoint(0.0);
		pid.enable();
	}
	
	@Override
	protected void execute() {
		servo.set(target);
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
