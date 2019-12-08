
package frc.commands;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class PIDDrive extends Command {

  private final DifferentialDrive drive;
  private final Joystick joystick;
  private final Encoder leftEncoder;
  private final Encoder rightEncoder;
  private PIDController pidLeft;
  private PIDController pidRight;
  double left = 0;
  double right = 0;
  double p = 0.1;
  double i = 0;
  double d = 0;

  public PIDDrive(
    DifferentialDrive drive,
    Joystick joystick,
    Encoder leftEncoder,
    Encoder rightEncoder
    ) {
      this.drive=drive;
      this.joystick=joystick;
      this.leftEncoder=leftEncoder;
      this.rightEncoder=rightEncoder;

      leftEncoder.setPIDSourceType(PIDSourceType.kRate);
      pidLeft = new PIDController(p, i, d, 
      leftEncoder, 
      (out) -> {left = out;});
      
      rightEncoder.setPIDSourceType(PIDSourceType.kRate);
      pidRight = new PIDController(p, i, d, 
      rightEncoder, 
      (out) -> {right = out;});
    }

  @Override
  protected void initialize() {
    leftEncoder.reset();
    pidLeft.setContinuous(false);
    pidLeft.setAbsoluteTolerance(50);
    pidLeft.setInputRange(-100, 100); //esoteric number, we will measure our actual input range using SmartDash
    pidLeft.setOutputRange(-1, 1);
    pidLeft.enable();

    rightEncoder.reset();  
    pidRight.setContinuous(false);
    pidRight.setAbsoluteTolerance(50);
    pidRight.setInputRange(-100, 100); //esoteric number, we will measure our actual input range using SmartDash
    pidRight.setOutputRange(-1, 1);
    pidRight.enable();
  }

  @Override
  protected void execute() {
    double leftSetpoint;
    double rightSetpoint;
    double xSpeed = joystick.getY();
    double zRotation = joystick.getTwist();
    double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);
    if (xSpeed >= 0.0) {
      // joystick is pushed forward
      if (zRotation >= 0.0) {
        leftSetpoint = maxInput;
        rightSetpoint = xSpeed - zRotation;
      } else {
        leftSetpoint = xSpeed + zRotation;
        rightSetpoint = maxInput;
      }
    } else {
      // joystick is pulled back
      if (zRotation >= 0.0) {
        leftSetpoint = xSpeed + zRotation;
        rightSetpoint = maxInput;
      } else {
        leftSetpoint = maxInput;
        rightSetpoint = xSpeed - zRotation;
      }
    }

    pidLeft.setSetpoint(leftSetpoint*100);  //esoteric number, same as max input
    pidRight.setSetpoint(rightSetpoint*100); //esoteric number, should max out as same as max input

    drive.tankDrive(left, right, false);
    
    SmartDashboard.putNumber("pidLeft Error", pidLeft.getError());
    SmartDashboard.putNumber("pidRight Error", pidRight.getError());
    SmartDashboard.putNumber("pidLeft Setpoint", pidLeft.getSetpoint());
    SmartDashboard.putNumber("pidRight Setpoint", pidRight.getSetpoint());
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    drive.stopMotor();
    pidRight.reset();
    pidLeft.reset();
    pidRight.disable();
    pidLeft.disable();
  }

  @Override
  protected void interrupted() {
    end();
  }
}