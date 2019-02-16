package frc.robot;

import edu.wpi.first.wpilibj.SpeedController;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
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
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.DigitalInput;

public class Robot extends TimedRobot {
 
  MotorFactory hybridFactory = new MotorFactoryHybrid();

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //Drive system
  private final SpeedController leftMain = hybridFactory.create(5, 4, 6);
  private final SpeedController rightMain = hybridFactory.create(2, 1, 3);
  private final DifferentialDrive drive = new DifferentialDrive(leftMain, rightMain);

  //Subsystem Motors
  private final WPI_VictorSPX winchMotor = new WPI_VictorSPX(7);
  private final WPI_TalonSRX elevatorMotor = new WPI_TalonSRX(8);
  private final WPI_TalonSRX gantryMotor = new WPI_TalonSRX(9);

  //Input
  private final Joystick joystick = new Joystick(0);
  private final JoystickButton buttonA = new JoystickButton(joystick, 3);
  private final JoystickButton buttonB = new JoystickButton(joystick, 4);
  private final JoystickButton buttonC = new JoystickButton(joystick, 5);
  private final JoystickButton topTrigger = new JoystickButton(joystick, 1);
  private final JoystickButton bottomTrigger = new JoystickButton(joystick, 6);
  private final JoystickButton fireButton = new JoystickButton(joystick, 2);
  private final JoystickButton dpadUp  = new JoystickButton(joystick, 20);
  private final JoystickButton dpadDown  = new JoystickButton(joystick, 21);
  private final JoystickButton dpadLeft  = new JoystickButton(joystick, 22);
  private final JoystickButton dpadRight  = new JoystickButton(joystick, 23);

  //Sensors
  private final Encoder leftDriveEncoder = new Encoder(4, 5);
  private final Encoder rightDriveEncoder = new Encoder(6, 7);
  private final AHRS navx = new AHRS(SPI.Port.kMXP);
  private final DigitalInput elevatorTop = new DigitalInput(0);
  private final DigitalInput elevatorBottom = new DigitalInput(1);
  private final DigitalInput gantryLeft = new DigitalInput(2);
  private final DigitalInput gantryRight = new DigitalInput(3);

  //Auxiliary Objects
  private ArcadeMode arcadeMode = new ArcadeMode();
  public DirectionRef absAngle = new DirectionRef();

  //Pneumatics
  private Compressor compressor = new Compressor(10);
  private DoubleSolenoid flipperSolenoid = new DoubleSolenoid(10, 0, 1);
  private DoubleSolenoid pusherSolenoid = new DoubleSolenoid(10, 2, 3);
  private DoubleSolenoid wedgeSolenoid = new DoubleSolenoid(10, 4, 5);

  //Commands
  TeleopDrive teleopDrive = new TeleopDrive(drive, joystick);
  EncoderArcade encoderArcade = new EncoderArcade(leftMain, rightMain, rightDriveEncoder, leftDriveEncoder, 0.0001, 0.0, 0.0); //encoders are switched
  AlignHatch alignHatch = new AlignHatch(encoderArcade);
  EncoderDistance encoderDistance = new EncoderDistance(rightDriveEncoder, leftDriveEncoder, leftMain, rightMain, 1.0);
  ActuateDoubleSolenoid deployPistons = new ActuateDoubleSolenoid(pusherSolenoid, DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kReverse);
  ActuateDoubleSolenoid wedgePistons = new ActuateDoubleSolenoid(wedgeSolenoid, DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kReverse);
  SetMotor winchDown = new SetMotor(winchMotor, -1.0);
  SetMotor winchUp = new SetMotor(winchMotor, 1.0);

  Command deployHatch()
  {
    CommandGroup command = new CommandGroup();
    command.addParallel(new ActuateDoubleSolenoid(flipperSolenoid, DoubleSolenoid.Value.kReverse, DoubleSolenoid.Value.kForward));
    command.addParallel(deployPistons);

    return command;
  }

  //Command Groups
  CommandGroup teleop = new CommandGroup();
  CommandGroup autonomous = new CommandGroup();
  CommandGroup test = new CommandGroup();
  CommandGroup robotMain = new CommandGroup();

  @Override
  public void robotInit() 
  {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    joystick.setTwistChannel(5);

    drive.setSafetyEnabled(false);

    leftDriveEncoder.setDistancePerPulse(1);
		rightDriveEncoder.setDistancePerPulse(1);
    leftDriveEncoder.setReverseDirection(true);

    CameraServer.getInstance().startAutomaticCapture(0);

    //Add Commands to robotMain sequence
    robotMain.addParallel(teleopDrive);

    topTrigger.toggleWhenPressed(new ActuateDoubleSolenoid(flipperSolenoid, Value.kReverse, Value.kForward));
    
    bottomTrigger.whenPressed(deployPistons);
    bottomTrigger.whenReleased(new InstantCommand()
    {
      @Override
      protected void execute()
      {
        deployPistons.cancel();
      }
    });

    dpadUp.whileHeld(new MotorWithLimitSwitch(elevatorMotor, 0.5, elevatorTop, elevatorBottom));
    dpadDown.whileHeld(new MotorWithLimitSwitch(elevatorMotor, -0.5, elevatorTop, elevatorBottom));
    dpadLeft.whileHeld(new MotorWithLimitSwitch(gantryMotor, 0.5, gantryLeft, gantryRight));
    dpadRight.whileHeld(new MotorWithLimitSwitch(gantryMotor, -0.5, gantryLeft, gantryRight));

    fireButton.toggleWhenPressed(deployHatch());

    buttonA.whenPressed(winchUp);
    buttonA.whenReleased(new InstantCommand()
    {
      @Override
      protected void execute()
      {
        winchUp.cancel();
      }
    });

    buttonB.whenPressed(winchDown);
    buttonB.whenReleased(new InstantCommand()
    {
      @Override
      protected void execute()
      {
        winchDown.cancel();
      }
    });

    buttonC.whenPressed(wedgePistons);
    buttonC.whenReleased(new InstantCommand()
    {
      @Override
      protected void execute()
      {
        wedgePistons.cancel();
      }
    });

    robotMain.start();
  }

  @Override
  public void robotPeriodic() 
  {
    Scheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() 
  {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    compressor.setClosedLoopControl(false);

    autonomous.start();
  }

  @Override
  public void autonomousPeriodic() 
  {
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
  public void teleopInit() 
  {
    compressor.setClosedLoopControl(true);

    buttonA.toggleWhenPressed(new ActuateDoubleSolenoid(flipperSolenoid, DoubleSolenoid.Value.kReverse, DoubleSolenoid.Value.kForward));
    buttonB.toggleWhenPressed(new ActuateDoubleSolenoid(pusherSolenoid, DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kReverse));

    teleop.start();
  }

  @Override
  public void teleopPeriodic() 
  {
    Scheduler.getInstance().run();
  }

  @Override
  public void disabledInit() 
  {
    if(autonomous != null)
    {
      autonomous.cancel();
    }

    teleop.cancel();

    if(test != null)
    {
      test.cancel();
    }

    Scheduler.getInstance().removeAll();
  }

  @Override
  public void testInit()
  {
    test.start();
  }

  @Override
  public void testPeriodic() 
  {
    Scheduler.getInstance().run();
  }
}
