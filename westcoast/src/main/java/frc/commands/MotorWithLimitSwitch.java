package frc.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.DigitalInput;

public class MotorWithLimitSwitch extends Command {

	private final SpeedController motor;
	private final double value;
	private final DigitalInput switch1;
	private final DigitalInput switch2;

	public MotorWithLimitSwitch(
        SpeedController motor, 
		double value,
		DigitalInput switch1,
		DigitalInput switch2
	) {
		
		this.motor = motor;
		this.value = value;
		this.switch1 = switch1;
		this.switch2 = switch2;
		
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
		
		return (switch1.get() || switch2.get());
		
	}
	
	@Override
	protected void end() {
		
		motor.stopMotor();
		
	}
	
}
