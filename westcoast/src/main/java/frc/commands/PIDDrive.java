
package frc.commands;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class PIDDrive extends Command {
  
  private final AHRS ahrs;
  private final DifferentialDrive drive;
  private final Joystick joystick;
  private PIDController PIDControl;
  
  public PIDDrive(
    DifferentialDrive drive,
    Joystick joystick,
    AHRS ahrs
    ) {
      this.drive=drive;
      this.joystick=joystick;
      this.ahrs=ahrs;
    
      PIDControl = new PIDController(0.5, 0, 0.6, ahrs, (turnController) -> drive.arcadeDrive(-joystick.getY(), -(turnController)));
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    PIDControl.setContinuous(true);
    PIDControl.setAbsoluteTolerance(4.0);
    PIDControl.setOutputRange(-1, 1);
    PIDControl.enable();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    PIDControl.setSetpoint(joystick.getTwist()*15);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    drive.stopMotor();
    PIDControl.disable();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}