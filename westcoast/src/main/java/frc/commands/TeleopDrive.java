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
		
		drive.arcadeDrive(joystick.getY(), -joystick.getTwist());
		
	}
	
	@Override
	protected boolean isFinished() {
		
		return false;
		
	}
	
	@Override
	protected void end() {
		
		drive.stopMotor();
		
	}
	
}
