/*package frc.subsystem;

import frc.util.subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;
import frc.util.pid;
import frc.robot.constants;
import frc.subsystem.controller;

public class cargointake {
    private static cargointake instance = new cargointake();
    private pid mCargoPID;
    private DigitalInput mBump;
    private Encoder mEncoder; 
    private Talon mActuator;
    private Talon mRoller; 
    private Timer mIntakeTimer;
    private int kStowedHeight = constants.CARGO_STOWED_HEIGHT; 
    private int kIntakeHeight = constants.CARGO_INTAKE_HEIGHT;
    private controller c;

    public cargointake(){
        intakePID = new pid(0,0,0);
        mBump = new DigitalInput(constants.CARGO_INTAKE_DOWN);
        mEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
        mActuator = new Talon(constants.CARGO_INTAKE_ACTUATOR);
        mRoller = new Talon(constants.CARGO_INTAKE_ROLLER);
        mIntakeTimer = new Timer();
        mIntakeTimer.start();

        c = controller.getInstance();
    }

    public static cargointake getInstance(){ return instance; }

    public boolean isDeployed() {
        return mBump.get();
    }

    public boolean isStowed() {
        if(mEncoder.get() == 90) {
            return true;
        }
        else {
            return false;
        }
    }

    private void closedLoopControl (int wantedHeight){
        mActuator.set(intakePID.returnOutput(mEncoder.getRaw(), wantedHeight));
    }

    public void deploy(){
        if(mBump.get()){
            mActuator.set(0);
            mEncoder.reset();
        }
        else {
            closedLoopControl(kIntakeHeight);
        }
    }

    public void stow(){
        closedLoopControl(kStowedHeight);
    }

    public void run(double speed){
        mRoller.set(speed);
    }

    public void zeroAll(){
        mActuator.set(0);
        mRoller.set(0);
        mEncoder.reset();
    }

<<<<<<< HEAD
    public void outputToSmartDashboard(){}
}*/
=======
    public void outputToSmartDashboard() {
        SmartDashboard.putBoolean("isDeployed: ", isDeployed());
        SmartDashboard.putBoolean("isStowed: ", isStowed());
        SmartDashboard.putBoolean("mBump: ", mBump.get());
    }
}
>>>>>>> bba17b8a28bfd9875155690486b4dc66aca4f5fc
