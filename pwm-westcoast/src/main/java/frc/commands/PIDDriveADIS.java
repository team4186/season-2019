package frc.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.analog.adis16448.frc.ADIS16448_IMU;

public class PIDDriveADIS extends Command {

  private final Joystick joystick;
  private final PIDController pid;
  double p = 0.1;
  double i = 0;
  double d = 0.5;

  public PIDDriveADIS(
      ADIS16448_IMU ahrs,
      Joystick joystick,
      DifferentialDrive drive
  ) {
    this.joystick=joystick;
    this.pid=new PIDController(p, i, d, 
    new PIDSource(){
    
      @Override
      public void setPIDSourceType(PIDSourceType pidSource) {
        
      }
    
      @Override
      public double pidGet() {
        return ahrs.getRateZ();
      }
    
      @Override
      public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kRate;
      }
    }, (turnFactor) -> {      
      drive.arcadeDrive(joystick.getY(), (turnFactor), false);
    });
    ahrs.setPIDSourceType(PIDSourceType.kRate);
    pid.setAbsoluteTolerance(20);
    pid.setContinuous(false);
    pid.setInputRange(-500, 500);
    pid.setOutputRange(-1, 1);
  }

  @Override
  protected void initialize() {
    pid.setEnabled(true);
  }

  @Override
  protected void execute() {
    pid.setSetpoint(joystick.getTwist() * 500);
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
