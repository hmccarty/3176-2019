/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.subsystem.*;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.*;
import frc.auton.*;

public class Robot extends TimedRobot {
	private loopmanager myLoops = loopmanager.getInstance();
	private drivetrain mDriveTrain = drivetrain.getInstance(); 
	private vision mVision = vision.getInstance();
	private controller mController = controller.getInstance();
	private superstructure mSuperstructure = superstructure.getInstance();
	private elevator mElevator = elevator.getInstance();

	double startTime = -0.5; 
	double endTime = 0; 

	boolean visionIntaking = false; 
	boolean visionDeploying = false; 

	@Override
	public void robotInit() {
		mDriveTrain.registerLoop(); 
		mVision.registerLoop();
		mSuperstructure.registerLoop();
		mElevator.registerLoop();
		myLoops.startLoops();
	}
	
	public void robotPeriodic(){
		/*****************\
		|* Vision States *|
		\*****************/
 
		if(mController.switchVisionCamera()) {
			mVision.setWantedState(vision.state.SWITCH_CAMERA);
		} else if(mController.switchVisionMode()) {
			mVision.setWantedState(vision.state.SWITCH_MODE);
		}
	}

	public void autonomousPeriodic() {
		driverControl();
	}

	@Override
	public void teleopPeriodic() {
		try { 
			driverControl();
		} catch (Exception e) {

		}
	}

	public void driverControl() {
		myLoops.runLoops();
	
		endTime = Timer.getFPGATimestamp();

		/*********************\
		|* Drivetrain States *|
		\*********************/

		if(mController.trackTarget()) {
			if(mSuperstructure.getCurrentState() == superstructure.state.INTAKE_H_CB){
				visionIntaking = true; 
			} else {
				visionDeploying = true; 
			}
			mDriveTrain.setWantedState(drivetrain.state.VISION_TRACK);
		} else if (mController.finishedTracking() || (endTime - startTime) < 0.15){
			startTime = Timer.getFPGATimestamp(); 
			mDriveTrain.setWantedState(drivetrain.state.VISION_EXIT); 
	    } else {
			visionIntaking = false; 
			visionDeploying = false; 
			mDriveTrain.setWantedState(drivetrain.state.DRIVE);
		}		

		/*************************\
		|* Superstructure States *|
		\*************************/

		if(mController.crossbowIntake()) {
		 	mSuperstructure.setWantedState(superstructure.state.INTAKE_H_CB);
		} else if (mController.wantedCargoIntakeOpenLoop() != 0) {
			mSuperstructure.setWantedState(superstructure.state.OPENLOOP_CARGO); 
		} else if(mController.crossbowHold() || (mController.finishedTracking() && visionIntaking)) {
		 	mSuperstructure.setWantedState(superstructure.state.HOLD_H_CB);
		} else if(mController.crossbowDeliver() || (mController.finishedTracking() && visionDeploying)) {
		 	mSuperstructure.setWantedState(superstructure.state.DELIVER_HATCH);
		} else if(mController.deployCargoIntake()) {
		 	mSuperstructure.setWantedState(superstructure.state.INTAKE_C_ROLLER);
		} else if(mController.rocketCargoIntake()) {
			mSuperstructure.setWantedState(superstructure.state.ROCKET_C_ROLLER);
		} else if (mController.spitCargoIntake()) {
		 	mSuperstructure.setWantedState(superstructure.state.DELIVER_CARGO);
		} else if (mController.neutral()) {
		 	mSuperstructure.setWantedState(superstructure.state.NEUTRAL);
		} else if (mController.transferCargo()) {
			mSuperstructure.setWantedState(superstructure.state.TRANSFER_CARGO);
		} else if (mController.deployClaw()) {
		mSuperstructure.setWantedState(superstructure.state.DEPLOY_CLAW);
		} else if (mController.intakeClaw()) {
			mSuperstructure.setWantedState(superstructure.state.INTAKE_C_CLAW);
		} else if (mController.wantedCargoIntakePosition() != -1) {
		  	mSuperstructure.setWantedState(superstructure.state.C_ROLLER_MANUAL);
		}

		/*******************\
		|* Elevator States *|
		\*******************/

		if (mController.wantedElevatorHeight() != -1) {
			double visionHeight = (mElevator.getHeight() < 8.0) ? 8.0 : mElevator.getHeight(); 
			double wantedHeight = (mDriveTrain.isVisionDriving()) ? visionHeight : mController.wantedElevatorHeight();
			mElevator.setWantedElevatorHeight(wantedHeight);
		 	mElevator.setWantedState(elevator.state.POSITION_CONTROL);
		} else if (mController.wantedElevatorVelocity() != 0) {
			mElevator.setWantedState(elevator.state.MANUAL_CONTROL);
			mElevator.setWantedElevatorHeight(mElevator.getHeight() + mController.wantedElevatorVelocity());
		} else {
		  	mElevator.setWantedState(elevator.state.HOLDING);
		}
	}

	@Override
	public void testPeriodic() { 
		/**
		 * NEED TEST METHOD TO AUTO-CHECKLIST 
		 */
	}
}
