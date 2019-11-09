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
			drive.arcadeDrive( attenuate(-joystick.getY()),attenuate(-joystick.getTwist()));
			//drive.arcadeDrive( attenuate(-joystick.getY()),attenuate(-joystick.getX()));
			//drive.arcadeDrive( attenuate(-joystick.getZ()),attenuate(-joystick.getX()));
		
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