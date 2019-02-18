package Subsystem;

import Vision.PixyException;
import Vision.PixyPacket;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 * Handles interaction with the PixyCam, and translates raw data into comprehensive variables.
 * All done over the I2C port. 
 * @author Harrison McCarty, Drew Hatfield, Stewart G, and Team 1024
 */
public class PixyCam {
	private static PixyCam instance = new PixyCam();
	
	PixyPacket values;
	I2C pixy;
	PixyPacket[] packets;
	PixyException pixyExc;
	String print;
	
	private double currentHorizontal, currentVertical, currentWidth, currentHeight = 0;
	private int arrayLevel = 0;
	double[] horizontalValues = {0,0,0,0,0,0,0};
	double averageHorizontal = 0;
	
	double[] areaValues = {0,0,0,0,0,0,0};
	double averageArea = 0;
	
	double[] yValues = {0,0,0,0,0,0,0};
	double averageY = 0;

	
	public PixyCam() {
		pixy = new I2C(Port.kOnboard, 0x54);
		packets = new PixyPacket[7];
		pixyExc = new PixyException(print);
	}
	
	public static PixyCam getInstance()
	{
			return instance;
		
	}

	public void track_cube() {
		try {
			readPacket(1);
		} catch (PixyException e) {
			e.printStackTrace();
		}
		setAvgX();
		setAvgY();
		setAvgArea();
		SmartDashboard.putNumber("Pixy X", getAvgX());
		SmartDashboard.putNumber("Pixy Y", getY());
		//SmartDashboard.putNumber("Cube 2", currentHorizontal2);
		arrayLevel++; 
		if(arrayLevel >6) {
			arrayLevel = 0; 
		}
	}
	
	public int cvt(byte upper, byte lower) {
		return (((int)upper & 0xff) << 8) | ((int)lower & 0xff);
	}
	
	/**
	 * This method gathers vision data, then parses that data, and defines the data into int variables.
	 */
	public PixyPacket readPacket(int Signature) throws PixyException { 
		int Checksum;												   
		int Sig;
		byte[] rawData = new byte[32];
		
		try{
			pixy.readOnly(rawData, 32);
		} catch (RuntimeException e){}
		
		if(rawData.length < 32){
			System.out.println("Vision: Byte array length is broken");
			return null;
		}
		
		for (int i = 0; i <= 15; i++) {
			int syncWord = cvt(rawData[i+1], rawData[i+0]); //Parse first 2 bytes
			if (syncWord == 0xaa55) { //Check is first 2 bytes equal a "sync word", which indicates the start of a packet of valid data
				syncWord = cvt(rawData[i+3], rawData[i+2]); //Parse the next 2 bytes
				if (syncWord != 0xaa55){ //Shifts everything in the case that one syncword is sent
					i -= 2;
				}
				
				//This next block parses the rest of the data
				Checksum = cvt(rawData[i+5], rawData[i+4]);
				Sig = cvt(rawData[i+7], rawData[i+6]);
				if(Sig <= 0 || Sig > packets.length){
					break;
				}
				
				packets[Sig - 1] = new PixyPacket();
				packets[Sig - 1].X = cvt(rawData[i+9], rawData[i+8]);
				currentHorizontal = (double)packets[Sig-1].X;
				
				packets[Sig - 1].Y = cvt(rawData[i+11], rawData[i+10]);
				currentVertical = (double)packets[Sig-1].Y;
				
				packets[Sig - 1].Width = cvt(rawData[i+13], rawData[i+12]);
				currentWidth = (double)packets[Sig-1].Width;
				
				packets[Sig - 1].Height = cvt(rawData[i+15], rawData[i+14]);
				currentHeight = (double)packets[Sig-1].Height;
				
//				int Sig2 = cvt(rawData[i+17], rawData[i+16]);
//				packets[Sig - 1] = new PixyPacket();
//				packets[Sig - 1].X = cvt(rawData[i+17], rawData[i+16]);
//				currentHorizontal2 = (double)packets[Sig-1].X;
//				
//				packets[Sig - 1].Y = cvt(rawData[i+19], rawData[i+18]);
//				currentVertical2 = (double)packets[Sig-1].Y;
//				
//				packets[Sig - 1].Width = cvt(rawData[i+21], rawData[i+20]);
//				currentWidth2 = (double)packets[Sig-1].Width;
//				
//				packets[Sig - 1].Height = cvt(rawData[i+23], rawData[i+22]);
//				currentHeight2 = (double)packets[Sig-1].Height;
				
				
				//Checks whether the data is valid using the checksum *This if block should never be entered*
				if (Checksum != Sig + packets[Sig - 1].X + packets[Sig - 1].Y + packets[Sig - 1].Width + packets[Sig - 1].Height) {
					packets[Sig - 1] = null;
					throw pixyExc;
				}
				break;
			}
		}
		//Assigns our packet to a temp packet, then deletes data so that we dont return old data
		PixyPacket pkt = packets[Signature - 1];
		packets[Signature - 1] = null;
		return pkt;
	}
	
	public double getX() {return currentHorizontal;}
	public double getY() {return currentVertical;}
	public double getWidth() {return currentWidth;}
	public double getHeight() {return currentHeight;}
	public double getArea() {return (currentHeight*currentWidth);}
	
	public double getAvgArea() {return averageArea;}
	public double getAvgX() {return averageHorizontal;}
	public double getAvgY() {return averageY;}
	
	private void setAvgX() {
		horizontalValues[arrayLevel] = getX();
		averageHorizontal = (horizontalValues[0] + horizontalValues[1] + horizontalValues[2] + horizontalValues[3] +horizontalValues[4] + horizontalValues[5] +horizontalValues[6]) / 7;
	}
	private void setAvgArea() {
		areaValues[arrayLevel] = getArea();
		averageArea = (areaValues[0] + areaValues[1] + areaValues[2] + areaValues[3] +areaValues[4] + areaValues[5] +areaValues[6]) / 7;
	}
	private void setAvgY() {
		yValues[arrayLevel] = getY();
		averageY = (areaValues[0] + areaValues[1] + areaValues[2] + areaValues[3] +areaValues[4] + areaValues[5] +areaValues[6]) / 7;
	}
}