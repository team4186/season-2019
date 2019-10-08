/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class ActuateTwoDoubleSolenoids extends Command {
  private final DoubleSolenoid solenoid;
  private final DoubleSolenoid solenoid2;
	private final Value direction;
  private final Value endDirection;
  private int v = 0;

  public ActuateTwoDoubleSolenoids(
    DoubleSolenoid solenoid,
    DoubleSolenoid solenoid2,
    Value direction,
    Value endDirection
    ) 
    {
      this.direction = direction;
      this.endDirection = endDirection;
      this.solenoid = solenoid;
      this.solenoid2 = solenoid2;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(solenoid.get() == direction){
      if(solenoid2.get() == endDirection){
        v = 0;
      }
      if(solenoid2.get() == direction){
        solenoid2.set(endDirection);
        v = 1;
      }
    }
    else if (solenoid.get() == endDirection){
      solenoid.set(direction);
      if(solenoid2.get() == endDirection){
        v = 2;
      }
      if(solenoid2.get() == direction){
        solenoid2.set(endDirection);
        v = 3;
      }
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    if(v == 0){
    }
    else if(v == 1){
      solenoid2.set(direction);
    }
    else if(v >= 2){
      if(v ==2){
        solenoid.set(endDirection);
      }
      else{
        solenoid.set(endDirection);
        solenoid2.set(direction);
      }
    }
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    if(v == 0){
    }
    else if(v == 1){
      solenoid2.set(direction);
    }
    else if(v >= 2){
      if(v ==2){
        solenoid.set(endDirection);
      }
      else{
        solenoid.set(endDirection);
        solenoid2.set(direction);
      }
    }
  }
}
