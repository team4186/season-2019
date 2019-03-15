/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends IterativeRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  Joystick joystick = new Joystick(0);
  JoystickButton buttonA = new JoystickButton(joystick, 3);
  JoystickButton buttonB = new JoystickButton(joystick, 4);
  JoystickButton topTrigger = new JoystickButton(joystick, 1);
  JoystickButton bottomTrigger = new JoystickButton(joystick,6);

  Solenoid flipperSolenoid = new Solenoid(10, 0);
  Solenoid rampSolenoid = new Solenoid(10, 1);
  Compressor compressor = new Compressor(10);

  WPI_TalonSRX leftMain = new WPI_TalonSRX(5);
  WPI_VictorSPX left1 = new WPI_VictorSPX(4);
  WPI_VictorSPX left2 = new WPI_VictorSPX(6);
  WPI_TalonSRX rightMain = new WPI_TalonSRX(2);
  WPI_VictorSPX right1 = new WPI_VictorSPX(1);
  WPI_VictorSPX right2 = new WPI_VictorSPX(3);
  WPI_VictorSPX winchMotor = new WPI_VictorSPX(7);

  DifferentialDrive drive = new DifferentialDrive(leftMain, rightMain);

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    left1.follow(leftMain);
    left2.follow(leftMain);
    right1.follow(rightMain);
    right2.follow(rightMain);

    joystick.setTwistChannel(5);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  @Override
  public void teleopInit(){
    compressor.setClosedLoopControl(true);
  }
  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    drive.arcadeDrive(joystick.getY(), joystick.getTwist());

    if(buttonA.get()){
      flipperSolenoid.set(true);
    }
    else {
      flipperSolenoid.set(false);
    }

    if(buttonB.get()){
      rampSolenoid.set(true);
    }
    else {
      rampSolenoid.set(false);
    }

    if(topTrigger.get()){
      winchMotor.set(1.0);
    }
    else if(bottomTrigger.get()){
      winchMotor.set(-1.0);
    }
    else{
      winchMotor.stopMotor();
    }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
