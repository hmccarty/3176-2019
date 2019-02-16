package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class NeoTesting {
    private static NeoTesting instance = new NeoTesting();
    private Joystick stick;
    private CANSparkMax spark;

    private NeoTesting() {
        stick = new Joystick(0);
        spark = new CANSparkMax(0, MotorType.kBrushless);
    }

    public static NeoTesting getInstance() {
        return instance;
    }

    public void run() {
        spark.set(stick.getY());
    }
}