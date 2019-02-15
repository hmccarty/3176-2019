package frc.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class HatchIntake {
    
    private static HatchIntake instance = new HatchIntake();
    private DigitalInput isDown;
    private DigitalInput isUp;
    private DigitalInput sensor;
    private Talon roller;
    private Joystick stick;
    private DoubleSolenoid dSol;
    private Timer timer;
    //private controller c;

    
    public static HatchIntake getInstance() {
        return instance;
    }
    
    public HatchIntake() {
        isDown = new DigitalInput(constants.HATCH_INTAKE_DOWN);
        isUp = new DigitalInput(constants.HATCH_INTAKE_UP);
        sensor = new DigitalInput(constants.HATCH_IRSENSOR);
        roller = new Talon(constants.HATCH_INTAKE_ROLLER);
        stick = new Joystick(0);
        dSol = new DoubleSolenoid(1, 2);
        timer = new Timer();
        timer.start();

        //c = controller.getInstance();
    }

    public boolean getUp() {
        return isUp.get();
    }

    public boolean getDown() {
        return isDown.get();
    }
    
    public boolean getSensor() {
        return sensor.get();
    }

    public void deployIntake() {
        if (getDown()) {
            dSol.set(DoubleSolenoid.Value.kReverse);
            getDown();
        }
        else {
            dSol.set(DoubleSolenoid.Value.kOff);
            getDown();
        }
    }

    public void stowIntake() {
        if (getUp()) {
            dSol.set(DoubleSolenoid.Value.kForward);
            getUp();
        }
        else {
            dSol.set(DoubleSolenoid.Value.kOff);
            getUp();
        }
    }

    public void runIntake(double speed) {
        if (sensor.get()) {
            roller.set(0);
            getSensor();
        }
        else {
            roller.set(speed);
            getSensor();
        }
    }

    public void zeroAll() {
        dSol.set(DoubleSolenoid.Value.kOff);
        roller.set(0);
    }

    public void outputToSmartDashboard() {
        SmartDashboard.putBoolean("isDown value: ", getDown());
        SmartDashboard.putBoolean("isUp value: ", getUp());
        SmartDashboard.putBoolean("sensor value: ", getSensor());
    }
}