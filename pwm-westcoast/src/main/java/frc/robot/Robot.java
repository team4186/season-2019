package frc.robot;

import com.analog.adis16448.frc.ADIS16448_IMU;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.commands.PIDDriveADIS;

public class Robot extends TimedRobot {

  //Input
  private final Joystick joystick = new Joystick(0);

  //Sensors
  private final ADIS16448_IMU adis = new ADIS16448_IMU();
  
  //Drivetrain
  private final Talon leftTalons = new Talon(0);
  private final Talon rightTalons = new Talon(1);
  private DifferentialDrive drive = new DifferentialDrive(leftTalons, rightTalons);

  //Commands
  PIDDriveADIS PIDdrive = new PIDDriveADIS(drive, joystick, adis);

  //Extra Actuators
  private final Talon actuator1 = new Talon(2);


  @Override
  public void robotInit() {
    joystick.setTwistChannel(5); //I'm not sure what the twist channel on the logitech is
    drive.setSafetyEnabled(false);
  }

  @Override
  public void robotPeriodic() {

  }

  @Override
  public void autonomousInit() {
    teleopInit();
  }

  @Override
  public void autonomousPeriodic() {
    teleopPeriodic();
  }

  @Override
  public void teleopInit() {
    PIDdrive.cancel();
    PIDdrive.start();
  }

  @Override
  public void teleopPeriodic() {
    drive.arcadeDrive(joystick.getZ(), joystick.getTwist());
  }

  @Override
  public void testInit() {
    
  }

  @Override
  public void testPeriodic() {

  }
}
