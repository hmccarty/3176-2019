/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.subsystem.*;
import frc.subsystem.superstructure.commands;

public class robot extends TimedRobot {
  controller c;
  superstructure s; 

  @Override
  public void robotInit() {
    c = controller.getInstance();
    s = superstructure.getInstance();
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopPeriodic() {
    if(c.getCargoIntake(0)){
      s.addCommand(commands.INTAKE_CARGO);
    }
  }

  @Override
  public void testPeriodic() {}
}
