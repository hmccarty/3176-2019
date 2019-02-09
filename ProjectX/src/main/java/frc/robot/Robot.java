/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.subsystem.*;
import frc.subsystem.superstructure;
import frc.subsystem.drivetrain;

public class Robot extends TimedRobot {
  controller mController;
  superstructure mSubsystemManager; 
  drivetrain mDrivetrain; 

  @Override
  public void robotInit() {
    mController = controller.getInstance();
    mSubsystemManager = superstructure.getInstance();

  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopPeriodic() {
    if(mController.getCargoIntake(0)){
      //mSubsystemManager.setWantedState(mSubsystemManager.state.INTAKE_C_ROLLER);
    }
  }

  @Override
  public void testPeriodic() {}
}
