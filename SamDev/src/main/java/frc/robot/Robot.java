package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobot {
  private Joystick stick;
  private HatchIntake mHatchIntake = HatchIntake.getInstance();
  private CargoIntake mCargoIntake = CargoIntake.getInstance();
  //private NeoTesting mNeoTesting = NeoTesting.getInstance();
  private Talon motor;
  private Timer timer;

  
  @Override
  public void robotInit() {
    stick = new Joystick(0);
    motor = new Talon(0);
    timer = new Timer();
    timer.start();
  }

  @Override
  public void robotPeriodic() {
  }
4
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
    double time = 1.0;

    if(timer.get() <= time) {
      motor.setPosition(4096);
    }
    else if(timer.get() > time) {
      motor.setPosition(0);
    }
    else {
      timer.reset();
    }
    
  }
}