package frc.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Servo;

public class SetServo extends Command {

    private final Servo servo;
    private final double target;

    public SetServo(
        Servo servo,
        double target) {

        this.servo = servo;
        this.target = target;
    }

    @Override
    protected void initialize() {

        servo.set(target);

    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return servo.get() == target;
    }

    @Override
    protected void end() {

    }
}