package frc.commands;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Command;

public class DriveServo extends Command {	
    
	private Servo servo;
	private double direction;
	private double rate;

	double servoTarget;
	
    public DriveServo(Servo servo, double direction, double rate) {

			this.servo = servo;
			this.direction = direction;
			this.rate = rate;

	}
	
	@Override
	protected void initialize() {
		servoTarget = servo.get();
	}
	
	@Override
	protected void execute() {
		/*if(dPadLeft.get() && servoTarget > 0.01){
			servoTarget -= 0.01;
			servo.set(servoTarget);
		}
		else if(dPadRight.get() && servoTarget < 0.99){
			servoTarget += 0.01;
			servo.set(servoTarget);
		}*/
		System.out.println("Servo: " + servo.get() + ", rate: " + rate + ", direction: " + direction);
		
		servo.set(servo.get() + rate * direction);
		
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	@Override
	protected void end() {
	}
		
}
