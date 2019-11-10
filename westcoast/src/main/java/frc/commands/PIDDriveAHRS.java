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

  public PIDDriveAHRS(
      AHRS ahrs,
      Joystick joystick,
      DifferentialDrive drive
  ) {
    this.joystick=joystick;
    this.pid=new PIDController(0.2, 0.3, 0.5, ahrs, (turnFactor) -> {
      //System.out.println(turnFactor);
      
      drive.arcadeDrive(-joystick.getY(), -turnFactor);
    });
    ahrs.setPIDSourceType(PIDSourceType.kRate);
    pid.setAbsoluteTolerance(0.5);
    pid.setContinuous(false);
    pid.setInputRange(-6, 6);
    pid.setOutputRange(-.6, .6);
  }

  @Override
  protected void initialize() {
    pid.setEnabled(true);
  }

  @Override
  protected void execute() {
    pid.setSetpoint(joystick.getTwist() * 6);
    //System.out.println(pid.getError());
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


}
