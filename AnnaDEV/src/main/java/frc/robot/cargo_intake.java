/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class cargo_intake {

    private static cargo_intake instance = new cargo_intake();    
    Talon motorIn; 
    Talon motorPivot;
    Joystick stick;
 
    public static cargo_intake getInstance(){
        return instance;
    }

    private cargo_intake(){

        motorIn = new Talon(0); 
        motorPivot = new Talon(1);
        stick =  new Joystick(0);

    }

    public void checkButton(){
        if (stick.getRawButtonPressed(11) == true){
            
            motorIn.set(-1);

        }
    }

  
}
            // motorPivot.set(-1);
            // motorPivot.set(0);
            // motorIn.set(-1);
            // motorIn.set(0);
            // motorPivot.set(1);
            // motorPivot.set(0);
            // motorIn.set(-1);
            // motorIn.set(0);