/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class HatchIntake {
    
    private static HatchIntake instance = new HatchIntake();
    private DigitalInput isDown;
    private DigitalInput isUp;
    private DigitalInput sensor;
    private Talon roller;
    private Joystick stick;
    private Solenoid sol1;
    private Solenoid sol2;
    private DoubleSolenoid dSol;
    private Timer timer;
    //private controller c;

    
    public static HatchIntake getInstance() {
        return instance;
    }
    
    public HatchIntake() {
        isDown = new DigitalInput(constants.HATCH_INTAKE_DOWN);
        isUp = new DigitalInput(constants.HATCH_INTAKE_UP);
        sensor = new DigitalInput(constants.HATCH_IRSENSOR);
        roller = new Talon(constants.HATCH_INTAKE_ROLLER);
        stick = new Joystick(0);
        sol1 = new Solenoid(0);
        sol2 = new Solenoid(1);
        dSol = new DoubleSolenoid(0, 1);
        timer = new Timer();
        timer.start();

        //c = controller.getInstance();
    }

    public boolean getUp() {
        return isUp.get();
    }

    public boolean getDown() {
        return isDown.get();
    }
    
    public boolean getSensor() {
        return sensor.get();
    }

    public void deployIntake() {
        if (getDown()) {
            sol1.set(false);
            sol2.set(true);
            getDown();
        }
        else {
            sol1.set(false);
            sol2.set(false);
            getDown();
        }
    }

    public void stowIntake() {
        if (getUp()) {
            sol1.set(true);
            sol2.set(false);
            getUp();
        }
        else {
            sol1.set(false);
            sol2.set(false);
            getUp();
        }
    }

    public void runIntake(double speed) {
        if (sensor.get()) {
            roller.set(0);
            getSensor();
        }
        else {
            roller.set(speed);
            getSensor();
        }
    }

    public void zeroAll() {
        sol1.set(false);
        sol2.set(false);
        roller.set(0);
    }

    public void outputToSmartDashboard() {
        SmartDashboard.putBoolean("isDown value: ", isDown.get());
        SmartDashboard.putBoolean("isUp value: ", isUp.get());
        SmartDashboard.putBoolean("sensor value: ", sensor.get());
    }
}
