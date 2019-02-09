#include "Adafruit_VL53L0X.h"
#include <Wire.h>

//Adafruit_VL53L0X lox = Adafruit_VL53L0X();
String piOutput = "none";
String input = "blank";

void setup() {
  Serial.begin(115200);
  Wire.begin();
  // wait until serial port opens for native USB devices
//  while (! Serial) {
//    delay(1);
//  }
//  
//  Serial.println("Adafruit VL53L0X test");
//  if (!lox.begin()) {
//    Serial.println(F("Failed to boot VL53L0X"));
//    while(1);
//  }
//  // power 
//  Serial.println(F("VL53L0X API Simple Ranging example\n\n")); 
}


void loop() {
  //VL53L0X_RangingMeasurementData_t measure;
//    
//  Serial.print("Reading a measurement... ");
//  lox.rangingTest(&measure, false); // pass in 'true' to get debug data printout!
//
//  if (measure.RangeStatus != 4) {  // phase failures have incorrect data
//    Serial.print("Distance (mm): "); Serial.println(measure.RangeMilliMeter);
    piOutput = String(3176);
//  } else {
//    Serial.println(" out of range ");
//  }
    Serial.println(piOutput);
  
}

void requestEvent(){//called when RoboRIO request a message from this device
  Wire.write(piOutput.c_str()); //writes data to the RoboRIO, converts it to string
 
}

void receiveEvent(int bytes){//called when RoboRIO "gives" this device a message
}
