/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.subsystem.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.auton.*;


public class Robot extends IterativeRobot {
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	private loopmanager myLoops = loopmanager.getInstance();
	private drivetrain mDriveTrain = drivetrain.getInstance(); 
	private controller mController = controller.getInstance();
	private superstructure mSuperstructure = superstructure.getInstance();
	int testID = 0;
	String gameData;
	private boolean isIntakeOpenLoop;
	private boolean isElevatorOpenLoop;
	private static NetworkTableEntry x;
	private static NetworkTableEntry area;
	private static NetworkTableEntry bLeftX;
	private static NetworkTableEntry bRightX;
	private static NetworkTableEntry bLeftY;
	private static NetworkTableEntry bRightY;
	private static NetworkTableEntry angle; 
	private static NetworkTableEntry distance; 
	crossbow mCrossbow = crossbow.getInstance();
	@Override
	public void robotInit() {
		mDriveTrain.registerLoop(); 
		mSuperstructure.registerLoop();
		myLoops.startLoops();
	}
	
	public void autonomousPeriodic() {
		myLoops.runLoops();
		mSuperstructure.setWantedState(superstructure.state.NEUTRAL);
		mDriveTrain.setWantedState(drivetrain.systemStates.AUTON);
		leftHab.main.run();
	}

	@Override
	public void teleopPeriodic() {
		myLoops.runLoops();
		
		/*********************\
		|* Drivetrain States *|
		\*********************/
		if(mController.TrackTarget()){
			mDriveTrain.setWantedState(drivetrain.systemStates.VISION);
		} else {
			mDriveTrain.setWantedState(drivetrain.systemStates.DRIVE);
		}		

		if(mController.crossbowIntake()){
			mSuperstructure.setWantedState(superstructure.state.INTAKE_H_CB);
		}
		else if(mController.crossbowHold()){
			mSuperstructure.setWantedState(superstructure.state.HOLD_H_CB);
		}
		else if(mController.crossbowDeliver()){
			mSuperstructure.setWantedState(superstructure.state.DELIVER_HATCH);
		}
	}

	@Override
	public void testPeriodic() { 
	}
}
