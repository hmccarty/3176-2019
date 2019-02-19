package frc.subsystem; 

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class crossbow {
    private static crossbow instance = new crossbow();

    private DoubleSolenoid dSol;
    private DoubleSolenoid dSol2;

    public crossbow() {
        dSol = new DoubleSolenoid(3, 4);
        dSol2 = new DoubleSolenoid(2, 5);
    }

    public static crossbow getInstance() {
        return instance;
    }

    public void in() {
        dSol.set(DoubleSolenoid.Value.kForward);
        dSol2.set(DoubleSolenoid.Value.kForward);
    }

    public void out() {
        dSol.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void launch() {
        dSol.set(DoubleSolenoid.Value.kForward);
        dSol2.set(DoubleSolenoid.Value.kForward);
    }

    public void neutral() {
        dSol.set(DoubleSolenoid.Value.kOff);
        dSol2.set(DoubleSolenoid.Value.kOff);
    }
}
