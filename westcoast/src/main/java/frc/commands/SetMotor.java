package frc.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.SpeedController;

public class SetMotor extends Command {

	private final SpeedController motor;
    private final double value;

	public SetMotor(
        SpeedController motor, 
        double value
        ) {
		
		this.motor = motor;
		this.value = value;
		
	}
	
	@Override
	protected void initialize() {
		
	}
	
	@Override
	protected void execute() {
		
        motor.set(value);
        
	}
	
	@Override
	protected boolean isFinished() {
		
		return false;
		
	}
	
	@Override
	protected void end() {
		
		motor.stopMotor();
		
	}
	
}
