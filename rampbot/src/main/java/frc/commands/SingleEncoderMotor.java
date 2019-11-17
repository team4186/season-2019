package frc.commands;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;

public class SingleEncoderMotor extends Command {
		
	private final Encoder encoder;
    private double distance;
	
	private PIDController pidDist;
	private PIDController pidSpeed;
	
	private double setpoint = 0.0;
	
	public SingleEncoderMotor(
        Encoder encoder, 
        SpeedController motor, 
		double distance) {	

		this.encoder = encoder;
		this.distance = distance;
		
		pidDist = new PIDController(0.00001, 0.00000005, 0.00001, 
		new PIDSource() {	
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {	
			}
			
			@Override
			public double pidGet() {
				return encoder.get();	
			}
			
			@Override
			public PIDSourceType getPIDSourceType() {	
				return PIDSourceType.kDisplacement;			
			}
		},
		new PIDOutput() {	
			@Override
			public void pidWrite(double output) {					
				setpoint = output;	
			}
		});
		
		pidSpeed = new PIDController(0.0001, 0.0, 0.0, new PIDSource() {	
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {	
			}
			
			@Override
			public double pidGet() {
				//return adjustAngle(navx.getAngle());
				return encoder.getRate();	
			}
			
			@Override
			public PIDSourceType getPIDSourceType() {	
				return PIDSourceType.kRate;
			}
		}, 
		new PIDOutput() {	
			@Override
			public void pidWrite(double output) {
				motor.set(-output);	
			}
		});
	}
	
	@Override
	protected void initialize() {
		//encoder.setReverseDirection(true);
		encoder.reset();
		pidDist.setAbsoluteTolerance(15.0);
		pidDist.setOutputRange(-0.3, 0.3);
		pidDist.setContinuous(false);
		pidDist.setSetpoint(distance*2166);
		pidDist.enable();
		
		pidSpeed.setInputRange(-15000, 15000);
		pidSpeed.setAbsoluteTolerance(100);
		pidSpeed.setOutputRange(-1.0, 1.0);
		pidSpeed.setContinuous(false);
		pidSpeed.setSetpoint(0);
		
		pidDist.enable();
		pidSpeed.enable();
	}
	
	@Override
	protected void execute() {	
		setSpeed(setpoint);	
	}
	
	@Override
	protected boolean isFinished() {	
		return pidDist.onTarget();	
	}
	
	@Override
	protected void end() {	
		pidDist.reset();
		pidSpeed.reset();
		
		System.out.println("ded");	
	}
	
	public void setSpeed(double setpoint) {	
		pidSpeed.setSetpoint(setpoint*15000);
	}
}