package frc.subsystem; 

import frc.util.subsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class crossbow {
    private static crossbow instance = new crossbow();

    private DoubleSolenoid dSol;
    private DoubleSolenoid dSol2;

    public crossbow() {
        dSol = new DoubleSolenoid(0, 1);
        dSol2 = new DoubleSolenoid(2, 3);
    }

    public void in() {
        dSol.set(DoubleSolenoid.Value.kForward);
        dSol2.set(DoubleSolenoid.Value.kForward);
    }

    public void out() {
        dSol.set(DoubleSolenoid.Value.kReverse);
        dSol2.set(DoubleSolenoid.Value.kReverse);
    }

    public void off() {
        dSol.set(DoubleSolenoid.Value.kOff);
        dSol2.set(DoubleSolenoid.Value.kOff);
    }
    
    public static crossbow getInstance(){
        return instance; 
    }
}