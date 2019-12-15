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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PIDSourceType;

public class PIDDriveAHRS extends Command {

  private final Joystick joystick;
  private final PIDController pid;
  private final AHRS ahrs;
  private final DifferentialDrive drive;
  double p = 0.07;
  double i = 0.025;
  double d = 0.15;
  double output;

  public PIDDriveAHRS(
      AHRS ahrs,
      Joystick joystick,
      DifferentialDrive drive
  ) {
    this.drive = drive;
    this.joystick=joystick;
    this.ahrs = ahrs;
    this.pid=new PIDController(p, i, d, ahrs, (turnFactor) -> output = turnFactor, 0.1);
    ahrs.setPIDSourceType(PIDSourceType.kRate);
    pid.setAbsoluteTolerance(1);
    pid.setContinuous(false);
    pid.setInputRange(-4, 4);
    pid.setOutputRange(-0.6, 0.6);
  }

  @Override
  protected void initialize() {
    pid.setEnabled(true);
  }

  @Override
  protected void execute() {
    pid.setSetpoint(attenuate(joystick.getTwist()) * 4);
    
    drive.arcadeDrive(joystick.getY(), -attenuate(output), false);

    /*if(ahrs.getRate() >= 6){
      //pid.reset();
    }
    else if(ahrs.getRate() <= -6){
      pid.reset();
    }*/
    SmartDashboard.putNumber("Output", output);
    SmartDashboard.putNumber("Setpoint", pid.getSetpoint());
    SmartDashboard.putNumber("Error", pid.getError());
    SmartDashboard.putBoolean("On Target?", pid.onTarget());
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    pid.reset();
  }

  @Override
  protected void interrupted() {
    end();
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
