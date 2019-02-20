package frc.subsystem; 

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class crossbow {
    private static crossbow instance = new crossbow();

    private DoubleSolenoid dSol;
    private Solenoid first; 
    private Solenoid second; 
    private DoubleSolenoid dSol2;

    public crossbow() {
        //first = new Solenoid(0, 3);
        //second = new Solenoid(1, 4);
       dSol = new DoubleSolenoid(1, 3, 4);
       dSol2 = new DoubleSolenoid(1, 2, 5);
    }

    public static crossbow getInstance() {
        return instance;
    }

    public void set(){
        //first.set(false);
        //second.set(true);
        dSol.set(DoubleSolenoid.Value.kForward);
        dSol2.set(DoubleSolenoid.Value.kReverse);
    }

    public void draw(){
        dSol.set(DoubleSolenoid.Value.kForward);
        dSol2.set(DoubleSolenoid.Value.kForward);
    }

    public void shoot(){
        dSol.set(DoubleSolenoid.Value.kReverse);
        dSol2.set(DoubleSolenoid.Value.kReverse);
    }

    // public void in() {
    //     dSol.set(DoubleSolenoid.Value.kForward);
    //     dSol2.set(DoubleSolenoid.Value.kForward);
    // }

    // public void out() {
    //     dSol.set(DoubleSolenoid.Value.kReverse);
    // }
    
    // public void launch() {
    //     dSol.set(DoubleSolenoid.Value.kForward);
    //     dSol2.set(DoubleSolenoid.Value.kForward);
    // }

    // public void neutral() {
    //     dSol.set(DoubleSolenoid.Value.kOff);
    //     dSol2.set(DoubleSolenoid.Value.kOff);
    // }
}
