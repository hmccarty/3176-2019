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


public class HatchIntake {
    
    public static HatchIntake instance = new HatchIntake();
    private Timer timer;
    private Talon roller;
    private Talon actuator;
    private Joystick stick;
    private DigitalInput isDown;
    private DigitalInput isUp;
    private DigitalInput sensor;
    //private controller c;

    
    public static HatchIntake getInstance() {
        return instance;
    }
    
    public HatchIntake() {
        roller = new Talon(constants.HATCH_INTAKE_ROLLER);
        actuator = new Talon(constants.HATCH_INTAKE_ACTUATOR);
        isDown = new DigitalInput(constants.HATCH_INTAKE_DOWN);
        isUp = new DigitalInput(constants.HATCH_INTAKE_UP);
        sensor = new DigitalInput(constants.HATCH_IRSENSOR);
        stick = new Joystick(0);

        timer = new Timer();
        timer.start();

        //c = controller.start();
    }

    public void checkButtons() {
        if (stick.getRawButtonPressed(10)) {
            roller.set(1);
        }
        if (stick.getRawButtonPressed(11)) {
            roller.set(1);
        }
        if (stick.getRawButtonPressed(12)) {
            roller.set(1);
        }
    }

    public boolean deployIntake() {
        if (isDown.get()) {
            actuator.set(0);
            return true;
        }
        else {
            actuator.set(-.3);
            return false;
        }
    }

    public boolean stowIntake() {
        if (isUp.get()) {
            actuator.set(0);
            return true;
        }
        else {
            actuator.set(.3);
            return false;
        }
    }

    public boolean runIntake(double speed) {
        if (sensor.get()) {
            roller.set(0);
            return true;
        }
        else {
            roller.set(speed);
            return false;
        }
    }

    public void zeroAllSensors() {
        
    }

    public void outputToSmartDashboard() {
        SmartDashboard.putBoolean("isDown value: ", isDown.get());
        SmartDashboard.putBoolean("isUp value: ", isUp.get());
        SmartDashboard.putBoolean("sensor value: ", sensor.get());
    }
}