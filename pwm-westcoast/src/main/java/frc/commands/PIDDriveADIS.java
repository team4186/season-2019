package frc.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.analog.adis16448.frc.ADIS16448_IMU;

public class PIDDriveADIS extends Command {
  private final Joystick joystick;
  private final ADIS16448_IMU adis;
  private final PIDController pid;
  double p = 0.05;
  double i = 0.02;
  double d = 0.03;
  double wait = 0;
  
  public PIDDriveADIS(
    DifferentialDrive drive,
    Joystick joystick,
    ADIS16448_IMU adis
  ) {
    this.joystick = joystick;
    this.adis = adis;

    this.pid = new PIDController(p, i, d, adis, (turnfactor) -> drive.arcadeDrive(joystick.getY(), turnfactor));
  }

  @Override
  protected void initialize() {
    adis.reset();
    pid.reset();
    pid.setContinuous(false);
    pid.setAbsoluteTolerance(0.5);
    pid.setInputRange(-6, 6);
    pid.setOutputRange(-1,1);
    pid.setPIDSourceType(PIDSourceType.kRate);
    pid.enable();
  }

  @Override
  protected void execute() {
    pid.setSetpoint(joystick.getTwist()*6);

    if(adis.getRateZ() >= 6){
      pid.reset();
    }
    else if (adis.getRateZ() <= -6){
      pid.reset();
    }
    else if (adis.getRateZ() == 0){
      wait = wait + 1;
      if (wait == 5){
        pid.reset();
        wait = -20;
      }
    }
    else{
      if(wait >= 0){
        wait = 0;
      }
    }
    if(wait <= 0){
      wait = wait + 1;
    }
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    pid.disable();
  }

  @Override
  protected void interrupted() {
    end();
  }
}
