package frc.robot;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;


public class CargoClaw {
    private static CargoClaw instance = new CargoClaw();
    Value dsolval;

    public void CargoClaw(int sol1, int sol2){
        dsol = new DoubleSolenoid(sol1, sol2);
    }
    public void forward(int val){
        dsolval = DoubleSolenoid.Value.kForward;
        dsol.set(dsolval);
    }
    public void reverse(int val){
        dsolval = DoubleSolenoid.Value.kReverse;
        dsol.set(dsolval);
    }
    public void off(int val){
        dsolval = DoubleSolenoid.Value.kOff;
        dsol.set(dsolval);
    }
}