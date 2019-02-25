package frc.subsystem; 

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class crossbow {
    private static crossbow instance = new crossbow();

    private DoubleSolenoid outerBow;
    private DoubleSolenoid innerBow;

    public crossbow() {
       outerBow = new DoubleSolenoid(1, 3, 4);
       innerBow = new DoubleSolenoid(1, 2, 5);
    }

    public static crossbow getInstance() {
        return instance;
    }

    public void set(){
        outerBow.set(DoubleSolenoid.Value.kForward);
        innerBow.set(DoubleSolenoid.Value.kReverse);
    }

    public void draw(){
        outerBow.set(DoubleSolenoid.Value.kForward);
        innerBow.set(DoubleSolenoid.Value.kForward);
    }

    public void shoot(){
        outerBow.set(DoubleSolenoid.Value.kReverse);
        innerBow.set(DoubleSolenoid.Value.kReverse);
    }
}
