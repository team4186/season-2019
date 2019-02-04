package frc.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class ActuateDoubleSolenoid extends Command {

	private final DoubleSolenoid solenoid;
	private final Value direction;
	private final Value endDirection;
	
	public ActuateDoubleSolenoid(
		DoubleSolenoid solenoid,
		Value direction,
		Value endDirection
        ) 
        {
			this.direction = direction;
			this.solenoid = solenoid;
			this.endDirection = endDirection;
        }
	
	@Override
	protected void initialize() {
		
	}
	
	@Override
	protected void execute() {
		
		solenoid.set(direction);
		System.out.println("Running");
		
	}
	
	@Override
	protected boolean isFinished() {
		
		return false;
		
	}
	
	@Override
	protected void end() {

		solenoid.set(endDirection);
		System.out.println("Finished");
		
	}
	
}
