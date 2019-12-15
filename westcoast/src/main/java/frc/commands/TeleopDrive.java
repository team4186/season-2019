package frc.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class TeleopDrive extends Command {
	
	private final DifferentialDrive drive;
	private final Joystick joystick;
	
	public TeleopDrive(
        DifferentialDrive drive, 
		Joystick joystick
		) {

		this.drive = drive;
		this.joystick = joystick;
	}		
	
	@Override
	protected void initialize() {	
	}
	
	@Override
	protected void execute() {	
		double leftSetpoint;
		double rightSetpoint;
		double xSpeed = joystick.getY();
		double zRotation = -joystick.getTwist();
		double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);
		if (xSpeed >= 0.0) {
		  // joystick is pushed forward
		  if (zRotation >= 0.0) {
			leftSetpoint = maxInput;
			rightSetpoint = xSpeed - zRotation;
		  } else {
			leftSetpoint = xSpeed + zRotation;
			rightSetpoint = maxInput;
		  }
		} else {
		  // joystick is pulled back
		  if (zRotation >= 0.0) {
			leftSetpoint = xSpeed + zRotation;
			rightSetpoint = maxInput;
		  } else {
			leftSetpoint = maxInput;
			rightSetpoint = xSpeed - zRotation;
		  }
		}
			
		drive.tankDrive(leftSetpoint, rightSetpoint, false);
	}
	
	@Override
	protected boolean isFinished() {	
		return false;	
	}
	
	@Override
	protected void end() {	
		drive.stopMotor();	
	}

	private double attenuate(double value) {
		double v = value;
		boolean raw = joystick.getRawButton(5);
		if(raw == true){ 
			return (0.5*v);
		}
		else{
			return (Math.signum(v) * Math.pow(Math.abs(v), 1.2));
		}
	}
}