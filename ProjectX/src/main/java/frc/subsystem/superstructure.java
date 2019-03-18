package frc.subsystem; 

//import frc.subsystem.cargointake;
import frc.util.loop;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Timer;
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

    private boolean firstTime = true;

    private Timer transferTimer = new Timer();
    private boolean transferStarted = false; 
    double startTime = 0;
    double currentTime = 0;

    public superstructure (){
        mWantedState = state.HOLD_H_CB; 
        mCurrentState = state.HOLD_H_CB;
        transferTimer.start();
    }

    public static superstructure getInstance(){
        return instance; 
    }

    public void setWantedState(state wantedState){
        mWantedState = wantedState;
    }

    public void checkState(){
        if(mCurrentState != mWantedState){
            mCurrentState = mWantedState; 
        }
        mCompressor.start();
    }

    public void registerLoop(){
        mLoopMan.addLoop(
            new loop() {
                public void onStart(){
                    mCurrentState = state.HOME_C_ROLLER;
                    mWantedState = state.HOME_C_ROLLER;
                }
                public void onLoop(){
                    if(firstTime){
                        //mCrossbo
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
                            if(wantedHeight > 0 && wantedHeight < constants.DEPLOYED_HEIGHT){
                                cCargoIntakeHeight = wantedHeight;
                                mCargoIntake.manualControl(cCargoIntakeHeight, false);
                            } else {
                                mCargoIntake.manualControl(cCargoIntakeHeight, false);
                            }
                            if(mCargoIntake.hasBall()){
                                mCargoIntake.hold();
                            }
                            checkState();
                            break;
                        /**
                         * Deploys cargo intake, starts roller, then waits for cargo before stowing
                         */
                        case INTAKE_C_ROLLER:
                            if(!mCargoIntake.hasBall()){
                                mCargoIntake.deploy();
                                mCargoIntake.intake();
                            } else {
                                mController.alertOperator();
                                mWantedState = state.STOW_C_ROLLER;
                            }
                            checkState();
                            break;
                        /**
                         * Stows cargo intake and stops roller
                         */
                        case STOW_C_ROLLER:
                            mCargoIntake.stow();
                            mCargoIntake.hold();
                            checkState();
                            break;
                        /**
                         *Returns cargo intake back to stowed position and prepares to intake hatch
                         */
                        case ROCKET_C_ROLLER:
                            mCargoIntake.rocket();
                            mCargoIntake.hold();
                            checkState(); 
                            break;
                        case INTAKE_H_CB:
                            mCargoIntake.stow();
                            mCrossbow.set();
                            mLastState = mCurrentState;
                            checkState();
                            break;
                        /**
                         * Latches onto hatch
                         */
                        case HOLD_H_CB:
                            mCargoIntake.stow(); 
                            mCrossbow.draw();
                            mLastState = mCurrentState;
                            checkState();
                            break;
                        case HOME_C_ROLLER:
                            if(mCargoIntake.isHomed()){
                                mWantedState = state.NEUTRAL;    
                            } else {
                                mCrossbow.draw();
                                mCargoIntake.home();
                            }
                            checkState();
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
                            mCargoIntake.stow();
                            mClaw.stow(); 
                            if(mCargoIntake.isStowed()){
                                mCargoIntake.transfer(); 
                                currentTime = transferTimer.getFPGATimestamp();
                                if(!transferStarted){
                                    transferStarted = true; 
                                    startTime = transferTimer.getFPGATimestamp();
                                }
                                else if(currentTime - startTime == 0.2){
                                    transferStarted = false;
                                    startTime = 0; 
                                    mClaw.clamp(); 
                                    mWantedState = state.NEUTRAL;
                                }
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
                            if(mCargoIntake.hasBall()){
                                mCargoIntake.spit();
                            } else { 
                                mClaw.deploy();
                                mClaw.release();
                            }
                            checkState();
                            break;
                        /**
                         * Fires hatch from crossbow
                         */
                        case DELIVER_HATCH:
                            mCargoIntake.stow();
                            mCrossbow.shoot();
                            mLastState = mCurrentState;
                            checkState();
                            break;
                        case OPENLOOP_HATCH:
                            break;
                        /**
                         * Allows driver to control cargo intake without closed loop control
                         */
                        case OPENLOOP_CARGO:
                            break;
                        /**
                         * Returns all mechanism to their starting configuration
                         */
                        case NEUTRAL:
                            mCompressor.start();
                            mCargoIntake.stow();
                            mCargoIntake.hold();
                            mCargoIntake.zeroAllSensors();
                            mClaw.stow();
                            mClaw.clamp();
                            //mHatchIntake.stow();
                            mCrossbow.draw(); 
                            checkState();
                            break;
                }
                checkState();
                mCargoIntake.outputToSmartDashboard();
                //System.out.println("Current Height: " + mCargoIntake.getHeight());
                mLastState = mCurrentState;
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
