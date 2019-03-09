package frc.subsystem; 

//import frc.subsystem.cargointake;
import frc.util.loop;
import edu.wpi.first.wpilibj.Compressor;

public class superstructure {
    private static superstructure instance = new superstructure();
    
    private state mLastState; 
    private state mWantedState;
    private state mCurrentState;

    Compressor mCompressor = new Compressor(0);
    controller mController = controller.getInstance();
    crossbow mCrossbow = crossbow.getInstance();
    // hatchintake mHatchIntake = hatchintake.getInstance();
    // claw mClaw = claw.getInstance();
    cargointake mCargoIntake = cargointake.getInstance();
    // elevator mElevator = elevator.getInstance(); 
    //drivetrain mDrivetrain = drivetrain.getInstance(); 
    loopmanager mLoopMan = loopmanager.getInstance();

    private int cCargoIntakeHeight = 0; 

    public superstructure (){}

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
                    mCurrentState = state.NEUTRAL;
                    mWantedState = state.NEUTRAL;
                }
                public void onLoop(){
                    // if(mController.runCompressor()){
                    //     mCompressor.start();
                    // } else {
                    //     mCompressor.stop();
                    // }
                    switch(mCurrentState){
                        case C_ROLLER_MANUAL:
                            if(mLastState != state.C_ROLLER_MANUAL){ 
                                System.out.print(mLastState.toString()); 
                                cCargoIntakeHeight = mCargoIntake.getHeight();
                            }
                            System.out.println(cCargoIntakeHeight);
                            if(cCargoIntakeHeight+mController.getWantedCargoIntakePosition() > 100){
                                cCargoIntakeHeight += mController.getWantedCargoIntakePosition();
                                mCargoIntake.manualControl(cCargoIntakeHeight, false);
                            }
                            if(mCargoIntake.hasBall()){
                                mCargoIntake.hold();
                            }
                            checkState();
                            break;
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
                        case STOW_C_ROLLER:
                            mCargoIntake.stow();
                            mCargoIntake.hold();
                            checkState();
                            break;
                        case INTAKE_H_CB:
                            mCargoIntake.stow();
                            mCrossbow.set();
                            mLastState = mCurrentState;
                            checkState();
                            break;
                        case HOLD_H_CB:
                            mCargoIntake.stow(); 
                            mCrossbow.draw();
                            mLastState = mCurrentState;
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
                            mCargoIntake.spit();
                            checkState();
                            break;
                        case DELIVER_HATCH:
                            mCargoIntake.stow();
                            mCrossbow.shoot();
                            mLastState = mCurrentState;
                            checkState();
                            break;
                        case OPENLOOP_HATCH:
                            
                            break;
                        case OPENLOOP_CARGO:
                             mCargoIntake.cargoIntakeOpenLoop(mController.getCargoIntakeOpenLoopCommand());                            
                            break;
                        case NEUTRAL:
                            mCompressor.start();
                            mCargoIntake.stow();
                            mCargoIntake.hold();
                            mCargoIntake.zeroAllSensors();
                            //mClaw.stow();
                            //mHatchIntake.stow();
                            //mCrossbow.hold(); 
                            checkState();
                            break;
                }
                checkState();
                mCargoIntake.outputToSmartDashboard();
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
