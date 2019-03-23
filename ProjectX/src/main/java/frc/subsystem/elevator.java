package frc.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
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
    private CANEncoder mEncoder; 
    private sparkconfig mSparkConfig; 

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

    public elevator() {
        mWinchLeft = new CANSparkMax(constants.ELEVATOR_LEFT, MotorType.kBrushless);
        mWinchRight = new CANSparkMax(constants.ELEVATOR_RIGHT, MotorType.kBrushless);
        mWinchLeft.restoreFactoryDefaults();
        mWinchRight.restoreFactoryDefaults();
        mEncoder = mWinchLeft.getEncoder();
        mPIDController = mWinchLeft.getPIDController();

        // mSparkConfig = new sparkconfig(mPIDController, mWinchLeft, constants.ELEVATOR_LEFT);
        // mSparkConfig.configPID(constants.ELEVATOR_PID_CONFIG);
        // mSparkConfig.configSmartMotion(constants.ELEVATOR_MOTION_CONFIG); 
        // mSparkConfig.configCurrentLimit(constants.SMART_CURRENT_LIMIT);
        mPIDController.setP(.0005);
        mPIDController.setFF(0.000009);
        mPIDController.setD(0.0001);
        mPIDController.setOutputRange(-0.1, 1);
        mWinchLeft.setSmartCurrentLimit(40);
        mWinchRight.setSmartCurrentLimit(40);

        mPIDController.setSmartMotionMaxVelocity(25000, 0);
        mPIDController.setSmartMotionMinOutputVelocity(-25000, 0);
        mPIDController.setSmartMotionMaxAccel(22000, 0);
        mPIDController.setSmartMotionAllowedClosedLoopError(0.3, 0);

        mLeftBumpSwitch = new DigitalInput(constants.LEFT_BUMP_SWITCH);
        mRightBumpSwitch = new DigitalInput(constants.RIGHT_BUMP_SWITCH);
    }

    public static elevator getInstance() {
        return instance;
    }

    private void setHeight(double wantedHeight) {
        if(mClaw.isExtended()){
            wantedHeight -= 5;
        }
        mPIDController.setReference(wantedHeight, ControlType.kSmartMotion); 
    }

    private void setSpeed(double wantedSpeed) {
        mPIDController.setReference(wantedSpeed, ControlType.kVelocity);
        // mWinchLeft.set(-mPid.returnOutput(mEncoder.getVelocity()/100, wantedSpeed/20));
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

    private void updateBumpSwitches() {
        if(!mLeftBumpSwitch.get() && !mRightBumpSwitch.get()){
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
                SmartDashboard.putNumber("Wanted Height", mController.wantedElevatorHeight());
                SmartDashboard.putNumber("Current Height", mEncoder.getPosition());
                SmartDashboard.putNumber("Last Height", lastWantedHeight);
                SmartDashboard.putNumber("Current Velocity", mEncoder.getVelocity());
                switch(mCurrentState) {
                    case OPEN_LOOP:
                        System.out.println("In Open Loop State");
                        cWantedSpeed = mController.openLoopElevator();
                        SmartDashboard.putNumber("Speed", cWantedSpeed);
                        mWinchLeft.set(cWantedSpeed);
                        break;
                    case HOLDING:
                        // updateBumpSwitches();
                        setHeight(lastWantedHeight);
                        break; 
                    case MANUAL_CONTROL:
                        cWantedHeight = mEncoder.getPosition() + mController.wantedElevatorVelocity();
                        updateBumpSwitches();
                        if(cWantedSpeed < 0 && isAtBottom){
                            setSpeed(0);
                        } else {
                            setHeight(cWantedSpeed);
                        }
                        System.out.println("In velocity control. Velocity: " + cWantedSpeed);
                        break;
                    case POSITION_CONTROL:
                        updateBumpSwitches();
                        //cWantedHeight = mController.wantedElevatorHeight();///(2*Math.PI);
                        //if(cWantedHeight < 0 && isAtBottom){
                        //    mWantedState = state.HOLDING;
                        //} else{
                        //if(Math.abs(cWantedHeight - mEncoder.getPosition()) > 0.5){
                            setHeight(cWantedHeight);
                            lastWantedHeight = cWantedHeight;
                        //}  else {
                         //   mWantedState = state.HOLDING;
                        //}
                        //}
						break;     
                }
            mWinchRight.follow(mWinchLeft, true);
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