/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.I2C;

/**
 * Add your docs here.
 */
public class I2C_Coms {
    private I2C sensorHub;
    private byte[] toSend = new byte[1];
    private byte[] recieved = new byte[1];

    public I2C_Coms() {
        sensorHub = new I2C(I2C.Port.kOnboard, 9);
    }

    public int getValue(){
        int returnValue = -1;
        sensorHub.readOnly(recieved, 1);
        returnValue = recieved[0];
        return returnValue;
    }
}