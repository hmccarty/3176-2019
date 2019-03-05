package frc.subsystem; 

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;

public class crossbow {
    private static crossbow instance = new crossbow();

    private DoubleSolenoid outerBow;
    private DoubleSolenoid innerBow;

    private boolean shotStarted = false; 
    private Timer shotTimer; 

    public crossbow() {
       outerBow = new DoubleSolenoid(1, 3, 4);
       innerBow = new DoubleSolenoid(1, 2, 5);

       shotTimer.start(); 
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
        if(!shotStarted){
            shotStarted = true;
            shotTimer.start();
            innerBow.set(DoubleSolenoid.Value.kReverse);
        }
        if(shotTimer.get() > 0.15){
            shotTimer.stop(); 
            shotTimer.reset(); 
            outerBow.set(DoubleSolenoid.Value.kReverse);
        }
    }
}
