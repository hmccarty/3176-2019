package frc.subsystem; 

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.*;

public class crossbow {
    private static crossbow instance = new crossbow();

    private DoubleSolenoid  mOuterBow;
    private DoubleSolenoid  mInnerBow;

    private boolean cShotStarted = false; 
    private Timer mShotTimer; 

    private static constants c = new constants();

    public crossbow() {
        mOuterBow = new DoubleSolenoid(1, c.CROSSBOW_OUTER_FRONT, c.CROSSBOW_OUTER_BACK);
        mInnerBow = new DoubleSolenoid(1, c.CROSSBOW_INNER_FRONT, c.CROSSBOW_INNER_BACK);

       mShotTimer = new Timer();

       mShotTimer.start(); 
    }

    public static crossbow getInstance() {
        return instance;
    }

    /**
     * Sets crossbow in position to intake hatch
     */
    public void set(){
         mOuterBow.set(DoubleSolenoid.Value.kForward);
         mInnerBow.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Sets crossbow in position to hold hatch
     */
    public void draw(){
         mOuterBow.set(DoubleSolenoid.Value.kForward);
         mInnerBow.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Fires hatch by releasing the back fins before the front fins
     */
    public void shoot(){
        if(!cShotStarted){
            cShotStarted = true;
            mShotTimer.start();
             mInnerBow.set(DoubleSolenoid.Value.kReverse);
        }
        if(mShotTimer.get() > 1.0){
            cShotStarted = false; 
            mShotTimer.stop(); 
            mShotTimer.reset(); 
             mOuterBow.set(DoubleSolenoid.Value.kReverse);
        }
    }
}
