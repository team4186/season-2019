package frc.robot;

import edu.wpi.first.wpilibj.SpeedController;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
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

  //Auxiliary Objects
  public DirectionRef absAngle = new DirectionRef();
  private final WPI_VictorSPX winchMotor = new WPI_VictorSPX(7);

  //Pneumatics
  private Compressor compressor = new Compressor(10);
  private Solenoid rampdeploy = new Solenoid(10, 0);

  //Commands
  //private PIDDriveAHRS teleopDrive = new PIDDriveAHRS(ahrs, joystick, drive);
  private TeleopDrive teleopDrive = new TeleopDrive(drive, joystick);

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
    teleopPeriodic();
  }

  @Override
  public void teleopInit() {
    teleopDrive.cancel();

    compressor.setClosedLoopControl(true);

    topTrigger.toggleWhenPressed(new ActuateSingleSolenoid(rampdeploy));

    buttonA.whileHeld(new SetMotor(winchMotor, 1.0));
    buttonB.whileHeld(new SetMotor(winchMotor, -1.0));
    
    teleopDrive.start();
  }
 
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {
    Scheduler.getInstance().removeAll();
  }

  @Override
  public void testInit() {
    
  }

  @Override
  public void testPeriodic() {

  }
}
