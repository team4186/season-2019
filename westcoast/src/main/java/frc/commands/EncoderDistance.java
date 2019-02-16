package frc.commands;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;

public class EncoderDistance extends Command {
	
	private final Encoder leftEncoder;
	private final Encoder rightEncoder;
    private double distance;
	
	private PIDController pidLeft;
	private PIDController pidRight;
	private PIDController speedLeft;
	private PIDController speedRight;
	
	private double leftSetpoint = 0.0;
	private double rightSetpoint = 0.0;
	
	public EncoderDistance(
        Encoder leftEncoder, 
        Encoder rightEncoder, 
        SpeedController victorLeft, 
        SpeedController victorRight, 
        double distance
        ) {
		
		this.leftEncoder = leftEncoder;
		this.rightEncoder = rightEncoder;
		this.distance = distance;
		
		pidLeft = new PIDController(0.00001, 0.00000005, 0.00001, new PIDSource() {
			
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
				
			}
			
			@Override
			public double pidGet() {

				return leftEncoder.get();
				
			}
			
			@Override
			public PIDSourceType getPIDSourceType() {
				
				return PIDSourceType.kDisplacement;
			
			}
		}, new PIDOutput() {
			
			@Override
			public void pidWrite(double output) {
								
				leftSetpoint = output;
				
			}
		});
		
		pidRight = new PIDController(0.00001, 0.00000005, 0.00001, new PIDSource() {
			
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
				
			}
			
			@Override
			public double pidGet() {

				return rightEncoder.get();
				
			}
			
			@Override
			public PIDSourceType getPIDSourceType() {
				
				return PIDSourceType.kDisplacement;
			
			}
		}, new PIDOutput() {
			
			@Override
			public void pidWrite(double output) {
								
				rightSetpoint = output;
				
			}
		});

		speedLeft = new PIDController(0.0001, 0.0, 0.0, new PIDSource() {
			
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
				
			}
			
			@Override
			public double pidGet() {

				//return adjustAngle(navx.getAngle());
				return leftEncoder.getRate();
				
			}
			
			@Override
			public PIDSourceType getPIDSourceType() {
				
				return PIDSourceType.kRate;
			
			}
		}, new PIDOutput() {
			
			@Override
			public void pidWrite(double output) {
								
				victorLeft.set(output);
				
			}
		});
		
		speedRight = new PIDController(0.0001, 0.0, 0.0, new PIDSource() {
			
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
				
			}
			
			@Override
			public double pidGet() {

				//return adjustAngle(navx.getAngle());
				return rightEncoder.getRate();
				
			}
			
			@Override
			public PIDSourceType getPIDSourceType() {
				
				return PIDSourceType.kRate;
			
			}
		}, new PIDOutput() {
			
			@Override
			public void pidWrite(double output) {
								
				victorRight.set(-output);
				
			}
		});
	}
	
	@Override
	protected void initialize() {
		//encoder.setReverseDirection(true);
		leftEncoder.reset();
		pidLeft.setAbsoluteTolerance(15.0);
		pidLeft.setOutputRange(-0.3, 0.3);
		pidLeft.setContinuous(false);
		pidLeft.setSetpoint(distance*2166);
		pidLeft.enable();
		
		rightEncoder.reset();
		pidRight.setAbsoluteTolerance(15.0);
		pidRight.setOutputRange(-0.3, 0.3);
		pidRight.setContinuous(false);
		pidRight.setSetpoint(distance*2166);
		pidRight.enable();
		
		speedLeft.setInputRange(-15000, 15000);
		speedLeft.setAbsoluteTolerance(100);
		speedLeft.setOutputRange(-1.0, 1.0);
		speedLeft.setContinuous(false);
		speedLeft.setSetpoint(0);
		
		speedRight.setInputRange(-15000, 15000);
		speedRight.setAbsoluteTolerance(100);
		speedRight.setOutputRange(-1.0, 1.0);
		speedRight.setContinuous(false);
		speedRight.setSetpoint(0);
		
		pidLeft.enable();
		pidRight.enable();
		speedLeft.enable();
		speedRight.enable();
		
	}
	
	@Override
	protected void execute() {
		
		setDrive(leftSetpoint, rightSetpoint);
		
		System.out.println(leftEncoder.get());
						
	}
	
	@Override
	protected boolean isFinished() {
		
		return pidLeft.onTarget() && pidRight.onTarget();
		
	}
	
	@Override
	protected void end() {
		
		pidLeft.reset();
		pidRight.reset();
		
		speedLeft.reset();
		speedRight.reset();
		System.out.println("ded");
		
	}
	
	public void setDrive(double leftSetpoint, double rightSetpoint) {
		
		speedLeft.setSetpoint(leftSetpoint*15000);
		speedRight.setSetpoint(rightSetpoint*15000);
		
	}
	
}
