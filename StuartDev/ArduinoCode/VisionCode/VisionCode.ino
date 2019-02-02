#include <Pixy2.h>
#include <Wire.h>
#include <math.h>

String piOutput = "none";//string to be sent to the robot           
String input = "blank";  //string received from the robot
Pixy2 pixy;
int widthAvgSum;
int widthAvg;
int smallBlocks[] = {};
int block1Ratio;
int block2Ratio;
double camOffset = -14.5*(PI/180);
double xAdjusted;
double yAdjusted;
double yCalc;

void setup(){
  Serial.begin(9600);
  Wire.begin(4);                // join i2c bus with address #4 as a slave device
//  Wire.onReceive(receiveEvent); // Registers a function to be called when a slave device receives a transmission from a master
//  Wire.onRequest(requestEvent); // Register a function to be called when a master requests data from this slave device
  pixy.init();
}

void loop(){
//  pixy.ccc.getBlocks();
//  yCalc = double(pixy.ccc.blocks[0].m_y)*-1+207;
//  xAdjusted = (double(pixy.ccc.blocks[0].m_x)*cos(camOffset))-(yCalc*sin(camOffset));
//  yAdjusted = (yCalc*cos(camOffset))+(double(pixy.ccc.blocks[0].m_x)*sin(camOffset));
//
//  for(int i = 0; i < pixy.ccc.blocks.numBlocks; i++) {
//     widthAvgSum += pixy.ccc.blocks[i];
//  }
//  widthAvg = widthAvgSum / pixy.ccc.blocks.numBlocks;
//  for(int i = 0; i < pixy.ccc.blocks.numBlocks; i++) {
//    if(pixy.ccc.blocks[i] < widthAvg) {
//      smallBlocks += pixy.ccc.blocks[i];
//    }
//  }
// 
//
//  piOutput = String(pixy.ccc.numBlocks);
//  piOutput += "|";
//  for(int i = 0; i < pixy.ccc.numBlocks; i++) {
//    piOutput += "X " + String(pixy.ccc.blocks[i].m_x);
//    piOutput += "|";
//    piOutput += "Y " + String(pixy.ccc.blocks[i].m_y);
//    piOutput += "|";
//    piOutput += "Height " + String(pixy.ccc.blocks[i].m_height);
//    piOutput += "|";
//    piOutput += "Width " + String(pixy.ccc.blocks[i].m_width);
//    piOutput += "|";
//  }
//  Serial.println(piOutput);
}

void requestEvent(){//called when RoboRIO request a message from this device
  Wire.write(piOutput.c_str()); //writes data to the RoboRIO, converts it to string


}

void receiveEvent(int bytes){//called when RoboRIO "gives" this device a message
  Serial.println(bytes);
}

