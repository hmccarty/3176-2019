package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class CargoClaw {
    private static CargoClaw instance = new CargoClaw(2,3);
    Value dsolval;
    static DoubleSolenoid dsol;
    
    public CargoClaw(int sol1, int sol2){
        dsol = new DoubleSolenoid(sol1,sol2);
    }

    public static CargoClaw getInstance(){
        return instance; 
    }
    public void forward() {
        dsol.set(DoubleSolenoid.Value.kForward);
    }
    public void reverse() {
        dsol.set(DoubleSolenoid.Value.kReverse);
    }
    public void off() {
        dsol.set(DoubleSolenoid.Value.kOff);
    }

}