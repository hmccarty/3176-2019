package frc.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class M_I2C {
	private static I2C Wire = new I2C(Port.kOnboard, 4);//uses the i2c port on the RoboRIO
														//uses address 4, must match arduino
	private static final int MAX_BYTES = 32;
	
	public void write(String Speed){//writes to the arduino 
			char[] CharArray = Speed.toCharArray();//creates a char array from the input string
			byte[] WriteData = new byte[CharArray.length];//creates a byte array from the char array
			for (int i = 0; i < CharArray.length; i++) {//writes each byte to the arduino
				WriteData[i] = (byte) CharArray[i];//adds the char elements to the byte array 
            }
			Wire.transaction(WriteData, WriteData.length, null, 0);//sends each byte to arduino
			
        }
    }