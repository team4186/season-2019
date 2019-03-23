package frc.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class ActuateDoubleSolenoid extends Command {

	private final DoubleSolenoid solenoid;
	private final Value direction;
	
	public ActuateDoubleSolenoid(DoubleSolenoid solenoid, Value direction) {
		this.direction = direction;
		this.solenoid = solenoid;
	}
	
	@Override
	protected void initialize() {
		System.out.println("actuate");
	}
	
	@Override
	protected void execute() {	
		System.out.println("actuate-execute");
		solenoid.set(direction);		
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}
}
