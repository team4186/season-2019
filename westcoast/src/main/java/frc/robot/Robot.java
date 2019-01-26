package frc.robot;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Encoder;
import frc.motorFactory.*;
import com.kauailabs.navx.frc.*;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.commands.*;
import frc.robot.ArcadeMode.Result;
import edu.wpi.first.wpilibj.CameraServer;

public class Robot extends TimedRobot {
 
  MotorFactory hybridFactory = new MotorFactoryHybrid();

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //Drive system
  /*
  Removed for talon-victor-victor setup
  private final SpeedController leftMain = motorFactory.create(5, 6, 7);
  private final SpeedController rightMain = motorFactory.create(2, 3, 4);
  */
  private final SpeedController leftMain = hybridFactory.create(7, 8, 9);
  private final SpeedController rightMain = hybridFactory.create(1, 2, 3);
   
  private final DifferentialDrive drive = new DifferentialDrive(leftMain, rightMain);

  //Input
  private final Joystick joystick = new Joystick(0);

  //Sensors
  private final Encoder leftDriveEncoder = new Encoder(4, 5);
  private final Encoder rightDriveEncoder = new Encoder(6, 7);

  //Auxiliary Objects
  private ArcadeMode arcadeMode = new ArcadeMode();
	public DirectionRef absAngle = new DirectionRef();

  //Commands
  CommandGroup teleop = new CommandGroup();
  CommandGroup autonomous = new CommandGroup();
  CommandGroup test = new CommandGroup();
  TeleopDrive teleopDrive = new TeleopDrive(drive, joystick);
  EncoderArcade encoderArcade = new EncoderArcade(leftMain, rightMain, leftDriveEncoder, rightDriveEncoder, 0.0001, 0.0, 0.0);

  @Override
  public void robotInit() {

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    joystick.setTwistChannel(5);

    drive.setSafetyEnabled(false);

    leftDriveEncoder.setDistancePerPulse(1);
		rightDriveEncoder.setDistancePerPulse(1);
    leftDriveEncoder.setReverseDirection(true);

    teleop.addParallel(teleopDrive);

    CameraServer.getInstance().startAutomaticCapture();

  }

  @Override
  public void robotPeriodic() {

  }

  @Override
  public void autonomousInit() {

    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    autonomous.start();

  }

  @Override
  public void autonomousPeriodic() {

    /*switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }*/

    Scheduler.getInstance().run();

  }

  @Override
  public void teleopInit() {

    teleop.start();

  }

  @Override
  public void teleopPeriodic() {

    Scheduler.getInstance().run();

    Result arcadeResult = arcadeMode.drive(joystick.getY(), joystick.getTwist());

  }

  @Override
  public void disabledInit() {

    if(autonomous != null){
      
      autonomous.cancel();

    }

    teleop.cancel();

    if(test != null){

      test.cancel();

    }

  }

  @Override
  public void testInit(){

    test.start();

  }

  @Override
  public void testPeriodic() {

    Scheduler.getInstance().run();

  }
}
