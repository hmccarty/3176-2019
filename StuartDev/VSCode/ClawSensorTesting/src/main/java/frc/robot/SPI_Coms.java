/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;

/**
 * Add your docs here.
 */
public class SPI_Coms {
    SPI mSPI;

    public SPI_Coms() {
        mSPI = new SPI(Port.kOnboardCS0);
    }

    public int getInt() {
        int buffer[] = {};
        mSPI.readAutoReceivedData(buffer, 1, 50);
        return buffer[0];
    }
}
