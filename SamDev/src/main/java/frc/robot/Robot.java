package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot {
  Joystick stick;
  private HatchIntake mHatchIntake = HatchIntake.getInstance();
  private CargoIntake mCargoIntake = CargoIntake.getInstance();
  private NeoTesting mNeoTesting = NeoTesting.getInstance();
  
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

  @Override
  public void autonomousPeriodic() {
  }

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

  @Override
  public void testPeriodic() {
  }
}