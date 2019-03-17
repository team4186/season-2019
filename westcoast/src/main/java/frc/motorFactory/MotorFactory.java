package frc.motorFactory;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedController;

public interface MotorFactory {
    SpeedController create(int channelMain, int channel1, int channel2);

    //public void currentLimitTalon(WPI_TalonSRX talon);
}