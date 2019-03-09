package frc.subsystem; 

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.*;

public class crossbow {
    private static crossbow instance = new crossbow();

    private DoubleSolenoid outerBow;
    private DoubleSolenoid innerBow;

    private boolean shotStarted = false; 
    private Timer shotTimer; 

    private static constants c = new constants();

    public crossbow() {
       outerBow = new DoubleSolenoid(1, c.CROSSBOW_OUTER_FRONT, c.CROSSBOW_OUTER_BACK);
       innerBow = new DoubleSolenoid(1, c.CROSSBOW_INNER_FRONT, c.CROSSBOW_INNER_BACK);

       shotTimer = new Timer();

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
            shotStarted = false; 
            shotTimer.stop(); 
            shotTimer.reset(); 
            outerBow.set(DoubleSolenoid.Value.kReverse);
        }
    }
}
