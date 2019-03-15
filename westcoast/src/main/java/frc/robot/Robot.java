package frc.robot;

import edu.wpi.first.wpilibj.SpeedController;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
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
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.commands.*;
import edu.wpi.first.wpilibj.CameraServer;
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
  private final WPI_VictorSPX winchMotor = new WPI_VictorSPX(7);
  private final WPI_TalonSRX elevatorMotor = new WPI_TalonSRX(8);
  private final WPI_TalonSRX gantryMotor = new WPI_TalonSRX(9);

  //Servos
  private final Servo servo = new Servo(0);

  //Input
  private final Joystick joystick = new Joystick(0);
  private final JoystickButton buttonA = new JoystickButton(joystick, 3); //Ramp ascend (hold)
  private final JoystickButton buttonB = new JoystickButton(joystick, 4); //Ramp descend (hold)
  private final JoystickButton buttonC = new JoystickButton(joystick, 5); //Deploy ramp (hold)
  private final JoystickButton buttonE = new JoystickButton(joystick, 8); //Servo align (toggle)
  private final JoystickButton topTrigger = new JoystickButton(joystick, 1); //Tongue (toggle)
  private final JoystickButton bottomTrigger = new JoystickButton(joystick, 6); //Hatch push (hold)
  private final JoystickButton fireButton = new JoystickButton(joystick, 2); //Tongue + hatch (toggle)
  private final JoystickButton dpadUp  = new JoystickButton(joystick, 20); //Elevator up (hold)
  private final JoystickButton dpadDown  = new JoystickButton(joystick, 21); //Elevator down (hold)
  private final JoystickButton dpadLeft  = new JoystickButton(joystick, 22); //Gantry left (hold)
  private final JoystickButton dpadRight  = new JoystickButton(joystick, 23); //Gantry right (hold)
  //private final JoystickButton levelTwoUp = new JoystickButton(joystick, 9);
  private final JoystickButton levelTwoDown = new JoystickButton(joystick, 10); //Level two pistons (toggle)

  //Sensors
  private final Encoder leftDriveEncoder = new Encoder(4, 5);
  private final Encoder rightDriveEncoder = new Encoder(6, 7);
  private final DigitalInput elevatorTop = new DigitalInput(0);
  private final DigitalInput elevatorBottom = new DigitalInput(1);
  private final DigitalInput gantryLeft = new DigitalInput(2);
  private final DigitalInput gantryRight = new DigitalInput(3);

  //Auxiliary Objects
  public DirectionRef absAngle = new DirectionRef();

  //Pneumatics
  private Compressor compressor = new Compressor(10);
  private DoubleSolenoid flipperSolenoid = new DoubleSolenoid(10, 0, 1);
  private DoubleSolenoid pusherSolenoid = new DoubleSolenoid(10, 2, 3);
  //private DoubleSolenoid wedgeSolenoid = new DoubleSolenoid(10, 4, 5);
  private Solenoid rampSolenoid = new Solenoid(11, 0);
  private Solenoid levelTwoSolenoid = new Solenoid(11, 1);

  //Commands
  TeleopDrive teleopDrive = new TeleopDrive(drive, joystick);
  EncoderArcade encoderArcade = new EncoderArcade(leftMain, rightMain, rightDriveEncoder, leftDriveEncoder, 0.0001, 0.0, 0.0); //encoders are switched
  AlignHatch alignHatch = new AlignHatch(encoderArcade);
  EncoderDistance encoderDistance = new EncoderDistance(rightDriveEncoder, leftDriveEncoder, leftMain, rightMain, 1.0);
  ActuateDoubleSolenoid deployPistons = new ActuateDoubleSolenoid(pusherSolenoid, DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kReverse);
  ActuateSingleSolenoid rampPistons = new ActuateSingleSolenoid(rampSolenoid);
  SetMotor winchDown = new SetMotor(winchMotor, -1.0);
  SetMotor winchUp = new SetMotor(winchMotor, 1.0);

  Command deployHatch() {
    CommandGroup command = new CommandGroup();
    command.addParallel(new ActuateDoubleSolenoid(flipperSolenoid, DoubleSolenoid.Value.kReverse, DoubleSolenoid.Value.kForward));
    command.addParallel(new ActuateDoubleSolenoid(pusherSolenoid, DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kReverse));

    return command;
  }

  Command alignForDelivery() {
    CommandGroup command = new CommandGroup();
    command.addSequential(new HatchBackup(encoderArcade));
    command.addSequential(new AlignHatchServo(servo));

    return command;
  }

  //Command Groups
  //CommandGroup teleop = new CommandGroup();
  //CommandGroup autonomous = new CommandGroup();
  //CommandGroup test = new CommandGroup();
  CommandGroup teleop;
  CommandGroup autonomous;
  CommandGroup test;

  @Override
  public void robotInit() {
    joystick.setTwistChannel(5);

    drive.setSafetyEnabled(false);

    leftDriveEncoder.setDistancePerPulse(1);
		rightDriveEncoder.setDistancePerPulse(1);
    leftDriveEncoder.setReverseDirection(true);

    //CameraServer.getInstance().startAutomaticCapture(0);

    teleop.addParallel(teleopDrive);
  }

  @Override
  public void robotPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
    if(teleop != null){
      teleop.cancel();
    }

    if(test != null){
      test.cancel();
    }

    autonomous = new CommandGroup();

    compressor.setClosedLoopControl(true);

    levelTwoDown.toggleWhenPressed(new ActuateSingleSolenoid(levelTwoSolenoid));

    buttonE.toggleWhenPressed(new AlignHatchServo(servo));

    topTrigger.toggleWhenPressed(new ActuateDoubleSolenoid(flipperSolenoid, Value.kReverse, Value.kForward));
    
    bottomTrigger.whenPressed(deployPistons);
    bottomTrigger.whenReleased(new InstantCommand() {
      @Override
      protected void execute()
      {
        deployPistons.cancel();
      }
    });

    dpadUp.whileHeld(new MotorWithLimitSwitch(elevatorMotor, 0.5, elevatorTop, elevatorBottom));
    dpadDown.whileHeld(new MotorWithLimitSwitch(elevatorMotor, -0.5, elevatorTop, elevatorBottom));
    //dpadLeft.whileHeld(new MotorWithLimitSwitch(gantryMotor, 0.5, gantryLeft, gantryRight));
    //dpadRight.whileHeld(new MotorWithLimitSwitch(gantryMotor, -0.5, gantryLeft, gantryRight));
    dpadLeft.whileHeld(new DriveServo(servo, -1.0, -0.01));
    dpadRight.whileHeld(new DriveServo(servo, 1.0, 0.01));

    fireButton.toggleWhenPressed(deployHatch());

    buttonA.whenPressed(winchUp);
    buttonA.whenReleased(new InstantCommand() {
      @Override
      protected void execute() {
        winchUp.cancel();
      }
    });

    buttonB.whenPressed(winchDown);
    buttonB.whenReleased(new InstantCommand() {
      @Override
      protected void execute() {
        winchDown.cancel();
      }
    });

    buttonC.whenPressed(rampPistons);
    buttonC.whenReleased(new InstantCommand() {
      @Override
      protected void execute() {
        rampPistons.cancel();
      }
    });

    autonomous.start();
  }

  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    if(autonomous != null){
      teleop.cancel();
    }

    if(test != null){
      test.cancel();
    }

    teleop = new CommandGroup();

    compressor.setClosedLoopControl(true);

    levelTwoDown.toggleWhenPressed(new ActuateSingleSolenoid(levelTwoSolenoid));
    
    buttonE.toggleWhenPressed(new AlignHatchServo(servo));

    topTrigger.toggleWhenPressed(new ActuateDoubleSolenoid(flipperSolenoid, Value.kReverse, Value.kForward));
    
    bottomTrigger.whenPressed(deployPistons);
    bottomTrigger.whenReleased(new InstantCommand() {
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
    buttonA.whenReleased(new InstantCommand() {
      @Override
      protected void execute() {
        winchUp.cancel();
      }
    });

    buttonB.whenPressed(winchDown);
    buttonB.whenReleased(new InstantCommand() {
      @Override
      protected void execute() {
        winchDown.cancel();
      }
    });

    buttonC.whenPressed(rampPistons);
    buttonC.whenReleased(new InstantCommand() {
      @Override
      protected void execute() {
        rampPistons.cancel();
      }
    });

    teleop.start();
  }
 
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();

    //servo.set(joystick.getX());
  }

  @Override
  public void disabledInit() {
    Scheduler.getInstance().removeAll();

    if(autonomous != null) {
      autonomous.cancel();
    }

    if(teleop != null){
      teleop.cancel();
    }

    if(test != null) {
      test.cancel();
    }
  }

  @Override
  public void testInit() {
    if(teleop != null){
      teleop.cancel();
    }

    if(autonomous != null){
      test.cancel();
    }

    test = new CommandGroup();

    test.start();
  }

  @Override
  public void testPeriodic() {
    Scheduler.getInstance().run();
  }
}
