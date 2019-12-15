package frc.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Command;
import frc.pidSources.FindTwoTargets;

public class HatchBackup extends Command {

    private EncoderArcade encoderArcade;
    private PIDController pid;
    private double power;

    public HatchBackup(
        EncoderArcade encoderArcade) {

        this.encoderArcade = encoderArcade;

        pid = new PIDController(0.0001, 0.0, 0.0, new FindTwoTargets(), new PIDOutput(){
        
            @Override
            public void pidWrite(double output) {
                power = output;
            }
        });
    }

    @Override
    protected void initialize() {
        pid.setAbsoluteTolerance(0.0);
		pid.setOutputRange(-0.5, 0.5);
		pid.setContinuous(false);
		pid.setSetpoint(2.0);
        pid.enable();
        
        encoderArcade.start();
    }

    @Override
    protected void execute() {
        encoderArcade.setDrive(15000*power, 15000*power);
    }

    @Override
    protected boolean isFinished() {
        return pid.onTarget();
    }

    @Override
    protected void end() {
        encoderArcade.cancel();
        pid.reset();
    }
}