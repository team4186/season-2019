package frc.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Solenoid;

public class ActuateSingleSolenoid extends Command {

	private final Solenoid solenoid;
	
	public ActuateSingleSolenoid(Solenoid solenoid) 
        {
			this.solenoid = solenoid;
        }
	
	@Override
	protected void initialize() {
		
	}
	
	@Override
	protected void execute() {
		
		solenoid.set(true);
		System.out.println("solenoid excecute");
	}
	
	@Override
	protected boolean isFinished() {
		
		return false;
		
	}
	
	@Override
	protected void end() {

		solenoid.set(false);
		System.out.println("solenoid end");
	}
	
}
