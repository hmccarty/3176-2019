package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot {
  Elevator elevator;
  Joystick stick;

  @Override
  public void robotInit() {
    elevator = new Elevator(0);
    stick = new Joystick(0);

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
    elevator.run(stick.getY());
    elevator.getPostition();
  }

  @Override
  public void testPeriodic() {
  }
}
