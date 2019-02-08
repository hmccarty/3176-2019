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

public class Robot extends IterativeRobot {
  Joystick stick;
  private HatchIntake mHatchIntake = HatchIntake.getInstance();
  private CargoIntake mCargoIntake = CargoIntake.getInstance();
  
  @Override
  public void robotInit() {
    stick = new Joystick(0);
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
//     if (stick.getRawButton(2)) {
//       mHatchIntake.deployIntake();
//     } else if (stick.getRawButton(3)) {
//       mHatchIntake.stowIntake();
//     }
//     if (stick.getRawButton(2)) {
//       mCargoIntake.deployIntake();
//     } else if (stick.getRawButton(3)) {
//       mCargoIntake.stowIntake();
//     }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
