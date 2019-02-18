/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.IterativeRobot;
import frc.robot.*;

public class Robot extends IterativeRobot {
 // VL53L0X sensor;

  @Override
  public void robotInit() {
    //sensor = new VL53L0X();
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
    //System.out.println(sensor.getVL53L0XData());
  }

  @Override
  public void testPeriodic() {
  }
}