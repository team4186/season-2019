
package frc.commands;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class PIDDrive extends Command {

  private final DifferentialDrive drive;
  private final Joystick joystick;
  private final Encoder leftEncoder;
  private final Encoder rightEncoder;
  private PIDController pidLeft;
  private PIDController pidRight;
  double left = 0;
  double right = 0;
  double P = 0.5;
  double I = 0;
  double D = 0.6;

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

      pidLeft = new PIDController(P, I, D, 
      new PIDSource(){
        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
        }
        @Override
        public double pidGet() {
          return leftEncoder.getRate();
        }
        @Override
        public PIDSourceType getPIDSourceType() {
          return PIDSourceType.kRate;
        }
      }, 
      (out) -> {left = out;});
      
      pidRight = new PIDController(P, I, D, 
      new PIDSource(){
        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
        }
        @Override
        public double pidGet() {
          return rightEncoder.getRate();
        }
        @Override
        public PIDSourceType getPIDSourceType() {
          return PIDSourceType.kRate;
        }
      }, 
      (out) -> {right = out;});
    }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    leftEncoder.reset();
    pidLeft.setContinuous(false);
    pidLeft.setAbsoluteTolerance(50);
    pidLeft.setInputRange(-15000, 15000);
    pidLeft.setOutputRange(-1, 1);
    pidLeft.enable();

    rightEncoder.reset();
    pidRight.setContinuous(false);
    pidRight.setAbsoluteTolerance(50);
    pidRight.setInputRange(-15000, 15000);
    pidRight.setOutputRange(-1, 1);
    pidRight.enable();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double leftSetpoint;
    double rightSetpoint;
    double xSpeed = joystick.getY();
    double zRotation = joystick.getTwist();
    double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);
    if (xSpeed >= 0.0) {
      // First quadrant, else second quadrant
      if (zRotation >= 0.0) {
        leftSetpoint = maxInput;
        rightSetpoint = xSpeed - zRotation;
      } else {
        leftSetpoint = xSpeed + zRotation;
        rightSetpoint = maxInput;
      }
    } else {
      // Third quadrant, else fourth quadrant
      if (zRotation >= 0.0) {
        leftSetpoint = xSpeed + zRotation;
        rightSetpoint = maxInput;
      } else {
        leftSetpoint = maxInput;
        rightSetpoint = xSpeed - zRotation;
      }
    }

    pidLeft.setSetpoint(leftSetpoint*15000);
    pidRight.setSetpoint(rightSetpoint*15000);

    drive.tankDrive(left, right, false);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    drive.stopMotor();
    pidLeft.disable();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}