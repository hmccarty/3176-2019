/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6713.robot;

import Auton.Autos.*;
import Auton.Autos.Deprecated.TrajectoryTest;
import Subsystem.*;
import Subsystem.Intake.systemStates;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	private static final String auto1 = "auto1";
	private static final String auto2 = "auto2";
	private static final String auto3 = "auto3";
	private static final String auto4 = "auto4";
	private static final String auto5 = "auto5";
	private static final String auto6 = "auto6";
	private static final String auto7 = "auto7";
	private static final String auto8 = "auto8";
	private Loop_Manager myLoops = Loop_Manager.getInstance();
	private Drivetrain driveTrain = Drivetrain.getInstance(); 
	private Controller controller = Controller.getInstance();
	private LED led = LED.getInstance();
	private Elevator elevator = Elevator.getInstance();
	private Intake intake = Intake.getInstance();
	int testID = 0;
	String gameData;
	private boolean isIntakeOpenLoop;
	private boolean isElevatorOpenLoop;
	@Override
	public void robotInit() {
		driveTrain.registerLoop(); 
		intake.registerLoop(); 
		led.registerLoop();
		elevator.registerLoop();
		myLoops.startLoops();
		CameraServer.getInstance().startAutomaticCapture();
		isIntakeOpenLoop = false;
		isElevatorOpenLoop = false;
		m_chooser.addObject("middle switch", auto1);
		m_chooser.addObject("middle 2 switch", auto6);
		m_chooser.addObject("left switch", auto2);
		m_chooser.addObject("right switch", auto3);
		m_chooser.addObject("drive straight", auto4);
		m_chooser.addObject("scale left", auto5);
		m_chooser.addObject("scale right", auto7);
		m_chooser.addObject("traj test", auto8);
		m_chooser.addDefault("default", "default");
		SmartDashboard.putData("Auto choices", m_chooser);
	}
	
	@Override
	public void autonomousInit() {
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		Auto.setGameData(gameData);
		myLoops.runLoops();
	}
	
	@Override
	public void autonomousPeriodic() {
		myLoops.runLoops();
		String selected = m_chooser.getSelected();
		if(selected.equals(auto1))
		{
			if(gameData.substring(0,1).equals("R"))
			{
				middleSwitchRight.main.run();
			}
			else
			{
				middleSwitchLeft.main.run();
			}
		}
		else if(selected.equals(auto2))
		{
			sideSwitchLeft.main.run();
		}
		else if(selected.equals(auto3))
		{
			sideSwitchRight.main.run();
		}
		else if(selected.equals(auto4))
		{
			driveStraight.main.run();
		}
		else if(selected.equals(auto5))
		{
			if(gameData.substring(1,2).equals("L"))
			{
				scaleLeftStart.main.run();
			}
			else if(gameData.substring(0,1).equals("L"))
			{
				sideSwitchLeft.main.run();
			}
			else
			{
				driveStraight.main.run();
			}
			
		}
		else if(selected.equals(auto6))
		{
			if(gameData.substring(0,1).equals("R"))
			{
				middleSwitch2Right.main.run();
			}
			else
			{
				middleSwitch2Left.main.run();
			}
		}
		else if(selected.equals(auto7))
		{
			if(gameData.substring(1,2).equals("R"))
			{
				scaleRightStart.main.run();
			}
			else if(gameData.substring(0,1).equals("R"))
			{
				sideSwitchRight.main.run();
			}
			else
			{
				driveStraight.main.run();
			}
		}
		else if(selected.equals(auto8))
		{
			TrajectoryTest.main.run();
		}
	}

	@Override
	public void teleopPeriodic() {
		myLoops.runLoops();
		
		if(controller.executeAutoButton()) { driveTrain.setSystemState(Drivetrain.systemStates.AUTON);}
		else if(controller.getVisionTracking()) { 
			PixyCam.getInstance().track_cube();
			driveTrain.setSystemState(Drivetrain.systemStates.VISION);
		}
		else if(controller.secretSauce()) {
			driveTrain.setSystemState(Drivetrain.systemStates.SECRET_SAUCE);
		}
		else { driveTrain.setSystemState(Drivetrain.systemStates.DRIVE);}

		/**
		 * Drive Train States
		 */
		controller.checkIsMaster();
		if(controller.getSlowFieldCentricButton() == true)
		{
			driveTrain.swerve(controller.getForward(), 
					controller.getStrafe(), 
					controller.getRotation()* 1.15, 
					Drivetrain.driveCoords.FIELDCENTRIC, 
					Drivetrain.driveType.PERCENTPOWER);
		}
		else if(controller.getSlowRobotCentricButton() == true)
		{
			driveTrain.swerve(controller.getForward() *Constants.MAXSLOWPERCENTSPEED, 
					controller.getStrafe() *Constants.MAXSLOWPERCENTSPEED, 
					controller.getRotation()* 1.15 *Constants.MAXSLOWPERCENTSPEED, 
					Drivetrain.driveCoords.ROBOTCENTRIC, 
					Drivetrain.driveType.PERCENTPOWER);
		}
		else
		{
			driveTrain.swerve(controller.getForward()*Constants.MAXSLOWPERCENTSPEED,
					controller.getStrafe() *Constants.MAXSLOWPERCENTSPEED, 
					controller.getRotation() * 1.15 * Constants.MAXSLOWPERCENTSPEED, 
					Drivetrain.driveCoords.FIELDCENTRIC, 
					Drivetrain.driveType.PERCENTPOWER);
		}
		
		/**
		 * Intake States
		 */
		if(controller.Stow() == true && controller.unStow() == true && isIntakeOpenLoop == false)
		{
			isIntakeOpenLoop = true;
			intake.setOpenLoopMode(true);
		}
		if(controller.getIntakeButton() || controller.getVisionTracking()) {intake.setWantedState(systemStates.Intaking);} 
		else if(controller.getOuttakeButton()) {intake.setWantedState(systemStates.Scoring);}
		else if(controller.getSlowOuttakeButton()) {intake.setWantedState(systemStates.SlowScoring);}
		else if(controller.unjamButton()) {intake.setWantedState(systemStates.UnJamming);}
		else {intake.setWantedState(systemStates.Neutral);}
		
		if(controller.Stow()) {intake.setPosition(Intake.getInstance().neutralPosition);}
		else if(controller.unStow()) {Intake.getInstance().setPosition(intake.downPosition);}
		else
		{
			if(controller.getintakePositionJoystick()>.05 || controller.getintakePositionJoystick()<-.05)
			{
				intake.setPosition(intake.getCurrPosition()+controller.getintakePositionJoystick()*2000);
			}
		}
		/**
		 * Elevator States
		 */
		if(controller.elevatorHigh() == true && controller.elevatorMid() == true && isElevatorOpenLoop == false)
		{
			isElevatorOpenLoop = true;
			elevator.setWantedState(Elevator.systemStates.OPEN_LOOP);
		}
		if(!isElevatorOpenLoop)
		{
			elevator.setWantedState(Elevator.systemStates.POSITION_FOLLOW);
		}
		if(controller.elevatorHigh()) {elevator.setWantedFloor(Constants.SCALEHIGHHEIGHT); }
		else if (controller.elevatorMid()) {elevator.setWantedFloor(Constants.SCALEMIDHEIGHT);}
		else if (controller.elevatorLow()) {elevator.setWantedFloor(Constants.SCALELOWHEIGHT);}
		else if (controller.elevatorSwitch()) {elevator.setWantedFloor(Constants.SWITCHHEIGHT);}
		else 
		{
			if(controller.elevatorPositionJoystick()>.05||controller.elevatorPositionJoystick()<-.05)
			{
			elevator.setWantedFloor(elevator.getHeight()+(controller.elevatorPositionJoystick()*7000.0));
			}
		}
		
	}
	
	@Override
	public void testPeriodic() { 
		myLoops.runLoops();
		/*
		 * Runs the Pre-Match Checklist
		 */
		SmartDashboard.putNumber("test ID", testID);
		if(controller.getSlowFieldCentricButton()) {testID++;}
		switch(testID) {
		case 1: //Drive Forwards and Field-Centric test
			driveTrain.swerve(1.0, 0, 0, 
					Drivetrain.driveCoords.FIELDCENTRIC, 
					Drivetrain.driveType.PERCENTPOWER);
		case 2: //Drive Backwards
			driveTrain.swerve(-1.0, 0.0, 0.0);
		case 3: //Strafe Right
			driveTrain.swerve(0.0, 1.0, 0.0);
		case 4: //Strafe Left
			driveTrain.swerve(0.0, -1.0, 0.0);
		case 5: //Rotate Clockwise
			driveTrain.swerve(0.0, 0.0, 0.5);
		case 6: //Rotate Counter-Clockwise
			driveTrain.swerve(0.0, 0.0, -0.5);
		case 7:
			driveTrain.swerve(0, 0, 0);
		case 8: //Intake In
			intake.setWantedState(Intake.systemStates.Intaking);
		case 9: //Intake Out
			
			
			intake.setWantedState(Intake.systemStates.Scoring);
		case 10: //Intake UnStow
			intake.setWantedState(Intake.systemStates.Neutral);
			intake.setPosition(intake.downPosition);
		case 11: //Intake Stow
			intake.setPosition(0.0);
		case 12: //Elevator High Position
			elevator.setWantedState(Elevator.systemStates.POSITION_FOLLOW);
			elevator.setWantedFloor(Constants.SCALEHIGHHEIGHT);
		case 13: //Elevator Med Position
			elevator.setWantedFloor(Constants.SCALEMIDHEIGHT);
		case 14: //Elevator in Rest Position
			elevator.setWantedFloor(Constants.SCALELOWHEIGHT);
		case 15: //Set Wheels in Forward Direction
			driveTrain.swerve(.1, 0.0, 0.0,
					Drivetrain.driveCoords.FIELDCENTRIC,
					Drivetrain.driveType.PERCENTPOWER);
		case 16:
			driveTrain.swerve(0, 0, 0);
		}	
	}
}
