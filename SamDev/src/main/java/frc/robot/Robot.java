/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.HatchIntake;
import frc.robot.CargoIntake;
import frc.robot.NeoTesting;

public class Robot extends IterativeRobot {
  Joystick stick;
  private HatchIntake mHatchIntake = HatchIntake.getInstance();
  private CargoIntake mCargoIntake = CargoIntake.getInstance();
  private NeoTesting mNeoTesting = NeoTesting.getInstance();
  
  @Override
  public void robotInit() {
    //stick = new Joystick(0);
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    mNeoTesting.run();
    // if (stick.getRawButton(4)) {
    //   mHatchIntake.deployIntake();
    // } else if (stick.getRawButton(5)) {
    //   mHatchIntake.stowIntake();
    // }
    // if (stick.getY() < 0.1 || stick.getY() > 0.1) {
    //   mHatchIntake.runIntake(stick.getY());
    // }

    // if (stick.getRawButton(4)) {
    //   mCargoIntake.deployIntake();+
    // } else if (stick.getRawButton(5)) {
    //   mCargoIntake.stowIntake();
    // }
    // if (stick.getY() < 0.1 || stick.getY() > 0.1) {
    //   mCargoIntake.runIntake(stick.getY());
    // }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}