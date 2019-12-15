
package frc.commands;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class EncoderTest extends Command {

  private final DifferentialDrive drive;
  private final Joystick joystick;
  private final Encoder leftEncoder;
  private final Encoder rightEncoder;
  private PIDController pidcontroller;
  double output;
  double p = 0.01;
  double i = 0;
  double d = 5000;

  public EncoderTest(
    DifferentialDrive drive,
    Joystick joystick,
    Encoder leftEncoder,
    Encoder rightEncoder
    ) {
      this.drive=drive;
      this.joystick=joystick;
      this.leftEncoder=leftEncoder;
      this.rightEncoder=rightEncoder;
     
      rightEncoder.setPIDSourceType(PIDSourceType.kRate);
      
      /*pidcontroller = new PIDController(p, i, d, 
      rightEncoder, 
      (out) -> output = out,);*/
      pidcontroller = new PIDController(p, i, d, rightEncoder, out -> output = -out, 0.02);
    }

  @Override
  protected void initialize() {
    leftEncoder.reset();
    rightEncoder.reset();  
    pidcontroller.setContinuous(false);
    pidcontroller.setAbsoluteTolerance(30);
    pidcontroller.setInputRange(-800, 800); //esoteric number, we will measure our actual input range using SmartDash
    pidcontroller.setOutputRange(-0.4, 0.4);
    pidcontroller.enable();
  }

  @Override
  protected void execute() {
    
    pidcontroller.setSetpoint(-joystick.getY()*250); //esoteric number, should max out as same as max input

    SmartDashboard.putNumber("Setpoint", pidcontroller.getSetpoint());
    SmartDashboard.putBoolean("This is going to be false, but indeed it should be true", pidcontroller.onTarget());
    SmartDashboard.putNumber("Output", output);
    SmartDashboard.putNumber("Error", pidcontroller.getError());
    
    drive.tankDrive(0, output);
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    drive.stopMotor();
    pidcontroller.disable();
    pidcontroller.reset();
  }

  @Override
  protected void interrupted() {
    end();
  }
}