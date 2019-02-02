/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * Add your docs here.
 */

import com.sun.source.tree.ReturnTree;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class M_I2C {
	private static I2C Wire = new I2C(Port.kOnboard, 4);//uses the i2c port on the RoboRIO
														//uses address 4, must match arduino
	private static final int MAX_BYTES = 32;
	
	public void write(String input){//writes to the arduino 
			char[] CharArray = input.toCharArray();//creates a char array from the input string
			byte[] WriteData = new byte[CharArray.length];//creates a byte array from the char array
			for (int i = 0; i < CharArray.length; i++) {//writes each byte to the arduino
				WriteData[i] = (byte) CharArray[i];//adds the char elements to the byte array 
			}
			Wire.transaction(WriteData, WriteData.length, null, 0);//sends each byte to arduino
			System.out.println(input);
	}
	
	// public PixyPacket[] getPixy(){//reads the data from arduino and saves it
	// 	String info[] = read().split("\\|");//everytime a "|" is used it splits the data,
	// 										//and adds it as a new element in the array
	// 	PixyPacket pkt[] = new PixyPacket[Integer.parseInt(info[0])];
	// 	for(int i = 0; i < pkt.length; i++){
	// 		pkt[i] = new PixyPacket();
	// 	}
	// 	creates a new packet to hold the data 
	// 	if(info[0].equals("none") || info[0].equals("")){//checks to make sure there is data 
	// 		pkt.X = -1;//the x val will never be -1 so we can text later in code to make sure 
	// 		pkt.Y = -1;
	// 		pkt.Width = -1;
	// 		pkt.Height = -1;
	// 	}else if(info.length == 3){//if there is an x, y, and area value the length equals 3
	// 		pkt.X = Integer.parseInt(info[1]);
	// 		pkt.Y = Integer.parseInt(info[2]);
	// 		pkt.Width = Integer.parseInt(info[3]);
	// 		pkt.Height = Integer.parseInt(info[4]);
	// 	}
	// 	for (int i = 1; i < info.length; i += 4) {
	// 		for(int k = 0; k < pkt.length; k++){
	// 			pkt[k].setX(i);
	// 			pkt[k].setY(i+1);
	// 			pkt[k].setHeight(i+2);
	// 			pkt[k].setWidth(i+3);
	// 		}
	// 	}
	// 	return pkt;
    // }
    
    // public String getXCoord(){
    //     String xCoord = read();
    //     return xCoord;
    // }
	
	private String read(){//function to read the data from arduino
		byte[] data = new byte[MAX_BYTES];//create a byte array to hold the incoming data
		Wire.read(4, MAX_BYTES, data);//use address 4 on i2c and store it in data
		String output = new String(data);//create a string from the byte array
		int pt = output.indexOf((char)255);
		return (String) output.subSequence(0, pt < 0 ? 0 : pt);//im not sure what these last two lines do
															   //sorry :(
	}
	
	
}

