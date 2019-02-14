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

import edu.wpi.first.networktables.*;

public class Robot extends IterativeRobot {
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	private loopmanager myLoops = loopmanager.getInstance();
	private drivetrain mDriveTrain = drivetrain.getInstance(); 
	private controller mController = controller.getInstance();
	int testID = 0;
	String gameData;
	private boolean isIntakeOpenLoop;
	private boolean isElevatorOpenLoop;
	private static NetworkTableEntry x;
	private static NetworkTableEntry area;
	private static NetworkTableEntry bLeftX;
	private static NetworkTableEntry X;
	private static NetworkTableEntry bLeftY;
	UsbCamera camera1;
	UsbCamera camera2;
	private static NetworkTableEntry bRightY;
	private static NetworkTableEntry angle; 
	private static NetworkTableEntry distance; 
	@Override
	public void robotInit() {
		mDriveTrain.registerLoop(); 
		myLoops.startLoops();
		CameraServer.getInstance().startAutomaticCapture();
		
		NetworkTableInstance inst = NetworkTableInstance.getDefault();
		NetworkTable table = inst.getTable("SmartDashboard");
		//x = table.getEntry("Block Center X");
		area = table.getEntry("Block Area");
		bLeftX = table.getEntry("Point 2 X Coord");
		bLeftY = table.getEntry("Point 2 Y Coord");
		X = table.getEntry("X");
		bRightY = table.getEntry("Point 3 Y Coord");
		angle = table.getEntry("Angle");
		distance = table.getEntry("distance");
		isIntakeOpenLoop = false;
		isElevatorOpenLoop = false;
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
	}

	public static double getDistance(){
		return distance.getDouble(-1.0);
	}

	public static double getAngle(){
		return angle.getDouble(0);
	}
	
	public static double getX(){
		return X.getDouble(0);
	}

	@Override
	public void testPeriodic() { 
	}
}
