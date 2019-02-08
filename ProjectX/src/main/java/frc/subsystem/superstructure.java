package frc.subsystem; 

import frc.subsystem.cargointake;
import frc.util.loop;

public class superstructure {
    private static superstructure instance = new superstructure();
    
    private state mLastState; 
    private state mWantedState;
    private state mCurrentState;

    controller mController = controller.getInstance();
    crossbow mCrossBow = crossbow.getInstance();
    hatchintake mHatchIntake = hatchintake.getInstance();
    claw mClaw = claw.getInstance();
    cargointake mCargoIntake = cargointake.getInstance();
    elevator mElevator = elevator.getInstance(); 
    drivetrain mDrivetrain = drivetrain.getInstance(); 
    loopmanager mLoopMan = loopmanager.getInstance();

    public superstructure (){}

    public static superstructure getInstance(){
        return instance; 
    }

    public void setWantedState(state wantedState){}

    public void checkState(){
        if(mCurrentState != mWantedState){
            mCurrentState = mWantedState; 
        }
    }

    public void registerLoop(){
        mLoopMan.addLoop(new loop() {
            public void onStart(){
                mCurrentState = state.NEUTRAL;
            }
            public void onLoop(){
                switch(mCurrentState){
                    case INTAKE_C_ROLLER:
                        mCargoIntake.deploy();
                        mCargoIntake.run(-.2);
                        break;
                    case INTAKE_H_CB:
                    /*  mHatchIntake.stow();
                    *   mClaw.stow();
                    *   mCrossbow.set();
                    */
                        break;
                    case HOLD_H_CB:
                    /*  mCrossbow.draw();
                    */
                        break;
                    case INTAKE_C_CLAW:
                    /*  if(mLastState != mCurrentState){
                    *       mClaw.aim();
                    *       mClaw.prepare();
                    *   }
                    *   mClaw.clamp();
                    */
                        break;    
                    case INTAKE_H_G:
                    /*  mHatchIntake.deploy(); 
                    *   mHatchIntake.intake();
                    */
                        break;
                    case TRANSFER_CARGO:
                    /*  mCargoIntake.stow();
                    */
                        break;
                    case TRANSFER_HATCH:
                        //mCrossbow.set();
                        //mClaw.stow();
                        /*if(mHatchIntake.stowError() < 1000){
                        *   
                        *
                        */ 
                        break;
                    case DELIVER_CARGO:
                        break;
                    case DELIVER_HATCH:
                        break;
                    case OPENLOOP_HATCH:
                        
                        break;
                    case OPENLOOP_CARGO:
                        mCargoIntake.openLoop();
                        //
                        break;
                    case NEUTRAL:
                        mCargoIntake.stow();
                        //mClaw.stow();
                        //mHatchIntake.stow();
                        //mCrossbow.stow();
                        //mCrossbow.draw();
                        break;
                }
                mLastState = mCurrentState;
                checkState();
            }

            public void onStop(){}
        });
    }

    public enum state {
        INTAKE_C_ROLLER,
        INTAKE_C_CLAW,
        INTAKE_H_CB,
        HOLD_H_CB,
        INTAKE_H_G,
        TRANSFER_CARGO,
        TRANSFER_HATCH,
        DELIVER_CARGO,
        DELIVER_HATCH,
        OPENLOOP_HATCH,
        OPENLOOP_CARGO,
        NEUTRAL
    }
}
