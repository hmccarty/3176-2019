package frc.subsystem;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class hatchintake {
    
    private static hatchintake instance = new hatchintake();
    private DigitalInput isDown;
    private DigitalInput isUp;
    private DigitalInput mSensor;
    private Talon mRoller;
    private DoubleSolenoid mSol;
    private controller c;

    
    public static hatchintake getInstance() {
        return instance;
    }
    
    public hatchintake() {
        isDown = new DigitalInput(constants.HATCH_INTAKE_DOWN);
        isUp = new DigitalInput(constants.HATCH_INTAKE_UP);
        mSensor = new DigitalInput(constants.HATCH_IRSENSOR);
        mRoller = new Talon(constants.HATCH_INTAKE_ROLLER);
        mSol = new DoubleSolenoid(0, 1);
        c = controller.getInstance();
    }

    public boolean isDeployed() {
        return isDown.get();
    }

    public boolean isStowed() {
        return isUp.get();
    }
    
    public boolean getSensor() {
        return mSensor.get();
    }

    public void deploy() {
        if (isDown.get()) {
            mSol.set(DoubleSolenoid.Value.kReverse);
        }
        else {
            mSol.set(DoubleSolenoid.Value.kOff);
        }
    }

    public void stow() {
        if (isUp.get()) {
            mSol.set(DoubleSolenoid.Value.kForward);
        }
        else {
            mSol.set(DoubleSolenoid.Value.kOff);
        }
    }

    public void run(double speed) {
        if (mSensor.get()) {
            mRoller.set(0);
        }
        else {
            mRoller.set(speed);
        }
    }

    public void zeroAll() {
        mSol.set(DoubleSolenoid.Value.kOff);
        mRoller.set(0);
    }

    public void outputToSmartDashboard() {
        SmartDashboard.putBoolean("isDeployed: ", isDeployed());
        SmartDashboard.putBoolean("isStowed: ", isStowed());
        SmartDashboard.putBoolean("getSensor: ", getSensor());
    }
}