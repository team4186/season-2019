package frc.commands;

import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class TeleopDrive extends Command {
	
	private final DifferentialDrive drive;
	private final Joystick joystick;
	//private final JoystickButton button;
	
	public TeleopDrive(
        DifferentialDrive drive, 
		Joystick joystick
		//JoystickButton button
		) {

		this.drive = drive;
		this.joystick = joystick;
		//this.button = button;
	}
	
	@Override
	protected void initialize() {	
	}
	
	@Override
	protected void execute() {	
			//drive.arcadeDrive( attenuate(-joystick.getY()),attenuate(-joystick.getTwist()));
			drive.arcadeDrive( attenuate(-joystick.getY()),attenuate(-joystick.getX()));
			//drive.arcadeDrive( attenuate(-joystick.getZ()),attenuate(-joystick.getX()));
			//System.out.println(attenuate());
		
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

// removed all "joystick button references" because it can get button without these constructors