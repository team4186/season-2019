package frc.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class ActuateTwoDoubleSolenoids extends Command {
  private final DoubleSolenoid solenoid;
  private final DoubleSolenoid solenoid2;
	private final Value direction;
  private final Value endDirection;
  public enum state {
    bothout, flapperout, pusherout, bothin
  }
  private state fire = state.bothout;
  
  public ActuateTwoDoubleSolenoids(
    DoubleSolenoid solenoid,
    DoubleSolenoid solenoid2,
    Value direction,
    Value endDirection
    ) 
    {
      this.direction = direction;
      this.endDirection = endDirection;
      this.solenoid = solenoid;
      this.solenoid2 = solenoid2;
      
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
    if(solenoid.get() == direction){
      if(solenoid2.get() == endDirection){
        fire = state.bothout;
      }
      if(solenoid2.get() == direction){
        solenoid2.set(endDirection);
        fire = state.flapperout;
      }
    }
    else if (solenoid.get() == endDirection){
      solenoid.set(direction);
      if(solenoid2.get() == endDirection){
        fire = state.pusherout;
      }
      if(solenoid2.get() == direction){
        solenoid2.set(endDirection);
        fire = state.bothin;
      }
    }
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    if(fire.equals(state.flapperout)){
      solenoid2.set(direction);
    }
    else if(fire.equals(state.pusherout)){
        solenoid.set(endDirection);
    }
    else if(fire.equals(state.bothin)){
        solenoid.set(endDirection);
        solenoid2.set(direction);
    }
  }

  @Override
  protected void interrupted() {
    end();
  }
}
