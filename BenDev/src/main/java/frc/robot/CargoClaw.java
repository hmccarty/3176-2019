package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class CargoClaw {
    private static CargoClaw instance = new CargoClaw(2,3);
    Value dsolval;
    static DoubleSolenoid dsol;
    Sensor clawSensor = new Sensor();
    
    public CargoClaw(int sol1, int sol2){
        dsol = new DoubleSolenoid(sol1,sol2);
    }

    public static CargoClaw getInstance(){
        return instance; 
    }
    public void close() {
        dsol.set(DoubleSolenoid.Value.kForward);
    }
    public void open() {
        dsol.set(DoubleSolenoid.Value.kReverse);
    }
    public void off() {
        dsol.set(DoubleSolenoid.Value.kOff);
    }

    public void testClawSideTransfer(){
        if(dsol.get() != DoubleSolenoid.Value.kOff) {
            close();
        }
        off();
        if(clawSensor.getValue() == 1){
            close();
        }
    }
}