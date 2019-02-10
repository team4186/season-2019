package frc.robot;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Encoder;
import frc.motorFactory.*;
import frc.pidSources.CenterDisplacement;
import frc.pidSources.WallMidpoint;
import com.kauailabs.navx.frc.*;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.commands.*;
import frc.robot.ArcadeMode.Result;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.networktables.*;

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
  private final SpeedController leftMain = hybridFactory.create(5, 4, 6);
  private final SpeedController rightMain = hybridFactory.create(2, 1, 3);
   
  private final DifferentialDrive drive = new DifferentialDrive(leftMain, rightMain);

  //Input
  private final Joystick joystick = new Joystick(0);
  private final JoystickButton buttonA = new JoystickButton(joystick, 3);
  private final JoystickButton buttonB = new JoystickButton(joystick, 4);

  //Sensors
  private final Encoder leftDriveEncoder = new Encoder(4, 5);
  private final Encoder rightDriveEncoder = new Encoder(6, 7);

  //Auxiliary Objects
  private ArcadeMode arcadeMode = new ArcadeMode();
  public DirectionRef absAngle = new DirectionRef();
  private Compressor compressor = new Compressor(10);
  private DoubleSolenoid actuator = new DoubleSolenoid(10, 0, 1);

  //Commands
  CommandGroup teleop = new CommandGroup();
  CommandGroup autonomous = new CommandGroup();
  CommandGroup test = new CommandGroup();
  TeleopDrive teleopDrive = new TeleopDrive(drive, joystick);
  EncoderArcade encoderArcade = new EncoderArcade(leftMain, rightMain, rightDriveEncoder, leftDriveEncoder, 0.0001, 0.0, 0.0); //encoders are switched
  AlignHatch alignHatch = new AlignHatch(encoderArcade);

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
    autonomous.addSequential(alignHatch);
    //test.addSequential(alignHatch);

    CameraServer.getInstance().startAutomaticCapture(0);
  }

  @Override
  public void robotPeriodic() {

  }

  @Override
  public void autonomousInit() {

    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    compressor.setClosedLoopControl(false);

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

    compressor.setClosedLoopControl(true); //set to true 

    buttonA.toggleWhenPressed(new ActuateDoubleSolenoid(actuator, DoubleSolenoid.Value.kReverse, DoubleSolenoid.Value.kForward));

    teleop.start();

  }

  @Override
  public void teleopPeriodic() {

    Scheduler.getInstance().run();

    //Result arcadeResult = arcadeMode.drive(joystick.getY(), joystick.getTwist());

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

  NetworkTableEntry angleArray;
  NetworkTableEntry lengthArray;
  NetworkTableEntry centerXArray;
  NetworkTableEntry centerYArray;

  static final double[] EMPTY = {};

  //AlignHatch align = new AlignHatch();

  @Override
  public void testInit(){

    test.start();
    //centerDisplacement = new CenterDisplacement();
    /*NetworkTable referenceLines = NetworkTableInstance
      .getDefault()
      .getTable("GRIP/referenceLines");
    
    angleArray = referenceLines.getEntry("angle");
    
    lengthArray = referenceLines.getEntry("length");

    SmartDashboard.putData(align);*/
  }

  @Override
  public void testPeriodic() {

    /*double[] angles = angleArray.getDoubleArray(EMPTY);
    double[] lengths = lengthArray.getDoubleArray(EMPTY);


    double weightedMean = 0;
    double denom = 0;

    for (int i = 0; i < Math.min(lengths.length, angles.length); ++i) {

      weightedMean += ((angles[i] + 180) % 180)*lengths[i];
      denom += lengths[i];

    }

    weightedMean /= denom;*/

    Scheduler.getInstance().run();

  }
}
