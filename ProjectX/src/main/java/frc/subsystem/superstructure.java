package frc.subsystem; 

import frc.util.loop;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.*;
import frc.robot.constants;

public class superstructure {
    private static superstructure instance = new superstructure();
    
    private state mLastState; 
    private state mWantedState;
    private state mCurrentState;

    Compressor mCompressor = new Compressor(1);
    controller mController = controller.getInstance();
    crossbow mCrossbow = crossbow.getInstance();
    claw mClaw = claw.getInstance();
    cargointake mCargoIntake = cargointake.getInstance();
    loopmanager mLoopMan = loopmanager.getInstance();

    private int cCargoIntakeHeight = 0; 

    private boolean alertStarted; 

    Timer mTimer = new Timer(); 

    private boolean firstTime = true;

    private Timer transferTimer = new Timer();
    private boolean transferStarted = false; 
    double startTime = 0;
    double currentTime = 0;
    double deltaTime = 0;

    public superstructure (){
        mWantedState = state.HOLD_H_CB; 
        mCurrentState = state.HOLD_H_CB;
        transferTimer.start();
        mTimer.start();
    }

    public static superstructure getInstance(){
        return instance; 
    }

    public void setWantedState(state wantedState){
        mWantedState = wantedState;
    }

    public state getCurrentState(){
        return mCurrentState;
    }

    public void checkState(){
        if(mCurrentState != mWantedState){
            mCurrentState = mWantedState; 
        }
        if(!mController.runCompressor()){
            mCompressor.stop();
            SmartDashboard.putBoolean("Compressor", false);
        } else {
            mCompressor.start();
            SmartDashboard.putBoolean("Compressor", true);
        }
    }

    public void registerLoop() {
        mLoopMan.addLoop(
            new loop() {
                public void onStart(){
                    mCurrentState = state.HOME_C_ROLLER;
                    mWantedState = state.HOME_C_ROLLER;
                }
                public void onLoop(){ 
                    if(mCurrentState != state.INTAKE_C_ROLLER) {
                        mController.stopOperatorAlert();
                    }
                    if(mCurrentState == state.OPENLOOP_CARGO) {
                        mCargoIntake.setOpenLoop(true);
                    } else {
                        mCargoIntake.setOpenLoop(false);
                    }
                    if(mController.toggleCompressor()){
                        boolean compressorState = (mCompressor.enabled()) ? false : true; 
                        if(compressorState == true){
                            mCompressor.stop();
                        } else {
                            mCompressor.start();
                        }
                    } 
                    switch(mCurrentState){
                        /**
                         * Allows driver to control cargo intake manually
                         */
                        case C_ROLLER_MANUAL:
                            if(mLastState != state.C_ROLLER_MANUAL){ 
                                cCargoIntakeHeight = mCargoIntake.getHeight();
                            }
                            int wantedHeight = cCargoIntakeHeight;

                            wantedHeight = wantedHeight +  mController.wantedCargoIntakePosition(); 

                            SmartDashboard.putNumber("Wanted Height", wantedHeight);
                        
                            if(wantedHeight > 0 && wantedHeight < constants.DEPLOYED_HEIGHT){
                                cCargoIntakeHeight = wantedHeight;
                                mCargoIntake.manualControl(cCargoIntakeHeight, false);
                            } else {
                                mCargoIntake.manualControl(cCargoIntakeHeight, false);
                            }
                        
                            SmartDashboard.putNumber("Cargo Intake Height", cCargoIntakeHeight);
                            if(mCargoIntake.hasBall()){
                                mCargoIntake.hold();
                            }
                            break;
                        /**
                         * Deploys cargo intake, starts roller, then waits for cargo before stowing
                         */
                        case INTAKE_C_ROLLER:
                            if(!mCargoIntake.hasBall()){
                                mCargoIntake.deploy();
                                mCargoIntake.intake();
                            } else {
                                mWantedState = state.STOW_C_ROLLER;
                            }
                            break;
                        /**
                         * Stows cargo intake and stops roller
                         */
                        case STOW_C_ROLLER:
                            mCargoIntake.stow();
                            mCargoIntake.hold();
                            break;
                        /**
                         * Returns cargo intake back to stowed position and prepares to intake hatch
                         */
                        case ROCKET_C_ROLLER:
                            mCargoIntake.rocket();
                            mCargoIntake.hold();
                            break;
                        /**
                         * Prepares to fingers for hatch
                         */
                        case INTAKE_H_CB:
                            mCargoIntake.stow();
                            mCrossbow.set();
                            mLastState = mCurrentState;
                            break;
                        /**
                         * Latches onto hatch
                         */
                        case HOLD_H_CB:
                            mCargoIntake.stow(); 
                            mCrossbow.draw();
                            mLastState = mCurrentState;
                            break;
                        /**
                         * Homes Cargo Intake Encoder
                         */
                        case HOME_C_ROLLER:
                            if(mCargoIntake.isHomed()){
                                mWantedState = state.NEUTRAL;    
                            } else {
                                mCrossbow.draw();
                                mCargoIntake.home();
                            }
                            break;
                        /**
                         * Prepares Claw for intaking Cargo
                         */
                        case INTAKE_C_CLAW:
                            currentTime = mTimer.getFPGATimestamp();
                            deltaTime = currentTime - startTime;
                            if(!(mLastState == state.INTAKE_C_CLAW)){
                                startTime = mTimer.getFPGATimestamp();
                                mClaw.release();
                            } else if (deltaTime > 0.1){ 
                                mClaw.intake();
                            }
                            break;    
                        case FIX_C_CLAW:
                            mClaw.release();
                            break;
                        case TRANSFER_CARGO: 
                            mCargoIntake.moveToTransfer();
                            mClaw.stow(); 
                            mClaw.intake();
                            if(!mCargoIntake.isStowed()){
                                mCargoIntake.transfer(); 
                                currentTime = transferTimer.getFPGATimestamp();
                                if(!transferStarted){
                                    transferStarted = true; 
                                    startTime = transferTimer.getFPGATimestamp();
                                }
                                else if(currentTime - startTime > 0.5){
                                    transferStarted = false;
                                    startTime = 0; 
                                    mClaw.clamp(); 
                                    mCargoIntake.stow();
                                    mCargoIntake.hold();
                                    mWantedState = state.NEUTRAL;
                                }
                                SmartDashboard.putNumber("Delta Time", currentTime - startTime);
                                SmartDashboard.putNumber("Current Time", currentTime);
                                SmartDashboard.putNumber("Started Time", startTime);
                            }

                            break;
                        /**
                         * Spits cargo out of cargo intake
                         */
                        case DELIVER_CARGO:
                            if(mClaw.isExtended()){
                                mClaw.release(); 
                            } else {//if (mCargoIntake.hasBall() || mLastState == ){
                                mCargoIntake.spit();
                            }
                            break;
                        /**
                         * Fires hatch from crossbow
                         */
                        case DELIVER_HATCH:
                            currentTime = mTimer.getFPGATimestamp();
                            deltaTime = currentTime - startTime;
                            mCargoIntake.stow();
                            if(mLastState != state.DELIVER_HATCH){
                                startTime = currentTime; 
                                mCrossbow.shootOut();
                            } else if(deltaTime > 0.175){
                                mCrossbow.shootIn();
                            }
                            break;
                        case DEPLOY_CLAW:
                            mClaw.deploy();
                            break;
                        /**
                         * Allows driver to control cargo intake without closed loop control
                         */
                        case OPENLOOP_CARGO:
                            SmartDashboard.putBoolean("In Cargo Openloop", true);
                            SmartDashboard.putNumber("Open Loop Joystick", mController.wantedCargoIntakeOpenLoop());
                            mCargoIntake.openLoop(mController.wantedCargoIntakeOpenLoop());
                            break;
                        /**
                         * Returns all mechanism to their starting configuration
                         */
                        case NEUTRAL:
                            mCargoIntake.stow();
                            mCargoIntake.hold();
                            mClaw.stow();
                            mClaw.clamp();
                            mCrossbow.draw();
                            break;
                }
                mCargoIntake.outputToSmartDashboard();
                mLastState = mCurrentState;
                checkState();
            }

            public void onStop(){}
        });
    }

    public enum state {
        C_ROLLER_MANUAL,
        INTAKE_C_ROLLER,
        STOW_C_ROLLER,
        INTAKE_C_CLAW,
        INTAKE_H_CB,
        HOLD_H_CB,
        HOME_C_ROLLER,
        INTAKE_H_G,
        FIX_C_CLAW,
        DEPLOY_CLAW,
        ROCKET_C_ROLLER,
        TRANSFER_CARGO,
        TRANSFER_HATCH,
        DELIVER_CARGO,
        DELIVER_HATCH,
        OPENLOOP_HATCH,
        OPENLOOP_CARGO,
        NEUTRAL
    }
}
