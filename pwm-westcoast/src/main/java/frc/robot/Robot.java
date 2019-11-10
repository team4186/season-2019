package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends TimedRobot {

  //Input
  private final Joystick joystick = new Joystick(0);
  
  //Drivetrain
  private final Talon leftTalons = new Talon(0);
  private final Talon rightTalons = new Talon(1);
  private DifferentialDrive drive = new DifferentialDrive(leftTalons, rightTalons);

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
