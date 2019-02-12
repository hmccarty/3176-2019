/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.constants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class HatchIntake {
    private static HatchIntake instance = new HatchIntake();
    private  DigitalInput isDown;
    private DigitalInput isUp;
    private DigitalInput Sen;
    private Talon roller;
    private Solenoid Sol1;
    private Solenoid Sol2;
    private DoubleSolenoid SolDo;

    private HatchIntake(){
      isDown = new DigitalInput(constants.HATCH_INTAKE_DOWN);
      isUp = new DigitalInput(constants.HATCH_INTAKE_UP);
      Sen = new DigitalInput(constants.HATCH_IR_SENSOR);
      roller = new Talon(constants.HATCH_INTAKE_ROLLER); 
      Sol1 = new Solenoid(0);
      Sol2 = new Solenoid(1); 
      SolDo = new DoubleSolenoid(1, 2);
  }

    public static HatchIntake getInstance() {
      return instance; 
    }
    public void DoSol() {
      SolDo.set(DoubleSolenoid.Value.kOff);
      SolDo.set(DoubleSolenoid.Value.kForward);
      SolDo.set(DoubleSolenoid.Value.kReverse);
    }
    public boolean getDown() {
      return isDown.get();
    }

    public boolean getUp() {
      return isUp.get();
    }

    public void IntakeUp() {
      if (getDown()) {
        DoSol();
        getDown();
      }
      else {
        getDown();
      }
    }
    public void IntakeDown() {
      if (getUp()) {
        DoSol();
        getUp();
      }
      else {
        getUp();
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
      SmartDashboard.putBoolean("Is Down:", getDown());
      SmartDashboard.putBoolean("Is Up:", getUp());
      SmartDashboard.putBoolean("Sensor:", Sen.get());
      SmartDashboard.putBoolean("Solenoid 1:", Sol1.get());
      SmartDashboard.putBoolean("Solenoid 2:", Sol2.get());

  }
}