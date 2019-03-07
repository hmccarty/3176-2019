// package frc.subsystem;

// import com.revrobotics.CANEncoder;
// import com.revrobotics.CANPIDController;
// import com.revrobotics.CANSparkMax;
// import com.revrobotics.ControlType;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;
// import frc.subsystem.controller;
// import frc.robot.constants;
// import frc.util.*;
// import frc.util.trajectory;

// public class elevator {
//     private static elevator instance = new elevator();
//     private controller mController = controller.getInstance();

//     private CANSparkMax mWinch;
//     private CANPIDController mPIDController;
//     private CANEncoder mEncoder; 
//     private sparkconfig mSparkConfig; 

//     private trajectory mTrajectory;

//     private double cWantedHeight;
//     private double cWantedSpeed; 
//     private double cSpeed;
//     private double cTrajectoryStartTime;

//     public enum state {
//         HOLDING,
//         OPEN_LOOP,
//         VELOCITY_CONTROL,
//         POSITION_CONTROL
//     }

//     private state mCurrentState;
//     private state mWantedState;

//     public elevator() {
//         mWinch = new CANSparkMax(constants.ELEVATOR, MotorType.kBrushless);
//         mEncoder = mWinch.getEncoder();
//         mPIDController = mWinch.getPIDController();

//         mSparkConfig = new sparkconfig(mPIDController, constants.ELEVATOR);
//         mSparkConfig.configPID(constants.ELEVATOR_PID_CONFIG);
//         mSparkConfig.configSmartMotion(constants.ELEVATOR_MOTION_CONFIG); 
//     }

//     public static elevator getInstance() {
//         return instance;
//     }

//     private void setHeight(double wantedHeight) {
//         mPIDController.setReference(wantedHeight, ControlType.kSmartMotion); 
//     }

//     private void setSpeed(double wantedSpeed) {
//         mPIDController.setReference(wantedSpeed, ControlType.kVelocity);
//     }

//     public boolean inPosition() {
//         if(Math.abs(mEncoder.getVelocity())  < .1){
//             return true; 
//         } else {
//             return false; 
//         }
//     }

//     public double getHeight() {
//         return mEncoder.getPosition();
//     }

//     public void setWantedState(state wantedState) {
//         this.mWantedState = wantedState;
//     }

//     public state getState() {
//         return mCurrentState;
//     }

//     public void checkState() {
//         if (mCurrentState != mWantedState) {
//             mCurrentState = mWantedState;
//         }
//     }

//     public void registerLoop() {
//         loopmanager.getInstance().addLoop(new loop()
//         {
//             @Override
//             public void onStart() {
//                 mCurrentState = state.HOLDING;
//                 mWantedState = state.HOLDING;
//             }

//             @Override
//             public void onLoop() {
//                 switch(mCurrentState) {
//                     case OPEN_LOOP:
//                         cWantedSpeed = mController.getElevatorVelocity()/2.0;
//                         mWinch.set(cWantedSpeed); 
//                         break;
//                     case HOLDING:
//                         setHeight(getHeight());
//                         break; 
//                     case VELOCITY_CONTROL:
//                         cWantedSpeed = mController.getElevatorVelocity()*3; 
//                         setSpeed(cWantedSpeed);
//                         break;
//                     case POSITION_CONTROL:
//                         cWantedHeight = mController.getElevatorHeight()/(2*Math.PI);
//                         setHeight(cWantedHeight);
// 						break;     
//                 }
//             checkState();
//             }
//             public void onStop(){}
//         });
//     }
// }
// /* 
// switch case between setPosition/openLoop/PIDLoop
//         setLevel
//             gets level from controller and PID controls to that level
//         openLoop
//             gets velocity from joystick
//         PIDLoop get ticks from controller than adjusts speed to match joystick through PIDLoop
//             Similar to openLoop but more gradual
//@*/