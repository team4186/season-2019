package frc.commands;

import frc.robot.ArcadeMode;
import frc.robot.ArcadeMode.Result;
import frc.robot.DirectionRef;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;

public class TurnAbsAngle extends Command {

	private final AHRS navx;	
	private final double direction;	
	private DirectionRef absAngle;
	
	private PIDController pidDir;
	
	private final double p = 0.004; //Tune + adjust to account for dead zone (use encoders)
	private final double i = 0.0001;
	private final double d = 0.0;
	
    private EncoderArcade turnDrive;
    	
	public TurnAbsAngle(
        AHRS navx, 
        double direction, 
        DirectionRef absAngle, 
        EncoderArcade turnDrive, 
        ArcadeMode arcadeMode
        ) {
		
		this.navx = navx;
		this.direction = direction;
		this.turnDrive = turnDrive;
		this.absAngle = absAngle;
		
		pidDir = new PIDController(p, i, d, new PIDSource() {
			
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
				
			}
			
			@Override
			public double pidGet() {

				//return adjustAngle(navx.getAngle());
				return navx.getAngle();
				
			}
			
			@Override
			public PIDSourceType getPIDSourceType() {
				
				return PIDSourceType.kDisplacement;
			
			}
		}, new PIDOutput() {
			
			@Override
			public void pidWrite(double output) {
				
				Result arcadeResult = arcadeMode.drive(0.0, output);

				//System.out.println(output);
				
				turnDrive.setDrive(15000*arcadeResult.leftSpeed, 15000*arcadeResult.rightSpeed);
				
			}
		});
		
	}
	
	@Override
	protected void initialize() {
		
		navx.reset();
		pidDir.setInputRange(-180.0, 180.0);
		pidDir.setAbsoluteTolerance(1.0);
		pidDir.setOutputRange(-0.5, 0.5);
		pidDir.setContinuous(true);
		pidDir.setSetpoint(direction - absAngle.absAngle);
		pidDir.enable();
		
		turnDrive.start();
				
	}
	
	@Override
	protected void execute() { 
				
	}
	
	@Override
	protected boolean isFinished() {
		
		return pidDir.onTarget();
		
	}
	
	@Override
	protected void end() {
		
		absAngle.absAngle = direction;
		turnDrive.cancel();
		pidDir.reset();
		
	}
	
}