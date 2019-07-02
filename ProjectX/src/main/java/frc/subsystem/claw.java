package frc.subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.constants;

public class claw {
    private static claw instance = new claw();
    DoubleSolenoid extender = new DoubleSolenoid(1, constants.CLAW_EXTENDER_FRONT, //On PCM 1
                                                    constants.CLAW_EXTENDER_BACK); 
    DoubleSolenoid pincher = new DoubleSolenoid(0,  constants.CLAW_PINCHER_FRONT,  //On PCM 0
                                                    constants.CLAW_PINCHER_BACK);

    public static claw getInstance() {
        return instance; 
    }

    public boolean isExtended(){
        if(extender.get() == DoubleSolenoid.Value.kForward){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Extends claw 
     */
    public void deploy() {
        extender.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Brings claw to center
     */
    public void stow() {
        extender.set(DoubleSolenoid.Value.kReverse);
    }
    
    /**
     * Releases claw arms, allowing cargo to slip in
     */
    public void intake() {
        pincher.set(DoubleSolenoid.Value.kOff);
    }

    /**
     * Clamps onto cargo, not allowing it to escape
     */
    public void clamp() {
        pincher.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Shoots cargo by pushing 
     */
    public void release() {
        pincher.set(DoubleSolenoid.Value.kForward);
    }
}