#include "pins_arduino.h"
#include "Adafruit_VL53L0X.h"

char buf [2] = {5};
volatile byte pos;
volatile boolean process_it;
boolean hasRan = false;
Adafruit_VL53L0X lox = Adafruit_VL53L0X();
int data = -1;
int lastEventTime;


void setup (void)
{
 Serial.begin (9600);   // debugging
 while (! Serial) {
    delay(1);
  }
  
  Serial.println("Adafruit VL53L0X test");
  if (!lox.begin()) {
    Serial.println(F("Failed to boot VL53L0X"));
    while(1);
  }
  // power 
  Serial.println(F("VL53L0X API Simple Ranging example\n\n")); 

 // have to send on master in, *slave out*
 pinMode(MISO, OUTPUT);
 
 // turn on SPI in slave mode
 SPCR |= _BV(SPE);
 
 // turn on interrupts
 SPCR |= _BV(SPIE);
 
 pos = 0;
 process_it = false;
 lastEventTime = millis();
}  // end of setup


// SPI interrupt routine
ISR (SPI_STC_vect)
{
//byte c = SPDR;
// 
// // add to buffer if room
// if (pos < sizeof buf)
//   {
//   buf [pos++] = c;
//   
//   // example: newline means time to process buffer
//   if (c == '\n')
//     process_it = true;
//     
//   }  // end of room available
  buf[0] = data&0xFF;
  buf[1] = (data>>8)&0xFF;
  SPDR = buf[0];
  hasRan = true;
}

int sensorData() {
  VL53L0X_RangingMeasurementData_t measure;
//    
// // Serial.print("Reading a measurement... ");
  lox.rangingTest(&measure, false); // pass in 'true' to get debug data printout!
//
//  if (measure.RangeStatus != 4) {  // phase failures have incorrect data
//   // Serial.print("Distance (mm): "); Serial.println(measure.RangeMilliMeter);
  return measure.RangeMilliMeter;
//  
//  } else {
   // Serial.println(" out of range ");
//    return 5;
//  }  
}

// main loop - wait for flag set in interrupt routine
void loop (void)
{
// if (process_it)
//   {
//   buf [pos] = 0;  
//   Serial.println (buf);
//   pos = 0;
//   process_it = false;
//   }  // end of flag set
  if(millis() > (lastEventTime + 30) {
    data = sensorData();
  }
  
   
}  // end of loop
