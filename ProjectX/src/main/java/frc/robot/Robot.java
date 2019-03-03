/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.subsystem.*;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.auton.*;


public class Robot extends TimedRobot {
	private loopmanager myLoops = loopmanager.getInstance();
	private drivetrain mDriveTrain = drivetrain.getInstance(); 
	private vision mVision = vision.getInstance();
	private controller mController = controller.getInstance();
	private superstructure mSuperstructure = superstructure.getInstance();
	private elevator mElevator = elevator.getInstance();

	@Override
	public void robotInit() {
		mDriveTrain.registerLoop(); 
		mVision.registerLoop();
		mSuperstructure.registerLoop();
		mElevator.registerLoop();
		myLoops.startLoops();
	}
	
	public void autonomousPeriodic() {
		myLoops.runLoops();
		mSuperstructure.setWantedState(superstructure.state.NEUTRAL);
		mDriveTrain.setWantedState(drivetrain.state.AUTON);
		leftHab.main.run();
	}

	@Override
	public void teleopPeriodic() {
		myLoops.runLoops();
		
		/*********************\
		|* Drivetrain States *|
		\*********************/
		if(mController.trackTarget()){
			mDriveTrain.setWantedState(drivetrain.state.VISION);
		} else {
			mDriveTrain.setWantedState(drivetrain.state.DRIVE);
		}		

		/*************************\
		|* Superstructure States *|
		\*************************/

		if(mController.crossbowIntake()){
			mSuperstructure.setWantedState(superstructure.state.INTAKE_H_CB);
		}
		else if(mController.crossbowHold()){
			mSuperstructure.setWantedState(superstructure.state.HOLD_H_CB);
		}
		else if(mController.crossbowDeliver()){
			mSuperstructure.setWantedState(superstructure.state.DELIVER_HATCH);
		}
		else if(mController.deployCargoIntake()){
			mSuperstructure.setWantedState(superstructure.state.INTAKE_C_ROLLER);
		} 
		else if (mController.spitCargoIntake()){
			mSuperstructure.setWantedState(superstructure.state.DELIVER_CARGO);
		} 
		else if (mController.neutral()){
			mSuperstructure.setWantedState(superstructure.state.NEUTRAL);
		}

		/*******************\
		|* Elevator States *|
		\*******************/

		// if(mController.openLoopEnabled()){
		// 	mElevator.setWantedState(elevator.state.OPEN_LOOP);
		// }
		if (mController.getElevatorHeight() != -1){
			mElevator.setWantedState(elevator.state.POSITION_CONTROL);
		}
		else if (mController.getElevatorVelocity() != 0){
			mElevator.setWantedState(elevator.state.VELOCITY_CONTROL);
		}
		else if (mElevator.inPosition()){
			mElevator.setWantedState(elevator.state.HOLDING);
		}

		/*****************\
		|* Vision States *|
		\*****************/
		if(mController.visionFront()){
			mVision.setWantedState(vision.state.STREAM_FRONT);
		}
		else if(mController.visionBack()){
			mVision.setWantedState(vision.state.STREAM_BACK);
		}
		else if(mController.trackTarget()){
			mVision.setWantedState(vision.state.TRACK_TARGET);
		}
	}

	@Override
	public void testPeriodic() { 
	}
}
