package frc.subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.constants;

//CURRENTLY NOT IN USE

public class claw {
    private static claw instance = new claw(constants.CLAW_EXTENDER_FRONT, 
                                            constants.CLAW_EXTENDER_BACK,
                                            constants.CLAW_PINCHER_FRONT, 
                                            constants.CLAW_PINCHER_BACK);
    static DoubleSolenoid pincher;
    static DoubleSolenoid extender; 
    
    public claw(int pincherFront, int pincherBack, int extenderFront, int extenderBack){
        pincher = new DoubleSolenoid(1, pincherFront, pincherBack);
        extender = new DoubleSolenoid(extenderFront, extenderBack); 
    }

    public static claw getInstance(){
        return instance; 
    }

    public void deploy() {
        extender.set(DoubleSolenoid.Value.kForward);
    }

    public void stow() {
        extender.set(DoubleSolenoid.Value.kReverse);
    }
    public void intake() {
        pincher.set(DoubleSolenoid.Value.kOff);
    }
    public void clamp() {
        pincher.set(DoubleSolenoid.Value.kReverse);
    }

    public void release() {
        pincher.set(DoubleSolenoid.Value.kForward);
    }
}