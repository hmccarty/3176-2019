package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
  Joystick stick;
  M_I2C i2c;
  
  @Override
  public void robotInit() {
    stick = new Joystick(0);
    i2c = new M_I2C();
   
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
    i2c.write("hello");
  }

  @Override
  public void testPeriodic() {
  }
}
