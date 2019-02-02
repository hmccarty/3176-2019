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


public class HatchIntake {
    static HatchIntake instance = new HatchIntake();
    Joystick Joy;
    Timer Tim1;
    DigitalInput isDown;
    DigitalInput isUp;
    DigitalInput Sen;
    Talon actuator;
    Talon roller;

  public HatchIntake(){
    Joy = new Joystick(0);
    Tim1 = new Timer();
    isDown = new DigitalInput(constants.HATCH_INTAKE_DOWN);
    isUp = new DigitalInput(constants.HATCH_INTAKE_UP);
    Sen = new DigitalInput(constants.HATCH_IR_SENSOR);
    actuator = new Talon(constants.HATCH_INTAKE_ACTUATOR);
    roller = new Talon(constants.HATCH_INTAKE_ROLLER); 
  }

  public static HatchIntake getInstance() {
    return instance; 
  }

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
  }
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