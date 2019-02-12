/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;

public class NeoTesting {
    private static NeoTesting instance = new NeoTesting();
    private Joystick stick;
    private Spark spark;

    private NeoTesting() {
        stick = new Joystick(0);
        spark = new Spark(0);
    }

    public static NeoTesting getInstance() {
        return instance;
    }

    public void run() {
        spark.set(stick.getY());
    }
}