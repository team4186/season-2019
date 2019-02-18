package frc.commands;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class EncoderArcade extends Command 
{
	private PIDController pidLeft;
	private PIDController pidRight;
	private SpeedController victorLeft;
	private SpeedController victorRight;
	
	public EncoderArcade(
			SpeedController victorLeft,
			SpeedController victorRight,
			Encoder leftEncoder, 
			Encoder rightEncoder,
			double p,
			double i,
			double d) 
	{	
		leftEncoder.reset();
		rightEncoder.reset();

		this.victorLeft = victorLeft;
		this.victorRight = victorRight;
		
		leftEncoder.setPIDSourceType(PIDSourceType.kRate);
		rightEncoder.setPIDSourceType(PIDSourceType.kRate);
		
		pidLeft = new PIDController(p, i, d, leftEncoder, 
		new PIDOutput() 
		{	
			@Override
			public void pidWrite(double output) 
			{	
				victorLeft.set(output);
				SmartDashboard.putNumber("Left", output);
			}
		});
		pidRight = new PIDController(p, i, d, rightEncoder, 
		new PIDOutput() 
		{
			@Override
			public void pidWrite(double output) 
			{
				victorRight.set(-output);
				SmartDashboard.putNumber("Right", output);	
			}
		});
	}
	
	@Override
	protected void initialize() 
	{	
		setupPid(pidLeft);
		setupPid(pidRight);
		
		pidLeft.enable();
		pidRight.enable();	
	}
	
	@Override
	protected void execute() 
	{	
	}
	
	@Override
	protected boolean isFinished() 
	{	
		return false;	
	}
	
	@Override
	protected void end() 
	{
		victorLeft.stopMotor();
		victorRight.stopMotor();
				
		pidLeft.reset();
		pidRight.reset();
	}
	
	public void setDrive(double leftSpeed, double rightSpeed) 
	{
		pidLeft.setSetpoint(leftSpeed);
		pidRight.setSetpoint(rightSpeed);	
	}
	
	private void setupPid(PIDController pid) 
	{
		pid.setInputRange(-15000, 15000);
		pid.setAbsoluteTolerance(100);
		pid.setOutputRange(-1, 1);
		pid.setContinuous(false);
		pid.setSetpoint(0);	
	}
}