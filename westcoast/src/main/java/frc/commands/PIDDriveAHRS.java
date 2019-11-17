/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.commands;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.PIDSourceType;

public class PIDDriveAHRS extends Command {

  private final Joystick joystick;
  private final PIDController pid;
  double p = 0.07;
  double i = 0.025;
  double d = 0.2;

  public PIDDriveAHRS(
      AHRS ahrs,
      Joystick joystick,
      DifferentialDrive drive
  ) {
    this.joystick=joystick;
    this.pid=new PIDController(p, i, d, ahrs, (turnFactor) -> {      
      drive.arcadeDrive(joystick.getY(), -attenuate(turnFactor), false);
    });
    ahrs.setPIDSourceType(PIDSourceType.kRate);
    pid.setAbsoluteTolerance(0.4);
    pid.setContinuous(false);
    pid.setInputRange(-6, 6);
    pid.setOutputRange(-1, 1);
  }

  @Override
  protected void initialize() {
    pid.setEnabled(true);
  }

  @Override
  protected void execute() {
    pid.setSetpoint(attenuate(joystick.getTwist()) * 6);
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
  }

  @Override
  protected void interrupted() {
  }

  private double attenuate(double value) {
		double v = value;
		boolean raw = joystick.getRawButton(5);
		if(raw == true){ 
			return (0.5*v);
		}
		else{
			return (Math.signum(v) * Math.pow(Math.abs(v), 1.3));
		}
	}
}
