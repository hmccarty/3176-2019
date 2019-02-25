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
        mSPI.setClockRate(300000);
        mSPI.setMSBFirst();
        mSPI.setSampleDataOnRising();
        mSPI.setClockActiveHigh();
        mSPI.setChipSelectActiveLow();
    }

    public int getInt() {
        byte buffer[] = new byte[2];
        // mSPI.forceAutoRead();
        // mSPI.readAutoReceivedData(buffer, 0, 50);
        mSPI.read(true, buffer, 1);
        return buffer[0];
    }
}
