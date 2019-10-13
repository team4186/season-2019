package frc.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class ActuateTwoDoubleSolenoids extends Command {
  private final DoubleSolenoid solenoid;
  private final DoubleSolenoid solenoid2;
	private final Value direction;
  private final Value endDirection;
  private final Value direction2;
  private final Value endDirection2;
  public enum State {
    BOTH_OUT, FLAPPER_OUT, PUSHER_OUT, BOTH_IN
  }
  private State fire = State.BOTH_OUT;
  
  public ActuateTwoDoubleSolenoids(
    DoubleSolenoid solenoid,
    DoubleSolenoid solenoid2,
    Value direction,
    Value endDirection,
    Value direction2,
    Value endDirection2
    ) 
    {
      this.direction = direction;
      this.endDirection = endDirection;
      this.direction2 = direction2;
      this.endDirection2 = endDirection2;
      this.solenoid = solenoid;
      this.solenoid2 = solenoid2;
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
    if(solenoid.get() == direction){
      if(solenoid2.get() == direction2){
        fire = State.BOTH_OUT;
      }
      if(solenoid2.get() == endDirection2){
        fire = State.FLAPPER_OUT;
      }
    }
    else if (solenoid.get() == endDirection){
      if(solenoid2.get() == direction2){
        fire = State.PUSHER_OUT;
      }
      if(solenoid2.get() == endDirection2){
        fire = State.BOTH_IN;
      }
    }
    solenoid.set(direction);
    solenoid2.set(direction2);
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    if(fire.equals(State.FLAPPER_OUT)){
      solenoid.set(direction);
      solenoid2.set(endDirection2);
    }
    else if(fire.equals(State.PUSHER_OUT)){
        solenoid.set(endDirection);
        solenoid2.set(direction2);
    }
    else if(fire.equals(State.BOTH_IN)){
        solenoid.set(endDirection);
        solenoid2.set(endDirection2);
    }
  }

  @Override
  protected void interrupted() {
    end();
  }
}
