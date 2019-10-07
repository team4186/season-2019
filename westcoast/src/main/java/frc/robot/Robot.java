package frc.robot;

import edu.wpi.first.wpilibj.SpeedController;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Encoder;
import frc.motorFactory.*;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.commands.*;
import edu.wpi.first.cameraserver.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;

public class Robot extends TimedRobot {
  MotorFactory hybridFactory = new MotorFactoryHybrid();

  //Drive system
  private final SpeedController leftMain = hybridFactory.create(5, 4, 6);
  private final SpeedController rightMain = hybridFactory.create(2, 1, 3);
  private final DifferentialDrive drive = new DifferentialDrive(leftMain, rightMain);

  //Subsystem Motors
  //private final WPI_VictorSPX winchMotor = new WPI_VictorSPX(7);
  //private final WPI_TalonSRX elevatorMotor = new WPI_TalonSRX(8);
  //private final WPI_TalonSRX gantryMotor = new WPI_TalonSRX(9);

  //Servos
  private final Servo servo = new Servo(0);

  //Input
  private final Joystick joystick = new Joystick(0);
  private final JoystickButton buttonA = new JoystickButton(joystick, 3); //Ramp ascend (hold)
  private final JoystickButton buttonB = new JoystickButton(joystick, 4); //Ramp descend (hold)
  //private final JoystickButton buttonC = new JoystickButton(joystick, 5); //Deploy ramp (hold)
  //private final JoystickButton buttonE = new JoystickButton(joystick, 8); //Servo align (toggle)
  private final JoystickButton topTrigger = new JoystickButton(joystick, 1); //Tongue (toggle)
  private final JoystickButton bottomTrigger = new JoystickButton(joystick, 6); //Hatch push (hold)
  private final JoystickButton fireButton = new JoystickButton(joystick, 2); //Tongue + hatch (toggle)
  //private final JoystickButton dpadUp  = new JoystickButton(joystick, 20); //Elevator up (hold)
  //private final JoystickButton dpadDown  = new JoystickButton(joystick, 21); //Elevator down (hold)
  //private final JoystickButton buttonD = new JoystickButton(joystick, 7); //Level two pistons (toggle)

  //Sensors
  //private final Encoder leftDriveEncoder = new Encoder(0, 1);
  //private final Encoder rightDriveEncoder = new Encoder(2, 3);
  //private final DigitalInput elevatorTop = new DigitalInput(4);
  //private final DigitalInput elevatorBottom = new DigitalInput(5);
  //private final DigitalInput gantryLeft = new DigitalInput(6);
  //private final DigitalInput gantryRight = new DigitalInput(7);

  //Auxiliary Objects
  public DirectionRef absAngle = new DirectionRef();

  //Pneumatics
  private Compressor compressor = new Compressor(10);
  private DoubleSolenoid flipperSolenoid = new DoubleSolenoid(10, 0, 1);
  // private DoubleSolenoid pusherSolenoid = new DoubleSolenoid(10, 2, 3);
  //private DoubleSolenoid wedgeSolenoid = new DoubleSolenoid(10, 4, 5);
  //private Solenoid rampSolenoid = new Solenoid(11, 0);
  //private DoubleSolenoid levelTwoSolenoid = new DoubleSolenoid(11, 5, 4);
  private Solenoid frontFoot = new Solenoid(12, 5);
  private Solenoid rearFeet = new Solenoid(12, 4);

  //Commands
  TeleopDrive teleopDrive = new TeleopDrive(drive, joystick);
  //EncoderArcade encoderArcade = new EncoderArcade(leftMain, rightMain, rightDriveEncoder, leftDriveEncoder, 0.0001, 0.0, 0.0); //encoders are switched
  //AlignHatch alignHatch = new AlignHatch(encoderArcade);
  //EncoderDistance encoderDistance = new EncoderDistance(rightDriveEncoder, leftDriveEncoder, leftMain, rightMain, 1.0);
  ActuateDoubleSolenoid deployPistons = new ActuateDoubleSolenoid(pusherSolenoid, DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kReverse);
  //ActuateSingleSolenoid rampPistons = new ActuateSingleSolenoid(rampSolenoid);
  // SetMotor winchDown = new SetMotor(winchMotor, -1.0);
  // SetMotor winchUp = new SetMotor(winchMotor, 1.0);
  //Command flipperCommandAuto = new ActuateDoubleSolenoid(flipperSolenoid, Value.kReverse, Value.kForward);
  //Command flipperCommandTeleop = new ActuateDoubleSolenoid(flipperSolenoid, Value.kReverse, Value.kForward);
  //Command ejectFlipperAuto = new ActuateDoubleSolenoid(flipperSolenoid, DoubleSolenoid.Value.kReverse, DoubleSolenoid.Value.kForward);
  //Command ejectFlipperTeleop = new ActuateDoubleSolenoid(flipperSolenoid, DoubleSolenoid.Value.kReverse, DoubleSolenoid.Value.kForward);


  /*Command deployHatch() {
    CommandGroup command = new CommandGroup();
    command.addParallel(new ActuateDoubleSolenoid(flipperSolenoid, DoubleSolenoid.Value.kReverse, DoubleSolenoid.Value.kForward));
    command.addParallel(new ActuateDoubleSolenoid(pusherSolenoid, DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kReverse));

    return command;
  }*/

  @Override
  public void robotInit() {
    joystick.setTwistChannel(5);

    drive.setSafetyEnabled(false);

    //leftDriveEncoder.setDistancePerPulse(1);
		//rightDriveEncoder.setDistancePerPulse(1);
    //leftDriveEncoder.setReverseDirection(true);

    // CameraServer.getInstance().startAutomaticCapture(0);
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
    
    teleopPeriodic();
  }

  @Override
  public void teleopInit() {
    teleopDrive.cancel();

    compressor.setClosedLoopControl(true);

    //buttonA.toggleWhenPressed(new ActuateDoubleSolenoid(actuator, DoubleSolenoid.Value.kReverse, DoubleSolenoid.Value.kForward));
    //buttonB.toggleWhenPressed(new ActuateDoubleSolenoid(delivery, DoubleSolenoid.Value.kReverse, DoubleSolenoid.Value.kForward));

    CommandGroup combinedSolenoid = new CommandGroup();
    combinedSolenoid.addParallel(new ActuateDoubleSolenoid(flipperSolenoid, Value.kReverse, Value.kForward));
    combinedSolenoid.addParallel(new ActuateDoubleSolenoid(pusherSolenoid, DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kReverse));
    fireButton.toggleWhenPressed(combinedSolenoid);

    topTrigger.toggleWhenPressed(new ActuateDoubleSolenoid(flipperSolenoid, Value.kReverse, Value.kForward));
    bottomTrigger.toggleWhenPressed(new ActuateDoubleSolenoid(pusherSolenoid, DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kReverse));

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

    servo.set(0.5*(joystick.getRawAxis(4) + 1));
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
    servo.set(0.5*(joystick.getRawAxis(4) + 1));
  }
}
