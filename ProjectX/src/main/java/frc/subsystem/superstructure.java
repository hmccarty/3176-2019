package frc.subsystem; 

//import frc.subsystem.cargointake;
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

    Compressor mCompressor = new Compressor(0);
    controller mController = controller.getInstance();
    crossbow mCrossbow = crossbow.getInstance();
    // hatchintake mHatchIntake = hatchintake.getInstance();
    claw mClaw = claw.getInstance();
    cargointake mCargoIntake = cargointake.getInstance();
    // elevator mElevator = elevator.getInstance(); 
    //drivetrain mDrivetrain = drivetrain.getInstance(); 
    loopmanager mLoopMan = loopmanager.getInstance();

    private int cCargoIntakeHeight = 0; 

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

    public void registerLoop(){
        mLoopMan.addLoop(
            new loop() {
                public void onStart(){
                    mCurrentState = state.HOME_C_ROLLER;
                    mWantedState = state.HOME_C_ROLLER;
                }
                public void onLoop(){
                    //System.out.println(mLastState);
                    if(mCurrentState == state.OPENLOOP_CARGO) {
                        mCargoIntake.setOpenLoop(true);
                    } else {
                        mCargoIntake.setOpenLoop(false);
                    }
                    switch(mCurrentState){
                        /**
                         * Allows driver to control cargo intake manually
                         */
                        case C_ROLLER_MANUAL:
                            if(mLastState != state.C_ROLLER_MANUAL){ 
                                cCargoIntakeHeight = mCargoIntake.getHeight();
                            }
                            int wantedHeight = cCargoIntakeHeight + mController.wantedCargoIntakePosition(); 
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
                                mController.alertOperatorMain();
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
                         *Returns cargo intake back to stowed position and prepares to intake hatch
                         */
                        case ROCKET_C_ROLLER:
                            mCargoIntake.rocket();
                            mCargoIntake.hold();
                            break;
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
                        case HOME_C_ROLLER:
                            if(mCargoIntake.isHomed()){
                                mWantedState = state.NEUTRAL;    
                            } else {
                                mCrossbow.draw();
                                mCargoIntake.home();
                            }
                            break;
                        case INTAKE_C_CLAW:
                            currentTime = mTimer.getFPGATimestamp();
                            deltaTime = currentTime - startTime;
                            if(!(mLastState == state.INTAKE_C_CLAW)){
                            SmartDashboard.putBoolean("Check Boolean", true);
                                startTime = mTimer.getFPGATimestamp();
                                mClaw.release();
                            } else if (deltaTime > 0.1){ 

                                mClaw.intake();
                            }
                            //System.out.println(mLastState);
                            //if(mLastState != state.INTAKE_C_CLAW){
                             //   mClaw.release();
                            //} else {
                            
                            //}
                            //}
                        /*  if(mLastState != mCurrentState){
                        *       mClaw.aim();
                        *       mClaw.prepare();
                        *   }
                        *   mClaw.clamp();
                        */
                            // mLastState = state.INTAKE_C_CLAW;
                            break;    
                        case INTAKE_H_G:
                        /*  mHatchIntake.deploy(); 
                        *   mHatchIntake.intake();
                        */
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
                        case TRANSFER_HATCH:
                            //mCrossbow.set();
                            //mClaw.stow();
                            /*if(mHatchIntake.stowError() < 1000){
                            *   
                            *
                            */ 
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
                            // mLastState = state.DEPLOY_CLAW;
                            break;
                        case OPENLOOP_HATCH:
                            // mLastState = state.OPENLOOP_HATCH;
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
                            mCompressor.start();
                            mCargoIntake.stow();
                            mCargoIntake.hold();
                            //mCargoIntake.zeroAllSensors();
                            mClaw.stow();
                            mClaw.clamp();
                            //mCargoIntake.stow();
                            //mHatchIntake.stow();
                            mCrossbow.draw();
                            // mLastState = state.NEUTRAL;
                            break;
                }
                mCargoIntake.outputToSmartDashboard();
                //System.out.println("Current Height: " + mCargoIntake.getHeight());
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
