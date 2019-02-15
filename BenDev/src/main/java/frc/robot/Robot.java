/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot {

  Joystick joy1 = new Joystick(0);
  CargoClaw grabber = CargoClaw.getInstance();

  @Override
  public void robotInit() {
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopPeriodic() {
    if (joy1.getRawButton(2)) {
      grabber.off();
    }
    if (joy1.getRawButton(3)) {
      grabber.open();
    }
    if (joy1.getRawButton(4)) {
      grabber.close();
    }
    if(joy1.getRawButton(5)) {
      grabber.testClawSideTransfer();
    }
  }

  @Override
  public void testPeriodic() {
  }
}
