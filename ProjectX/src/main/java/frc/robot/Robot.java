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
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.auton.*;

import edu.wpi.first.networktables.*;

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
	UsbCamera camera1;
	UsbCamera camera2;
	private static NetworkTableEntry bRightY;
	private static NetworkTableEntry angle; 
	private static NetworkTableEntry distance; 
	crossbow mCrossbow = crossbow.getInstance();
	@Override
	public void robotInit() {
		mDriveTrain.registerLoop(); 
		mSuperstructure.registerLoop();
		myLoops.startLoops();
		CameraServer.getInstance().startAutomaticCapture();
		
		NetworkTableInstance inst = NetworkTableInstance.getDefault();
		NetworkTable table = inst.getTable("SmartDashboard");
		//x = table.getEntry("Block Center X");
		area = table.getEntry("Block Area");
		bLeftX = table.getEntry("Point 2 X Coord");
		bLeftY = table.getEntry("Point 2 Y Coord");
		bRightX = table.getEntry("Point 3 X Coord");
		bRightY = table.getEntry("Point 3 Y Coord");
		angle = table.getEntry("Angle");
		distance = table.getEntry("distance");
		isIntakeOpenLoop = false;
		isElevatorOpenLoop = false;
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
		//mSuperstructure.setWantedState(superstructure.state.DELIVER_HATCH);
		//mCrossbow.set();
		
	}

	public static double getDistance(){
		return distance.getDouble(-1.0);
	}

	public static double getAngle(){
		return angle.getDouble(0);
	}
	
	// public static double getType(){
	// }

	@Override
	public void testPeriodic() { 
	}
}
