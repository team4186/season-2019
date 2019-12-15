package frc.robot;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Encoder;
import frc.motorFactory.*;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.commands.*;

public class Robot extends TimedRobot {

  //Drive system
  MotorFactory hybridFactory = new MotorFactoryHybrid();
  private final SpeedController leftMain = hybridFactory.create(2, 1, 3);
  private final SpeedController rightMain = hybridFactory.create(5, 4, 6);
  private final DifferentialDrive drive = new DifferentialDrive(leftMain, rightMain);

  //Input
  private final Joystick joystick = new Joystick(0);
  private final JoystickButton buttonA = new JoystickButton(joystick, 3); //Ramp ascend (hold)
  private final JoystickButton buttonB = new JoystickButton(joystick, 4); //Ramp descend (hold)
  private final JoystickButton topTrigger = new JoystickButton(joystick, 1); //Tongue (toggle)
  private final JoystickButton bottomTrigger = new JoystickButton(joystick, 6); //Hatch push (hold)
  private final JoystickButton fireButton = new JoystickButton(joystick, 2); //Tongue + hatch (toggle)

  //Sensors
  private final Encoder leftEncoder = new Encoder(0, 1);
  private final Encoder rightEncoder = new Encoder(3, 2);

  //Pneumatics
  private Compressor compressor = new Compressor(10);
  private DoubleSolenoid flipperSolenoid = new DoubleSolenoid(10, 0, 1);
  private DoubleSolenoid pusherSolenoid = new DoubleSolenoid(10, 2, 3);
  private Solenoid frontFoot = new Solenoid(12, 5);
  private Solenoid rearFeet = new Solenoid(12, 4);

  //Commands
  //PIDDrive teleopDrive = new PIDDrive(drive, joystick, leftEncoder, rightEncoder);
  //TeleopDrive teleopDrive = new TeleopDrive(drive,joystick);
  EncoderTest teleopDrive = new EncoderTest(drive, joystick, leftEncoder, rightEncoder);
  
  @Override
  public void robotInit() {
    joystick.setTwistChannel(5);
  
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
    SmartDashboard.putBoolean("Flipper Deployed", flipperSolenoid.get() == Value.kForward ? true : false);
    SmartDashboard.putBoolean("Front Piston Deployed", frontFoot.get());
    SmartDashboard.putBoolean("Rear Piston Deployed", rearFeet.get());
    SmartDashboard.putNumber("leftEncoder value", leftEncoder.getRate());
    SmartDashboard.putNumber("rightEncoder value", rightEncoder.getRate());
    
    teleopPeriodic();
  }

  @Override
  public void teleopInit() {
    teleopDrive.cancel();

    compressor.setClosedLoopControl(false);

    fireButton.whenPressed(new ActuateTwoDoubleSolenoids(flipperSolenoid, pusherSolenoid, Value.kForward, Value.kReverse, Value.kReverse, Value.kForward));

    topTrigger.toggleWhenPressed(new ActuateDoubleSolenoid(flipperSolenoid, Value.kReverse, Value.kForward));
    bottomTrigger.toggleWhenPressed(new ActuateDoubleSolenoid(pusherSolenoid, Value.kForward, Value.kReverse));

    buttonA.toggleWhenPressed(new ActuateSingleSolenoid(frontFoot));
    buttonB.toggleWhenPressed(new ActuateSingleSolenoid(rearFeet));
    
    teleopDrive.start();
  }
 
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();

    SmartDashboard.putBoolean("Flipper Deployed", flipperSolenoid.get() == Value.kForward ? true : false);
    SmartDashboard.putBoolean("Front Piston Deployed", frontFoot.get());
    SmartDashboard.putBoolean("Rear Piston Deployed", rearFeet.get());
    SmartDashboard.putNumber("leftEncoder value", leftEncoder.getRate());
    SmartDashboard.putNumber("rightEncoder value", rightEncoder.getRate());
  }

  @Override
  public void disabledInit() {
    Scheduler.getInstance().removeAll();
  }

  @Override
  public void testInit() {
    compressor.setClosedLoopControl(false);
  }

  @Override
  public void testPeriodic() {

  }
}
