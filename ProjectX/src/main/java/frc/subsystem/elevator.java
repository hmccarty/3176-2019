package frc.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.*;
import frc.subsystem.controller;
import frc.robot.constants;
import frc.util.*;
import frc.util.trajectory;

//CURRENTLY NOT IN USE

public class elevator {
    private static elevator instance = new elevator();
    private controller mController = controller.getInstance();
    private claw mClaw = claw.getInstance();

    private CANSparkMax mWinchLeft;
    private CANSparkMax mWinchRight;
    private CANPIDController mPIDController;
    private CANPIDController mSpeedController;
    private CANEncoder mEncoder; 
    private sparkconfig mSparkConfig; 

    private PowerDistributionPanel powerPanel = new PowerDistributionPanel();

    private pid mPid = new pid(0.007, 0, 0, 0.8);

    private trajectory mTrajectory;

    private double cWantedHeight;
    private double lastWantedHeight = 0; 
    private double cWantedSpeed; 
    private double cSpeed;
    private double cTrajectoryStartTime;

    private DigitalInput mLeftBumpSwitch;
    private DigitalInput mRightBumpSwitch;
    private Boolean isAtBottom = false;

    public enum state {
        HOLDING,
        OPEN_LOOP,
        MANUAL_CONTROL,
        POSITION_CONTROL
    }

    private state mCurrentState;
    private state mWantedState;
    private state mLastState;

    public elevator() {
        mWinchLeft = new CANSparkMax(constants.ELEVATOR_LEFT, MotorType.kBrushless);
        mWinchRight = new CANSparkMax(constants.ELEVATOR_RIGHT, MotorType.kBrushless);
        mWinchLeft.restoreFactoryDefaults();
        mWinchRight.restoreFactoryDefaults();
        mEncoder = mWinchLeft.getEncoder();
        mPIDController = mWinchLeft.getPIDController();
        mSpeedController = mWinchLeft.getPIDController();

        // mSparkConfig = new sparkconfig(mPIDController, mWinchLeft, constants.ELEVATOR_LEFT);
        // mSparkConfig.configPID(constants.ELEVATOR_PID_CONFIG);
        // mSparkConfig.configSmartMotion(constants.ELEVATOR_MOTION_CONFIG); 
        // mSparkConfig.configCurrentLimit(constants.SMART_CURRENT_LIMIT);
        mPIDController.setP(.3); //3.0
        mPIDController.setFF(0.002);
        mPIDController.setI(0.0000);

        mSpeedController.setP(0.3);
        //mSpeedController.setFF(0.00);
        //mPIDController.setD(1000);
        mSpeedController.setOutputRange(-.1, 0.35);//-.3,.5
        mPIDController.setOutputRange(-0.1, 1.0);//-.015,1
        int kStallCurrent = 40;//100; 
        //int kFreeCurrent = 100; 
        mWinchLeft.setSmartCurrentLimit(kStallCurrent);//, kFreeCurrent);
        mWinchRight.setSmartCurrentLimit(kStallCurrent);//, kFreeCurrent);

        mWinchLeft.setClosedLoopRampRate(0.3);
        mWinchRight.setClosedLoopRampRate(0.3);

        // mPIDController.setSmartMotionMaxVelocity(1750, 0);
        // mPIDController.setSmartMotionMinOutputVelocity(-1750, 0);
        // mPIDController.setSmartMotionMaxAccel(1300, 0);
        // mPIDController.setSmartMotionAllowedClosedLoopError(0.2, 0);

        mLeftBumpSwitch = new DigitalInput(constants.LEFT_BUMP_SWITCH);
        mRightBumpSwitch = new DigitalInput(constants.RIGHT_BUMP_SWITCH);

        mEncoder.setPosition(0);
    }

    public static elevator getInstance() {
        return instance;
    }

    private void setHeight(double wantedHeight) {
        if(wantedHeight == -1){
            wantedHeight = 0;
        }
        if(mClaw.isExtended() && wantedHeight > 4.5 && mCurrentState != state.MANUAL_CONTROL){
            if(mController.wantedElevatorHeight() > 15.25 ){
                wantedHeight -= 4.0;
            } else if (mController.wantedElevatorHeight() < 14) {
                wantedHeight -= 5.1;
            } else {
                wantedHeight -= 6.0;
            }
        }
        // if(mCurrentState == state.MANUAL_CONTROL){
        //     wantedHeight += 4.5;
        // }
        if(!cargointake.getInstance().isStowed()){
            mPIDController.setReference(wantedHeight, ControlType.kPosition); 
        } 
    }


    private void setSpeed(double wantedSpeed) {
        mWinchLeft.set(mPid.returnOutput(mEncoder.getVelocity(), wantedSpeed));
        //mSpeedController.setReference(wantedSpeed, ControlType.kVelocity);
        //mWinchLeft.set(-mPid.returnOutput(mEncoder.getVelocity()/100, wantedSpeed/20));
        // System.out.println("Encoder Velocity: " + mEncoder.getVelocity());
        // SmartDashboard.putNumber("Encoder Velocity", mEncoder.getVelocity());
        // SmartDashboard.putNumber("Wanted Velocity", mPid.returnOutput(mEncoder.getVelocity(), wantedSpeed/20));
    }

    public boolean inPosition() {
        if(Math.abs(mEncoder.getVelocity())  < .1){
            return true; 
        } else {
            return false; 
        }
    }

    public void setWantedElevatorHeight(double wantedHeight){
        cWantedHeight = wantedHeight;
    }

    public double getHeight() {
        return mEncoder.getPosition();
    }

    public double getLastWantedHeight(){
        return lastWantedHeight;
    }

    private void updateBumpSwitches() {
        if(!mLeftBumpSwitch.get() || !mRightBumpSwitch.get()){
            isAtBottom = true;
            mEncoder.setPosition(0);
        } else{
            isAtBottom = false;
        }
    }

    public void setWantedState(state wantedState) {
        this.mWantedState = wantedState;
    }

    public state getState() {
        return mCurrentState;
    }

    public void checkState() {
        if (mCurrentState != mWantedState) {
            mCurrentState = mWantedState;
        }
    }

    public void registerLoop() {
        loopmanager.getInstance().addLoop(new loop()
        {
            @Override
            public void onStart() {
                mCurrentState = state.HOLDING;
                mWantedState = state.HOLDING;
            }

            @Override
            public void onLoop() {
                SmartDashboard.putNumber("Left Output", mWinchLeft.getOutputCurrent());
                SmartDashboard.putNumber("Right Output", mWinchRight.getOutputCurrent());
                SmartDashboard.putNumber("Right Current", powerPanel.getCurrent(2));
                SmartDashboard.putNumber("Left Current", powerPanel.getCurrent(1));
                SmartDashboard.putNumber("Current Height", mEncoder.getPosition());
                SmartDashboard.putNumber("Last Height", lastWantedHeight);
                SmartDashboard.putNumber("Wanted Height", cWantedHeight);
                SmartDashboard.putNumber("Current Velocity", mEncoder.getVelocity());
                SmartDashboard.putBoolean("rightBottom", mRightBumpSwitch.get());
                SmartDashboard.putBoolean("leftBottom", mLeftBumpSwitch.get());
                switch(mCurrentState) {
                    case OPEN_LOOP:
                        //System.out.println("In Open Loop State");
                        //cWantedSpeed = mController.openLoopElevator();
                        SmartDashboard.putNumber("Speed", cWantedSpeed);
                        mWinchLeft.set(cWantedSpeed);
                        break;
                    case HOLDING:
                        // updateBumpSwitches();
                        if(mLastState == state.MANUAL_CONTROL && mClaw.isExtended()){
                            lastWantedHeight += 4.5;
                        }
                        setHeight(lastWantedHeight);
                        
                        break; 
                    case MANUAL_CONTROL:
                        cWantedSpeed = mController.wantedElevatorVelocity();
                        setSpeed(cWantedSpeed); 
                        lastWantedHeight = getHeight();
                        //if(Math.abs(cWantedHeight - mEncoder.getPosition()) > 0.5){
                            //if(cWantedHeight > 0 && cWantedHeight < 29.5){
                                //if(mController.wantedElevatorVelocity() != 0){
                                   // setHeight(cWantedHeight);
                                //}
                              //  lastWantedHeight = cWantedHeight;
                            //}
                        //}
                        
                        //updateBumpSwitches();
                        // if(cWantedSpeed < 0 && isAtBottom){
                        //     setSpeed(0);
                        // } else {
                        //     setSpeed(cWantedSpeed);
                        // }
                        break;
                    case POSITION_CONTROL:
                        //updateBumpSwitches();
                        //cWantedHeight = mController.wantedElevatorHeight();///(2*Math.PI);
                        // if(cWantedHeight < 0 && isAtBottom){
                        //    mWantedState = state.HOLDING;
                        // } else{
                        
                        //if(Math.abs(cWantedHeight - mEncoder.getPosition()) > 0.5){
                            setHeight(cWantedHeight);
                            lastWantedHeight = cWantedHeight;
                        //}
                        // } else {
                        //    mWantedState = state.HOLDING;
                        // }
                        // }
						break;     
                }
            mWinchRight.follow(mWinchLeft, true);
            mLastState = mCurrentState;
            SmartDashboard.putString("Elevator state", mCurrentState.toString());
            checkState();
            }
            public void onStop(){}
        });
    }
}
/* 
switch case between setPosition/openLoop/PIDLoop
        setLevel
            gets level from controller and PID controls to that level
        openLoop
            gets velocity from joystick
        PIDLoop get ticks from controller than adjusts speed to match joystick through PIDLoop
            Similar to openLoop but more gradual
@*/