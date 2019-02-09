/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.Util.PIDLoop;
import frc.robot.constants;

public class CargoIntake {
    
    private static CargoIntake instance = new CargoIntake();
    private DigitalInput isDown;
    private PIDLoop intakeControlLoop;
    private Encoder encoder;
    private Talon roller;
    private Talon actuator;
    private Joystick stick;
    private Timer timer;
    //private controller c;
    
    public static CargoIntake getInstance() {
        return instance;
    }
    
    private CargoIntake() {
        isDown = new DigitalInput(constants.CARGO_INTAKE_DOWN);
        intakeControlLoop = new PIDLoop(constants.CARGO_KP,
                                        constants.CARGO_KI,
                                        constants.CARGO_KD,
                                        1);
        encoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
        roller = new Talon(constants.CARGO_INTAKE_ROLLER);
        actuator = new Talon(constants.CARGO_INTAKE_ACTUATOR);
        stick = new Joystick(0);
        timer = new Timer();
        timer.start();

        //c = controller.getInstance();
    }

    public boolean getDown() {
        return isDown.get();
    }

    public boolean getUp() {
        if (encoder.get() == 90) {
            return true;
        } else {
            return false;
        }
    }
    
    public void deployIntake() {
        if (getDown()) {
            actuator.set(0);
            encoder.reset();
            getDown();
        } else {
            actuator.set(intakeControlLoop.returnOutput(-1));
            getDown();
        }
    }

    public void stowIntake() {
        if (getUp()) {
            actuator.set(0);
            getUp();
        }
        else {
            actuator.set(intakeControlLoop.returnOutput(1));
            getUp();
        }
    }

    public void runIntake(double speed) {
        roller.set(speed);
    }

    public void zeroAll() {
        actuator.set(0);
        roller.set(0);
        encoder.reset();
    }

    public void outputToSmartDashboard() {
        SmartDashboard.putBoolean("getDown value: ", getDown());
        SmartDashboard.putBoolean("getUp value: ", getUp());
    }
}