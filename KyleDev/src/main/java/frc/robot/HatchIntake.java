/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.constants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Solenoid;

public class HatchIntake {
    private static HatchIntake instance = new HatchIntake();
    private static Joystick Joy;
    private static Timer Tim1;
    private static DigitalInput isDown;
    private static DigitalInput isUp;
    private static DigitalInput Sen;
    private static Talon actuator;
    private static Talon roller;
    private static Solenoid Sol1;
    private static Solenoid Sol2;

    private  HatchIntake(){
    Joy = new Joystick(0);
    Tim1 = new Timer();
    isDown = new DigitalInput(constants.HATCH_INTAKE_DOWN);
    isUp = new DigitalInput(constants.HATCH_INTAKE_UP);
    Sen = new DigitalInput(constants.HATCH_IR_SENSOR);
    actuator = new Talon(constants.HATCH_INTAKE_ACTUATOR);
    roller = new Talon(constants.HATCH_INTAKE_ROLLER); 
    Sol1 = new Solenoid(0);
    Sol2 = new Solenoid(1);
  }

  public static HatchIntake getInstance() {
    return instance; 
  }

  public boolean getDown() {
    return isDown.get();
  }

  public boolean getUp() {
    return isUp.get();
  }

  public void IntakeUp() {
    if (getDown()) {
      Sol1.set(true);
      Sol2.set(false);
      getDown();
    }
    else {
      getDown();
    }
  }
  public void IntakeDown() {
    if (getUp()) {
      Sol1.set(false);
      Sol2.set(true);
      getUp();
    }
    else {
      getUp();
    }
  }   

/*
  public boolean IntakeUp() {
    if(isDown.get()) {
        actuator.set(0);
        return true; 
    }
    else {
        actuator.set(-.3);
        return false; 
    }
  }

  public boolean IntakeDown() {
    if(isUp.get()) {
        actuator.set(0);
        return true;
    }
    else {
        actuator.set(.3);
        return false;
    }
  } */
  public boolean IntoIntake(double speed) {
    if (Sen.get()) {
      roller.set(0);
      return true;
    }
    else {
      roller.set(speed);
      return false;
    } 
  }
  public void ZeroSensors() {
    
  }
  public void OutputToDash() {
    SmartDashboard.putBoolean("Is Down:", isDown.get());
    SmartDashboard.putBoolean("Is Up:", isUp.get());
    SmartDashboard.putBoolean("Sensor:", Sen.get());
  }
}