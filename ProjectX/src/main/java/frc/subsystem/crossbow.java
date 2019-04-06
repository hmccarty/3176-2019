package frc.subsystem; 

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.*;

public class crossbow {
    private static crossbow instance = new crossbow();

    private DoubleSolenoid  mOuterBow = new DoubleSolenoid(1, constants.CROSSBOW_OUTER_FRONT, 
                                                              constants.CROSSBOW_OUTER_BACK);
    private DoubleSolenoid  mInnerBow = new DoubleSolenoid(1, constants.CROSSBOW_INNER_FRONT, 
                                                              constants.CROSSBOW_INNER_BACK);

    private boolean shotStarted = false; 
    private Timer mShotTimer = new Timer(); 

    public crossbow() {
       mShotTimer.start(); 
    }

    public static crossbow getInstance() {
        return instance;
    }

    /**
     * Sets crossbow in position to intake hatch
     */
    public void set() {
         mOuterBow.set(DoubleSolenoid.Value.kReverse);
         mInnerBow.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Sets crossbow in position to hold hatch
     */
    public void draw() {
        mOuterBow.set(DoubleSolenoid.Value.kReverse);
        mInnerBow.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Fires hatch by releasing the back fins before the front fins
     */
    public void shootOut() {
        mOuterBow.set(DoubleSolenoid.Value.kForward);
    }

    public void shootIn() {
        mInnerBow.set(DoubleSolenoid.Value.kForward);
    }
}
