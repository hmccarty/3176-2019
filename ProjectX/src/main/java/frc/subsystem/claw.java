package frc.subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.constants;

public class claw {
    private static claw instance = new claw();
    DoubleSolenoid extender = new DoubleSolenoid(0, constants.CLAW_PINCHER_FRONT,  //On PCM 0
                                                    constants.CLAW_PINCHER_BACK); 
    DoubleSolenoid pincher = new DoubleSolenoid(1,  constants.CLAW_EXTENDER_FRONT, //On PCM 1
                                                    constants.CLAW_EXTENDER_BACK);

    public static claw getInstance() {
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